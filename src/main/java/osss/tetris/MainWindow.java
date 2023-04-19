package osss.tetris;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;

import java.io.IOException;

public class MainWindow implements GLEventListener, KeyListener {
    private GLWindow window;
    private Animator animator;

    private Grid grid;

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
        window.setSize(400, 400);

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

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glClearColor(0.8f, 0.8f, 1f, 0f);

        try {
            grid = new Grid(gl);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        grid.draw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        drawable.getGL().getGL3().glViewport(x, y, width, height);
    }
}
