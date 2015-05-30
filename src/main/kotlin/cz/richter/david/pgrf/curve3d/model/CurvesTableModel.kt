package cz.richter.david.pgrf.curve3d.model

import java.awt.Color
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableCellRenderer
import javax.swing.table.TableModel

/**
 * @author David
 * @since 19. 5. 2015
 */
public class CurvesTableModel(private val curves: List<Curve>) : AbstractTableModel(), TableCellRenderer {
    override fun getTableCellRendererComponent(table: JTable, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
        val dtcr = DefaultTableCellRenderer()
        val component = dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        component.setBackground(curves[row].getCurveColor());
        return component
    }

    val columnNames = arrayOf("Cubic name", "End points", "Vectors/Middle points")

    override fun getColumnName(column: Int): String {
        return columnNames[column]
    }

    override fun getRowCount(): Int {
        return curves.size()
    }

    override fun getColumnCount(): Int {
        return 3
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        return curves[rowIndex][columnIndex]
    }

    override public fun fireTableDataChanged() {
        super<AbstractTableModel>.fireTableDataChanged()
    }
}


