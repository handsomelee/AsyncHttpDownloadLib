/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <android/log.h>

#define  LOG_TAG    "hello_jni"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

static const char *classPathName = "com/example/nativedemo/JNIActivity";
//com.example.nativedemo

jstring nativeGetString(JNIEnv* env, jobject thiz) {
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
#define ABI "armeabi-v7a/NEON"
#else
#define ABI "armeabi-v7a"
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__mips__)
#define ABI "mips"
#else
#define ABI "unknown"
#endif
	return (*env)->NewStringUTF(env,
			"Hello from JNI !  Compiled with ABI " ABI ".");
}

static jintArray nativeGreyScale(JNIEnv* env, jintArray buf,
		jint w, jint h) {
	jint *cbuf;
	cbuf = (*env)->GetIntArrayElements(env, buf, JNI_FALSE);
	if (cbuf == NULL) {
		return 0; /* exception occurred */
	}
	int alpha = 0xFF << 24;
	int i;
	int j;
	int color;
	int red;
	int green;
	int blue;
	int size;
	for (i = 0; i < h; i++) {
		for (j = 0; j < w; j++) {
			// 获得像素的颜色
			color = cbuf[w * i + j];
			red = ((color & 0x00FF0000) >> 16);
			green = ((color & 0x0000FF00) >> 8);
			blue = color & 0x000000FF;
			color = (red + green + blue) / 3;
			color = alpha | (color << 16) | (color << 8) | color;
			cbuf[w * i + j] = color;
		}
	}
	size = w * h;
	jintArray result = (*env)->NewIntArray(env, size);
	(*env)->SetIntArrayRegion(env, result, 0, size, cbuf);
	(*env)->ReleaseIntArrayElements(env, buf, cbuf, 0);
	return result;
}

// Java和JNI函数的绑定表
static JNINativeMethod method_table[] = { { "nativeGetString",
		"()Ljava/lang/String;", (void*) nativeGetString }, { "nativeGreyScale",
		"([III)[I", (void*) nativeGreyScale } };

static int registerNativeMethods(JNIEnv* env, const char* className,
		JNINativeMethod* gMethods, int numMethods) {
	jclass clazz;
	clazz = (*env)->FindClass(env, className);
	if (clazz == NULL) {
		return JNI_FALSE;
	}
	if ((*env)->RegisterNatives(env, clazz, gMethods, numMethods) < 0) {
		return JNI_FALSE;
	}
	return JNI_TRUE;
}

static int register_native_methods(JNIEnv* env) {
	LOGI("register_native_methods()");
	if (!registerNativeMethods(env, classPathName, method_table,
			sizeof(method_table) / sizeof(method_table[0]))) {
		return JNI_FALSE;
	}
	return JNI_TRUE;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	LOGI("JNI_OnLoad()");
	JNIEnv* env = NULL;
	jint result = -1;

	if ((*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return result;
	}
	register_native_methods(env);

	// 返回jni的版本
	return JNI_VERSION_1_4;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
	LOGI("JNI_OnUnload()");
}
