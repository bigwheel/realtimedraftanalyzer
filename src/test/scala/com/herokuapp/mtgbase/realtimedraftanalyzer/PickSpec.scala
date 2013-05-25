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

  "not accept more than one picked flag" in {
    Pick(List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage", true),
      Card("Righteous Authority", true),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException]
  }

  "not accept picked flag nothing" in {
    Pick(List(
      Card("Drainpipe Vermin"),
      Card("Giant Growth"),
      Card("Rix Maadi Guildmage"),
      Card("Righteous Authority"),
      Card("Swamp")
    )) must throwAn[IllegalArgumentException]
  }
}
