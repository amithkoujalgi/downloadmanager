#!/bin/bash 
os = ""
if [ "$(uname)" == "Darwin" ]; then
    os="mac" # Do something under Mac OS X platform        
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    os="linux" # Do something under GNU/Linux platform
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
    os="windows" # Do something under Windows NT platform
fi

if [ "$os" == "linux" ]; then
    command -v aria2c >/dev/null 2>&1 || { echo "Installing Aria2 Library" >&2; sudo apt-get -y install aria2;}
elif [ "$os" == "mac" ]; then
    brew install aria2 #Still need to check for installation and then install.
elif [ "$os" == "windows" ]; then
    #Install windows cygwin


aria2c --enable-rpc --rpc-listen-all &
if [ ! -f ../target/DownloadManager-jar-with-dependencies.jar ]; then
    echo "Jar not Found, Running Build. . ." &
    mvn -f ../pom.xml clean compile assembly:single
fi
java -jar ../target/DownloadManager-jar-with-dependencies.jar &
sleep 5

# For Macintosh
#open http://localhost:9999
#To do.. check for all environments and run actions based on environments
