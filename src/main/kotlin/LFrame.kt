package org.example

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class LFrame : JFrame() {
    private val fSize = Dimension(800, 600)
    lateinit var cp: Container

    // Start Menu components
    private val startGameBtn = JButton("Start Game")
    private val gameSettingsBtn = JButton("Setup Game")
    private val bombsCountLabel = JLabel()
    private val coveredTileCountLabel = JLabel()

    // Game components
    val btnList: ArrayList<ArrayList<SweeperTile>> = ArrayList()


    fun initFrame() {
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Minesweeper"
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

        bombsCountLabel.setBounds(50, 10, 150, 40)
        container.add(bombsCountLabel)

        coveredTileCountLabel.setBounds(250, 10, 150, 40)
        container.add(coveredTileCountLabel)

        val btnContainer = JPanel()
        btnContainer.preferredSize = Dimension(btnField.width * 20, btnField.height * 20)
//        btnContainer.isFocusable = false
//        btnContainer.layout = GridLayout(btnField.width, btnField.height)
        btnContainer.layout = null

        val scroll = JScrollPane()
        scroll.setViewportView(btnContainer)
        scroll.setBounds(50, 50, 700, 500)
        scroll.verticalScrollBar.unitIncrement = 10
        scroll.horizontalScrollBar.unitIncrement = 10
        scroll.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        scroll.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        scroll.border = BorderFactory.createRaisedBevelBorder()
        container.add(scroll)


        for (y in 0..<btnField.height) {
            btnList.add(ArrayList())
            for (x in 0..<btnField.width) {
                btnList[y].add(SweeperTile())

//                btnList[y][x].preferredSize = Dimension(20,20)
                btnList[y][x].setBounds(x * 20, y * 20, 20, 20)

                btnList[y][x].setFocusPainted(false)
//                btnList[y][x].text =  "${x}"
                btnList[y][x].isFocusable = false
                btnList[y][x].border = BorderFactory.createRaisedBevelBorder()
                btnList[y][x].background = Color(200, 200, 200)
                btnList[y][x].font = Font("Arial", Font.BOLD, 15)
                btnList[y][x].addActionListener {
                    if ((btnList[y][x].text == "F") && (!btnList[y][x].isRevealed())) {
                        btnList[y][x].text = "?"
                        btnList[y][x].foreground = Color.BLACK
                        bombsCount++
                        tilesCovered++
                    } else if ((btnList[y][x].text == "?") && (!btnList[y][x].isRevealed())) {
                        btnList[y][x].text = ""
                    } else if (!btnList[y][x].isRevealed()) {
                        btnList[y][x].text = "F"
                        btnList[y][x].foreground = Color.RED
                        bombsCount--
                        tilesCovered--
                    } else if (btnList[y][x].isRevealed()) revealCluster(btnList, x, y)
                    checkGameWon()
                }
                btnList[y][x].addMouseListener(object : MouseAdapter() {
                    override fun mouseReleased(e: MouseEvent?) {
                        super.mouseReleased(e)
                        if (e?.button == MouseEvent.BUTTON3) {
                            if (!btnList[y][x].isRevealed()) {
                                if (btnList[y][x].getTileValue() == "B") endGame()
                                btnList[y][x].background = Color(240, 240, 240)
                                btnList[y][x].isOpaque = false
                                btnList[y][x].foreground = Color.RED
                                btnList[y][x].setNumberAsText()
                                btnList[y][x].setRevealed()
                                tilesCovered--
                                revealNumbers(btnList, x, y)
                            } else {
                                revealCluster(btnList, x, y)
                            }
                            checkGameWon()
                        }
                    }
                })
                btnContainer.add(btnList[y][x])
            }
        }
//        container.add(tArea)
        return container
    }

    fun getSetupScene(): Container {
        val container = Container()
        container.preferredSize = fSize
        container.layout = null

        val widthField = JTextField()
        widthField.text = btnField.width.toString()
        widthField.setBounds((fSize.width + 100) / 2, 100, 50, 50)
        container.add(widthField)

        val widthLabel = JLabel("Game Field Width")
        widthLabel.setBounds((fSize.width - 150) / 2, 100, 150, 50)
        container.add(widthLabel)

        val heightField = JTextField()
        heightField.text = btnField.height.toString()
        heightField.setBounds((fSize.width + 100) / 2, 175, 50, 50)
        container.add(heightField)

        val heightLabel = JLabel("Game Field Height")
        heightLabel.setBounds((fSize.width - 150) / 2, 175, 150, 50)
        container.add(heightLabel)

        val bombsCountField = JTextField()
        bombsCountField.text = bombsCount.toString()
        bombsCountField.setBounds((fSize.width + 100) / 2, 250, 50, 50)
        container.add(bombsCountField)

        val bombsCountLabel = JLabel("Bombs Count")
        bombsCountLabel.setBounds((fSize.width - 150) / 2, 250, 150, 50)
        container.add(bombsCountLabel)

        val confirmBtn = JButton("save")
        confirmBtn.setBounds((fSize.width - 200) / 2, 325, 200, 50)
        confirmBtn.addActionListener {
            try {
                btnField = Dimension(widthField.text.toInt(), heightField.text.toInt())
                bombsCount = bombsCountField.text.toInt()
                tilesCovered = btnField.width * btnField.height
                startGame()
            }
            catch (_:Exception){
                JOptionPane.showMessageDialog(this, "Please just enter Numbers", "Error", JOptionPane.PLAIN_MESSAGE)
            }
        }
        container.add(confirmBtn)

        return container
    }

    fun loadScenes(c: Container) {
        cp.removeAll()
        cp.add(c)
        revalidate()
        repaint()
        pack()
    }

    fun displayBombsCount(count: Int, coveredCount: Int) {
        bombsCountLabel.text = "Bombs: $count"
        coveredTileCountLabel.text = "Remaining: $coveredCount"
    }
}