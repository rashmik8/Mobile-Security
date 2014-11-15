import subprocess

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
print output
