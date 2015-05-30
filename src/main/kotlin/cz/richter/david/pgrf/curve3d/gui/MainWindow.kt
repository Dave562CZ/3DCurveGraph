package cz.richter.david.pgrf.curve3d.gui

import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.FPSAnimator
import cz.richter.david.pgrf.curve3d.app.showHelp
import cz.richter.david.pgrf.curve3d.model.*
import transforms3D.Kubika
import transforms3D.Point3D
import java.awt.*
import java.awt.event.*
import java.util.ArrayList
import java.util.HashMap
import javax.swing.*
import kotlin.properties.Delegates

/**
* @author D.Richter
* @since 16.5.2015
*/
public class MainWindow() : JFrame(), MouseListener, ActionListener {
    private val FPS = 30

    private val curves: MutableList<Curve> = ArrayList()

    private val curvesTable = JTable()
    private val butAddCurve = JButton("Add")
    private val butRemoveCurve = JButton("Remove")
    private val butHelp = JButton("Help")

    private val canvas : GLCanvas by Delegates.lazy {
        // setup OpenGL Version 2
        val profile = GLProfile.get(GLProfile.GL2)
        val capabilities = GLCapabilities(profile)
        return@lazy GLCanvas(capabilities)
    }

    public fun initGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setTitle("Drawing curve in 3D")
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
        val vertBox = Box.createVerticalBox()
        val buttonBox = Box.createHorizontalBox()
        butAddCurve.addActionListener(this)
        butAddCurve.setPreferredSize(Dimension(100, 25))
        butRemoveCurve.addActionListener(this)
        butRemoveCurve.setPreferredSize(Dimension(100, 25))
        butHelp.addActionListener(this)
        butHelp.setPreferredSize(Dimension(100, 25))

        buttonBox.add(Box.createHorizontalGlue())
        buttonBox.add(butAddCurve)
        buttonBox.add(Box.createHorizontalStrut(5))
        buttonBox.add(butRemoveCurve)
        buttonBox.add(Box.createHorizontalStrut(5))
        buttonBox.add(butHelp)
        buttonBox.add(Box.createHorizontalGlue())
        vertBox.add(Box.createVerticalStrut(5))
        vertBox.add(buttonBox)
        vertBox.add(Box.createVerticalStrut(5))

        val curvesTableModel = CurvesTableModel(curves)
        curvesTable.setModel(curvesTableModel)
        curvesTable.setDefaultRenderer(Any().javaClass , curvesTableModel)
        curvesTable.addMouseListener(this)
        curvesTable.getColumnModel().getColumn(0).setPreferredWidth(30)
        val scrollPane = JScrollPane(curvesTable)
        vertBox.add(scrollPane)
        add(vertBox, BorderLayout.EAST)
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

    override fun mouseEntered(e: MouseEvent) {}

    override fun mouseClicked(e: MouseEvent) {
        val source = e.getSource()
        if (e.getClickCount() == 2 && source is JTable) {
            val editDialog = CurveSettingsDialog(curves, source.getSelectedRow(), source.getModel())
            editDialog.initGui()
        }
    }

    override fun mouseReleased(e: MouseEvent) {}

    override fun mouseExited(e: MouseEvent) {}

    override fun mousePressed(e: MouseEvent) {}

    override fun actionPerformed(e: ActionEvent) {
        val source = e.getSource()
        if (source is JButton) {
            when (source) {
                butAddCurve -> {
                    val dialog = CurveSettingsDialog(curves = curves, tableModel = curvesTable.getModel())
                    dialog.initGui()
                }
                butRemoveCurve -> {
                    val row = curvesTable.getSelectedRow()
                    if (row < 0) {
                        JOptionPane.showMessageDialog(this, "No curve was selected to remove", "Error while removing curve", JOptionPane.WARNING_MESSAGE)
                        return
                    }
                    if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this curve", "Remove curve", JOptionPane.YES_NO_OPTION) == 0) {
                        curves.remove(row)
                        val model = curvesTable.getModel()
                        if (model is CurvesTableModel) {
                            model.fireTableDataChanged()
                        }
                    }
                }
                butHelp -> {
                    showHelp()
                }
            }
        }

    }
}

public fun Window.tryToCenterWindow() {
    pack()
    val fSize = getSize()
    val sSize = getToolkit().getScreenSize()
    setLocation((sSize.width / 2) - (fSize.width / 2), (sSize.height / 2) - (fSize.height / 2))
}