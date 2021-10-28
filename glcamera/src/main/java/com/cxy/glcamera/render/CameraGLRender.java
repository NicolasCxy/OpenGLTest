package com.cxy.glcamera.render;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.os.Environment;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.cxy.glcamera.CameraGlView;
import com.cxy.glcamera.filter.RecordFilter;
import com.cxy.glcamera.utils.CameraHelper;
import com.cxy.glcamera.filter.CameraFilter;
import com.cxy.glcamera.utils.MediaRecorder;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraGLRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private CameraGlView mHostGlView;
    private CameraHelper cameraHelper;
    private SurfaceTexture mSurfaceTexture;
    private float[] mtx = new float[16];
    private int[] texture;
    private CameraFilter mCameraFilter;
    private RecordFilter mRecordFilter;

    public CameraGLRender(CameraGlView cameraGlView) {
        this.mHostGlView = cameraGlView;
        //获取相机相关
        LifecycleOwner lifecycleOwner = (LifecycleOwner) mHostGlView.getContext();
        cameraHelper = new CameraHelper(lifecycleOwner,this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        texture = new int[1];
        mSurfaceTexture.attachToGLContext(texture[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);

        mCameraFilter = new CameraFilter(mHostGlView.getContext());
        mRecordFilter = new RecordFilter(mHostGlView.getContext());

        String path = new File(Environment.getExternalStorageDirectory(),
                "inputTest.mp4").getAbsolutePath();
        MediaRecorder mediaRecorder  = new MediaRecorder(mHostGlView.getContext(),
                path, EGL14.eglGetCurrentContext(),1280,720);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mCameraFilter.setSize(width,height);
        mRecordFilter.setSize(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mSurfaceTexture.updateTexImage();
        //获取变化矩阵，为了后续将openGL 世界坐标与 纹理坐标做转换 求出真实的像素点
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter.setTransformMatrix(mtx);

        int fobId = mCameraFilter.onDraw(texture[0]);
        //让其渲染
        mRecordFilter.onDraw(fobId);


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
