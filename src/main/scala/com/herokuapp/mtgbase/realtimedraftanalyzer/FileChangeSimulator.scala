package com.herokuapp.mtgbase.realtimedraftanalyzer

import io.Source
import java.io.File

object FileChangeSimulator {
  def main(args: Array[String]) {
    val testTarget = new File("src/test/test-target.txt")
    if (testTarget.exists())
      testTarget.delete()

    val samplePickScore = Source.fromFile("src/test/sample-pick-score.txt")
    for (line <- samplePickScore.getLines()) {
      println(line)
      if (line.matches("\\s*"))
        Thread.sleep(1000)
    }
  }
}