package com.cxy.glcamera.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.cxy.glcamera.R;

/**
 * 相机采集Filter 指定视图如何渲染
 */
public class CameraFilter extends AbstractFBOFilter {
    private float[] mtx;
    private int vMatrix;

    public CameraFilter(Context context) {
        super(context, R.raw.camera_vert, R.raw.camera_frag);
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

    @Override
    protected void beforeDraw() {
        //绘制定制内容
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
    }
}
