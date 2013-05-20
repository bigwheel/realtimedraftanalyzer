package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file._
import scala.collection.JavaConversions._

object App {
  def main(args: Array[String]) {
    FileChangeSimulator.start

    val watcher = FileSystems.getDefault.newWatchService
    val keys = collection.mutable.Map[WatchKey, Path]()
    val dir = Paths.get("src/test/resources/")

    val key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
      StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)
    keys += (key -> dir)

    while(true) {
      val key = watcher.take
      val dir = keys.get(key).asInstanceOf[Path]

      for (tmp <- key.pollEvents().toList) {
        val event = tmp.asInstanceOf[WatchEvent[Path]]
        val kind = event.kind()
        if (kind != StandardWatchEventKinds.OVERFLOW) {
          val ev = kind.asInstanceOf[WatchEvent[Path]]
          val name = ev.context()
          val child = dir.resolve(name)
          System.err.println("%s: %s\n".format(event.kind().name(), child))
        }
      }
      if (!key.reset()) {
        keys.remove(key)
      }
    }
  }
}
