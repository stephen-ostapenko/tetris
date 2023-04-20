package osss.tetris;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.jogamp.opengl.GL.GL_TRUE;
import static com.jogamp.opengl.GL2ES2.*;
import static com.jogamp.opengl.GL3.GL_GEOMETRY_SHADER;

// some utils for OpenGL
public class Util {
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    public static int createShader(GL3 gl, int shaderType, String source) {
        int shader = gl.glCreateShader(shaderType);
        String[] lines = { source };
        IntBuffer length = GLBuffers.newDirectIntBuffer(new int[]{ lines[0].length() });
        gl.glShaderSource(shader, 1, lines, length);
        gl.glCompileShader(shader);

        IntBuffer status = GLBuffers.newDirectIntBuffer(1);
        gl.glGetShaderiv(shader, GL_COMPILE_STATUS, status);

        if (status.get(0) != GL_TRUE) {
            IntBuffer infoLogLength = GLBuffers.newDirectIntBuffer(1);
            gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, infoLogLength);

            ByteBuffer bufferInfoLog = GLBuffers.newDirectByteBuffer(infoLogLength.get(0));
            gl.glGetShaderInfoLog(shader, infoLogLength.get(0), null, bufferInfoLog);
            byte[] bytes = new byte[infoLogLength.get(0)];
            bufferInfoLog.get(bytes);
            String strInfoLog = new String(bytes);

            String strShaderType = "";
            switch (shaderType) {
                case GL_VERTEX_SHADER:
                    strShaderType = "vertex";
                    break;
                case GL_GEOMETRY_SHADER:
                    strShaderType = "geometry";
                    break;
                case GL_FRAGMENT_SHADER:
                    strShaderType = "fragment";
                    break;
            }
            System.err.println("Compiler failure in " + strShaderType + " shader: " + strInfoLog);

            infoLogLength.clear();
            bufferInfoLog.clear();
        }

        length.clear();
        status.clear();

        return shader;
    }

    public static int createProgram(GL3 gl, List<Integer> shaderList) {
        int program = gl.glCreateProgram();
        shaderList.forEach(shader -> gl.glAttachShader(program, shader));
        gl.glLinkProgram(program);

        IntBuffer status = GLBuffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(program, GL_LINK_STATUS, status);

        if (status.get(0) != GL_TRUE) {
            IntBuffer infoLogLength = GLBuffers.newDirectIntBuffer(1);
            gl.glGetProgramiv(program, GL_INFO_LOG_LENGTH, infoLogLength);

            ByteBuffer bufferInfoLog = GLBuffers.newDirectByteBuffer(infoLogLength.get(0));
            gl.glGetProgramInfoLog(program, infoLogLength.get(0), null, bufferInfoLog);
            byte[] bytes = new byte[infoLogLength.get(0)];
            bufferInfoLog.get(bytes);
            String strInfoLog = new String(bytes);

            System.err.println("Linker failure: " + strInfoLog);

            infoLogLength.clear();
            bufferInfoLog.clear();
        }

        status.clear();

        return program;
    }

    public static int genVAO(GL3 gl) {
        IntBuffer buf = GLBuffers.newDirectIntBuffer(1);
        gl.glGenVertexArrays(1, buf);
        int vao = buf.get(0);
        buf.clear();
        return vao;
    }
}
