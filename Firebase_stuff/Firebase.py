import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os

cred = credentials.Certificate(r'C:\Users\richa\OneDrive\Documents\GitHub\Geofence\Firebase_stuff\firebase_sdk.json')
#cred = credentials.Certificate(r'C:\Users\a_ide\StudioProjects\Geofence\Firebase_stuff\firebase_sdk.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://geofencingtest-342422-default-rtdb.firebaseio.com/'
})

ref = db.reference('/')
ref.update({
    'gpstest':{
        'GPRMC':'42',
        'lat':0,
        'long':42
    }
})

list = db.reference('/Trackers/123').get()
print(list)

### Testing Update to Tracker 111 ###
trackerReference = db.reference('/Trackers')
tracker_ref = trackerReference.child('111')
tracker_ref.update({
    'latitude': 30.6532,
    'longitude': -963071
})
### End Test ###

### Retrieving the Safe Area signal from database ###
isInGeofence = tracker_ref.child('isInGeofence').get()
print(isInGeofence)
### End Test ###
