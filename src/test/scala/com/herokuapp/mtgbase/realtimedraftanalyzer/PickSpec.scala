package com.herokuapp.mtgbase.realtimedraftanalyzer

import org.specs2.mutable.Specification

class PickSpec extends Specification {
  "accept a valid cardlist" in {
    Pick(List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage"),
      Card("Righteous Authority", true),
      Card("Swamp")
    )) must not be throwAn[Exception]
  }

  "not accept a invalid cardlist that have more than one picked flag" in {
    Pick(List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage", true),
      Card("Righteous Authority", true),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException]
  }

  "not accept a invalid cardlist that dont have a picked flag" in {
    Pick(List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage"),
      Card("Righteous Authority"),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException]
  }
}
