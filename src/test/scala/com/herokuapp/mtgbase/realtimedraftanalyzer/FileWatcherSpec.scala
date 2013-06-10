package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification
import scala.actors.Actor

class FileWatcherSpec extends Specification {
  "be able to create DraftScore object in always" in {
    val fcs = new FileChangeSimulator("src/test/resources/test-target.txt",
      "src/test/resources/sample-pick-score.txt", 100)

    var count = 0
    new FileWatcher("src/test/resources/test-target.txt",
      (_: Unit) => {
        count += 1
      }
    )

    while (fcs.process.getState != Actor.State.Terminated) {
      Thread.sleep(100)
    }
    count must_== 50
  }
}
