package com.herokuapp.mtgbase.realtimedraftanalyzer

import java.nio.file.{FileSystems, WatchService}

object App {
  def main(args: Array[String]) {
    FileChangeSimulator.start

    while(true) {
    val watcher = FileSystems.getDefault.newWatchService
    val key = watcher.take

    for (event <- key.pollEvents) {

    }
    }
  }
}
