package com.herokuapp.mtgbase.realtimedraftanalyzer

case class Players(recordingPlayer: String = null,
                   others: List[String] = List.empty[String]) {
  def appendOtherPlayer(appendingPlayer: String): Players = {
    Players(this.recordingPlayer, appendingPlayer::this.others)
  }
}
