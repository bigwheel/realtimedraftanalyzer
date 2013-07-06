package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable._

class AppSpec extends Specification {
  "App" should {
    "not throw exception in processing sample pick score" in {
      // テストでおいてるだけなのできちんと削除すること
      new FileChangeSimulator("src/test/resources/test-target.txt",
        "src/test/resources/sample-pick-score.txt", 10000)

      var a = false
      App.reactions += {
        case _: swing.event.WindowClosed => a = true
      }
      App.main(Array.empty[String])
      while (a == false)
        Thread.sleep(1000) must not throwA
    }
  }
}
