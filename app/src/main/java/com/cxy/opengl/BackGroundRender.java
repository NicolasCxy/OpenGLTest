package com.cxy.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BackGroundRender implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {       //初始化工作
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {  //大小发小变化
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {   //开始绘制
        GLES20.glClearColor(0f,0f,0f,0f);
    }
}
