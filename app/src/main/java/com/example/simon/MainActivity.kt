package com.example.simon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.simon.ui.theme.SimonBorderColor
import com.example.simon.ui.theme.SimonBorderWidth
import com.example.simon.ui.theme.SimonShape
import com.example.simon.ui.theme.SimonTheme
import com.example.simon.ui.theme.dim


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

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Reference: https://developer.android.com/develop/ui/compose/state-saving
    var c1 by rememberSaveable { mutableStateOf(false) }
    var c2 by rememberSaveable { mutableStateOf(false) }

    // Reference: https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    ConstraintLayout(modifier = modifier) {
        val (sw1, tv, red, green,cyan, blue, magenta, yellow , delete, end_game) = createRefs()
        var list = listOf(red, green, cyan, blue, magenta, yellow)
        var str = "ggg"

        /* Switch(
             checked = c1,
             onCheckedChange = { c1 = it },
             modifier = Modifier.constrainAs(sw1) {
                 start.linkTo(parent.start)
                 top.linkTo(parent.top)
                 if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                     end.linkTo(tv.start)
                     bottom.linkTo(parent.bottom)
                 }
                 else {
                     end.linkTo(parent.end)
                     bottom.linkTo(tv.top)
                 }
             }
         )
 */
        Text(
            modifier = Modifier.constrainAs(tv) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, dim*3)
                bottom.linkTo(parent.bottom)
            },
            text = str
        )

        Button(
            {str = " Red" },
            colors = ButtonDefaults.buttonColors(Color.Red),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(red) {
                    start.linkTo(parent.start)
                    end.linkTo(green.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(tv.top) //, 100.dp)
                }
                .size(dim, dim).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Red")
        }

        Button(
            {},
            colors = ButtonDefaults.buttonColors(Color.Green),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(green) {
                    start.linkTo(red.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(tv.top)
                }
                .size(dim).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape)
        ) {
            Text("Red")
        }

        Button(
            {},
            colors = ButtonDefaults.buttonColors(Color.Cyan),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(cyan) {
                    start.linkTo(parent.start)
                    end.linkTo(yellow.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(red.top)
                }
                .size(dim).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Red")
        }

        Button(
            {},
            colors = ButtonDefaults.buttonColors(Color.Yellow),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(yellow) {
                    start.linkTo(cyan.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(green.top)
                }
                .size(dim).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Red")
        }

        Button(
            {},
            colors = ButtonDefaults.buttonColors(Color.Magenta),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(magenta) {
                    start.linkTo(parent.start)
                    end.linkTo(blue.start)
                    top.linkTo(red.bottom)
                    bottom.linkTo(tv.top)
                }
                .size(dim).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Red")
        }

        Button(
            {},
            colors = ButtonDefaults.buttonColors(Color.Blue),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(blue) {
                    start.linkTo(magenta.end)
                    end.linkTo(parent.end)
                    top.linkTo(green.bottom)
                    bottom.linkTo(tv.top)
                }
                .size(dim).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Red")
        }



        Button(
            {},
            //colors = ButtonDefaults.buttonColors(Color.Magenta),
            //shape = RectangleShape,
            modifier = Modifier
                .constrainAs(delete) {
                    start.linkTo(parent.start)
                    end.linkTo(end_game.start)
                    top.linkTo(tv.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .size(dim, dim/3).padding(5.dp)
        ) {
            Text("Delete")
        }

        Button(
            {},
            //colors = ButtonDefaults.buttonColors(Color.Magenta),
            //shape = RectangleShape,
            modifier = Modifier
                .constrainAs(end_game) {
                    start.linkTo(delete.end)
                    end.linkTo(parent.end)
                    top.linkTo(tv.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .size(dim, dim/3).padding(5.dp)
        ) {
            Text("End Game")
        }


    }
}

fun SimonClick (ref: ConstrainedLayoutReference, tv: ConstrainedLayoutReference){

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
