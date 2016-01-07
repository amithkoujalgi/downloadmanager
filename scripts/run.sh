#!/bin/bash 
command -v aria2c >/dev/null 2>&1 || { echo "Installing Aria2 Library" >&2; sudo apt-get -y install aria2;}

aria2c --enable-rpc --rpc-listen-all &
if [ ! -f ../target/DownloadManager-jar-with-dependencies.jar ]; then
    echo "Jar not Found, Running Build. . ." &
    mvn -f ../pom.xml clean compile ass!!embly:single
fi
java -jar ../target/DownloadManager-jar-with-dependencies.jar &
sleep 5

# For Macintosh
#open http://localhost:9999
#To do.. check for all environments and run actions based on environments
