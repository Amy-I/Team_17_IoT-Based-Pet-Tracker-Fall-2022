import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os

cred = credentials.Certificate(r'C:\Users\richa\OneDrive\Documents\GitHub\Geofence\Firebase_stuff\firebase_sdk.json')

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

list = db.reference('/gpstest/GPRMC').get()
print(list)