#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "es_udc_gac_hmusket_MusketCaller.h"

int main(int argc, char **argv);

JNIEXPORT void JNICALL Java_es_udc_gac_hmusket_MusketCaller_callMusket(JNIEnv *env, jobject thisObj, jstring jstr) {
	
	// Some variables declaration
	const char *args = (*env)->GetStringUTFChars(env, jstr, NULL);

  char argv[strlen(args) + 1];

  strcpy(argv, args);
  printf("%s\n\n", argv);

  // Some variables declaration
  char delim = ' ';
  int str_size = strlen(args) + 1;
  char* split = strtok(argv, &delim);
  int arguments_number = 3; // executable name, first and last argument
  
  // Count how many gaps are in the string
  for (int i = 0; i < str_size; i++) {
    if (argv[i] == delim){
      arguments_number++;
    }
  }

  // And create an array to save each argument
  char* arguments[arguments_number];
  arguments[0] = "musket";

  // Do split and save it in arguments array
  int i = 1;
  while(split != NULL) {
      arguments[i] = split;
      split=strtok(NULL, &delim);
      i++;
  }
	
  for (int j = 0; j<arguments_number; j++) {
    printf("%s\n", arguments[j]);
  }
  printf("\n%d\n", arguments_number);

	// Call musket's main
	main(arguments_number, arguments);

	//return;
}
