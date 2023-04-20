package osss.tetris;

import com.jogamp.opengl.GL3;

import java.util.Arrays;
import java.util.Random;

import static com.jogamp.opengl.GL.GL_TRIANGLE_STRIP;

abstract class MovingFigure {
    // link to grid
    protected Grid grid;
    // number of cells in figure (4 in our case)
    protected int cellsCnt;
    // position of main cell of the figure
    protected int rowPos, colPos;
    // offsets between main cell and all other cells
    protected int[] rowOffset, colOffset;
    // needed for checking if transformation or move is available
    protected int[] nxtRowOffset, nxtColOffset;
    // color of the figure
    protected float red, green, blue, alpha;

    // delays between downward moves
    private long delay = 500;
    private long minDelay = 0;
    // last time figure moved downward
    private long lastTime;
    // flag that the figure is already landed
    private boolean figureLanded = false;

    // OpenGL stuff

    private final int program;

    private final int vao;

    private final int fieldWidthLocation, fieldHeightLocation, rowLocation, colLocation;
    private final int colorLocation;

    // choose next figure randomly
    public static MovingFigure getRandomMovingFigure(GL3 gl, Grid grid, Random rnd) {
        int fig = rnd.nextInt(7);
        switch (fig) {
            case 0: return new BoxFigure(gl, grid, rnd);
            case 1: return new TFigure(gl, grid, rnd);
            case 2: return new GFigure1(gl, grid, rnd);
            case 3: return new GFigure2(gl, grid, rnd);
            case 4: return new StickFigure(gl, grid, rnd);
            case 5: return new SFigure1(gl, grid, rnd);
            case 6: return new SFigure2(gl, grid, rnd);
            default: throw new IllegalStateException("Wrong figure type id");
        }
    }

    MovingFigure(GL3 gl, Grid grid, int cellsCnt, Random rnd) {
        // initializing figure position and color
        this.grid = grid;
        this.cellsCnt = cellsCnt;
        rowPos = grid.HEIGHT; colPos = grid.WIDTH / 2 - 1;
        nxtRowOffset = new int[this.cellsCnt]; nxtColOffset = new int[this.cellsCnt];
        red = rnd.nextFloat() * 0.8f + 0.2f;
        green = rnd.nextFloat() * 0.8f + 0.2f;
        blue = rnd.nextFloat() * 0.8f + 0.2f;
        alpha = 1f;

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

        lastTime = System.currentTimeMillis();
    }

    // draw figure
    public void draw(GL3 gl) {
        gl.glUseProgram(program);

        gl.glUniform1i(fieldWidthLocation, grid.WIDTH);
        gl.glUniform1i(fieldHeightLocation, grid.HEIGHT);

        gl.glBindVertexArray(vao);

        for (int i = 0; i < cellsCnt; i++) {
            gl.glUniform1i(rowLocation, rowPos + rowOffset[i]);
            gl.glUniform1i(colLocation, colPos + colOffset[i]);
            gl.glUniform4f(colorLocation, red, green, blue, alpha);

            gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    // move the figure down if it's possible
    // or report that it's landed
    public boolean updateState(boolean force) {
        long curTime = System.currentTimeMillis();
        if ((force && curTime - lastTime >= minDelay) || curTime - lastTime >= delay || figureLanded) {
            lastTime = curTime;

            if (moveDown()) {
                return true;
            } else {
                figureLanded = true;
                return false;
            }
        }
        return true;
    }

    // check thar potential figure position is valid
    protected boolean check() {
        for (int i = 0; i < cellsCnt; i++) {
            int curRow = rowPos + nxtRowOffset[i], curCol = colPos + nxtColOffset[i];

            if (curRow < 0) {
                return false;
            }
            if (curCol < 0 || curCol >= grid.WIDTH) {
                return false;
            }

            if (!grid.checkCell(curRow, curCol)) {
                return false;
            }
        }

        return true;
    }

    // counter-clockwise rotation of the figure
    protected void rotate() {
        for (int i = 0; i < cellsCnt; i++) {
            nxtRowOffset[i] = colOffset[i];
            nxtColOffset[i] = -rowOffset[i];
        }

        if (!check()) {
            return;
        }

        for (int i = 0; i < cellsCnt; i++) {
            rowOffset[i] = nxtRowOffset[i];
            colOffset[i] = nxtColOffset[i];
        }
    }

    protected void moveLeft() {
        for (int i = 0; i < cellsCnt; i++) {
            nxtRowOffset[i] = rowOffset[i];
            nxtColOffset[i] = colOffset[i] - 1;
        }

        if (!check()) {
            return;
        }

        colPos--;
    }

    protected void moveRight() {
        for (int i = 0; i < cellsCnt; i++) {
            nxtRowOffset[i] = rowOffset[i];
            nxtColOffset[i] = colOffset[i] + 1;
        }

        if (!check()) {
            return;
        }

        colPos++;
    }

    // returns false if the figure can't move down
    protected boolean moveDown() {
        for (int i = 0; i < cellsCnt; i++) {
            nxtRowOffset[i] = rowOffset[i] - 1;
            nxtColOffset[i] = colOffset[i];
        }

        if (!check()) {
            return false;
        }

        rowPos--;
        return true;
    }
}

