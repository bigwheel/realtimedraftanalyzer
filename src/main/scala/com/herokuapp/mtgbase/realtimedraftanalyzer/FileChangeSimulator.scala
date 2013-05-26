package com.herokuapp.mtgbase.realtimedraftanalyzer

import io.Source
import java.io.{FileOutputStream, PrintWriter, File}

class FileChangeSimulator(source: String, dest: String) extends Thread {
  override def run() {
    deleteFileIfExist(source)

    val samplePickScore = Source.fromFile(dest)
    for (line <- samplePickScore.getLines()) {
      val out = new PrintWriter(new FileOutputStream(source, true))
      out.println(line)
      out.close()

      if (line.matches("\\s*"))
        Thread.sleep(1000)
    }
    deleteFileIfExist(source)
  }

  def deleteFileIfExist(filename: String) {
    val testTarget = new File(filename)
    if (testTarget.exists())
      testTarget.delete()
  }
}