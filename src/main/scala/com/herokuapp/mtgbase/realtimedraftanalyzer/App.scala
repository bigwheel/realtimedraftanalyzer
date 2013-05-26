package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file._
import scala.collection.JavaConversions._
import java.util

object App {
  def eventHandler(event: WatchEvent[Path], fullpath: Path) {
    System.err.println("%s: %s\n".format(event.kind().name(), fullpath))
  }

  def directoryWatch(directoryPath: String,
                     eventHandler: ((WatchEvent[Path], Path) => Unit)) {
    // constructor
    val watcher = FileSystems.getDefault.newWatchService()
    val keys = new util.HashMap[WatchKey, Path]()
    val dir = Paths.get(directoryPath)

    // register
    val key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
      StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)
    keys.put(key, dir)

    while(true) {
      val key = watcher.take()
      val dir = keys.get(key)

      for (tmp <- key.pollEvents().toList) {
        val event = tmp.asInstanceOf[WatchEvent[Path]]
        val kind = event.kind()
        if (kind != StandardWatchEventKinds.OVERFLOW) {
          val filename = event.context()
          val fullpath = dir.resolve(filename)
          eventHandler(event, fullpath)
        }
      }
      if (!key.reset()) {
        keys.remove(key)
      }
    }
  }

  def main(args: Array[String]) {
    FileChangeSimulator.start

    directoryWatch("src/test/resources/", eventHandler)
  }
}
