aria2c --enable-rpc --rpc-listen-all &
mvn -f ../pom.xml clean compile assembly:single
java -jar ../target/app-jar-with-dependencies.jar &
sleep 5

# For Macintosh
open http://localhost:9999