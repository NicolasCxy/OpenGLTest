#extension GL_OES_EGL_image_external : require
varying vec2 aCood;
uniform sampler2D  vTexture;// samplerExternalOES: 图片， 采样器

void main() {
    vec4 rgba = texture2D(vTexture, aCood);
    gl_FragColor = rgba;
}
