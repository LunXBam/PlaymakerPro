package com.example.coaching_app

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team (
    var teamName: String? = null,
    var sport: String? = null,
    var city: String? = null,
    var colors: String? = null,
    var logo: String? = null,
    var coachID: String? = null,
    var teamID: String? = null
):Parcelable{}