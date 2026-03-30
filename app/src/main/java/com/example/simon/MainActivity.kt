package com.example.simon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.simon.ui.theme.SimonTheme
import com.example.simon.ui.theme.buttonDim


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimonTheme {
                // Reference: https://developer.android.com/develop/ui/compose/components/scaffold
                // The scaffold fills the whole display area
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // MainScreen consumes the insets
                    // to keep the app UI away from the system UI and display cutouts
                    MainScreen(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier)
{
    val orientation = LocalConfiguration.current.orientation

    // Reference: https://developer.android.com/develop/ui/compose/state-saving
    var c1 by rememberSaveable { mutableStateOf(false) }
    var c2 by rememberSaveable { mutableStateOf(false) }

    // Reference: https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    ConstraintLayout(
        modifier = modifier){
        val (row1) = createRefs()
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(row1) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        ) {
            //ConstraintLayout() { }
            Column {

                Button(
                    modifier = Modifier.height(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
                Button(
                    modifier = Modifier.height(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
                Button(
                    modifier = Modifier.height(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
            }

            Column {

                Button(
                    modifier = Modifier.height(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
                Button(
                    modifier = Modifier.height(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
                Button(
                    modifier = Modifier.height(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
            }

            Column {

                Button(
                    modifier = Modifier.height(buttonDim).width(buttonDim),
                    shape = RectangleShape,
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
                Button(
                    modifier = Modifier.height(buttonDim).width(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
                Button(
                    modifier = Modifier.height(buttonDim),
                    colors = ButtonColors(Color.Red, Color.Transparent, Color.Green, Color.Yellow),
                    onClick = { },
                ) {
                    Text("cc")
                }
            }

        }
    }
}

@Composable
fun Grid() {

    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("riga1")
            Text("riga2")
            Text("riga3")
        }

        Column {
            Text("riga1")
            Text("riga2")
            Text("riga3")
        }

        Column {
            Text("riga1")
            Text("riga2")
            Text("riga3")
        }

    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonTheme {
        Greeting("Android")
    }
}
