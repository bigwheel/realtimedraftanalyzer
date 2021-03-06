package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.swing._
import java.awt.{Color, Dimension}
import scala.actors.Actor._
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.DraftScore
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.Pick
import scala.Some
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.Card
import scala.swing.TabbedPane.Page
import javax.swing.text.html.HTMLDocument
import scala.swing.event.MouseWheelMoved
import javax.swing.filechooser.FileNameExtensionFilter

object App extends SimpleSwingApplication {
  private[this] def getFileChooser = {
    new FileChooser() {
      fileFilter = new FileNameExtensionFilter("ドラフトピック譜", "txt")
    }
  }
  val fileChooser = getFileChooser
  fileChooser.title = "ファイルを選択する"
  private[this] var filePath: String = ""
  fileChooser.showOpenDialog(null) match {
    case FileChooser.Result.Approve =>
      if (fileChooser.selectedFile.exists)
        this.filePath = fileChooser.selectedFile.toPath().toString()
      else
        this.quit
    case _ => this.quit
  }

  val tabbedPane = new TabbedPane {
    listenTo(this.mouse.wheel)
    reactions += {
      case MouseWheelMoved(_, _, _, rotation) => selection.index =
        (selection.index + rotation + pages.size) % pages.size
    }
  }
  val putter = actor {
    loop {
      react {
        case draftScore: DraftScore => {
          def getLastAnd8beforePickCards(draftScore: DraftScore): Option[(Pick, Option[Pick])] = {
            val packs = draftScore.packs
            if (packs.size == 0)
              None
            else {
              val pack = if (packs.last.picks.size != 0)
                packs.last
              else if (packs.size == 1)
                return None
              else
                packs(packs.size - 2)
              Some(pack.picks.last, if (9 <= pack.picks.size)
                Some(pack.picks(pack.picks.size - 1 - 8)) else None)
            }
          }

          getLastAnd8beforePickCards(draftScore) match {
            case None => ()
            case Some((pick, _8beforePick: Option[Pick])) => {
              def tabTitle(pick: Pick): String = pick.packNumber + "-" + pick.pickNumber

              if (tabbedPane.pages.find(_.title == tabTitle(pick)) == None) {
                val gridPanel = new GridPanel(3, 5)

                val shouldRenderedPick = if (_8beforePick == None) pick else _8beforePick.get
                val editorPanes = shouldRenderedPick.cards.map((card: Card) => {
                  val imageUrl = ImageUrlFromSearch(card.name, draftScore.packs(pick.packNumber - 1).expansion) match {
                    case None => "http://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=145765"
                    case Some(a) => a
                  }
                  val editorPane = new EditorPane("text/html",
                    <html>
                      <head>
                      </head>
                      <body id="body"></body>
                    </html>
                      .toString)
                  val document = editorPane.peer.getDocument.asInstanceOf[HTMLDocument]
                  val bodyElement = document.getElement("body")
                  val body = <img width="223" height="310" alt={card.name} src={imageUrl} />
                  document.insertBeforeEnd(bodyElement, body.toString)
                  editorPane.editable = false
                  editorPane.background = if (pick.cards.find(_.name == card.name) == None) {
                    if (card.picked)
                      Color.PINK
                    else
                      Color.GRAY
                  } else if(pick.cards.find(_.name == card.name).get.picked)
                    Color.RED
                  else
                    Color.WHITE

                  editorPane
                })
                editorPanes.foreach(gridPanel.contents += _)

                tabbedPane.pages += new Page(tabTitle(pick), gridPanel)
                tabbedPane.selection.page = if (8 <= pick.pickNumber)
                  tabbedPane.pages(math.abs(tabbedPane.pages.size - 8))
                else
                  tabbedPane.pages.last
                tabbedPane.repaint
              }
            }
          }
        }
      }
    }
  }

  new FileWatcher(filePath, (_: Unit) => {
    putter ! new DraftScore(filePath)
  })

  def top = new MainFrame {
    title = filePath
    minimumSize = new Dimension(1150, 1000)
    contents = tabbedPane
  }
}
