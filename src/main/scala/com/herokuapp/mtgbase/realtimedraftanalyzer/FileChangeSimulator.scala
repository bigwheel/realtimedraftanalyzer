package com.herokuapp.mtgbase.realtimedraftanalyzer

import io.Source
import java.io.{PrintWriter, File}

class FileChangeSimulator(source: String, dest: String) extends Thread {
  override def run() {
    deleteFileIfExist(source)

    val samplePickScore = Source.fromFile(dest)
    val out = new PrintWriter(source)
    for (line <- samplePickScore.getLines()) {
      out.println(line)

      if (line.matches("\\s*")) {
        out.flush()
        Thread.sleep(1000)
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