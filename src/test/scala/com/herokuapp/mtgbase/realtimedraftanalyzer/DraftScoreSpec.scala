package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification

class DraftScoreSpec extends Specification {
  val sampleFilePath = "src/test/resources/sample-pick-score.txt"
  val obj = new DraftScore(sampleFilePath)

  "DraftScore" should {
    "be able to be created" in {
      obj must not be throwA[Exception]
    }
  }

  "DraftScore's text" should {
    "be String" in {
      obj.text must beAnInstanceOf[String]
    }
  }
}
