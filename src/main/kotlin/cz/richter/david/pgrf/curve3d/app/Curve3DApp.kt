package cz.richter.david.pgrf.curve3d.app

import cz.richter.david.pgrf.curve3d.gui.HelpDialog
import cz.richter.david.pgrf.curve3d.gui.MainWindow
import javax.swing.SwingUtilities

/**
 * @author D.Richter
 * @since 16.5.2015
 */

/**
 * Main function used for starting the application
 */
fun main(args: Array<String>) {
    SwingUtilities.invokeLater(Runnable {
        val window = MainWindow()
        window.initGui()
    })
}


/**
 * function which creates and shows help dialog
 */
public fun showHelp() {
    SwingUtilities.invokeLater(Runnable {
        val helpDialog = HelpDialog()
        helpDialog.initGui()
    })
}