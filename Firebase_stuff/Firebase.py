import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('firebase_sdk.json')

firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://geofencingtest-342422-default-rtdb.firebaseio.com/'
})

ref = db,reference('/')
