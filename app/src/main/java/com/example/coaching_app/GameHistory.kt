package com.example.coaching_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameHistory (
    var teamID: String? = null,
    var gameDate: String? = null,
    var opponent: String? = null,
    var result: String? = null,
    var ourScore: String? = null,
    var opponentScore: String? = null,
    var venue: String? = null,
    var weather: String? = null,
    var gameID: String? = null,
    var userID: String? = null
): Parcelable {
}