package com.cxy.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRender implements GLSurfaceView.Renderer {
    private static final String TAG = "TriangleRender";

    private final String vertexShaderCode =
            "attribute vec4 vPosition; " +
                    "void main() {" +
                    "gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    static float triangleCoords[] = {
            -0.3f, 0.7f, 0.0f,
            0.3f, 0.7f, 0.0f,
            0.0f, -0.5f, 0.0f

    };
    float color[] = {1.0f, 1.0f, 0.0f, 1.0f};

    private int mProgram;
    private int mPositionHandle;
    private FloatBuffer vertexBuffer;
    private int mColorHandle;

    /**
     * 1. 创建顶点
     * 2. 创建byteBuffer 获取 xxBuffer
     * 3. 将数据放到 xxBuffer 中
     * 4. 创建顶点着色器，将gl sl 代码复制到顶点着色器中
     * 5. 创建片源着色器，将gl sl 代码复制到顶点着色器中
     * 6. 进行将着色器进行关联
     * 7. onDrawFrame 方法中对刚才设置的着色器进行使用
     * 8.
     */

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        //重新排序
        byteBuffer.order(ByteOrder.nativeOrder());
        //生成FloatBuffer，通过改buffer与GPU进行通讯
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        //创建着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
//        int program = GLES20.glCreateProgram();
//        GLES20.glAttachShader(program,vertexShader);
//        GLES20.glAttachShader(program,fragmentShader);
//        GLES20.glLinkProgram(program);



        //创建一个GL程序，将片源与顶点着色器进行关联
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

//        GLES20.glUseProgram(mProgram);
//        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
//        GLES20.glEnableVertexAttribArray(mProgram);
//        GLES20.glVertexAttribPointer(vPosition,3,GLES20.GL_FLOAT,false,0,vertexBuffer);


    }

    private int loadShader(int type, String source) {
        //当前对象索引
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //启动GL程序
        GLES20.glUseProgram(mProgram);
        //给gl语法中对应的字段赋值
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //设置三角形坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
                false, 12, vertexBuffer);
        //获取属性给片元颜色值赋值
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //绘制颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
