package com.example.simon.ui.theme

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Simon (
    private var gamesOrder: MutableList<Int> = mutableListOf(),
    private var score: MutableList<String> = mutableListOf()

){
    val simonLetter = arrayOf(" Y", " G", " B", " M", " C", " R")

    suspend fun startRound() = withContext(Dispatchers.Default){
        val index = Random.nextInt(0, 6)
        delay(1000L)
        gamesOrder.add(index)
        for( i in gamesOrder) {
            blink(i)
            delay(250L)
        }

    }

    suspend fun blink(index: Int) = withContext(Dispatchers.Default){
        when (index) {
            0 -> { Yellow = Color.White; delay(250); Yellow = Color.Yellow }
            1 -> { Green = Color.White; delay(250); Green = Color.Green }
            2 -> { Blue = Color.White; delay(250); Blue = Color.Blue }
            3 -> { Magenta = Color.White; delay(250); Magenta = Color.Magenta }
            4 -> { Cyan = Color.White; delay(250); Cyan = Color.Cyan }
            5 -> { Red = Color.White; delay(250); Red = Color.Red }
        }

    }

    fun setScore (str : String) {
        score.add(str)
    }

   /* suspend fun check (str: String) : String{
        val strList = str.split(",")
        var cont = 0
        for (i in 0 until strList.size -1 ) {
            if (strList[i].equals(simonLetter[gamesOrder[i]]))
                ++cont
        }
        if (cont == gamesOrder.size){

            startRound()
            return ""
        }else if (strList.size - 1 == gamesOrder.size)
            return ""
        return str
    }*/

    fun getScores (): MutableList<String> {
        return score
    }

}

@Composable
fun SButton (modifier: Modifier){
    Button(
        modifier = modifier,
        colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
        onClick = { },
    ) {
        Text("cc")
    }
}
