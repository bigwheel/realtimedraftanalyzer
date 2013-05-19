package com.herokuapp.mtgbase.realtimedraftanalyzer

import io.Source
import java.io.{PrintWriter, File}

object FileChangeSimulator {
  def main(args: Array[String]) {
    val filename = "src/test/resources/test-target.txt"
    deleteFileIfExist(filename)

    val samplePickScore = Source.fromFile("src/test/resources/sample-pick-score.txt")
    for (line <- samplePickScore.getLines()) {
      val out = new PrintWriter(filename)
      println(line)
      out.println(line)
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