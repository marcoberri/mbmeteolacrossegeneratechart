mvn clean assembly:assembly
cd target
java -jar MBMeteoChart-jar-with-dependencies.jar -url http://meteo.marcoberri.it/ -f /tmp/ -d -m -y -w
cd..