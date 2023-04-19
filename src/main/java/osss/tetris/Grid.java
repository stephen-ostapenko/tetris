package osss.tetris;

import com.jogamp.opengl.GL3;

import java.io.IOException;
import java.util.Arrays;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

public class Grid {

    private final int program;

    private final int vao;

    public Grid(GL3 gl) throws IOException {
        String vertexShaderSource = Util.readFile("./src/main/resources/GridVertexShaderSource.cpp");
        String fragmentShaderSource = Util.readFile("./src/main/resources/GridFragmentShaderSource.cpp");

        int vertexShader = Util.createShader(gl, GL3.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = Util.createShader(gl, GL3.GL_FRAGMENT_SHADER, fragmentShaderSource);

        program = Util.createProgram(gl, Arrays.asList(vertexShader, fragmentShader));

        vao = Util.genVAO(gl);
    }

    public void draw(GL3 gl) {
        gl.glUseProgram(program);
        gl.glBindVertexArray(vao);
        gl.glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
