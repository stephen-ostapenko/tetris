package osss.tetris;

import com.jogamp.opengl.GL3;

import java.io.IOException;
import java.util.Arrays;

import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;

class Grid {
    private static class Cell {
        boolean free;
        float red, green, blue, alpha;

        Cell(boolean _free, float _red, float _green, float _blue, float _alpha) {
            free = _free;
            red = _red; green = _green; blue = _blue; alpha = _alpha;
        }
    }

    public final int WIDTH = 10;
    public final int HEIGHT = 20;

    Cell[][] grid = new Cell[HEIGHT][WIDTH];

    public boolean checkCell(int row, int col) {
        return grid[row][col].free;
    }

    private final int program;

    private final int vao;
    private final int fieldWidthLocation, fieldHeightLocation, rowLocation, colLocation;
    private final int colorLocation;

    public Grid(GL3 gl) throws IOException {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                grid[i][j] = new Cell(true, 0, 0, 0, 1);
            }
        }

        String vertexShaderSource = Util.readFile("./src/main/resources/GridVertexShaderSource.cpp");
        String fragmentShaderSource = Util.readFile("./src/main/resources/GridFragmentShaderSource.cpp");

        int vertexShader = Util.createShader(gl, GL3.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = Util.createShader(gl, GL3.GL_FRAGMENT_SHADER, fragmentShaderSource);

        program = Util.createProgram(gl, Arrays.asList(vertexShader, fragmentShader));

        fieldWidthLocation = gl.glGetUniformLocation(program, "field_width");
        fieldHeightLocation = gl.glGetUniformLocation(program, "field_height");
        rowLocation = gl.glGetUniformLocation(program, "row");
        colLocation = gl.glGetUniformLocation(program, "col");
        colorLocation = gl.glGetUniformLocation(program, "color");

        vao = Util.genVAO(gl);
    }

    public void draw(GL3 gl) {
        gl.glUseProgram(program);

        gl.glUniform1i(fieldWidthLocation, WIDTH);
        gl.glUniform1i(fieldHeightLocation, HEIGHT);

        gl.glBindVertexArray(vao);

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (grid[i][j].free) {
                    continue;
                }

                gl.glUniform1i(rowLocation, i);
                gl.glUniform1i(colLocation, j);
                gl.glUniform4f(colorLocation, grid[i][j].red, grid[i][j].green, grid[i][j].blue, grid[i][j].alpha);

                gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
            }
        }
    }

    public void addFigure(MovingFigure figure) {
        for (int i = 0; i < figure.cellsCnt; i++) {
            int curRow = figure.rowPos + figure.rowOffset[i];
            int curCol = figure.colPos + figure.colOffset[i];

            grid[curRow][curCol].free = false;
            grid[curRow][curCol].red = figure.red;
            grid[curRow][curCol].green = figure.green;
            grid[curRow][curCol].blue = figure.blue;
            grid[curRow][curCol].alpha = figure.alpha;
        }
    }
}
