#!/bin/bash

PROJECT_PATH=../src/main/java/es/udc/gac/hmusket
if [[ "$1" == "compile" ]]
then
	echo "Compiling..."
	# Step 1: compile the .class file with invocation to a native method
	$JAVA_HOME/bin/javac $PROJECT_PATH/MusketCaller.java

	# Step 2: auto-generate a .h header file from said Java source
	$JAVA_HOME/bin/javah -cp ../src/main/java es.udc.gac.hmusket.MusketCaller
	mv es_udc_gac_hmusket_MusketCaller.h $PROJECT_PATH

	# Step 3: make the shared library with the name linked in said Java source, and implementing said native method
	gcc -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -o libmusketcaller.so -shared $PROJECT_PATH/callMusket.c
	mv libmusketcaller.so $PROJECT_PATH

	# Step 4: run JVM with java.library.path set to include said shared library
	$JAVA_HOME/bin/java -cp ../src/main/java -Djava.library.path=$PROJECT_PATH es.udc.gac.hmusket.MusketCaller
fi

if [[ "$1" == "clean" ]]
then
	echo "Cleaning..."
	rm $PROJECT_PATH/MusketCaller.class $PROJECT_PATH/es_udc_gac_hmusket_MusketCaller.h $PROJECT_PATH/libmusketcaller.so
fi
