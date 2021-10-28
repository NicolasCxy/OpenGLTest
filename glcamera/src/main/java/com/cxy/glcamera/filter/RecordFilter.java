package com.cxy.glcamera.filter;

import android.content.Context;

import com.cxy.glcamera.R;

/**
 * 从FBO取数据去渲染
 */
public class RecordFilter extends AbstractFilter {

    public RecordFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
    }
}
