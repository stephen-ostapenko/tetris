package osss.tetris;

import com.jogamp.opengl.GL3;

import java.util.Random;

/*
shape:
.....
.###.
.#...
.....
 */
class GFigure1 extends MovingFigure {
    GFigure1(GL3 gl, Grid grid, Random rnd) {
        super(gl, grid, 4, rnd);
        rowOffset = new int[]{0, 0, 0, -1};
        colOffset = new int[]{0, -1, 1, -1};
    }
}
