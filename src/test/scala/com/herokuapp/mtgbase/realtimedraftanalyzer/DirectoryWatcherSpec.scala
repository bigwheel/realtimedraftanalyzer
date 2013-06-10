package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file.{StandardWatchEventKinds, Path, WatchEvent}
import org.specs2.mutable.Specification
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.DraftScore
import scala.actors.Actor._
import scala.actors.Actor
import scala.collection.mutable

class DirectoryWatcherSpec extends Specification {
  "FileChangeSimulator" should {
    "be able to create DraftScore object in always" in {
      val fcs = new FileChangeSimulator("src/test/resources/test-target.txt",
        "src/test/resources/sample-pick-score.txt", 1000)

      val actorSet = new mutable.HashSet[Actor]

      new DirectoryWatcher("src/test/resources/",
        (event: WatchEvent[Path], fullpath: Path) => {
          actorSet.foreach(_ ! 'ファイルが更新されましたよシグナル)
          actorSet.retain(_.getState != Actor.State.Terminated)
          val a = actor {
            reactWithin(500) {
              case actors.TIMEOUT => {
                new DraftScore(fullpath.toString)
                println("created")
              }
              case 'ファイルが更新されましたよシグナル => exit
            }
          }
          actorSet.add(a)
          event.kind != StandardWatchEventKinds.ENTRY_DELETE
        }
      )

      while (fcs.process.getState != Actor.State.Terminated) {
        Thread.sleep(1000)
      } must not be throwA[Exception]
    }
  }
}
