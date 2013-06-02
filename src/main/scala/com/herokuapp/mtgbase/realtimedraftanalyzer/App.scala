package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.swing._
import java.awt.Dimension
import java.nio.file.{StandardWatchEventKinds, WatchEvent, Path}
import scala.actors.Actor
import scala.actors.Actor._
import scala.collection.mutable
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.DraftScore
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.Pick
import scala.Some
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.Card
import scala.swing.TabbedPane.Page

object App extends SimpleSwingApplication {
  private[this] var directoryPath: String = ""
  Dialog.showInput(message="ピック譜が置かれるディレクトリを入力してください",
    initial="src/test/resources/") match {
    case None => this.quit
    case Some(path) => this.directoryPath = path
  }

  // テストでおいてるだけなのできちんと削除すること
  new FileChangeSimulator("src/test/resources/test-target.txt",
    "src/test/resources/sample-pick-score.txt", 10000)

  val tabbedPane = new TabbedPane
  val putter = actor {
    loop {
      react {
        case draftScore: DraftScore => {
          def getLastPickCards(draftScore: DraftScore): Option[Pick] = {
            val packs = draftScore.packs
            if (packs.size == 0)
              None
            else if (packs.last.picks.size != 0)
              Some(packs.last.picks.last)
            else if (packs.size == 1)
              None
            else
              Some(packs(packs.size - 2).picks.last)
          }

          getLastPickCards(draftScore) match {
            case None => ()
            case Some(pick) => {
              //tabbedPane.pages.clear
              val editorPane = new EditorPane
              editorPane.contentType = "text/html"
              editorPane.editable = false
              tabbedPane.pages += new Page(pick.packNumber + "-" + pick.pickNumber, editorPane)

              editorPane.text = pick.cards.map( (card : Card) => {
                val imageUrl = ImageUrlFromSearch(card.name, draftScore.packs(pick.packNumber - 1).expansion).get
                "<img width=223 height=310 alt=\"" + card.name + "\" src=\"" +
                  imageUrl  + "\" >"
              }).grouped(5).toList.map(_.mkString).mkString("<BR>")
              tabbedPane.repaint
            }
          }
          //
        }
      }
    }
  }

  val actorSet = new mutable.HashSet[Actor]

  new DirectoryWatcher(directoryPath, (event: WatchEvent[Path], fullpath: Path) => {
    if (event.kind == StandardWatchEventKinds.ENTRY_MODIFY) {
      actorSet.foreach(_ ! 'ファイルが更新されましたよシグナル)
      actorSet.retain(_.getState != Actor.State.Terminated)
      val a = actor {
        reactWithin(500) {
          case actors.TIMEOUT => {
            putter ! new DraftScore(fullpath.toString)
          }
          case 'ファイルが更新されましたよシグナル => exit
        }
      }
      actorSet.add(a)
    }
    event.kind != StandardWatchEventKinds.ENTRY_DELETE
  })

  def top = new MainFrame {
    title = directoryPath
    minimumSize = new Dimension(1150, 1000)
    contents = tabbedPane
  }
}
