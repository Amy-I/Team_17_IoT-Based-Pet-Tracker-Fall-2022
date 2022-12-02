/*CODE NOT USED
BACKUP GPS PROGRAM
-----------------------
CODE NOT USED*/
#include <Arduino.h>
#include <TinyGPS++.h> // library for GPS module
#include <SoftwareSerial.h>
#include <bits/stdc++.h>
#include <stdlib.h>
#include <ESP8266WiFi.h>
#include <Firebase_ESP_Client.h>
const unsigned int MAX_MESSAGE_LENGTH = 70;
#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"
// Insert your network credentials
//#define WIFI_SSID "ARRIS-7032"
//#define WIFI_PASSWORD "2PM7H7600767"
//#define WIFI_SSID "ATT72bbB6t"
//#define WIFI_PASSWORD "9zaq=kjc9f?z"
#define WIFI_SSID "IDEOZU_TABLET"
#define WIFI_PASSWORD "1a98!8P9"
TinyGPSPlus gps;  // The TinyGPS++ object
SoftwareSerial ss(4, 5); // The serial connection to the GPS device
float firebaselat , firebaselong;
// Insert Firebase project API Key
#define API_KEY "AIzaSyCBio1uDyFV51Ex5q3MLz22ed1yp0J1FKI"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://geofencingtest-342422-default-rtdb.firebaseio.com/" 

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;
unsigned long sendDataPrevMillis = 0;
int count = 0;
bool signupOK = false;

void setup() {
  pinMode(12,OUTPUT);
  Serial.begin(2400);
  ss.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
 /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
  
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  if (Firebase.RTDB.getInt(&fbdo, "Trackers/111/isInGeofence")) {
    if (fbdo.dataTypeEnum() == fb_esp_rtdb_data_type_integer) {
      Serial.println(fbdo.to<int>());
      int z = fbdo.to<int>();
      if(z == 0)
    {
      digitalWrite(12,HIGH);
      Serial.println("HIGH");
    }
    if(z == 1)
    {
      digitalWrite(12,LOW);
      Serial.println("LOW");
    }
    Serial.println(z);
  }  
    }
    char firebaselatstr[100];
  char firebaselongstr[100];
  sprintf(firebaselatstr,"%.12f",firebaselat);
  sprintf(firebaselongstr,"%.12f",firebaselong);
  while (ss.available() > 0) //while data is available
    if (gps.encode(ss.read())) //read gps data
    {
      if (gps.location.isValid()) //check whether gps location is valid
      {
        firebaselat = gps.location.lat();
        lat_str = String(firebaselat , 6); // latitude location is stored in a string
        firebaselong = gps.location.lng();
        lng_str = String(firebaselong , 6); //longitude location is stored in a string
      }
      if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    FirebaseJson updateData;
    updateData.add("latitude",firebaselat);
    updateData.add("stringlat",firebaselatstr);
    updateData.add("stringlong",firebaselongstr);
    updateData.add("longitude",firebaselong);
    if(Firebase.RTDB.updateNode(&fbdo, "Trackers/111",&updateData)){

    }
      }
    }
    

}
