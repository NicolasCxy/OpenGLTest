package com.cxy.glcamera.utils;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.view.Surface;

/**
 * 创建EGL
 * - EGL是 OpenGL与 本地视图交互的桥梁，版本独立
 * - 主要作用是创建EGL_Context（绘制管线各种状态）、frameBuffer，属性配置、绘制目标surface
 */
public class EglEnv {

    private EGLDisplay mEglDisplay;

    public EglEnv(Context context, EGLContext glContext, Surface surface, int width, int height) {
        //获取前端绘制目标 - 物理屏幕
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if(mEglDisplay == EGL14.EGL_NO_DISPLAY){
            throw new RuntimeException("eglGetDisplay failed!");
        }
        //初始化
        int[] version = new int[2];
        boolean initStatus = EGL14.eglInitialize(mEglDisplay, version, 0, version, 1);
        if(!initStatus){
            mEglDisplay = null;
            throw new RuntimeException("eglInitialize FAILED!!");
        }


    }
}


