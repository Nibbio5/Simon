package com.example.simon

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.simon.ui.theme.Blue
import com.example.simon.ui.theme.Cyan
import com.example.simon.ui.theme.Green
import com.example.simon.ui.theme.Magenta
import com.example.simon.ui.theme.Red
import com.example.simon.ui.theme.Simon
import com.example.simon.ui.theme.SimonBorderColor
import com.example.simon.ui.theme.SimonBorderWidth
import com.example.simon.ui.theme.SimonShape
import com.example.simon.ui.theme.SimonTheme
import com.example.simon.ui.theme.Yellow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    val simon = Simon()
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
                    MainScreen3(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    , simon)


                }
            }
        }

    }

}



@Composable
fun MainScreen3(modifier: Modifier = Modifier, simon: Simon)
{
    val orientation = LocalConfiguration.current.orientation

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var dim = (screenWidth / 2.2.dp).dp
    var tvDimW = (screenWidth/1.1.dp).dp
    var tvDimH = (screenHeight/12.dp).dp

    if (screenWidth > screenHeight){
        dim = (screenHeight / 2.2.dp).dp
        tvDimW = (screenWidth/4.dp).dp
        tvDimH = (screenHeight/5.3.dp).dp
    }

    // Reference: https://developer.android.com/develop/ui/compose/state-saving
    var c1 by rememberSaveable { mutableStateOf(false) }
    var c2 by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()



    // Reference: https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    ConstraintLayout(modifier = modifier) {
        val bottomGuideLine = createGuidelineFromBottom(screenHeight/3)
        val bottomVerticalGuideLine = createGuidelineFromEnd(screenWidth/3)
        val bottomtext = createGuidelineFromBottom(screenHeight/3/2)
        val (tv1, tv, red, green,cyan, blue, magenta, yellow , delete, end_game) = createRefs()
        var list = listOf(red, green, cyan, blue, magenta, yellow)
        var str by rememberSaveable { mutableStateOf("") }
        var scrollState = rememberScrollState(0)
        //str = ""

        LaunchedEffect(str) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        Text(
            modifier = Modifier.constrainAs(tv) {
                if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                    start.linkTo(bottomVerticalGuideLine)
                    top.linkTo(parent.top, 15.dp)
                    bottom.linkTo(delete.top)
                    end.linkTo(parent.end, 15.dp)
                }else {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(bottomGuideLine) //dim*3)
                    bottom.linkTo(bottomtext)
                }
            }.width(tvDimW)
                .height(tvDimH)
                .verticalScroll(scrollState, true),
            text = str,

        )

        Button(
            {
                str += (" R,")
                scope.launch {
                    simon.blink(5)
                    str = simon.check(str)
                }
            },
            colors = ButtonDefaults.buttonColors(Red),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(red) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(bottomVerticalGuideLine)
                        //top.linkTo(yellow.bottom)
                        //start.linkTo(blue.end)
                    }else{
                        start.linkTo(parent.start)
                        top.linkTo(blue.bottom)
                        end.linkTo(green.start, 1.dp)
                        bottom.linkTo(bottomGuideLine) //, 100.dp)
                    }
                }
                .height(dim).width(dim)
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Red")
        }

        Button(
            {
                str += (" G,")
                scope.launch {
                    simon.blink(1)
                    str = simon.check(str)
                }
            },
            colors = ButtonDefaults.buttonColors(Green),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(green) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        top.linkTo(parent.top)
                        bottom.linkTo(red.top)
                        end.linkTo(bottomVerticalGuideLine)
                    }else{
                        start.linkTo(red.end)
                        bottom.linkTo(bottomGuideLine)
                        end.linkTo(parent.end)
                        top.linkTo(yellow.bottom)
                    }


                }
                .height(dim).width(dim)
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Green")
        }

        Button(
            {
                str += (" B,")
                scope.launch {
                    simon.blink(2)
                    str = simon.check(str)
                }
            },
            colors = ButtonDefaults.buttonColors(Blue),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(blue) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        top.linkTo(yellow.bottom)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(red.start)
                        start.linkTo(cyan.end)
                    }else{
                        start.linkTo(parent.start)
                        bottom.linkTo(red.top)
                        end.linkTo(yellow.start)
                        top.linkTo(cyan.bottom)
                    }


                }
                .height(dim).width(dim)
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Green")
        }

        Button(
            colors = ButtonDefaults.buttonColors(Yellow),
            onClick = {str += (" Y,")
                scope.launch {
                    simon.blink(0)
                    str = simon.check(str)
                }},
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(yellow) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        top.linkTo(parent.top)
                        bottom.linkTo(blue.top)
                        end.linkTo(green.start)
                        start.linkTo(magenta.end)
                    }else{
                        start.linkTo(blue.end)
                        bottom.linkTo(green.top)
                        end.linkTo(parent.end)
                        top.linkTo(magenta.bottom)
                    }


                }
                .height(dim).width(dim)
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Green")
        }

        Button(
            {
                str += (" C,")
                scope.launch {
                    simon.blink(4)
                    str = simon.check(str)
                }
            },
            colors = ButtonDefaults.buttonColors(Cyan),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(cyan) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        top.linkTo(magenta.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(blue.start)
                    }else{
                        start.linkTo(parent.start)
                        bottom.linkTo(blue.top)
                        end.linkTo(magenta.start)
                        top.linkTo(parent.top)
                    }


                }
                .height(dim).width(dim)
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Green")
        }

        Button(
            {
                str += (" M,")
                scope.launch {
                    simon.blink(3)
                }
                scope.launch {
                    str = simon.check(str)
                }
            },
            colors = ButtonDefaults.buttonColors(Magenta),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(magenta) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(cyan.top)
                        end.linkTo(yellow.start)
                    }else{
                        start.linkTo(cyan.end)
                        bottom.linkTo(yellow.top)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }


                }
                .height(dim).width(dim)
                .border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
        ) {
            Text("Green")
        }

        Button(
            {},
            //colors = ButtonDefaults.buttonColors(Color.Magenta),
            //shape = RectangleShape,
            modifier = Modifier
                .constrainAs(delete) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        bottom.linkTo(parent.bottom)
                        //end.linkTo(end_game.start)
                        //start.linkTo(bottomVerticalGuideLine)
                        top.linkTo(tv.bottom)
                    }else{
                        start.linkTo(parent.start)
                        end.linkTo(end_game.start)
                        top.linkTo(bottomtext)
                        bottom.linkTo(parent.bottom)
                    }
                }
                .size((dim/1.2.dp).dp, dim/3).padding(5.dp)
        ) {
            Text("Delete")
        }

        Button(
            {},
            //colors = ButtonDefaults.buttonColors(Color.Magenta),
            //shape = RectangleShape,
            modifier = Modifier
                .constrainAs(end_game) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        //start.linkTo(delete.end)
                        top.linkTo(tv.bottom)
                    }else{
                        start.linkTo(delete.end)
                        end.linkTo(parent.end)
                        top.linkTo(bottomtext)
                        bottom.linkTo(parent.bottom)
                    }
                }
                .size((dim/1.2.dp).dp, dim/3).padding(5.dp)
        ) {
            Text("End Game")
        }

        LaunchedEffect(Unit) {
            simon.startRound()
        }


    }

}

@Composable
fun MainScreen2(modifier: Modifier = Modifier)
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
    ConstraintLayout(modifier = modifier) {
        val bottomGuideLine = createGuidelineFromBottom(screenHeight/3)
        val bottomVerticalGuideLine = createGuidelineFromEnd(screenHeight/3)
        val bottomtext = createGuidelineFromBottom(screenHeight/3/2)
        val (sw1, tv, red, green,cyan, blue, magenta, yellow , delete, end_game) = createRefs()
        var list = listOf(red, green, cyan, blue, magenta, yellow)
        var str by rememberSaveable { mutableStateOf("") }
        var dim = 0.dp
        //str = ""

        Text(
            modifier = Modifier.constrainAs(tv) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(bottomGuideLine) //dim*3)
                bottom.linkTo(bottomtext)
            },
            text = str
        )

        Button(
            {str += " R," },
            colors = ButtonDefaults.buttonColors(Yellow),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(red) {
                    start.linkTo(parent.start)
                    end.linkTo(green.start, 1.dp)
                    bottom.linkTo(bottomGuideLine) //, 100.dp)
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(bottomVerticalGuideLine)
                    }
                    }
                .height(dim2).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                ).fillMaxWidth()
        ) {
            Text("Red")
        }

        Button(
            {},
            colors = ButtonDefaults.buttonColors(Color.Green),
            shape = SimonShape,
            modifier = Modifier
                .constrainAs(green) {
                    start.linkTo(red.end, 1.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomGuideLine)
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(bottomVerticalGuideLine)
                    }

                }
                .height(dim2).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                )
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
                    bottom.linkTo(red.top, 2.dp)
                }
                .height(dim2).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                ).fillMaxWidth()
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
                    bottom.linkTo(green.top, 2.dp)
                }
                .height(dim2).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                ).fillMaxWidth()
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
                    bottom.linkTo(cyan.top)
                }
                .height(dim2).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                ).fillMaxWidth()
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
                    bottom.linkTo(yellow.top)
                }
                .height(dim2).border(
                    width = SimonBorderWidth,
                    color = SimonBorderColor,
                    shape = SimonShape
                ).fillMaxWidth()
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
                    top.linkTo(bottomtext)
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
                    top.linkTo(bottomtext)
                    bottom.linkTo(parent.bottom)
                }
                .size(dim, dim/3).padding(5.dp)
        ) {
            Text("End Game")
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
        val bottomGuideLine = createGuidelineFromBottom(screenHeight/3)
        val (sw1, tv, red, green,cyan, blue, magenta, yellow , delete, end_game) = createRefs()
        var list = listOf(red, green, cyan, blue, magenta, yellow)
        var dim = 0.dp
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
                top.linkTo(bottomGuideLine) //dim*3)
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
