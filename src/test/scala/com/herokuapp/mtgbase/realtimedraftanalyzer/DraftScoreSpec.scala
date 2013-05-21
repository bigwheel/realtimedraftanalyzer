package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification

class DraftScoreSpec extends Specification {
  "The DraftScore Class" should {
    "be able to be created" in {
      (new DraftScore) must not be throwA[Exception]
    }
  }
}
