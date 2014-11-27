import subprocess
import urllib2
import time
import StringIO
import os

class ProcessDump:
	def getAppName(self, Url):
		if "secure.splitwise.com" in Url:
			return "Splitwise"
		if "wwwbcchase.gslb.bankone.com" in Url:
			return "ChaseBank"

			#epoch_time= int(time.time())
		#urllib2.urlopen('http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?data='+appName+"+"+str(epoch_time))

#tcpDump = subprocess.Popen("tcpdump -w ~/Documents/DumpFile01.pcap",stdout=subprocess.PIPE, shell=True)
#sleep = subprocess.Popen("sleep 10",stdin=tcpDump.stdout, stdout=subprocess.PIPE, shell=True)
#tcpDump.stdout.close()
#output = sleep.communicate()

#taskList = subprocess.Popen("pgrep -f tcpdump", stdout=subprocess.PIPE, shell=True)
#output = taskList.communicate()[0]

#subprocess.Popen("kill -9 " + output, shell=True)

file = subprocess.Popen("tshark -nr ~/Documents/DumpFile01.pcap -T text -V",stdout=subprocess.PIPE, shell=True)
outputtext = file.communicate()[0]

#Ref: http://cmdlinetips.com/2011/08/three-ways-to-read-a-text-file-line-by-line-in-python/
## Open the file with read only permit
f = StringIO.StringIO(outputtext)
#print f
## Read the first line 
line = f.readline()
## If the file is not empty keep reading line one at a time
## till the file is empty
IPAppDict = {}
oldAppName =""
appName=""
epoch_time=""
while line:
		if "Epoch Time:" in line:
			timestamp = line.split(":")
			epoch_time = timestamp[1].split()[0]
		if "Answers" in line:
			line = f.readline()
			while "type A" not in line and line != "":
				line = f.readline()
			dnsAnswer = line.split()
			Url = dnsAnswer[0][:-1]
			IP = dnsAnswer[6]
			p = ProcessDump()
			knownAppName = p.getAppName(Url)
			if knownAppName != None:
				appName = knownAppName
			IPAppDict[IP]=appName
		if "Internet Protocol Version 4" in line:
			IPHeader = line.split()
			serverIP = IPHeader[5]
			#sourceIP = IPHeader[8] check either case
			if serverIP in IPAppDict.keys():
				appName = IPAppDict[serverIP]
			if oldAppName != appName:
				urllib2.urlopen('http://1-dot-nbotstwo.appspot.com/ninjabotscmu?appName='+oldAppName+"&timestamp="+str(epoch_time)+"&srcIp=")
				oldAppName = appName
		line = f.readline()
print appName
print epoch_time
print int(time.time())
print 'http://1-dot-nbotstwo.appspot.com/ninjabotscmu?appName='+appName+"&timestamp="+str(epoch_time)+"&srcIp="
if appName is None:
	urllib2.urlopen('http://1-dot-nbotstwo.appspot.com/ninjabotscmu?appName='+appName+"&timestamp="+str(epoch_time)+"&srcIp=")
f.close() 
