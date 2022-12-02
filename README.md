# Team 17: IoT Based Pet Tracker, Fall 2022
Project for ECEN 403/404, Spring '22 - Fall '22

# Running the Tracker
Plug in battery into input terminal on PCB, screw close enclosure and place it on you pet via collar. Open the app and login in or register if you do not have an account. On the pets page you can have the option to add pet, request video footage, or delete existing pets. Clicking on the action bar will open the navigation bar allowing you to access the maps, settings, and logout. In the maps you can track your pet as well as set, redefine, and delete safe areas.

# Set Up
APP

`local.properties` file not included for security reasons. In order to have map functionality, you must edit the `local.properties` file to have a `MAPS_API_KEY` variable. Store your Google Maps API key in the variable for full functionality.

CAMERA WEBSERVER

Add the following repo to the Additional boards manager URLs in Arudino from File > Preferences: `https://raw.githubusercontent.com/espressif/arduino-esp32/gh-pages/package_esp32_index.json`. Program the ESP32-Cam using the AI Thinker ESP32-CAM board.

**The Wifi on the phone and the Wifi used in the camera code must be the same**

FINAL ATTEMPT AT WIFI GPS WHOLE

Add the following repo to the Additional boards manager URLs in Arudino from File > Preferences: `https://arduino.esp8266.com/stable/package_esp8266com_index.json`.  Go to Sketch > Include Library > Add Zip Library and then add zip download of `https://github.com/mobizt/Firebase-ESP-Client`. Program ESP8266 using Generic ESP8266 Module.
Change WIFI SSID and WIFI PASSWORD to your WIFI SSID and WIFI PASSWORD

PCB Design

Includes all gerbers and NC drill files from altium needed to fabricate PCB

3D Enclosure

.stl files needed to 3D print TOP and BOTTOM of enclosure
