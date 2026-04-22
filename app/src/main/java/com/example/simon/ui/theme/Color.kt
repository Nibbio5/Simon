package com.example.simon.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

var Green by mutableStateOf(Color(0xFF008000))
var Blue by mutableStateOf(Color(0xFF0000FF))
var Red by mutableStateOf(Color(0xFFFF0000))
var Magenta by mutableStateOf(Color(0xFF9C27B0))
var Yellow by mutableStateOf(Color(0xFFFFFF00))
var Cyan by mutableStateOf(Color(0xFF00FFFF))

var simonColors = listOf(Cyan, Magenta, Blue, Yellow, Red, Green)

val simonLetters = listOf('C', 'M', 'B', 'Y', 'R', 'G')