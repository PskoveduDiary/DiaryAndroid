#include <jni.h>
#include <string.h>
#include<string.h>
#include<stdlib.h>
#include<math.h>

char* getHalfOfCharSeq(const char* seq)
{
    int len = strlen(seq);
    int halfLen = ceil(len / 2);
    char* halfSeq = (char*)malloc(halfLen + 1);

    for(int i = 0; i < halfLen; i++)
    {
        halfSeq[i] = seq[i];
    }
    halfSeq[halfLen] = '\0';

    return halfSeq;
}


jstring Java_com_alex_materialdiary_sys_common_cryptor_SuperCrypt_makeBlackMagic(JNIEnv *env, jobject javaThis, jstring plainText) {
    const char *plainTextChars = getHalfOfCharSeq((*env)->GetStringUTFChars(env, plainText, 0));
    const char *keyChars = "ru.integrics.mobileschool";
    int plainTextLength = strlen(plainTextChars);
    int keyLength = strlen(keyChars);

    char encryptedText[plainTextLength + 1];

    for (int i = 0; i < plainTextLength; i++) {
        encryptedText[i] = plainTextChars[i] ^ keyChars[i % keyLength];
    }
    encryptedText[plainTextLength] = '\0';

    (*env)->ReleaseStringUTFChars(env, plainText, plainTextChars);

    return (*env)->NewStringUTF(env, encryptedText);
}