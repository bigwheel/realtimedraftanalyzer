package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file._
import scala.collection.JavaConversions._
import scala.util.control.Breaks

class DirectoryWatcher(directoryPath: String,
                       eventHandler: ((WatchEvent[Path], Path) => Boolean)) {
  // constructor
  val watcher = FileSystems.getDefault.newWatchService()
  val dir = Paths.get(directoryPath)

  // register
  val key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)
  val keys: Map[WatchKey, Path] = Map(key -> dir)

  Breaks.breakable {
    while(true) {
      val key = watcher.take()
      val dir = keys.get(key) match {
        case Some(n) => n
        case _ => throw new IllegalStateException("keysの状態またはkeyの値が不正です")
      }

      for (tmp <- key.pollEvents().toList) {
        val event = tmp.asInstanceOf[WatchEvent[Path]]
        val kind = event.kind()
        if (kind != StandardWatchEventKinds.OVERFLOW) {
          val filename = event.context()
          val fullpath = dir.resolve(filename)
          if (eventHandler(event, fullpath) == false)
            Breaks.break

        }
      }
      if (!key.reset())
        keys.remove(key)
    }
  }
}