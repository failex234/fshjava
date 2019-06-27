/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class me_felixnaumann_fsh_Utils_Native */

#ifndef _Included_me_felixnaumann_fsh_Utils_Native
#define _Included_me_felixnaumann_fsh_Utils_Native
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     me_felixnaumann_fsh_Utils_Native
 * Method:    isAdministrator
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_me_felixnaumann_fsh_Utils_Native_isAdministrator
  (JNIEnv *, jclass);

/*
 * Class:     me_felixnaumann_fsh_Utils_Native
 * Method:    changeWorkingDirectory
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_me_felixnaumann_fsh_Utils_Native_changeWorkingDirectory
  (JNIEnv *, jclass, jstring);

/*
 * Class:     me_felixnaumann_fsh_Utils_Native
 * Method:    getWorkingDirectory
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_me_felixnaumann_fsh_Utils_Native_getWorkingDirectory
  (JNIEnv *, jclass);

/*
 * Class:     me_felixnaumann_fsh_Utils_Native
 * Method:    getOS
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_me_felixnaumann_fsh_Utils_Native_getOS
  (JNIEnv *, jclass);

/*
 * Class:     me_felixnaumann_fsh_Utils_Native
 * Method:    clearConsole
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_me_felixnaumann_fsh_Utils_Native_clearConsole
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
