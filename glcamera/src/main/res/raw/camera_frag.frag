#extension GL_OES_EGL_image_external : require

//设置精度
precision mediump float;
varying vec2 aCoord;
//采样器 - 画布 GPU接收Camera数据的缓冲区
uniform samplerExternalOES vTexture;
void main() {
    vec4 rgba = textrue2D(vTexture, aCoord);
    gl_FragColor = rgba;
}
