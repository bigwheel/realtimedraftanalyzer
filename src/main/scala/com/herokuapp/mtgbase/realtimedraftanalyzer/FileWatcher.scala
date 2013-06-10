package com.herokuapp.mtgbase.realtimedraftanalyzer

import scala.actors.Actor._
import java.io.File

class FileWatcher(filePath: String,
                  modifiedEventHandler: (Unit => Unit),
                  intervalMillisecond: Int = 30) {

  private[this] def getFileSize = new File(filePath).length

  var lastFileSize = getFileSize

  actor {
    loop {
      Thread.sleep(intervalMillisecond)
      val nowFileSize = getFileSize
      if (lastFileSize != nowFileSize) {
        lastFileSize = nowFileSize
        modifiedEventHandler()
      }
    }
  }
}
