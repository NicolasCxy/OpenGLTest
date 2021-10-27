package com.cxy.glcamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraGLRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private CameraGlView mHostGlView;
    private CameraHelper cameraHelper;
    private SurfaceTexture mSurfaceTexture;
    private float[] mtx = new float[16];
    private ScreenFilter screenFilter;

    public CameraGLRender(CameraGlView cameraGlView) {
        this.mHostGlView = cameraGlView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //获取相机相关
        LifecycleOwner lifecycleOwner = (LifecycleOwner) mHostGlView.getContext();
        cameraHelper = new CameraHelper(lifecycleOwner,this);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        screenFilter = new ScreenFilter(mHostGlView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mSurfaceTexture.updateTexImage();
        //获取变化矩阵，为了后续将openGL 世界坐标与 纹理坐标做转换 求出真实的像素点
        mSurfaceTexture.getTransformMatrix(mtx);

    }

    /**
     * 预览可用后会回调这个
     * @param output
     */
    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        /**
         * 可以通过 SurfaceTexture 将数据直接传到 GPU缓存区(openGL)，这样做更加高效
         */
        mSurfaceTexture = output.getSurfaceTexture();
    }

    /**
     * 有数据更新了
     * @param surfaceTexture
     */
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //手动触发刷新，这时候会触发onDrawFrame
        mHostGlView.requestRender();
    }
}
