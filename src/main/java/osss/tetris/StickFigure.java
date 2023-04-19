package osss.tetris;

import com.jogamp.opengl.GL3;

import java.util.Random;

class StickFigure extends MovingFigure {
    StickFigure(GL3 gl, Grid grid, Random rnd) {
        super(gl, grid, 4, rnd);
        rowOffset = new int[]{0, 0, 0, 0};
        colOffset = new int[]{0, -1, 1, 2};
    }
}

