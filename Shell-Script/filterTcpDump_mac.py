#####################
## Way to use:
## python filterTcpDump_mac.py t: live capture
## python filterTcpDump_mac.py ~/Documents/chase.pcap: parse input file 
#####################

import subprocess
import urllib2
import time
import StringIO
import os
import io
import sys

#####################
## IPAppDict : app name mapping with server IP
## IPAppSrcDict : app name mapping source IP(device)
#####################
IPAppDict = {}
IPAppSrcDict = {}

###################
## Write output to sample text file
###################
fileWrite = open('/Users/rashmi/Documents/sampleOutput.txt', 'w')

###################
## Our app filter for Chase, Splitwise, Snapchat, PNC
###################
def getAppName(Url):
	if "splitwise" in Url:
		return "Splitwise"
	if "chase" in Url:
		return "ChaseBank"
	if "sc-analytics" in Url:
		return "SnapChat"
	if "mb.mbankhost" in Url:
		return "PNC"
	if "nflx" in Url:
		return "Netflix"
	if "myfitnesspal" in Url:
		return "MyFitnessPal"


###################
## t option: live capture
## other option: input file
###################
def mainPart():
	if sys.argv[1] == "t":
		tcpDump = subprocess.Popen("tcpdump -w ~/Documents/DumpFile01.pcap",stdout=subprocess.PIPE, shell=True)
		sleep = subprocess.Popen("sleep 10",stdin=tcpDump.stdout, stdout=subprocess.PIPE, shell=True)
		tcpDump.stdout.close()
		output = sleep.communicate()

		taskList = subprocess.Popen("pgrep -f tcpdump", stdout=subprocess.PIPE, shell=True)
		output = taskList.communicate()[0]
		subprocess.Popen("kill -9 " + output, shell=True)
		filename = "~/Documents/DumpFile01.pcap"
	else:
		filename = sys.argv[1]
	#############################
	### Use tshark to open file in text
	#############################
	file = subprocess.Popen("tshark -nr "+filename+" -T text -V",stdout=subprocess.PIPE, shell=True)
	outputtext = file.communicate()[0]

#Ref: http://cmdlinetips.com/2011/08/three-ways-to-read-a-text-file-line-by-line-in-python/
## Open the file with read only permit
	f = StringIO.StringIO(outputtext)
## Read the first line 
	line = f.readline()
	appName= ""
	epoch_time= ""
	while line:
		if "Epoch Time:" in line:
			timeStamp = line.split(":")
			epoch_time = timeStamp[1].split()[0]
		if "Answers" in line:
			line = f.readline()
			dnsAnswer = line.split()
			# Get the URL in next line itself
			Url = dnsAnswer[0][:-1]
			# Match until 'type A' and 'addr' is found in line
			while "type A" not in line and "addr" not in line and line != "":
				line = f.readline()

			if "type A" in line:
				# IP addr and DNS found for server 
				dnsAnswer = line.split()
				IP = dnsAnswer[6]
				knownAppName = getAppName(Url)
				if knownAppName != None:
					appName = knownAppName
					# Store app name in dictionary only if the app is in the list
					IPAppDict[IP]=appName
		# Check for IP addresses
		if "Internet Protocol Version 4" in line:
			IPHeader = line.split()
			serverIP = IPHeader[5]
			sourceIP = IPHeader[8]
			appName = ""
			flag = False
			# Check if the IP addresses in the line belong to source or destination
			# Get app name for the given IP
			if serverIP in IPAppDict.keys():
				appName = IPAppDict[serverIP]
				#flag = True
			elif sourceIP in IPAppDict.keys():
				appName = IPAppDict[sourceIP]
				sourceIP = serverIP
				#flag = True
			oldAppName = ""
			# Get the old app name
			if sourceIP in IPAppSrcDict.keys():
				oldAppName = IPAppSrcDict[sourceIP]
			# Check if app has changed.  If it has assign oldAppName to new app name and send it to URL
			if appName is not "" and oldAppName is not appName:
				print "SourceIP = "+sourceIP
				IPAppSrcDict[sourceIP] = appName
				fileWrite.write('http://1-dot-nbotstwo.appspot.com/ninjabotscmu?appName='+oldAppName+"&timeStamp="+str(epoch_time)+"&srcIP="+sourceIP)
				fileWrite.write('\n')
				urllib2.urlopen('http://1-dot-nbotstwo.appspot.com/ninjabotscmu?appName='+oldAppName+"&timeStamp="+str(epoch_time)+"&srcIP="+sourceIP)
		line = f.readline()

	# Pass all the state information obtained till now: source IP vs appName to server
	for srcIP, appName in IPAppSrcDict.iteritems():
		fileWrite.write('http://1-dot-nbotstwo.appspot.com/ninjabotscmu?appName='+appName+"&timeStamp="+str(epoch_time)+"&srcIP="+srcIP)
		fileWrite.write('\n')
		urllib2.urlopen('http://1-dot-nbotstwo.appspot.com/ninjabotscmu?appName='+appName+"&timeStamp="+str(epoch_time)+"&srcIP="+srcIP)
	
	f.close()
if sys.argv[1] == "t":
	while True:
		mainPart()
else:
	mainPart()

#### Debug information
for srcIP, app in IPAppDict.iteritems():
	print "ServerIP: "+srcIP+" appName: "+app
for srcIP, app in IPAppSrcDict.iteritems():
	print "SourceIP: "+srcIP+" appName: "+app
fileWrite.close()
