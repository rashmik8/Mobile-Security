#!/bin/bash

while true; do

        windump -i 2 -s 0 -w C:/Users/PushkarJ/Desktop/DumpFile01.pcap &

        sleep 10

        ps=$(tasklist /v | grep -i "windump" | cut -d ' ' -f 20 | head -1);

        echo $ps

        taskkill /f /PID $ps

        data=$(windump -r C:/Users/PushkarJ/Desktop/chase1.pcap | grep CNAME);

        x=$(echo $data | perl -pne s/\n/+/g;)
        echo $x
        data=$(echo $x | perl -pne s/\ /+/g;)
        echo $data

        url=http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?data=$data

        echo $url

        curl $url

done