package org.example

import java.awt.Color
import java.awt.Container
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import kotlin.system.exitProcess

class LFrame : JFrame() {
    private val fSize = Dimension(800, 600)
    lateinit var cp: Container

    // Start Menu components
    private val startGameBtn = JButton("Start Game")
    private val gameSettingsBtn = JButton("Setup Game")

    // Game components
    val btnList: ArrayList<SweeperTile> = ArrayList()


    fun initFrame() {
        defaultCloseOperation = EXIT_ON_CLOSE
        cp.add(getStartMenu())
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun getStartMenu(): Container {
        val container = Container()
        container.preferredSize = fSize
        container.layout = null

        val btnDimension = Dimension(150, 50)
        startGameBtn.location = Point((fSize.width - btnDimension.width) / 2, 225)
        startGameBtn.size = btnDimension
        startGameBtn.isVisible = true
        startGameBtn.isFocusPainted = false
        startGameBtn.addActionListener { startGame() }
        container.add(startGameBtn)

        gameSettingsBtn.location = Point((fSize.width - btnDimension.width) / 2, 325)
        gameSettingsBtn.size = btnDimension
        gameSettingsBtn.isVisible = true
        gameSettingsBtn.isFocusPainted = false
        gameSettingsBtn.addActionListener { setUpGame() }
        container.add(gameSettingsBtn)
        return container
    }

    fun getGameScene(): Container {
        val container = Container()
        container.preferredSize = fSize
        container.layout = null



        val btnContainer = Container()
//        btnContainer.setBounds(100,100,width * 20, height * 20)
//        btnContainer.isFocusable = false
        btnContainer.layout = GridLayout(btnField.width, btnField.height)

        val scroll = JScrollPane(btnContainer)
        scroll.setBounds(50,50,700, 500)
        scroll.verticalScrollBar.unitIncrement = 10
        scroll.horizontalScrollBar.unitIncrement = 10
//        scroll.isWheelScrollingEnabled = false
        scroll.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        scroll.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        scroll.border = BorderFactory.createRaisedBevelBorder()
        container.add(scroll)


        for (y in 0..<btnField.height) {
            for (x in 0..<btnField.width) {
                btnList.add(SweeperTile())
                val n = y * btnField.width + x
                btnList[n].preferredSize = Dimension(20,20)
                btnList[n].setFocusPainted(false)
                btnList[n].isFocusable = false
                btnList[n].border = BorderFactory.createRaisedBevelBorder()
                btnList[n].background = Color(200, 200, 200)
                btnList[n].addActionListener{
                    if(btnList[n].getTileValue() == "B") exitProcess(0)
                    revealNumbers(btnList, n)
                }
                btnList[n].addMouseListener(object : MouseAdapter() {
                    override fun mouseReleased(e: MouseEvent?) {
                        super.mouseReleased(e)
                        if(e?.button == MouseEvent.BUTTON3) btnList[n].text = "F"
                    }
                })
                btnContainer.add(btnList[n])
            }
        }
//        container.add(tArea)

        return container
    }

    fun loadScenes(c: Container) {
        cp.removeAll()
        cp.add(c)
        revalidate()
        repaint()
        pack()
    }
}