package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification
import java.util.Calendar

class DraftScoreSpec extends Specification {
  val obj = new DraftScore("src/test/resources/sample-pick-score.txt")

  "DraftScore" should {
    "be able to be created" in {
      obj must not be throwA[Exception]
    }

    "#eventNumber" in {
      "be match with that in source file" in {
        obj.eventNumber must_== 4890335
      }
    }

    "#date" in {
      "be match with that in source file" in {
        val cal = Calendar.getInstance()
        cal.set(2013, 0, 12, 16, 43, 12)
        cal.set(Calendar.MILLISECOND, 0)
        obj.date must_== cal.getTime
      }
    }

    "#players" in {
      "be match with that in source file" in {
        obj.players must_== Players("bigwheel", List("sheepmage", "physique",
          "jwj8717", "jpfeinberg", "td_tsunehito", "lackystar", "motoo"))
      }
    }

    "#firstPack" in {
      "#expansion be match with that in source file" in {
        obj.firstPack.expansion must_== "RTR"
      }

      "#picks(0) be match with that in source file" in {
        obj.firstPack.picks(0) must_== Pick(1, 1, List(
          Card("Drainpipe Vermin"),
          Card("Giant Growth"),
          Card("Crosstown Courier"),
          Card("Rubbleback Rhino"),
          Card("Azorius Arrester"),
          Card("Explosive Impact"),
          Card("Mind Rot"),
          Card("Axebane Guardian"),
          Card("Cobblebrute"),
          Card("Hussar Patrol"),
          Card("Aquus Steed"),
          Card("Archweaver"),
          Card("Rix Maadi Guildmage"),
          Card("Righteous Authority", picked = true),
          Card("Swamp"))
        )
      }
    }
  }
}
