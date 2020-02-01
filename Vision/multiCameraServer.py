#!/usr/bin/env python3
#----------------------------------------------------------------------------
# Copyright (c) 2018 FIRST. All Rights Reserved.
# Open Source Software - may be modified and shared by FRC teams. The code
# must be accompanied by the FIRST BSD license file in the root directory of
# the project.
#----------------------------------------------------------------------------


#Importing Needed Libraries
import json
import time
import sys
import numpy as np
import cv2
import imutils
import argparse
from functools import reduce
from collections import deque

from cscore import CameraServer, VideoSource, UsbCamera, MjpegServer, CvSource
from networktables import NetworkTables, NetworkTablesInstance
import ntcore

RUNNING_AVERAGE_NUM = 3
CAMERA_WIDTH = None
CAMERA_HEIGHT = None
#   JSON format:
#   {
#       "team": <team number>,
#       "ntmode": <"client" or "server", "client" if unspecified>
#       "cameras": [
#           {
#               "name": <camera name>
#               "path": <path, e.g. "/dev/video0">
#               "pixel format": <"MJPEG", "YUYV", etc>   // optional
#               "width": <video mode width>              // optional
#               "height": <video mode height>            // optional
#               "fps": <video mode fps>                  // optional
#               "brightness": <percentage brightness>    // optional
#               "white balance": <"auto", "hold", value> // optional
#               "exposure": <"auto", "hold", value>      // optional
#               "properties": [                          // optional
#                   {
#                       "name": <property name>
#                       "value": <property value>
#                   }
#               ],
#               "stream": {                              // optional
#                   "properties": [
#                       {
#                           "name": <stream property name>
#                           "value": <stream property value>
#                       }
#                   ]
#               }
#           }
#       ]
#       "switched cameras": [
#           {
#               "name": <virtual camera name>
#               "key": <network table key used for selection>
#               // if NT value is a string, it's treated as a name
#               // if NT value is a double, it's treated as an integer index
#           }
#       ]
#   }


#Random first stuff I have no idea what it does
configFile = "/boot/frc.json"

class CameraConfig: pass

team = None
server = False
cameraConfigs = []
switchedCameraConfigs = []
cameras = []
input_output = []

def parseError(str):
    """Report parse error."""
    print("config error in '" + configFile + "': " + str, file=sys.stderr)

def readCameraConfig(config):
    """Read single camera configuration."""
    cam = CameraConfig()

    # name
    try:
        cam.name = config["name"]
    except KeyError:
        parseError("could not read camera name")
        return False

    # path
    try:
        cam.path = config["path"]
    except KeyError:
        parseError("camera '{}': could not read path".format(cam.name))
        return False

    # height, width
    try:
        cam.height = config["height"]
        cam.width = config["width"]
    except KeyError:
        parseError("camera '{}': could not read height or width".format(cam.name))
        return False

    # stream properties
    cam.streamConfig = config.get("stream")

    cam.config = config

    cameraConfigs.append(cam)
    return True

def readSwitchedCameraConfig(config):
    """Read single switched camera configuration."""
    cam = CameraConfig()

    # name
    try:
        cam.name = config["name"]
    except KeyError:
        parseError("could not read switched camera name")
        return False

    # path
    try:
        cam.key = config["key"]
    except KeyError:
        parseError("switched camera '{}': could not read key".format(cam.name))
        return False

    switchedCameraConfigs.append(cam)
    return True

def readConfig():
    """Read configuration file."""
    global team
    global server

    # parse file
    try:
        with open(configFile, "rt", encoding="utf-8") as f:
            j = json.load(f)
    except OSError as err:
        print("could not open '{}': {}".format(configFile, err), file=sys.stderr)
        return False

    # top level must be an object
    if not isinstance(j, dict):
        parseError("must be JSON object")
        return False

    # team number
    try:
        team = j["team"]
    except KeyError:
        parseError("could not read team number")
        return False

    # ntmode (optional)
    if "ntmode" in j:
        str = j["ntmode"]
        if str.lower() == "client":
            server = False
        elif str.lower() == "server":
            server = True
        else:
            parseError("could not understand ntmode value '{}'".format(str))

    # cameras
    try:
        cameras = j["cameras"]
    except KeyError:
        parseError("could not read cameras")
        return False
    for camera in cameras:
        if not readCameraConfig(camera):
            return False

    # switched cameras
    if "switched cameras" in j:
        for camera in j["switched cameras"]:
            if not readSwitchedCameraConfig(camera):
                return False

    return True

def startCamera(config):
    """Start running the camera."""
    print("Starting camera '{}' on {}".format(config.name, config.path))
    cs = CameraServer.getInstance()
    camera = UsbCamera(config.name, config.path)
    #camera.setExposureManual(5)
    cv_sink = cs.getVideo(camera=camera)
    output_stream = cs.putVideo('output_' + config.name, config.width, config.height)
    img = np.zeros(shape=(config.height, config.width, 3), dtype=np.uint8)
    input_output.append((cv_sink, output_stream, img))
    #server = cs.startAutomaticCapture(return_server=True)

    camera.setConfigJson(json.dumps(config.config))
    camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen)

    #if config.streamConfig is not None:
        #server.setConfigJson(json.dumps(config.streamConfig))

    return camera

def startSwitchedCamera(config):
    """Start running the switched camera."""
    print("Starting switched camera '{}' on {}".format(config.name, config.key))
    server = CameraServer.getInstance().addSwitchedCamera(config.name)

    def listener(fromobj, key, value, isNew):
        if isinstance(value, float):
            i = int(value)
            if i >= 0 and i < len(cameras):
              server.setSource(cameras[i])
        elif isinstance(value, str):
            for i in range(len(cameraConfigs)):
                if value == cameraConfigs[i].name:
                    server.setSource(cameras[i])
                    break

    NetworkTablesInstance.getDefault().getEntry(config.key).addListener(
        listener,
        ntcore.constants.NT_NOTIFY_IMMEDIATE |
        ntcore.constants.NT_NOTIFY_NEW |
        ntcore.constants.NT_NOTIFY_UPDATE)

    return server

def sum_of_points(acc, new):
    if new != None and acc != None:
        return (new[0] + acc[0], new[1] + acc[1])
    elif new == None and acc != None:
        return acc
    elif acc == None and new != None:
        return new
    else:
        return None

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        configFile = sys.argv[1]

    # read configuration
    if not readConfig():
        sys.exit(1)

    # start NetworkTables
    ntinst = NetworkTablesInstance.getDefault()
    if server:
        print("Setting up NetworkTables server")
        ntinst.startServer()
    else:
        print("Setting up NetworkTables client for team {}".format(team))
        ntinst.startClientTeam(team)

    # start cameras
    for config in cameraConfigs:
        CAMERA_HEIGHT = config.height
        CAMERA_WIDTH = config.width
        cameras.append(startCamera(config))

    # start switched cameras
    for config in switchedCameraConfigs:
        startSwitchedCamera(config)

    # loop forever
    #print("Before loop")
    visiontable = NetworkTables.getTable('Vision')
    recent_points = deque(maxlen=RUNNING_AVERAGE_NUM + 1)
    while True:
        #print("In loop")
        # (cv_sink, output_stream, img)
        for index, (cv_sink, output_stream, img) in enumerate(input_output[:1]):
            time, img  = cv_sink.grabFrame(img)

            #print('time: {}'.format(time))
            if time == 0:
                output_stream.notifyError(cv_sink.getError())
                continue
            #cv2.rectangle(img,(1,1),(100,100),(0,0,0),1)

            #Processing begins -Brayden-
            #converting to HSV & HSL
            # oversaturation_mask = cv2.inRange(img, (200, 200, 200), (255, 255, 255))
            # hsl = cv2.cvtColor(img, cv2.COLOR_BGR2HLS)
            # light_mask = cv2.inRange(hsl, (0, 0, 150), (0, 0, 255))
            hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
            #Segmentation
            greenLow = (50, 53, 55)
            greenUp = (92, 255, 223) 
            green_mask = cv2.inRange(hsv, greenLow, greenUp)

            mask = green_mask #- light_mask - oversaturation_mask

            #res = cv2.bitwise_and(img, img, mask=mask1)

            #Finding edges
            edged = cv2.Canny(mask, 30, 200)
            #getting Countors
            cnts = cv2.findContours(edged, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
            cnts = imutils.grab_contours(cnts)
            cnts = sorted(cnts, key = cv2.contourArea, reverse = True)
            #Loop through contours
            found_contour = None
            for c in cnts:
                peri = cv2.arcLength(c, True)
                approx = cv2.approxPolyDP(c, 0.02 * peri, True)
                #Draw rectangle
                boundRect = cv2.boundingRect(approx)
                start = (boundRect[0], boundRect[1])
                end = (boundRect[0] + boundRect[2], boundRect[1] + boundRect[3])
                #Finding the centerX
                centerX = int((start[0] + end[0])/2)
                #Bounding rectangle
                line_point_1_right = (centerX + 20, boundRect[1])
                line_point_1_left = (centerX - 20, boundRect[1])
                line_point_2_top = (centerX, boundRect[1] + 20)
                line_point_2_bottom = (centerX, boundRect[1] - 20)
                color = (0,0,0)
                found_contour = (centerX, boundRect)
                break
                #Green for 8 sides red for not 8
                
                    # screenCnt = approx
                    # color = (0,255,0)  
                    # centerX = centerX - 160
                    
                    # print("x:", centerX)
                    # print("y:", boundRect[1])   
                                
                # else:
                    # color = (0,0,255)
                    # visiontable.putBoolean('Found Contour', False)
                #Drawing Rectangle and cross arrow
                # Average[k] = 
                
                #Display frame to screen
                
            
            if found_contour != None:
                centerX = found_contour[0]
                boundRect = found_contour[1]
                color = (0, 255, 0)
                line_point_1_right = (centerX + 20, boundRect[1])
                line_point_1_left = (centerX - 20, boundRect[1])
                line_point_2_top = (centerX, boundRect[1] + 20)
                line_point_2_bottom = (centerX, boundRect[1] - 20)

                cv2.rectangle(img, (boundRect[0], boundRect[1]), (boundRect[0]+boundRect[2], boundRect[1]+boundRect[3]), color)
                cv2.line(img, line_point_1_right, line_point_1_left, color, 1)
                cv2.line(img, line_point_2_top, line_point_2_bottom, color, 1)
                cv2.line(img, (160, 240), (160, 0), (128,0,0))

                recent_points.append((centerX, boundRect[1]))
                if len(recent_points) > RUNNING_AVERAGE_NUM:
                    recent_points.popleft()

                visiontable.putNumber('XCenter', centerX / (CAMERA_WIDTH / 2.0) - 1.0)
                visiontable.putNumber('YCenter', boundRect[1] / (CAMERA_HEIGHT / 2.0) - 1.0)
                visiontable.putBoolean('Found Contour', True)
            else:
                visiontable.putBoolean('Found Contour', False)
            # if len(Recent_Points) >= RUNNING_AVERAGE_NUM:
            #     num_of_points = len(Recent_Points)
            #     average_point = reduce(sum_of_points, Recent_Points)
                
            #     if average_point != None:
            #         average_point = (round(average_point[0] / num_of_points), round(average_point[1] / num_of_points))

            if len(recent_points) != 0:
                average_point = [int(round(sum(x) / len(x))) for x in zip(*recent_points)]
                visiontable.putNumber('Running XCenter', average_point[0] / (CAMERA_WIDTH / 2.0) - 1.0)
                visiontable.putNumber('Running YCenter', average_point[1] / (CAMERA_HEIGHT / 2.0) - 1.0)
                print("average: ({}, {})".format(average_point[0], average_point[1]))
                cv2.line(img, (average_point[0] + 20, average_point[1]),  (average_point[0] - 20, average_point[1]), (0,0,255), 1)
                cv2.line(img,(average_point[0], average_point[1] + 20), (average_point[0], average_point[1] - 20) , (0,0,255), 1)
                
                
            output_stream.putFrame(img)
            input_output[index] = (cv_sink, output_stream, img)
