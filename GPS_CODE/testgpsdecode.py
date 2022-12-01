##PYTHON CODE USED IN 404 (NOT USED IN 403)
testlist = []
testlist.append('3035.8393')
testlist.append('09620.3975')
newlist = [float(i) for i in testlist]
newlist = [i/100 for i in newlist]
newlist = [str(i) for i in newlist]
latitudetemp = newlist[0]
longitudetemp = newlist[1]
latlist = latitudetemp.split('.')
longlist =longitudetemp.split('.')
lat0str = latlist[0]
long0str = longlist[0]
lat0 = int(latlist[0])
long0 = int(longlist[0])
latlist.pop(0)
longlist.pop(0)
lat1temp = latlist[0]
long1temp = longlist[0]
lat1str = lat1temp[:2] + "." + lat1temp[2:]
long1str = long1temp[:2] + "." + long1temp[2:]
lat1 = float(lat1str)
lat1 = round(lat1,4)
long1str = str(lat1) 
long1 = float(long1str)
long1 = round(long1,4)
long1str = str(long1) 
print(lat0str + " " + lat1str + " N "+ long0str + " "+ long1str)


