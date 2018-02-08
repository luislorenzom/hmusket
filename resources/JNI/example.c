#include <jni.h>
#include <stdio.h>
#include "Example.h"

JNIEXPORT void JNICALL Java_Example_sayHello(JNIEnv *env, jobject thisObj, jstring jstr) {
	const char *name = (*env)->GetStringUTFChars(env, jstr, NULL);
	//printf("Hello World\n");
	printf("%s\n", name);
	return;
}


char *scat(char *str1, char *str2) {
	// Reserve memory to hold the copy
	char *concatstr = malloc(strlen(str1)+strlen(str2)+1);
	
	// Init some helpers
	int globalIter = 0, localIter = 0;
	
	// Iterate first string to copy it
	while(str1[localIter] != '\0') {
		concatstr[globalIter++] = str1[localIter++];
	}

	// Now the same with the second string
	localIter = 0;
	while(str2[localIter] != '\0') {
		concatstr[globalIter++] = str2[localIter++];	
	}
	
	return concatstr;
}
