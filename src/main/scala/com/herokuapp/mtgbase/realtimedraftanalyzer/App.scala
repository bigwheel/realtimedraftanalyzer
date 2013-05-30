package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.swing.{MainFrame, SimpleSwingApplication}
import java.awt.Dimension

object App extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Window Title"
    minimumSize = new Dimension(300, 200)
  }
}
