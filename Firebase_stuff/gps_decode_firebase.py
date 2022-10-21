import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import os

cred = credentials.Certificate(r'C:\Users\richa\OneDrive\Documents\GitHub\Geofence\Firebase_stuff\firebase_sdk.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://geofencingtest-342422-default-rtdb.firebaseio.com/'
})

ref = db.reference('/Trackers')
while(1): 
    newdata = db.reference('/gpstest/GPRMC').get()
    if(newdata.find("GPRMC")!=-1): ##only accept GPRMC GPS DATA
        filterone = newdata.split(",") ##split parts of data into a list at comma points
        listlength = len(filterone) ## designate initial list length
        try:
            westcheck = filterone.index("W") ##check for value of West in Data
        except ValueError:  ##if West not in data, designate as -1
            westcheck=-1 
        try:
            eastcheck = filterone.index("E") ##check for value of East in Data
        except ValueError: ##if East not in data, designate as -1
            eastcheck = -1
        try:
            northcheck = filterone.index("N") ##check for value of North in Data
        except ValueError: ##if North not in data designate as -1
            northcheck = -1 
        try:
            southcheck = filterone.index("S") ##check for value of South in Data
        except ValueError: ##if South not in data, designate as -1
            southcheck = -1
        if(eastcheck == -1): ## if East is not in Data
            while(listlength>westcheck+1): ## remove end data until West is found in list
                filterone.pop(listlength-1)
                listlength = listlength-1
        else:
            while(listlength>eastcheck): ## East is in Data
                filterone.pop(listlength-1) ## remove end data until East is found
                listlength = listlength-1
        filterone.pop(1)  ## remove other two extraneous values from data, we are only concerned with GPS coordinates
        filterone.pop(1)
        print(filterone)
        filterone.remove("$GPRMC") ##remove GPRMC from data
        if(northcheck ==-1): ## if North isnt in Data
            if(westcheck==-1): ## and if West isnt in Data
                filterone.remove("S") ##south and east are in the list so remove them
                filterone.remove("E")
                snorth = "S" ##designate North/South variable as South
                weast = "E" ##designate West/East variable as East
            else:
                filterone.remove("S") ## South and West are in the list so remove them
                filterone.remove("W") 
                snorth = "S" ##designate North/South variable as South
                weast = "W" ##designate West/East variable as West
        elif(southcheck==-1): ## if South isnt in data
            if(westcheck==-1): ##if west isnt in data
                filterone.remove("N") ## north and east are in the list so remove them
                filterone.remove("E") 
                snorth = "N" ##designate North/South variable as North
                weast = "E" ##designate West/East variable as East
            else:
                filterone.remove("N") ## north and west are in the list so remove them
                filterone.remove("W")
                snorth = "N" ##designate North/South variable as North
                weast = "W" ##designate West/East variable as West
        ##WHERE NEW CODE WAS INSERTED
        filtertwo = [float(i) for i in filterone] ##turn data in list into float
        filtertwo = [i/100 for i in filtertwo] ## shift decimal place to better suit google maps data
        filtertwo = [str(i) for i in filtertwo] ## return gps data back to string to further manipulate data
        latitudetemp = filtertwo[0] ## seperate latitude value from data into its own variable
        longitudetemp = filtertwo[1] ##seperate longitude value from data into its own variable
        latlist = latitudetemp.split('.') ##split hh from mmss.ss in latitude data
        longlist =longitudetemp.split('.') #split hh from mmss.ss in longitude data
        lat0str = latlist[0] ##add hh latitude value to its own string variable
        long0str = longlist[0] ##add hh longitude value to its own string variable
        lat0 = int(latlist[0]) ##convert string value of hh to int
        long0 = int(longlist[0]) ##convert string value of hh to int
        latlist.pop(0) ##remove hh from latitude list
        longlist.pop(0) ##remove hh from longitude list
        lat1temp = latlist[0] ##designate latitude mmss.ss as its own variable
        long1temp = longlist[0] ##designate longitude mmss.ss as its own variable
        lat1str = lat1temp[:2] + "." + lat1temp[2:] ## seperate mm ss and ss using dot for input in google maps format
        long1str = long1temp[:2] + "." + long1temp[2:] ## seperate mm ss and ss using dot for input in google maps format
        lat1 = float(lat1str) ##designate float value of final mmss.ss
        lat1 = round(lat1,4) ##round latitude float value to usable decimal in google maps format
        lat1str = str(lat1) ##designate latitude string value of mmss.ss as rounded format
        long1 = float(long1str) ## designate float value of final mmss.ss
        long1 = round(long1,4) ##round longitude value to usable decimal in google maps format
        long1str = str(long1)  ##designate longitude string value of mmss.ss as rounded format
        #print(latitudetemp)
        print(lat0str + " " + lat1str + " " + snorth + " " + long0str + " " + long1str + " " + weast) ##print final gps coords in google maps format
        firebaselat = lat0 + lat1/60
        if(snorth=='S'):
            firebaselat*= -1
        firebaselong = long0 + long1/60
        if(weast=='W'):
            firebaselong*= -1
        ref.child('111').update({'latitude':firebaselat})    
        ref.child('111').update({'longitude':firebaselong})

