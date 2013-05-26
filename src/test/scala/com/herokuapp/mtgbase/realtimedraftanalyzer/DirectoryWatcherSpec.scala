package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file.{StandardWatchEventKinds, Path, WatchEvent}
import org.specs2.mutable.Specification
import com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure.DraftScore

class DirectoryWatcherSpec extends Specification {
  "FileChangeSimulator" should {
    "test" in {
      new FileChangeSimulator("src/test/resources/test-target.txt",
        "src/test/resources/sample-pick-score.txt", 100)

      new DirectoryWatcher("src/test/resources/",
        (event: WatchEvent[Path], fullpath: Path) => {
          //System.err.println("%s: %s\n".format(event.kind.name, fullpath))
          new DraftScore(fullpath.toString)
          event.kind != StandardWatchEventKinds.ENTRY_DELETE
        }
      ) must not be throwA[Exception]
    }
  }
}
