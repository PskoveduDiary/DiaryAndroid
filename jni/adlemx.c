//
// Created by DianaSilna on 11.02.2023.
//
#include<jni.h>
#include<string.h>

jstring Java_com_alex_materialdiary_sys_common_cryptor_SuperCrypt_cry(JNIEnv *env, jobject guid){
    return (*env)->NewStringUTF(env, "test");
}

