# MeteoLaCrosseGenerateChart

Java batch per generare i grafici per la visualizzazione sul sito [meteo.marcoberri.it](http://meteo.marcoberri.it)



#Maven Assembly Jar

* maven --> mvn clean assembly:assembly


#Run

* java -jar <jar_name> -url http://meteo.marcoberri.it/ -f /<app-path>/mbmeteolacrosse/app/public/charts -d -m -y -w

#Options

* -url : indirizzo delle api
* -f : folder path
* -d  : day graph generation
* -w  : week graph generation
* -y  : year graph generation
* -m  : month graph generation

