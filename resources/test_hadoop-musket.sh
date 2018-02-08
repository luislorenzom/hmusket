hdfs dfs -rm -r /user/luis/prueba2/
echo
rm /Volumes/Datos/luis/Desktop/prueba.txt
echo
cd /Volumes/Datos/luis/workspace/hmusket
mvn clean package -DskipTests
echo
hadoop jar target/hmusket-0.0.1-jar-with-dependencies.jar es.udc.gac.hmusket.HMusket /example.fastq prueba2 q
