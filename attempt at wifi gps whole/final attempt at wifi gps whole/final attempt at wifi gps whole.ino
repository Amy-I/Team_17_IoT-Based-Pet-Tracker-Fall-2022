#include <Arduino.h>
#include <SoftwareSerial.h>
#include <bits/stdc++.h>
#include <stdlib.h>
#include <stdio.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>
//Provide the tok en generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"
// Insert your network credentials
//#define WIFI_SSID "ARRIS-7032"
//#define WIFI_PASSWORD "2PM7H7600767"
#define WIFI_SSID "ATT72bbB6t"
#define WIFI_PASSWORD "9zaq=kjc9f?z"

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
SoftwareSerial ss(4,5);
const unsigned int MAX_MESSAGE_LENGTH = 70;
void setup() {
  // put your setup code here, to run once:
ss.begin(9600);
Serial.begin(2400);
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
  // put your main code here, to run repeatedly:
  char input = ss.read();
  static char message[MAX_MESSAGE_LENGTH];
  static unsigned int message_pos = 0;
  
      if( input != '\n' && (message_pos < MAX_MESSAGE_LENGTH -1))
    {
      message[message_pos] = input;
      message_pos++; 
    }
    else
    {
      message[message_pos] = '\0';
      Serial.println(message);
      message_pos = 0;
    }
  String data;
if (message[1]=='G' && message[2]=='P' && message[3]=='R' && message[4]=='M')
     {
    for(int i = 19; i < 70;i ++){
      if(message[i]== 'W' || message[i]=='E'){
        data = data + message[i];
        break;
      }
      else{
          data = data + message[i];
      }
     
    }
   char end = data[data.length()-1];
     char message[data.length()];
   for(int i = 0;i<data.length();i++){
           message[i]=data[i];
   }
    message[data.length()]='\0';
    
    if(end!='W' && end!='E'){
      end = 'X';
     }
  
  char latitude[12];
  for(int i = 0;i<10;i++)
  {
    latitude[i]=message[i];
  }
  char NS = message[11];
  char longitude[11];
  int count = 0;
  for(int i = 13;i<24;i++)
  {
    longitude[count]=message[i];
    count++;
  }
  char WE = message[25];
   char latdd[1];
  for(int i=0;i<2;i++){
    latdd[i]=latitude[i];
  }
  latdd[2] = '\0';
  float latddval = atof(latdd);
  char latminutes[12];
  int latc = 0;
  for(int i=2;i<10;i++){
    latminutes[latc]=latitude[i];
    latc++;
  }
  latminutes[11] = '\0';
  float latminutesval = atof(latminutes);
  char longdd[2];
  for(int i=0;i<3;i++){
    longdd[i]=longitude[i];
  }
  longdd[3] = '\0';
  float longddval = atof(longdd);
  int longc = 0;
  char longminutes[7];
  for(int i=3;i<12;i++){
    longminutes[longc]=longitude[i];
    longc++;
  }
  longminutes[8] = '\0';
  float longminutesval = atof(longminutes);
  float firebaselat = latddval+ latminutesval/60;
  if(NS=='S'){
    firebaselat = firebaselat*(-1);
  }
  float firebaselong = longddval + longminutesval/60;
 if(WE=='W'){
  firebaselong = firebaselong * (-1);
 }
 if(end!='X'&& isdigit(latdd[0]) && isdigit(longdd[0]))
 {
  /*Serial.println("GPRMC LATITUDE CODED"); 
  Serial.println(latitude);
  Serial.println("GPRMC LATITUDE");   
  Serial.printf("%.6f\n",firebaselat);
  Serial.println("GPRMC LONGITUDE CODED"); 
  Serial.println(longitude);
  Serial.println("GPRMC LONGITUDE");
  Serial.printf("%.6f\n",firebaselong);*/
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    // Write an Int number on the database path test/int
    if (Firebase.RTDB.setFloat(&fbdo, "Trackers/111/latitude", firebaselat)){
      //Serial.printf(firebaselat);
      //Serial.println("PATH: " + fbdo.dataPath());
     // Serial.println("TYPE: " + fbdo.dataType());
    }
    else {
      ///Serial.println("FAILED");
      //Serial.println("REASON: " + fbdo.errorReason());
    }
    if (Firebase.RTDB.setFloat(&fbdo, "Trackers/111/longitude", firebaselong)){
     //Serial.printf(firebaselong);
     // Serial.println("PATH: " + fbdo.dataPath());
      //Serial.println("TYPE: " + fbdo.dataType());
    }
    else {
     // Serial.println("FAILED");
     // Serial.println("REASON: " + fbdo.errorReason());
    }
    if(Firebase.RTDB.getInt(&fbdo, "Trackers/111/isInGeofence")== 0)
    {
      digitalWrite(12,HIGH);
    }
    if(Firebase.RTDB.getInt(&fbdo, "/Trackers/111/isInGeofence")== 1)
    {
      digitalWrite(12,LOW);
    }
 }

}
}
}
