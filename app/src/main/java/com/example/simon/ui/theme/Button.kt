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
    private var score: Int = 0,

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
            0 -> { val original = Yellow; Yellow = Color.White; delay(250); Yellow = original }
            1 -> { val original = Green; Green = Color.White; delay(250); Green = original }
            2 -> { val original = Blue; Blue = Color.White; delay(250); Blue = original }
            3 -> { val original = Magenta; Magenta = Color.White; delay(250); Magenta = original }
            4 -> { val original = Cyan; Cyan = Color.White; delay(250); Cyan = original }
            5 -> { val original = Red; Red = Color.White; delay(250); Red = original }
        }

    }

    suspend fun check (str: String) : String{
        val strList = str.split(",")
        var cont = 0
        for (i in 0 until strList.size -1 ) {
            if (strList[i].equals(simonLetter[gamesOrder[i]]))
                ++cont
        }
        if (cont == gamesOrder.size){
            ++score
            startRound()
            return ""
        }else if (strList.size - 1 == gamesOrder.size)
            return ""
        return str
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
