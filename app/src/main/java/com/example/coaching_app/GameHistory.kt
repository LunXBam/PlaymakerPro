package com.example.coaching_app

data class GameHistory (
    var teamID: String? = null,
    var gameDate: String? = null,
    var opponent: String? = null,
    var result: String? = null,
    var ourScore: String? = null,
    var oppScore: String? = null,
    var venue: String? = null,
    var weather: String? = null,
    var gameID: String
)