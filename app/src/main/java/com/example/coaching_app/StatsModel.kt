package com.example.coaching_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class StatsModel(
    var assists : String? = null,
    var blocks : String? = null,
    var fouls : String? = null,
    var gamesPlayed : String? = null,
    var goals : String? = null,
    var injured : String? = null,
    var playerID : String? = null,
    var redCards : String? = null,
    var shots : String? = null,
    var statsID : String? = null,
    var tackles : String? = null,
    var yellowCards: String? = null,
    var teamID: String? = null

):Parcelable{}