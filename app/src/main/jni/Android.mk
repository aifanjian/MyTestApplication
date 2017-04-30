LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := JniUtil
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	G:\githubProject\MyApplication\app\src\main\jni\JniUtil.c \

LOCAL_C_INCLUDES += G:\githubProject\MyApplication\app\src\main\jni
LOCAL_C_INCLUDES += G:\githubProject\MyApplication\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
