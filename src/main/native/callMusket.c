#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "es_udc_gac_hmusket_MusketCaller.h"

int main(int argc, char **argv);

JNIEXPORT void JNICALL Java_es_udc_gac_hmusket_MusketCaller_callMusket(JNIEnv *env, jobject thisObj, jstring jstr) {
	
	// Some variables declaration
	const char *args = (*env)->GetStringUTFChars(env, jstr, NULL);

	char argv2[strlen(args) + 1];
	strcpy(argv2, args);
	
	char delim = ' ';
	int str_size = strlen(argv2) + 1;
	
	int arguments_number = 1; // first argument

	// Count how many gaps are in the string
	for (int i = 0; i < str_size; i++) {
		if (argv2[i] == delim){
			arguments_number++;
		}
	}

	char* split = strtok(argv2, " ");
	// And create an array to save each argument
	char* arguments[arguments_number];

	// Do split and save it in arguments array
	int i = 0;
	while(split != NULL) {
		arguments[i] = split;
		split=strtok(NULL, " ");

		i++;
	}
	
	for (int j = 0; j<arguments_number; j++) {
		printf("%s\n", arguments[j]);
	}

	printf("\n%d\n", arguments_number);

	// Call musket's main method
	main(arguments_number, arguments);
}
