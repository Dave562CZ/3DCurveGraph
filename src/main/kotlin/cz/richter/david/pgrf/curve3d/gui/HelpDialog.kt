package cz.richter.david.pgrf.curve3d.gui

import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*

/**
 * @author David
 * @since 26. 5. 2015
 */
public class HelpDialog() : JDialog() {

    public fun initGui() {
        setLayout(BorderLayout())
        setTitle("Help dialog")
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        setModal(true)

        initMainPanel()

        tryToCenterWindow()
        setVisible(true)
    }

    private fun initMainPanel() {
        val mainBox = Box.createVerticalBox()
        val spaceDivider = Box.createVerticalStrut(5)
        mainBox.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15))
        mainBox.add(JLabel("Help for drawing curve in 3D application: "))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("   Controls:"))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("        W, S, A, D, Q, E: camera movement"))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("        Left mouse button: camera azimuth and zenith"))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("        Double click to row with curve: edit curve"))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("        F1: open this window"))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("   Author: David Richter"))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("   Last modified: 30. 5. 2015"))
        mainBox.add(spaceDivider)
        mainBox.add(JLabel("   Application made as project for PGRF3 course"))

        val buttonOK = JButton("OK")
        buttonOK.setPreferredSize(Dimension(100, buttonOK.getPreferredSize().height))
        buttonOK.addActionListener { dispose() }

        val hBox = Box.createHorizontalBox()
        hBox.add(Box.createHorizontalGlue())
        hBox.add(buttonOK)
        hBox.add(Box.createHorizontalGlue())

        val vBox = Box.createVerticalBox()
        vBox.add(hBox)
        vBox.add(Box.createVerticalStrut(15))

        add(vBox, BorderLayout.SOUTH)
        add(mainBox, BorderLayout.CENTER)
    }
}
