package com.herokuapp.mtgbase.realtimedraftanalyzer

import io.Source
import java.io.{PrintWriter, File}
import scala.actors.Actor

class FileChangeSimulator(source: String, dest: String, waitTime: Long) {
  val process = Actor.actor {
    deleteFileIfExist(source)

    val samplePickScore = Source.fromFile(dest)
    val out = new PrintWriter(source)
    for (line <- samplePickScore.getLines()) {
      out.println(line)

      if (line.matches("\\s*")) {
        out.flush

        Thread.sleep(waitTime)
      }
    }
    out.close()
    deleteFileIfExist(source)
  }

  def deleteFileIfExist(filename: String) {
    val testTarget = new File(filename)
    if (testTarget.exists())
      testTarget.delete()
  }
}