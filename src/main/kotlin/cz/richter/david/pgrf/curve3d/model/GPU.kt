package cz.richter.david.pgrf.curve3d.model

import com.jogamp.opengl.*
import com.jogamp.opengl.fixedfunc.GLMatrixFunc
import com.jogamp.opengl.glu.GLU
import transforms3D.GLCamera
import transforms3D.Point3D
import java.awt.Color
import java.awt.event.*
import java.util
import java.util.HashMap
import kotlin.properties.Delegates

/**
 * @author D.Richter
 * @since 16.5.2015
 */
public class GPU() : GLEventListener, KeyListener, MouseMotionListener, MouseListener {

    private val variables: MutableMap<in String, Any?> = HashMap()
    private val gl: GL2 by Delegates.mapVal(variables)
    private val glu: GLU = GLU()
    private val camera = GLCamera()

    private val SPEED = 0.01f
    private var mouseLPressed: Boolean = false
    private var ox: Int = 0
    private var oy: Int = 0

    override fun init(drawable: GLAutoDrawable?) {
        if (drawable == null) {
            throw IllegalArgumentException("drawable could not be null")
        }
        variables["gl"] = drawable.getGL().getGL2()
        println("Init GL is ${gl.javaClass.getName()}")
        println("GL_VENDOR ${gl.glGetString(GL.GL_VENDOR)}") // vyrobce
        println("GL_RENDERER ${gl.glGetString(GL.GL_RENDERER)}") // graficka karta
        println("GL_VERSION ${gl.glGetString(GL.GL_VERSION)}") // verze OpenGL
    }

    override fun display(drawable: GLAutoDrawable?) {
        val gl = this.gl //optimalization not sure how expensive is calling delegate
        gl.glClearColor(0.0.toFloat(), 0.0.toFloat(), 0.0.toFloat(), 0.0.toFloat())
        // nulujeme misto pro kresleni
        gl.glClear(GL.GL_COLOR_BUFFER_BIT)

        // nastaveni modelove transformace
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
        // inicializace na jednotkovou matici
        gl.glLoadIdentity()
        camera.set_matrix(glu)

        // nastaveni projekce
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
        gl.glLoadIdentity()// inicializace na jednotkovou matici

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE)


        gl.glLineWidth(1.0f)
        gl.glBegin(GL.GL_LINES)
        gl.glColor3ub(255.toByte(), 0, 0)
        gl.glVertex3d(-1.0, 0.0, 0.0)
        gl.glVertex3d(1.0, 0.0, 0.0)
        gl.glColor3ub(0, 255.toByte(), 0)
        gl.glVertex3d(0.0, -1.0, 0.0)
        gl.glVertex3d(0.0, 1.0, 0.0)
        gl.glColor3ub(0, 0, 255.toByte())
        gl.glVertex3d(0.0, 0.0, -1.0)
        gl.glVertex3d(0.0, 0.0, 1.0)
        gl.glEnd()

        gl.glLineWidth(5.0f)
        gl.glColor3f(0.0.toFloat(), 0.7.toFloat(), 0.0.toFloat())

        val list = BezierCurve(
                begin = Point3D(-0.5, 0.0, 0.0),
                vectorBegin = Point3D(-0.7, 0.8, 1.0),
                end = Point3D(0.5, 0.0, 0.0),
                vectorEnd = Point3D(0.8, -0.5, 0.0),
                color = Color.YELLOW).getLines()
        gl.glBegin(GL.GL_LINES)
        for ((begin, end, color) in list) {
            gl.glColor3ub(color.getRed().toByte(), color.getGreen().toByte(), color.getBlue().toByte())
            gl.glVertex3d(begin.x, begin.y, begin.z)
            gl.glVertex3d(end.x, end.y, end.z)
        }
        gl.glEnd()
    }

    override fun reshape(drawable: GLAutoDrawable?, x: Int, y: Int, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)
    }

    override fun keyPressed(e: KeyEvent) {
        when (e.getKeyCode()) {
            KeyEvent.VK_W -> camera.forward(SPEED)
            KeyEvent.VK_A -> camera.left(SPEED)
            KeyEvent.VK_S -> camera.backward(SPEED)
            KeyEvent.VK_D -> camera.right(SPEED)
            KeyEvent.VK_Q -> camera.up(SPEED)
            KeyEvent.VK_E -> camera.down(SPEED)
//            KeyEvent.VK_F1 ->
            else -> {
            }
        }
    }

    override fun mousePressed(e: MouseEvent) {
        mouseLPressed = true
        ox = e.getX()
        oy = e.getY()

    }

    override fun mouseDragged(e: MouseEvent) {
        if (mouseLPressed) {
            camera.add_azimuth((Math.PI * (e.getX() - ox).toDouble() / 600).toFloat())
            camera.add_zenith((Math.PI * (e.getY() - oy).toDouble() / 600).toFloat())
            ox = e.getX()
            oy = e.getY()
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        mouseLPressed = false
    }

    override fun dispose(drawable: GLAutoDrawable?) {}

    override fun keyTyped(e: KeyEvent) {}

    override fun keyReleased(e: KeyEvent?) {}

    override fun mouseMoved(e: MouseEvent) {}

    override fun mouseEntered(e: MouseEvent) {}

    override fun mouseClicked(e: MouseEvent) {}

    override fun mouseExited(e: MouseEvent) {}
}