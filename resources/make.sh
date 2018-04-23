#!/bin/bash

PROJECT_PATH=../src/main/java/es/udc/gac/hmusket
NATIVE_PROJECT_PATH=../src/main/native
if [[ "$1" == "compile" ]]
then
	echo "Compiling..."
	# Step 1: compile the .class file with invocation to a native method
	$JAVA_HOME/bin/javac $PROJECT_PATH/MusketCaller.java

	# Step 2: auto-generate a .h header file from said Java source
	$JAVA_HOME/bin/javah -cp ../src/main/java es.udc.gac.hmusket.MusketCaller
	mv es_udc_gac_hmusket_MusketCaller.h $NATIVE_PROJECT_PATH

if [[ "$1" == "clean" ]]
then
	echo "Cleaning..."
	rm $PROJECT_PATH/MusketCaller.class $PROJECT_PATH/es_udc_gac_hmusket_MusketCaller.h $PROJECT_PATH/libmusketcaller.so
fi
