package osss.tetris;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

import java.util.Random;

import static java.lang.Math.min;
import static java.lang.Math.round;

class MainWindow implements GLEventListener, KeyListener {
    private GLWindow window;
    private Animator animator;

    private Grid grid;
    private MovingFigure movingFigure;
    private Background background;
    private Random rnd;

    // flag that game is running
    private boolean isRunning = false;

    public void setup() {
        GLProfile glProfile = GLProfile.get(GLProfile.GL3);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);

        window.setUndecorated(false);
        window.setAlwaysOnTop(false);
        window.setFullscreen(false);
        window.setPointerVisible(true);
        window.confinePointer(false);
        window.setTitle("tetris");
        window.setSize(1600, 800);

        window.setVisible(true);

        window.addGLEventListener(this);
        window.addKeyListener(this);

        animator = new Animator();
        animator.add(window);
        animator.start();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                new Thread(() -> {

                    //stop the animator thread when user close the window
                    animator.stop();
                    // This is actually redundant since the JVM will terminate when all threads are closed.
                    // It's useful just in case you create a thread, and you forget to stop it.
                    System.exit(0);
                }).start();
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isRunning && e.getKeyCode() == KeyEvent.VK_LEFT) {
            movingFigure.moveLeft();
        }
        if (isRunning && e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movingFigure.moveRight();
        }
        if (isRunning && e.getKeyCode() == KeyEvent.VK_UP) {
            movingFigure.rotate();
        }
        if (isRunning && e.getKeyCode() == KeyEvent.VK_DOWN) {
            // trying to push the figure down
            movingFigure.updateState(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glViewport(600, 0, 400, 800);
        gl.glClearColor(0.8f, 0.8f, 1f, 0f);

        rnd = new Random();
        background = new Background(gl);
        grid = new Grid(gl);
        movingFigure = MovingFigure.getRandomMovingFigure(gl, grid, rnd);

        isRunning = true;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    // main drawing function
    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        if (isRunning && !movingFigure.updateState(false)) {
            if (!grid.addFigure(movingFigure)) {
                isRunning = false;
                return;
            }

            // getting the next figure if the game is still in process
            movingFigure = MovingFigure.getRandomMovingFigure(gl, grid, rnd);
        }

        background.draw(gl);
        grid.draw(gl);
        movingFigure.draw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        float aspect_ratio = 0.5f;
        int g_width = min(width, round(height * aspect_ratio));
        int g_height = min(height, round(width / aspect_ratio));

        int width_border = (width - g_width) / 2;
        int height_border = (height - g_height) / 2;

        drawable.getGL().getGL3().glViewport(width_border, height_border, g_width, g_height);
    }

    private void quit() {
        new Thread(() -> window.destroy()).start();
    }
}
