#!/bin/bash

if [[ "$1" == "compile" ]]
then
	echo "Compiling..."
	# Step 1: compile the .class file with invocation to a native method
	javac Example.java

	# Step 2: auto-generate a .h header file from said Java source
	javah Example

	# Step 3: make the shared library with the name linked in said Java source, and implementing said native method
	gcc -I/usr/lib/jvm/java-9-oracle/include -I/usr/lib/jvm/java-9-oracle/include/linux -o libhello.so -shared example.c

	# Step 4: run JVM with java.library.path set to include said shared library
	java -Djava.library.path=. Example
fi

if [[ "$1" == "clean" ]]
then
	echo "Cleaning..."
	rm Example.class Example.h libhello.so
fi