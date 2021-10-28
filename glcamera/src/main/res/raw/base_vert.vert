
attribute vec4 vPosition;

attribute vec2 vCood;

varying vec2 aCood;

void main() {
    gl_Position = vPosition;
    aCood = vCood;
}
