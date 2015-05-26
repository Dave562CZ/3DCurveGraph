package cz.richter.david.pgrf.curve3d.gui

import cz.richter.david.pgrf.curve3d.model.*
import transforms3D.Point3D
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.table.TableModel

/**
 * @author David
 * @since 20. 5. 2015
 */
public class CurveSettingsDialog(private val curves: MutableList<Curve>, private val indexOfExistingCurve: Int = -1, private val tableModel: TableModel) : JDialog(), ActionListener {
    private val labelNamesBezierAndFerguson = array("Start point", "Start vector", "End point", "End vector")
    private val labelNamesCoons = array("1. point", "2. point", "3. point", "4. point")

    private val comboType = JComboBox<String>()

    private val labelFirst = JLabel(labelNamesBezierAndFerguson[0])
    private val textFirst = JTextField()

    private val labelSecond = JLabel(labelNamesBezierAndFerguson[1])
    private val textSecond = JTextField()

    private val labelThird = JLabel(labelNamesBezierAndFerguson[2])
    private val textThird = JTextField()

    private val labelFourth = JLabel(labelNamesBezierAndFerguson[3])
    private val textFourth = JTextField()

    private val butColChooser = JButton("Pick color")
    private var pickedColor = Color.WHITE

    private val butOK = JButton("OK")
    private val butCancel = JButton("Cancel")

    public fun initGui() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        setLayout(BorderLayout())
        initSettingsPanel()

        val curve: Curve?
        if (indexOfExistingCurve != -1) {
            setTitle("Edit curve")
            curve = curves[indexOfExistingCurve]
            fillVariables(curve)
        } else {
            setTitle("Add curve")
            curve = null
            fillVariables(BezierCurve())
        }
        val labels = if (curve != null && curve is CoonsCurve) labelNamesCoons else labelNamesBezierAndFerguson
        setLabelsTexts(labels)
        tryToCenterWindow()
        setModal(true)
        setVisible(true)
    }

    private fun fillVariables(curve: Curve) {
        when (curve) {
            is BezierCurve -> fillVariables(0, curve.begin, curve.vectorBegin, curve.end, curve.vectorEnd, curve.color)
            is FergusonCurve -> fillVariables(1, curve.begin, curve.vectorBegin, curve.end, curve.vectorEnd, curve.color)
            is CoonsCurve -> fillVariables(2, curve.begin, curve.secondPoint, curve.thirdPoint, curve.end, curve.color)
        }
    }

    private fun fillVariables(type: Int, p1: Point3D, p2: Point3D, p3: Point3D, p4: Point3D, color: Color) {
        comboType.setSelectedIndex(type)
        textFirst.setText(p1.getTextForTA())
        textSecond.setText(p2.getTextForTA())
        textThird.setText(p3.getTextForTA())
        textFourth.setText(p4.getTextForTA())
        pickedColor = color
    }

    private fun setLabelsTexts(labels: Array<String>) {
        labelFirst.setText(labels[0])
        labelSecond.setText(labels[1])
        labelThird.setText(labels[2])
        labelFourth.setText(labels[3])
    }

    private fun initSettingsPanel() {
        val vertBox = JPanel(GridLayout(7, 2, 5, 5))

        val labelType = JLabel("Type of cubics")
        comboType.setModel(DefaultComboBoxModel(array("Bezier", "Ferguson", "Coons")))
        comboType.addActionListener(this)
        vertBox.add(labelType)
        vertBox.add(comboType)

        vertBox.add(labelFirst)
        vertBox.add(textFirst)

        vertBox.add(labelSecond)
        vertBox.add(textSecond)

        vertBox.add(labelThird)
        vertBox.add(textThird)

        vertBox.add(labelFourth)
        vertBox.add(textFourth)

        val labelColor = JLabel("Color of curve")

        butColChooser.addActionListener(this)
        vertBox.add(labelColor)
        vertBox.add(butColChooser)

        butOK.addActionListener(this)
        butCancel.addActionListener(this)
        vertBox.add(butOK)
        vertBox.add(butCancel)

        vertBox.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15))
        add(vertBox)
    }

    override fun actionPerformed(e: ActionEvent) {
        val source = e.getSource()
        when (source) {
            butOK -> createAndSetCurve()
            butCancel -> dispose()
            butColChooser -> {
                val color = JColorChooser.showDialog(this, "Pick Color", pickedColor)
                if (color != null) {
                    pickedColor = color
                }
            }
            comboType -> {
                if (comboType.getSelectedIndex() == 2) {
                    setLabelsTexts(labelNamesCoons)
                } else {
                    setLabelsTexts(labelNamesBezierAndFerguson)
                }
            }
        }
    }

    private fun createAndSetCurve() {
        val indexOfType = comboType.getSelectedIndex()
        val p1 = parsePoint(textFirst.getText().trim())
        val p2 = parsePoint(textSecond.getText().trim())
        val p3 = parsePoint(textThird.getText().trim())
        val p4 = parsePoint(textFourth.getText().trim())
        val color = pickedColor
        val curve: Curve = when (indexOfType) {
            0 -> BezierCurve(
                    begin = p1,
                    vectorBegin = p2,
                    end = p3,
                    vectorEnd = p4,
                    color = color)
            1 -> FergusonCurve(
                    begin = p1,
                    vectorBegin = p2,
                    end = p3,
                    vectorEnd = p4,
                    color = color)
            else -> CoonsCurve(
                        begin = p1,
                        secondPoint = p2,
                        thirdPoint = p3,
                        end = p4,
                        color = color)
        }
        if (indexOfExistingCurve != -1) {
            curves.set(indexOfExistingCurve, curve)
        } else {
            curves.add(curve)
        }
        if (tableModel is CurvesTableModel) {
            tableModel.fireTableDataChanged()
        }
        dispose()
    }

    private fun parsePoint(text: String): Point3D {
        val parsedText = text.split(";")
        parsedText.forEach { it.replace(",", ".") }
        return Point3D(
                parsedText[0].toDouble(),
                parsedText[1].toDouble(),
                parsedText[2].toDouble()
        )
    }
}

fun Point3D.getTextForTA(): String = "${x};${y};${z}"
