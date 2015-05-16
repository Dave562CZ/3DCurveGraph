package cz.richter.david.pgrf.curve3d.app

import cz.richter.david.pgrf.curve3d.gui.MainWindow
import javax.swing.SwingUtilities

/**
 * @author D.Richter
 * @since 16.5.2015
 */
fun main(args: Array<String>) {
    SwingUtilities.invokeLater(Runnable {
        val window = MainWindow()
        window.initGui()
    })
}