package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file.{Path, WatchEvent}
import org.specs2.mutable.Specification

class DirectoryWatcherSpec extends Specification {
  def eventHandler(event: WatchEvent[Path], fullpath: Path) {
    System.err.println("%s: %s\n".format(event.kind().name(), fullpath))
  }

  "FileChangeSimulator" should {
    "test" in {
      new FileChangeSimulator("src/test/resources/test-target.txt",
        "src/test/resources/sample-pick-score.txt").start()

      new DirectoryWatcher("src/test/resources/", eventHandler)
      success
    }
  }
}
