import subprocess
import urllib2
import time
class ProcessDump:
	def sendData(self, output):
		if "secure.splitwise.com" in output:
			appName= "Splitwise";
			epoch_time= int(time.time())
		urllib2.urlopen('http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?data='+appName+"+"+str(epoch_time))

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

readFile = subprocess.Popen("windump -r C:/Users/PushkarJ/Desktop/DumpFile01.pcap",stdout=subprocess.PIPE)
searchFile = subprocess.Popen("grep \"A?\"",stdin=readFile.stdout);
readFile.stdout.close()
output = searchFile.communicate()[0]

if output != None and output != "":
	p = ProcessDump()
	p.sendData(output)		
print output
