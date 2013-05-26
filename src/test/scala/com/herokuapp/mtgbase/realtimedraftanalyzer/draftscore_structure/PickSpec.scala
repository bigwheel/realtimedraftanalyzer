package com.herokuapp.mtgbase.realtimedraftanalyzer.draftscore_structure

import org.specs2.mutable.Specification

class PickSpec extends Specification {
  "accept a valid cardlist" in {
    Pick(1, 11, List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage"),
      Card("Righteous Authority", picked = true),
      Card("Swamp")
    )) must not be throwAn[Exception]
  }

  "not accept more than one picked flag" in {
    Pick(1, 11, List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage", picked = true),
      Card("Righteous Authority", picked = true),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException](message = "pickされたカードが１枚ではありません")
  }

  "not accept picked flag nothing" in {
    Pick(1, 11, List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage"),
      Card("Righteous Authority"),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException](message = "pickされたカードが１枚ではありません")
  }

  "not accept less than 1 as packNumber" in {
    Pick(0, 14, List(
      Card("Drainpipe Vermin"),
      Card("Righteous Authority", picked = true)
    )) must throwAn[IllegalArgumentException](message = """packNumberが1未満\(0\)です""")
  }

  "have just card number depend on pickNumber" in {
    "accept case" in {
      Pick(1, 14, List(
        Card("Drainpipe Vermin"),
        Card("Righteous Authority", picked = true)
      )) must not be throwAn[Exception]
    }

    "dont accept case" in {
      Pick(1, 14, List(
        Card("Righteous Authority", picked = true)
      )) must throwAn[Exception](message = "pickNumberとカードの枚数が矛盾します " +
        "pickNumber:14 カードの枚数:1")
    }
  }
}
