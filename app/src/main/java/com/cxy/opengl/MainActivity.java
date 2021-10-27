package com.cxy.opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private Handler mHanlder  = new Handler();
    private void initView() {
        mGfView = (GLSurfaceView) findViewById(R.id.gf_view);
        mGfView.setEGLContextClientVersion(2);
        mGfView.setRenderer(new TriangleRender());
        /*渲染方式，RENDERMODE_WHEN_DIRTY表示被动渲染，只有在调用requestRender或者onResume等方法时才会进行渲染。RENDERMODE_CONTINUOUSLY表示持续渲染*/
        mGfView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGfView.requestRender();
        Looper.loop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGfView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGfView.onPause();
    }


}