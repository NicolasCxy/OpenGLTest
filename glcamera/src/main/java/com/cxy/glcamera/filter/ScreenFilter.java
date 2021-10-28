package com.cxy.glcamera.filter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.cxy.glcamera.utils.OpenGLUtils;
import com.cxy.glcamera.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ScreenFilter {
    private static final String TAG = "ScreenFilter";
    private float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };

    private float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };
    private FloatBuffer verTexBuffer, textureBuffer;
    private int program;
    private int vPosition;
    private int vCoord;
    private int vMatrix;
    private int vTexture;

    private int mWidth, mHeight;
    private float[] mtx;

    public ScreenFilter(Context context) {
        initGLContent(context);
    }

    /**
     * 初始化OpenGL相关（坐标、着色器等）
     */
    private void initGLContent(Context context) {
        //创建buffer 填充坐标信息，后续要传递给GPU
        verTexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verTexBuffer.clear();
        verTexBuffer.put(VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.clear();
        textureBuffer.put(TEXTURE);

        String vertexShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_vert);

        String fragShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_frag);

        program = OpenGLUtils.loadProgram(vertexShader, fragShader);

        vPosition = GLES20.glGetAttribLocation(program, "vPosition");

        vCoord = GLES20.glGetAttribLocation(program, "vCoord");

        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");

        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
    }

    public void setSize(int width, int height) {
        Log.i(TAG, "setSizeChange: " + width + ",height: " + height);
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

    /**
     * 开始绘制
     * @param texture
     */
    public void onDraw(int texture) {
        GLES20.glViewport(0,0,mWidth,mHeight);
        GLES20.glUseProgram(program);

        //设置各种数据verTexBuffer ???
        verTexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,2*4,verTexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,2*4,textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(texture);
        //设置采样 - 设置采样缓存区
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture);
        GLES20.glUniform1f(vTexture,0);
        //设置矩阵
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mtx,0);

        //通知会话  ？ count
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);


    }
}
