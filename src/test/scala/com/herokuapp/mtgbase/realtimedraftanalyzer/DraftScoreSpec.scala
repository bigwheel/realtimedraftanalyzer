package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification
import java.util.{Calendar, Date}

class DraftScoreSpec extends Specification {
  val sampleFilePath = "src/test/resources/sample-pick-score.txt"
  val obj = new DraftScore(sampleFilePath)

  "DraftScore" should {
    "be able to be created" in {
      obj must not be throwA[Exception]
    }
  }

  "DraftScore#eventNumber" should {
    "be match with that in source file" in {
      obj.eventNumber must_== 4890335
    }
  }

  "DraftScore#date" should {
    "be match with that in source file" in {
      val cal = Calendar.getInstance()
      cal.set(2013, 0, 12, 16, 43, 12)
      cal.set(Calendar.MILLISECOND, 0)
      //obj.date.compareTo(cal.getTime) must_== 0
      obj.date must_== cal.getTime
    }
  }

  "DraftScore#players" should {
    "be match with that in source file" in {
      obj.players must_== Players("bigwheel", List("sheepmage", "physique",
        "jwj8717", "jpfeinberg", "td_tsunehito", "lackystar", "motoo"))
    }
  }

  "DraftScore#firstPack#expansion" should {
    "be match with that in source file" in {
      obj.firstPack.expansion must_== "RTR"
    }
  }

  /*
  "DraftScore's text" should {
    "be String" in {
      obj.text must beAnInstanceOf[String]
    }
  }*/
}
