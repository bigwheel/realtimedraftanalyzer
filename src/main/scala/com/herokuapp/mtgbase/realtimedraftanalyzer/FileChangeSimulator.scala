package com.herokuapp.mtgbase.realtimedraftanalyzer

import io.Source
import java.io.{FileOutputStream, PrintWriter, File}

object FileChangeSimulator extends Thread {
  override def run() {
    val filename = "src/test/resources/test-target.txt"
    deleteFileIfExist(filename)

    val samplePickScore = Source.fromFile("src/test/resources/sample-pick-score.txt")
    for (line <- samplePickScore.getLines()) {
      val out = new PrintWriter(new FileOutputStream(filename, true))
      //println(line)
      out.println(line)
      out.flush()
      out.close()

      if (line.matches("\\s*"))
        Thread.sleep(1000)
    }
    deleteFileIfExist(filename)
  }

  def deleteFileIfExist(filename: String) {
    val testTarget = new File(filename)
    if (testTarget.exists())
      testTarget.delete()
  }
}