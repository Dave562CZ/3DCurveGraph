package cz.richter.david.pgrf.curve3d.gui

import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.FPSAnimator
import cz.richter.david.pgrf.curve3d.model.Curve
import cz.richter.david.pgrf.curve3d.model.GPU
import transforms3D.Kubika
import transforms3D.Point3D
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.ArrayList
import java.util.HashMap
import javax.swing.JFrame
import kotlin.properties.Delegates

/**
* @author D.Richter
* @since 16.5.2015
*/
public class MainWindow() : JFrame() {
    private val FPS = 30

    private val curves: MutableList<Curve> = ArrayList()

    private val canvas : GLCanvas by Delegates.lazy {
        // setup OpenGL Version 2
        val profile = GLProfile.get(GLProfile.GL2)
        val capabilities = GLCapabilities(profile)
        return@lazy GLCanvas(capabilities)
    }

    public fun initGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setTitle("Vykreslení křivky v 3D")
        setLayout(BorderLayout())

        initCanvas()
        val animator = initAnimator(canvas)

        animator.start()

        tryToCenterWindow()
        setVisible(true)
    }

    private fun initAnimator(canvas: GLCanvas): FPSAnimator {
        val animator = FPSAnimator(canvas, FPS, true)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                Thread(Runnable {
                    if (animator.isStarted()) {
                        animator.stop()
                    }
                }).start()
            }
        })
        return animator
    }

    private fun initCanvas() {
        // The canvas is the widget that's drawn in the JFrame
        canvas.setPreferredSize(Dimension(600, 400))
        val ren = GPU()
        canvas.addGLEventListener(ren)
        canvas.addMouseListener(ren)
        canvas.addMouseMotionListener(ren)
        canvas.addKeyListener(ren)
        add(canvas, BorderLayout.CENTER)
    }

    private fun tryToCenterWindow() {
        pack()
        val fSize = getSize()
        val sSize = getToolkit().getScreenSize()
        setLocation((sSize.width / 2) - (fSize.width / 2), (sSize.height / 2) - (fSize.height / 2))
    }
}