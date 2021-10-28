package com.cxy.glcamera.filter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.cxy.glcamera.utils.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 过滤器 - 基类
 */
public class AbstractFilter {
    private static final String TAG = "AbstractFilter";

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
    protected int program;
    private int vPosition;
    private int vCoord;

    private int vTexture;

    private int mWidth, mHeight;


    private int mVertexShaderId, mFragmentShaderId;

    public AbstractFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        this.mVertexShaderId = vertexShaderId;
        this.mFragmentShaderId = fragmentShaderId;
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

        String vertexShader = OpenGLUtils.readRawTextFile(context, mVertexShaderId);

        String fragShader = OpenGLUtils.readRawTextFile(context, mFragmentShaderId);

        program = OpenGLUtils.loadProgram(vertexShader, fragShader);

        vPosition = GLES20.glGetAttribLocation(program, "vPosition");

        vCoord = GLES20.glGetAttribLocation(program, "vCoord");


        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
    }


    public void setSize(int width, int height) {
        Log.i(TAG, "setSizeChange: " + width + ",height: " + height);
        this.mWidth = width;
        this.mHeight = height;
    }


    /**
     * 开始绘制
     *
     * @param texture
     * @return
     */
    public int onDraw(int texture) {
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glUseProgram(program);

        //设置各种数据verTexBuffer ???
        verTexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, verTexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(texture);
        //设置采样 - 设置采样缓存区
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1f(vTexture, 0);

        beforeDraw();
        //通知会话  ？ count
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        return texture;
    }

    /**
     * 提供方法给上层去做一些定制需求
     */
    protected void beforeDraw() {

    }

    ;
}
