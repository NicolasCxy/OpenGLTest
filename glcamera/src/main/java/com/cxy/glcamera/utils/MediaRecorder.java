package com.cxy.glcamera.utils;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.io.IOException;

/**
 * 从FBO取数据并且渲染
 */
public class MediaRecorder {

    private Context mContext;
    private int mWidth;
    private int mHeight;
    private String mPath;
    private Surface mSurface;
    private Handler mHandler;
    private EGLContext mGlContext;
    private float mSpeed;
    private MediaCodec mediaCodec;
    private MediaMuxer mMuxer;
    private EglEnv engEnv;

    public MediaRecorder(Context context, String path, EGLContext glContext, int width, int height) {
        mContext = context.getApplicationContext();
        mPath = path;
        mWidth = width;
        mHeight = height;
        mGlContext = glContext;
    }

    /**
     * 开始录制
     *
     * @param speed
     */
    public void startRecording(float speed) {
        mSpeed = speed;
        MediaFormat format =
                MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight);

        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.
                COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 1500_000);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 25);

        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

            //提供surface与FOB关联，用来接收处理过的Video数据
            mSurface = mediaCodec.createInputSurface();

            mMuxer = new MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mediaCodec.start();

            //创建EGL环境，因为当前是在主线程无法与GPU通讯
            HandlerThread handlerThread= new HandlerThread("GL-THREAD");
            handlerThread.start();
            Handler mGlHandler = new Handler(handlerThread.getLooper());

            mGlHandler.post(new Runnable() {
                @Override
                public void run() {
                    engEnv = new EglEnv(mContext,mGlContext, mSurface,mWidth, mHeight);
                    //TODO://上一次在这个位置！
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void stopRecord() {

    }
}
