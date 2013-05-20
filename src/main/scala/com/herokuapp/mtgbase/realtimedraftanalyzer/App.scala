package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file._

object App {
  def main(args: Array[String]) {
    FileChangeSimulator.start

    val watcher = FileSystems.getDefault.newWatchService
    val keys = collection.mutable.Map[WatchKey, Path]()
    val dir = Paths.get("src/test/resources/")

    val key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
      StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)
    keys += (key -> dir)
  }
}
