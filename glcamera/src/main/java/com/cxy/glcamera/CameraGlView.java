package com.cxy.glcamera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class CameraGlView extends GLSurfaceView {
    public CameraGlView(Context context) {
        this(context,null);
    }

    public CameraGlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGlSurfaceView();
    }

    /**
     * 初始化GLView 1、设置Randerdr 2、设置渲染模式 3、设置OpenGL版本
     */
    private void initGlSurfaceView() {
        setEGLContextClientVersion(2);
        setRenderer(new CameraGLRender(this));
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
