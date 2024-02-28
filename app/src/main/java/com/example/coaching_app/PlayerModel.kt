package com.example.coaching_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerModel (
    var firstName: String? = null,
    var lastName: String? = null,
    var height: String? = null,
    var weight: String? = null,
    var birthdate: String? = null,
    var nationality: String? = null,
    var jerseyNumber: String? = null,
    var playerPosition: String? = null,
    var playerID: String? = null,
    var teamID: String? = null,
    var userID: String? = null
) : Parcelable{}