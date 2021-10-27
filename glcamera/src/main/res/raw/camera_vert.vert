#version 120

attribute vec4 vPosition;
//纹理坐标
attribute vec4 vCoord;
//矩阵 - 纹理坐标与变换矩阵相乘
uniform mat4 vMatrix;
//像素点
varying vec2 aCoord;

void main() {
    gl_Position = vPosition;
    aCoord =(vMatrix * vCoord).xy;
}
