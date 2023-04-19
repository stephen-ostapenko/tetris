package osss.tetris;

import com.jogamp.opengl.GL3;

import java.util.ArrayList;
import java.util.Arrays;

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

    ArrayList<Cell[]> grid = new ArrayList<>();

    private void fillCells() {
        while (grid.size() < HEIGHT) {
            Cell[] row = new Cell[WIDTH];
            for (int i = 0; i < WIDTH; i++) {
                row[i] = new Cell(true, 0, 0, 0, 1);
            }
            grid.add(row);
        }
    }

    public boolean checkCell(int row, int col) {
        if (row < 0) {
            return false;
        }
        if (row >= HEIGHT) {
            return true;
        }
        if (col < 0 || col >= WIDTH) {
            return false;
        }
        return grid.get(row)[col].free;
    }

    private final int program;

    private final int vao;
    private final int fieldWidthLocation, fieldHeightLocation, rowLocation, colLocation;
    private final int colorLocation;

    public Grid(GL3 gl) {
        fillCells();

        String vertexShaderSource = "" +
                "#version 330 core\n" +
                "\n" +
                "uniform int field_width;\n" +
                "uniform int field_height;\n" +
                "uniform int row;\n" +
                "uniform int col;\n" +
                "\n" +
                "void main() {\n" +
                "    float cell_width = 2.0 / field_width;\n" +
                "    float cell_height = 2.0 / field_height;\n" +
                "\n" +
                "    if (gl_VertexID == 0) {\n" +
                "        gl_Position = vec4(col * cell_width, row * cell_height, 0.0, 1.0);\n" +
                "    } else if (gl_VertexID == 1) {\n" +
                "        gl_Position = vec4(col * cell_width, (row + 1) * cell_height, 0.0, 1.0);\n" +
                "    } else if (gl_VertexID == 2) {\n" +
                "        gl_Position = vec4((col + 1) * cell_width, row * cell_height, 0.0, 1.0);\n" +
                "    } else if (gl_VertexID == 3) {\n" +
                "        gl_Position = vec4((col + 1) * cell_width, (row + 1) * cell_height, 0.0, 1.0);\n" +
                "    } else {\n" +
                "        // error\n" +
                "    }\n" +
                "    gl_Position -= vec4(1.0, 1.0, 0.0, 0.0);\n" +
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
                if (grid.get(i)[j].free) {
                    continue;
                }

                gl.glUniform1i(rowLocation, i);
                gl.glUniform1i(colLocation, j);
                gl.glUniform4f(colorLocation, grid.get(i)[j].red, grid.get(i)[j].green, grid.get(i)[j].blue, grid.get(i)[j].alpha);

                gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
            }
        }
    }

    public boolean addFigure(MovingFigure figure) {
        for (int i = 0; i < figure.cellsCnt; i++) {
            int curRow = figure.rowPos + figure.rowOffset[i];
            int curCol = figure.colPos + figure.colOffset[i];

            if (curRow < 0 || curRow >= HEIGHT) {
                return false;
            }
            if (curCol < 0 || curCol >= WIDTH) {
                return false;
            }

            grid.get(curRow)[curCol].free = false;
            grid.get(curRow)[curCol].red = figure.red;
            grid.get(curRow)[curCol].green = figure.green;
            grid.get(curRow)[curCol].blue = figure.blue;
            grid.get(curRow)[curCol].alpha = figure.alpha;
        }

        eraseAllFilledRows();

        return true;
    }

    private void eraseAllFilledRows() {
        grid.removeIf(cArr -> Arrays.stream(cArr).noneMatch(it -> it.free));
        fillCells();
    }
}
