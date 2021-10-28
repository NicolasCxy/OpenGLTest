package com.cxy.glcamera.filter;

import android.content.Context;
import android.opengl.GLES20;

/**
 * 创建FBO
 * - FBO是一个容器，可以对纹理进行多次绘制，然后在渲染到屏幕上或转发到其他部分
 * - FBO创建绑定纹理 -> 视图纹理 -> FBO拦截 -> FBO处理 -> 从FBO纹理中拿到相关数据：1、直播 2、显示 3、保存等
 * - 多数情况下我们要对纹理进行多次绘制，最后才显示到屏幕上，绘制过程中我们不需要显示给用户，所以就需要一个缓冲对象（离屏渲染），
 * 当数据绘制完毕后在显示到屏幕上。
 * - 这样处理更加高效，避免闪屏，而且还可以共享到其他地方去处理
 */
public class AbstractFBOFilter extends AbstractFilter {

    private int fboId;
    private int fobTextureId;
    private int[] frameTextures;
    private int[] frameBuffer;

    public AbstractFBOFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
    }

    /**
     * 创建FBO
     *
     * @param width
     * @param height
     */
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        //1. 创建FBO
        frameBuffer = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffer, 0);
        fboId = frameBuffer[0];

        //2. 绑定FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);

        //3.创建FBO纹理
        fobTextureId = createTexture();

        //开始绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fobTextureId);

        //设置纹理类型 （设置FBO大小）
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);

        //4.纹理  + FBO 关联
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, fobTextureId,
                0);

        //解除绑定
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

    }

    private int createTexture() {
        frameTextures = new int[1];
        GLES20.glGenTextures(frameTextures.length, frameTextures, 0);
        //设置纹理放大缩小限制
        for (int i = 0; i < frameTextures.length; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTextures[i]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINES); //放大过滤
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINES); //缩小过滤
            //解除绑定
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }

        return frameTextures[0];
    }

    @Override
    public int onDraw(int texture) {
        //对相机数据进行拦截，让其输入到FBO中
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        super.onDraw(texture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return frameBuffer[0];
    }
}
