#include <string.h>
#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,"jnitest",__VA_ARGS__)
#ifdef __cplusplus
extern "C" {
#endif

static const char* className = "pineapple/bd/com/pineapple/utils/JniUtils";

JNIEXPORT jstring JNICALL getKey(JNIEnv * env, jclass obj) {
	return env->NewStringUTF("56d23db34c49ab0334c427c034956b15");
}



static JNINativeMethod gMethods[] = { { "getKey", "()Ljava/lang/String;",
		(void*) getKey }};



// This function only registers the native methods, and is called from JNI_OnLoad
JNIEXPORT int JNICALL register_location_methods(JNIEnv *env)
{
	jclass clazz;

	/* look up the class */
	clazz = env->FindClass(className);
	//clazz = env->FindClass(env, className);
	if (clazz == NULL) {
		LOGD("Can't find class %s\n", className);return -1;
	}

	LOGD("register native methods");

	/* register all the methods */
	if (env->RegisterNatives(clazz, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) != JNI_OK) {
		LOGD("Failed registering methods for %s\n", className); return -1;
	}

	/* fill out the rest of the ID cache */
	return 0;
}

jint JNI_OnLoad(JavaVM* vm, void *reserved) {
	JNIEnv* env = NULL;
	jint result = -1;

	LOGD("%s: +has loaded", __FUNCTION__);

	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		LOGD("ERROR: GetEnv failed.\n");
		return result;
	}

	if (register_location_methods(env) < 0) {
		LOGD("ERROR: register location methods failed.\n");
		return result;
	}
	return JNI_VERSION_1_4;
}

void JNI_OnUnload(JavaVM* vm, void *reserved) {
	return;
}
#ifdef __cplusplus
}
#endif

