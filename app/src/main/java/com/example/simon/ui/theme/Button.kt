package com.example.simon.ui.theme

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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