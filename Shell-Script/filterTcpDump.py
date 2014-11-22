import subprocess
import urllib2
import time
class ProcessDump:
	def getAppName(self, Url):
		if "secure.splitwise.com" in Url:
			return "Splitwise"
			#epoch_time= int(time.time())
		#urllib2.urlopen('http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?data='+appName+"+"+str(epoch_time))

winDump = subprocess.Popen("windump -i 3 -s 0 -w C:/Users/PushkarJ/Desktop/DumpFile01.pcap",stdout=subprocess.PIPE)
sleep = subprocess.Popen("sleep 10",stdin=winDump.stdout, stdout=subprocess.PIPE)
winDump.stdout.close()
output = sleep.communicate()

taskList = subprocess.Popen("tasklist /v",stdout=subprocess.PIPE)
findWinDump = subprocess.Popen("grep -i \"windump\"",stdin=taskList.stdout,stdout=subprocess.PIPE)
getFirstWinDump = subprocess.Popen("head -1", stdin=findWinDump.stdout,stdout=subprocess.PIPE)
taskList.stdout.close()  # Allow taskList to receive a SIGPIPE if findWinDump exits.
findWinDump.stdout.close()
output = getFirstWinDump.communicate()[0]

processId= output.split()[1]
subprocess.Popen("taskkill /f /PID " + processId)

file = subprocess.Popen("tshark -nr DumpFile01.pcap -T text -V")
file.communicate()[0]

#Ref: http://cmdlinetips.com/2011/08/three-ways-to-read-a-text-file-line-by-line-in-python/
## Open the file with read only permit
f = file
## Read the first line 
line = f.readline()
## If the file is not empty keep reading line one at a time
## till the file is empty
IPAppDict = {}
oldAppName =""
appName=""
epoch_time=""
while line:
    print line
    line = f.readline()
    if "Epoch Time:" in line:
    	timestamp = line.split(":")
    	epoch_time = timestamp[0].split()
    if "Answers" in line:
    	line = f.readline()
   		dnsAnswer[] = line.split()
   		Url = dnsAnswer[0][:-1]
   		IP = dnsAnswer[2][4:]
   		appName = getAppName(Url)
   		IPAppDict[IP]=appName
   	if "Internet Protocol Version 4" in line:
   		line = f.readline()
   		IPHeader = line.split()
   		serverIP = IPHeader[1][4:]
   		if serverIP in dict.keys():
   			appName = IPAppDict[serverIP]
   			if oldAppName != appName
				urllib2.urlopen('http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?data='+oldAppName+"+"+str(epoch_time))
				oldAppName = appName

urllib2.urlopen('http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?data='+appName+"+"+str(epoch_time))
f.close()	

if output != None and output != "":
	p = ProcessDump()
	p.sendData(output)		
print output
