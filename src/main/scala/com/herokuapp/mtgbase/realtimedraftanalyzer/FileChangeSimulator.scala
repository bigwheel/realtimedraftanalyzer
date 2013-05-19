package com.herokuapp.mtgbase.realtimedraftanalyzer

import io.Source
import java.io.{PrintWriter, File}

object FileChangeSimulator {
  def main(args: Array[String]) {
    val filename = "src/test/test-target.txt"
    val testTarget = new File(filename)
    if (testTarget.exists())
      testTarget.delete()
    val out = new PrintWriter(filename)

    val samplePickScore = Source.fromFile("src/test/resources/sample-pick-score.txt")
    for (line <- samplePickScore.getLines()) {
      println(line)
      out.println(line)
      if (line.matches("\\s*"))
        Thread.sleep(1000)
    }
    out.close()
  }
}