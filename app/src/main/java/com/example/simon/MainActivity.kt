package com.example.simon

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simon.ui.theme.Blue
import com.example.simon.ui.theme.Cyan
import com.example.simon.ui.theme.Green
import com.example.simon.ui.theme.Magenta
import com.example.simon.ui.theme.Red
import com.example.simon.ui.theme.ScoreScreen
import com.example.simon.ui.theme.SimonBorderColor
import com.example.simon.ui.theme.SimonBorderWidth
import com.example.simon.ui.theme.SimonShape
import com.example.simon.ui.theme.SimonTheme
import com.example.simon.ui.theme.Yellow
import com.example.simon.ui.theme.simon
import com.example.simon.ui.theme.simonColors
import com.example.simon.ui.theme.simonLetters

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display on API level < 35
        enableEdgeToEdge()
        setContent {
            SimonTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "game-screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        /*composable("game-screen") {
                            MainScreen (
                                onEndGame = {
                                    navController.navigate("score-screen")

                                }
                            )
                        }*/
                        composable("game-screen"){
                            MainScreen2 (
                                onEndGame = {
                                    navController.navigate("score-screen")
                                }
                            )
                        }
                        composable ("score-screen") {
                            ScoreScreen ()
                        }
                        }
                    }
                }
            }
        }
    }


@Composable
fun MainScreen(     //modifier: Modifier = Modifier,
                onEndGame: () -> Unit)
{
    val orientation = LocalConfiguration.current.orientation

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var dim = (screenWidth / 2.dp).dp
    var tvDimW = (screenWidth/1.1.dp).dp
    var tvDimH = (screenHeight/12.dp).dp

    if (screenWidth > screenHeight){
        dim = (screenHeight / 3.5.dp).dp
        tvDimW = (screenWidth/3.dp).dp
        tvDimH = (screenHeight/5.dp).dp
    }

    // Reference: https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()) {

        val (tv, red, green,cyan, blue, magenta, yellow , delete, end_game) = createRefs()
        var str by rememberSaveable { mutableStateOf("") }
        var scrollState = rememberScrollState(0)

        LaunchedEffect(str) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        Text(
            modifier = Modifier
                .constrainAs(tv) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        start.linkTo(green.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(delete.top)
                        end.linkTo(parent.end)
                    } else {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(red.bottom) //dim*3)
                        bottom.linkTo(delete.top)
                    }
                }
                .width(tvDimW)
                .height(tvDimH)
                .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.small)
                .padding(5.dp)
                .verticalScroll(scrollState, true),
            text = str,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondary
            )

        Button(
            {
                if (str != ""){
                    str += (", R")
                }else{
                    str += ("R")
                }
                simon.press()
            },
            colors = ButtonDefaults.buttonColors(Red),
            shape = SimonShape,
            modifier =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier
                        .constrainAs(red) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(green.start)
                            top.linkTo(blue.bottom)
                            //start.linkTo(parent.start)
                        }
                        .height(dim)
                        .width((1.5 * dim))
                }else{
                    Modifier
                        .constrainAs(red) {
                            start.linkTo(parent.start)
                            top.linkTo(blue.bottom)
                            end.linkTo(green.start, 1.dp)
                            bottom.linkTo(tv.top) //, 100.dp)
                        }
                        .height(dim)
                        .width(dim)
                }
                    .border(
                        width = SimonBorderWidth,
                        color = SimonBorderColor,
                        shape = SimonShape
                    )
        ) {}

        Button(
            {
                if (str != ""){
                    str += (", G")
                }else{
                    str += ("G")
                }
                simon.press()
            },
            colors = ButtonDefaults.buttonColors(Green),
            shape = SimonShape,
            modifier =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier
                        .constrainAs(green) {

                            top.linkTo(yellow.bottom)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(tv.start)
                            start.linkTo(parent.start, 30.dp)

                        }
                        .height(dim)
                        .width((1.5 * dim))
                }else{
                    Modifier
                        .constrainAs(green) {
                            start.linkTo(red.end)
                            bottom.linkTo(tv.top)
                            end.linkTo(parent.end)
                            top.linkTo(yellow.bottom)
                        }

                        .height(dim)
                        .width(dim)
                }

                    .border(
                        width = SimonBorderWidth,
                        color = SimonBorderColor,
                        shape = SimonShape
                    )
        ) {}

        Button(
            {
                if (str != ""){
                    str += (", B")
                }else{
                    str += ("B")
                }
                simon.press()
            },
            colors = ButtonDefaults.buttonColors(Blue),
            shape = SimonShape,
            modifier =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier
                        .constrainAs(blue) {

                            top.linkTo(cyan.bottom) //
                            bottom.linkTo(red.top)
                            end.linkTo(yellow.start)
                            //start.linkTo(parent.start)
                        }
                        .height(dim)
                        .width((1.5 * dim))
                }
                else{
                        Modifier
                            .constrainAs(blue) {
                                start.linkTo(parent.start)
                                bottom.linkTo(red.top)
                                end.linkTo(yellow.start)
                                top.linkTo(cyan.bottom)
                            }
                            .height(dim)
                            .width(dim)
                }
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {}

        Button(
            colors = ButtonDefaults.buttonColors(Yellow),
            onClick = {
                if (str != ""){
                    str += (", Y")
                }else{
                    str += ("Y")
                }
                simon.press()
              },
            shape = SimonShape,
            modifier =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier
                        .constrainAs(yellow) {

                            top.linkTo(magenta.bottom)
                            bottom.linkTo(green.top)
                            end.linkTo(tv.start)
                            start.linkTo(parent.start)

                        }
                        .height(dim)
                        .width((1.5 * dim))
                }else{
                    Modifier
                        .constrainAs(yellow) {
                            start.linkTo(blue.end)
                            bottom.linkTo(green.top)
                            end.linkTo(parent.end)
                            top.linkTo(magenta.bottom)
                        }

                        .height(dim)
                        .width(dim)
                }
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {}

        Button(
            {
                if (str != ""){
                    str += (", C")
                }else{
                    str += ("C")
                }
                simon.press()
            },
            colors = ButtonDefaults.buttonColors(Cyan),
            shape = SimonShape,
            modifier =
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Modifier
                    .constrainAs(cyan) {

                        top.linkTo(parent.top)
                        // start.linkTo(parent.start)
                        bottom.linkTo(blue.top)
                        end.linkTo(magenta.start)

                    }
                    .height(dim)
                    .width((1.5 * dim))
            }else{
                Modifier
                    .constrainAs(cyan) {
                        start.linkTo(parent.start)
                        bottom.linkTo(blue.top)
                        end.linkTo(magenta.start)
                        top.linkTo(parent.top)
                    }

                    .height(dim)
                    .width(dim)
            }

                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {}

        Button(
            {
                if (str != ""){
                    str += (", M")
                }else{
                    str += ("M")
                }
                simon.press()
            },
            colors = ButtonDefaults.buttonColors(Magenta),
            shape = SimonShape,
            modifier =

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier
                        .constrainAs(magenta) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(yellow.top)
                            end.linkTo(tv.start)
                        }
                        .height(dim)
                        .width((1.5 * dim))
                }else{
                    Modifier
                        .constrainAs(magenta) {
                            start.linkTo(cyan.end)
                            bottom.linkTo(yellow.top)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .height(dim)
                        .width(dim)
                }
                    .border(
                        width = SimonBorderWidth,
                        color = SimonBorderColor,
                        shape = SimonShape
                    )
        ) {}

        Button(
            {
                str = ""

            },
            modifier =
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Modifier
                    .constrainAs(delete) {

                        bottom.linkTo(parent.bottom)
                        end.linkTo(end_game.start)
                        start.linkTo(green.end)
                        top.linkTo(tv.bottom)
                    }
                    .size(1.3 * dim, dim / 2)
                    .padding(5.dp)
                    }else{
                    Modifier
                        .constrainAs(delete) {
                            start.linkTo(parent.start)
                            end.linkTo(end_game.start)
                            top.linkTo(tv.bottom)
                            bottom.linkTo(parent.bottom)
                        }
                        .size((dim / 1.2.dp).dp, dim / 3)
                        .padding(5.dp)
                }
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)

        ) {
            Text(
                text = "Delete",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Button(
            onClick ={
                simon.endGame(str)
                str = ""
                onEndGame()
            },
            modifier =

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Modifier
                        .constrainAs(end_game) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(delete.end)
                            top.linkTo(tv.bottom)
                        }
                        .size(1.3 * dim, dim / 2)
                        .padding(5.dp)
                }
                else{
                    Modifier
                        .constrainAs(end_game) {
                            start.linkTo(delete.end)
                            end.linkTo(parent.end)
                            top.linkTo(tv.bottom)
                            bottom.linkTo(parent.bottom)
                        }
                        .size((dim / 1.2.dp).dp, dim / 3)
                        .padding(5.dp)
                }
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)

        ) {
            Text(
                text = "End Game",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@Composable
fun MainScreen2(onEndGame: () -> Unit)
{
    val orientation = LocalConfiguration.current.orientation

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val dim2 = (screenWidth / 2.4.dp).dp


    // Reference: https://developer.android.com/develop/ui/compose/state-saving
    var c1 by rememberSaveable { mutableStateOf(false) }
    var c2 by rememberSaveable { mutableStateOf(false) }

    // Reference: https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    Row (
        modifier =
            Modifier.fillMaxSize()
                .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        LazyColumn (
                modifier = Modifier.weight(2/7f)


            ){
                itemsIndexed(simonColors) {index, element ->
                    SimonButton(element, simonLetters[index])
                }
            }
            LazyColumn(
                modifier = Modifier.weight(2/7f)
                    .fillMaxHeight(),
                userScrollEnabled = false
            ) {

            }
            LazyColumn(
                modifier = Modifier.weight(3/7f)
                    .fillMaxHeight()
            ) {

            }
        }
    }


@Composable
fun SimonButton (color: Color, letter: Char){
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth()
            .height(90.dp)
        ,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(color)

    ){}
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
