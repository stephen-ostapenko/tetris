package osss.tetris;

import com.jogamp.opengl.GL3;

import java.util.Arrays;

import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;

public class Background {
    private final int program;

    private final int vao;
    private final int colorLocation;

    Background(GL3 gl) {
        String vertexShaderSource = "" +
                "#version 330 core\n" +
                "\n" +
                "void main() {\n" +
                "    if (gl_VertexID == 0) {\n" +
                "        gl_Position = vec4(-1.0, -1.0, 0.0, 1.0);\n" +
                "    } else if (gl_VertexID == 1) {\n" +
                "        gl_Position = vec4(-1.0, 1.0, 0.0, 1.0);\n" +
                "    } else if (gl_VertexID == 2) {\n" +
                "        gl_Position = vec4(1.0, -1.0, 0.0, 1.0);\n" +
                "    } else if (gl_VertexID == 3) {\n" +
                "        gl_Position = vec4(1.0, 1.0, 0.0, 1.0);\n" +
                "    } else {\n" +
                "        // error\n" +
                "    }\n" +
                "}";

        String fragmentShaderSource = "" +
                "#version 330 core\n" +
                "\n" +
                "uniform vec4 color;\n" +
                "\n" +
                "layout (location = 0) out vec4 out_color;\n" +
                "\n" +
                "void main() {\n" +
                "    out_color = color;\n" +
                "}";

        int vertexShader = Util.createShader(gl, GL3.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = Util.createShader(gl, GL3.GL_FRAGMENT_SHADER, fragmentShaderSource);

        program = Util.createProgram(gl, Arrays.asList(vertexShader, fragmentShader));

        colorLocation = gl.glGetUniformLocation(program, "color");

        vao = Util.genVAO(gl);
    }

    public void draw(GL3 gl) {
        gl.glUseProgram(program);

        gl.glUniform4f(colorLocation, 0, 0, 0, 1);

        gl.glBindVertexArray(vao);
        gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }
}
