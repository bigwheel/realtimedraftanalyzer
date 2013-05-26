package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file.{Path, WatchEvent}
import org.specs2.mutable.Specification

class DirectoryWatcherSpec extends Specification {
  def eventHandler(event: WatchEvent[Path], fullpath: Path) {
    System.err.println("%s: %s\n".format(event.kind().name(), fullpath))
  }

  "FileChangeSimulator" should {
    "test" in {
      FileChangeSimulator.start

      new DirectoryWatcher("src/test/resources/", eventHandler)
      success
    }
  }
}
