##PYTHON CODE USED IN 404 (NOT USED IN 403)
import serial

from serial import Serial ##imports the serial function from the serial module

ser = serial.Serial('COM10',4800) ##designates ser as the input port for serial data

while(1): #program runs constantly
    data = ser.readline(1000) ##read each individual line
    newdata = data.decode()
    if(newdata.find("GPRMC")!=-1): ##only accept GPRMC GPS DATA
     print(newdata)