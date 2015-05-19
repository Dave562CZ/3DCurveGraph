package cz.richter.david.pgrf.curve3d.gui

import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.FPSAnimator
import cz.richter.david.pgrf.curve3d.model.*
import transforms3D.Kubika
import transforms3D.Point3D
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.ArrayList
import java.util.HashMap
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable
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
        initCurvesTable()
        initInitialCurves()
        val animator = initAnimator(canvas)

        animator.start()

        tryToCenterWindow()
        setVisible(true)
    }

    private fun initInitialCurves() {
        curves.add(BezierCurve(
                begin = Point3D(-0.5, 0.0, 0.0),
                vectorBegin = Point3D(-0.7, 0.8, 1.0),
                end = Point3D(0.5, 0.0, 0.0),
                vectorEnd = Point3D(0.8, -0.5, 0.0),
                color = Color.MAGENTA))
        curves.add(FergusonCurve(
                begin = Point3D(0.0, -0.8, 0.0),
                end = Point3D(0.0, 0.6, 0.0),
                vectorBegin = Point3D(2.3, -0.4, 0.5),
                vectorEnd = Point3D(3.8, 1.3, -1.4),
                color = Color.ORANGE))
        curves.add(CoonsCurve(
                begin = Point3D(0.0, -0.7, -0.8),
                secondPoint = Point3D(-0.3, 0.4, -0.2),
                thirdPoint = Point3D(0.4, 0.1, 0.3),
                end = Point3D(-0.7, -0.2, 0.7),
                color = Color.CYAN))
    }

    private fun initCurvesTable() {
        val curvesTable = JTable()
        val curvesTableModel = CurvesTableModel(curves)
        curvesTable.setModel(curvesTableModel)
        curvesTable.setDefaultRenderer(Any().javaClass , curvesTableModel)
        val scrollPane = JScrollPane(curvesTable)
        add(scrollPane, BorderLayout.EAST)
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
        val ren = GPU(curves)
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