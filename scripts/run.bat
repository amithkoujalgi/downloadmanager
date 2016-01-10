@echo off
cls
echo ############################
echo # Aria2 Download manager   #
echo #   for windows            # 
echo ############################
@echo off
PATH=%PATH%;%CD%\aria2\
WHERE aria2c
IF %ERRORLEVEL% NEQ 0 ( 
ECHO Unable to find Aria2c
ECHO Downloading aria2c binaries . . .
start /w C:\windows\explorer.exe https://github.com/tatsuhiro-t/aria2/releases/download/release-1.19.3/aria2-1.19.3-win-64bit-build1.zip
ping 127.0.0.1 -n 15 > nul
ECHO Installing Aria2c . . .
MOVE /Y "c:\users\%USERNAME%\Downloads\aria2-1.19.3-win-64bit-build1.zip" 
unzip -j aria2-1.19.3-win-64bit-build1.zip -d aria2
ECHO Unzipping files . . .)
START /B aria2\aria2c --enable-rpc --rpc-listen-all > aria2.log


if exist ..\target\DownloadManager-jar-with-dependencies.jar (
    ECHO Running the program now . . .
    START /B java -jar ../target/DownloadManager-jar-with-dependencies.jar > downloadmanager.log
) else (
    ECHO "Building JAR File . . ."
    mvn -f ../pom.xml clean install compile assembly:single
    ECHO Running the program now . . .
    START /B java -jar ../target/DownloadManager-jar-with-dependencies.jar > downloadmanager.log
)
ECHO Completed