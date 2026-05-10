#include <jni.h>
#include <stdlib.h>
#include "pl_pwr_ite_dynak_filters_MedianFilter.h"

int compare(const void *a, const void *b) {
    return (*(int *)a - *(int *)b);
}

JNIEXPORT jintArray JNICALL
Java_pl_pwr_ite_dynak_filters_MedianFilter_applyFilterNative
  (JNIEnv *env, jobject obj, jintArray sourceArray,
   jint numPasses, jint width, jint height, jint radius)
{
    int size = (*env)->GetArrayLength(env, sourceArray);

    jint *source = (*env)->GetIntArrayElements(env, sourceArray, NULL);

    jint *target = malloc(size * sizeof(jint));
    jint *temp   = malloc(size * sizeof(jint));

    // copy source → target
    for (int i = 0; i < size; i++) {
        target[i] = source[i];
    }

    int kernelSize = (2 * radius + 1) * (2 * radius + 1);
    jint *neighborhood = malloc(kernelSize * sizeof(jint));

    for (int pass = 0; pass < numPasses; pass++) {

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int count = 0;

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {

                        int px = x + dx;
                        int py = y + dy;

                        if (px < 0) px = 0;
                        if (px >= width) px = width - 1;
                        if (py < 0) py = 0;
                        if (py >= height) py = height - 1;

                        neighborhood[count++] = target[py * width + px];
                    }
                }

                qsort(neighborhood, kernelSize, sizeof(jint), compare);

                temp[y * width + x] = neighborhood[kernelSize / 2];
            }
        }

        for (int i = 0; i < size; i++) {
            target[i] = temp[i];
        }
    }

    jintArray result = (*env)->NewIntArray(env, size);
    (*env)->SetIntArrayRegion(env, result, 0, size, target);

    (*env)->ReleaseIntArrayElements(env, sourceArray, source, 0);

    free(target);
    free(temp);
    free(neighborhood);

    return result;
}