#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "es_udc_gac_hmusket_MusketCaller.h"

int main(int argc, char **argv);

JNIEXPORT void JNICALL Java_es_udc_gac_hmusket_MusketCaller_callMusket(JNIEnv *env, jobject thisObj, jstring jstr) {
	const char *arguments = (*env)->GetStringUTFChars(env, jstr, NULL);
	printf("%s\n",arguments);
	main(0,NULL);
	return;
}
