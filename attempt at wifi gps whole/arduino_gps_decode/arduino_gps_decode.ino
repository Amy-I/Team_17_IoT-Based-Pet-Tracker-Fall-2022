#include <bits/stdc++.h>
#include <stdlib.h>
#include <stdio.h>
void setup() {
  // put your setup code here, to run once:
    Serial.begin(4800);
}

void loop() {
  // put your main code here, to run repeatedly:
  String gpr = "$GPRMC,234232.00,A,3037.23316,N,09620.39284,W,0.243,,211022,,,A*PGGA�";
  char message[70];
  int count = 0;
  for(int i=0; i<70;i++)
  {
    message[count] = gpr[count];
    count++; 
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
    if(end!='W' && end!='E'){
      data = "";
     }
     char message[data.length()];
   for(int i = 0;i<data.length();i++){
           message[i]=data[i];
   }
    message[data.length()]='\0';
  
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
 if(data!="")
 {
  Serial.printf("%.6f\n",firebaselat);
 }
  
  delay(1000);
  
}
}
