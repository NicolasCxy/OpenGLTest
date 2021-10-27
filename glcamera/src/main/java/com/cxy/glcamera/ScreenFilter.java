package com.cxy.glcamera;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ScreenFilter {

    private float[] VERTEX = {
            -1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };

    private float[] TEXTURE = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };
    private FloatBuffer verTexBuffer, textureBuffer;


    public ScreenFilter(Context context) {
        initGLContent();
    }

    /**
     * 初始化OpenGL相关（坐标、着色器等）
     */
    private void initGLContent() {
        //创建buffer 填充坐标信息，后续要传递给GPU
        verTexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verTexBuffer.clear();
        verTexBuffer.put(VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(4 * 4 * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verTexBuffer.clear();
        verTexBuffer.put(VERTEX);



    }
}
