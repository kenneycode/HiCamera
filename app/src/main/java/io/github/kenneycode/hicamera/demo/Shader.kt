package io.github.kenneycode.hicamera.demo

class Shader {

    companion object {

        val EFFECT_FRAGMENT_SHADER =
                "#version 300 es\n" +
                "precision mediump float;\n" +
                "in vec2 v_textureCoordinate;\n" +
                "layout(location = 0) out vec4 fragColor;\n" +
                "uniform sampler2D u_texture;\n" +
                "void main() {\n" +
                "    vec4 c = texture(u_texture, v_textureCoordinate);\n" +
                "    c.b = 0.5;\n" +
                "    fragColor = c;\n" +
                "}"

    }

}