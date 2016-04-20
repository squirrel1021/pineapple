LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := jniutils
LOCAL_SRC_FILES+=jniutils.cpp

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)