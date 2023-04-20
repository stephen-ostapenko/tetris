package osss.tetris;

import com.jogamp.opengl.GL3;

import java.util.Random;

/*
shape:
....
.##.
.##.
....
 */
class BoxFigure extends MovingFigure {
    BoxFigure(GL3 gl, Grid grid, Random rnd) {
        super(gl, grid, 4, rnd);
        rowOffset = new int[]{0, -1, -1, 0};
        colOffset = new int[]{0, 0, 1, 1};
    }

    @Override
    protected void rotate() {}
}
