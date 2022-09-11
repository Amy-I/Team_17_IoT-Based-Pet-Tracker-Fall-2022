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
        'lat':'37',
        'long':'30'
    }
})

list = db.reference('/Trackers/123').get()
print(list)