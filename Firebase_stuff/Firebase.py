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
lat = 29.0+ 42.38748/(60.0)
longi = (95.0+ 27.90084/(60.0))*(-1.0)
l = 5
lg = 4
### Testing Update to Tracker 111 ###
trackerReference = db.reference('/Trackers')
tracker_ref = trackerReference.child('111')
tracker_ref.update({
    'latitude': lat,
    'longitude': longi
})
### End Test ###

### Retrieving the Safe Area signal from database ###
isInGeofence = tracker_ref.child('isInGeofence').get()
print(isInGeofence)
### End Test ###
