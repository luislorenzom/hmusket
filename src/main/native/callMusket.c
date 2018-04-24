#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "es_udc_gac_hmusket_MusketCaller.h"

int main(int argc, char **argv);

char** str_split(char str[], const char delim) {
  
  // Some variables declaration
  char* split = strtok(str, &delim);
  char *word;

  // Do split
  while(split != NULL) {
      printf("%s\n", split);
      word = split;
      split=strtok(NULL, &delim);
  }
  
  return 0;
}

JNIEXPORT void JNICALL Java_es_udc_gac_hmusket_MusketCaller_callMusket(JNIEnv *env, jobject thisObj, jstring jstr) {
	
	// Some variables declaration
	const char *arguments = (*env)->GetStringUTFChars(env, jstr, NULL);
	char argv[strlen(arguments) + 1];
	strcpy(argv, args);
	
	printf("%s\n",argv);
	str_split(argv, *" ");
	
	// call musket's main
	main(2,arguments);

	return;
}
