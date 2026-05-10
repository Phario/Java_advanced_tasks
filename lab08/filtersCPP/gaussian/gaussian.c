#include <jni.h>
#include <stdlib.h>
#include "pl_pwr_ite_dynak_filters_GaussianFilter.h"

void horizontalBoxBlur(jint *source, jint *target,
                       int width, int height, int radius);

void verticalBoxBlur(jint *source, jint *target,
                     int width, int height, int radius);

JNIEXPORT jintArray JNICALL
Java_pl_pwr_ite_dynak_filters_GaussianFilter_applyFilterNative
  (JNIEnv *env, jobject obj, jintArray sourceArray,
   jint numPasses, jint width, jint height, jint radius)
{
    int size = (*env)->GetArrayLength(env, sourceArray);

    jint *source = (*env)->GetIntArrayElements(env, sourceArray, NULL);

    jint *target = (jint *)malloc(size * sizeof(jint));
    jint *temp   = (jint *)malloc(size * sizeof(jint));

    for (int i = 0; i < size; i++) {
        target[i] = source[i];
    }

    for (int pass = 0; pass < numPasses; pass++) {
        horizontalBoxBlur(target, temp, width, height, radius);
        verticalBoxBlur(temp, target, width, height, radius);
    }

    jintArray result = (*env)->NewIntArray(env, size);
    (*env)->SetIntArrayRegion(env, result, 0, size, target);

    (*env)->ReleaseIntArrayElements(env, sourceArray, source, 0);
    free(target);
    free(temp);

    return result;
}

void horizontalBoxBlur(jint *source, jint *target,
                       int width, int height, int radius)
{
    double scale = 1.0 / (radius * 2 + 1);

    for (int y = 0; y < height; y++) {
        int windowSum = 0;
        int offset = y * width;

        for (int x = -radius; x <= radius; x++) {
            int safeX = x < 0 ? 0 : (x >= width ? width - 1 : x);
            windowSum += source[offset + safeX];
        }

        for (int x = 0; x < width; x++) {
            target[offset + x] = (int)(windowSum * scale + 0.5);

            int leftX  = x - radius < 0 ? 0 : x - radius;
            int rightX = x + radius + 1 >= width ? width - 1 : x + radius + 1;

            windowSum -= source[offset + leftX];
            windowSum += source[offset + rightX];
        }
    }
}

void verticalBoxBlur(jint *source, jint *target,
                     int width, int height, int radius)
{
    double scale = 1.0 / (radius * 2 + 1);

    for (int x = 0; x < width; x++) {
        int windowSum = 0;

        for (int y = -radius; y <= radius; y++) {
            int safeY = y < 0 ? 0 : (y >= height ? height - 1 : y);
            windowSum += source[safeY * width + x];
        }

        for (int y = 0; y < height; y++) {
            target[y * width + x] = (int)(windowSum * scale + 0.5);

            int topY    = y - radius < 0 ? 0 : y - radius;
            int bottomY = y + radius + 1 >= height ? height - 1 : y + radius + 1;

            windowSum -= source[topY * width + x];
            windowSum += source[bottomY * width + x];
        }
    }
}