package osss.tetris;

import com.jogamp.opengl.GL3;

import java.util.Random;

/*
shape:
.....
.##..
..##.
.....
 */
public class SFigure1 extends MovingFigure {
    SFigure1(GL3 gl, Grid grid, Random rnd) {
        super(gl, grid, 4, rnd);
        rowOffset = new int[]{0, 0, 1, 1};
        colOffset = new int[]{0, -1, 0, 1};
    }
}
