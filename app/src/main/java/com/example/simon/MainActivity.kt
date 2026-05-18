package com.example.simon

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.InvalidationTracker
import com.example.simon.ui.theme.ScoreScreen
import com.example.simon.ui.theme.SimonTheme
import com.example.simon.ui.theme.gg
import com.example.simon.ui.theme.simon
import com.example.simon.ui.theme.simonColors
import com.example.simon.ui.theme.simonLetters
import database.GamesDatabase

class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels() {
        viewModelFactory {
            initializer {
                val database = GamesDatabase.getDatabase(applicationContext)
                val repository = GamesRepository(database.gamesDao())

                MainActivityViewModel(repository)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display on API level < 35
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        enableEdgeToEdge()
        setContent {
            SimonTheme {
                LaunchedEffect(Unit) {
                    mainActivityViewModel.startGame()
                }
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "game-screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("game-screen"){
                            MainScreen(onEndGame = {
                                    navController.navigate("score-screen")
                                }, mainActivityViewModel)

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

/**
 * Main screen is used for choosing the correct
 * screen layout for the correct orientation
 * a lambda function is used to pass the
 * @param onEndGame is used to navigate to the score screen
 */
@Composable
fun MainScreen (onEndGame: () -> Unit, mainActivityViewModel: MainActivityViewModel) {
    val orientation = LocalConfiguration.current.orientation
    var buttonsClicked by rememberSaveable { mutableStateOf("") }
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        RowMainScreen(onEndGame,buttonsClicked,{if (it == ""){
            buttonsClicked = it}
            else {
                buttonsClicked += it
        }
        },
            mainActivityViewModel)

    } else {
        ColumnMainScreen(onEndGame,buttonsClicked,
            {
                if (it == ""){
            buttonsClicked = it}
        else {
            buttonsClicked += it
        }
        },
            mainActivityViewModel)
    }
}

/**
 * This function along the ColumnMainScreen are the layout
 * for the Landscape orientation and the Portrait orientation
 * The structure is a little different for the Landscape orientation
 * since is use a Row and a LazyHorizontalGrid for the buttons and
 * the main widget distribution
 *
 * @param onEndGame is used to navigate to the score screen
 * @param onButtonsClickedChange is used to update the buttonsClicked
 * @param buttonsClicked is used to display the buttons pressed, and save the
 * pressed buttons in a string
 */
@Composable
fun RowMainScreen(
    onEndGame: () -> Unit, buttonsClicked: String,
    onButtonsClickedChange: (String) -> Unit,
    mainActivityViewModel: MainActivityViewModel) {
    val pressedText by mainActivityViewModel.getPressed().observeAsState("")
    val scrollState = rememberScrollState(0)
    LaunchedEffect(buttonsClicked) {
        scrollState.scrollTo(scrollState.maxValue)
    }


    Row(
        modifier = Modifier.fillMaxSize().padding(5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyHorizontalGrid (
            rows = GridCells.Fixed(3),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            itemsIndexed(
                mainActivityViewModel.colors) { index, entry ->
                SimonButton(
                    mainActivityViewModel.colors[index], simonLetters[index],
                    pressed = {
                        mainActivityViewModel.press(simonLetters[index])
                        //simon.press()
                    })
            }
        }


        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            userScrollEnabled = false,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                Text(
                    modifier = Modifier
                        .width(320.dp)
                        .height(85.dp)
                        .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.small)
                        .padding(5.dp)
                        .verticalScroll(scrollState, true),
                    text = pressedText,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Button(
                        {
                            onButtonsClickedChange("")
                            simon.resetPressed()
                        },
                        modifier = Modifier.size(130.dp, 50.dp).padding(5.dp),
                        shape = MaterialTheme.shapes.small,

                        ) {
                        Text(
                            text = stringResource(R.string.delete_button_name),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Button(
                        {
                            simon.endGame(buttonsClicked)
                            onButtonsClickedChange("")
                            onEndGame()
                        },
                        modifier = Modifier.size(130.dp, 50.dp).padding(5.dp),
                        shape = MaterialTheme.shapes.small,

                        ) {
                        Text(
                            text = stringResource(R.string.end_game_button_name),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

            }
        }
    }
}

/**
 * This function along the RowMainScreen are the layout
 * for the Landscape orientation and the Portrait orientation
 * The structure is a little different for the Landscape orientation
 * since is use a Column and a LazyVerticalGrid for the buttons and
 * the main widget distribution
 *
 * @param onEndGame is used to navigate to the score screen
 * @param onButtonsClickedChange is used to update the buttonsClicked
 * @param buttonsClicked is used to display the buttons pressed, and save the
 * pressed buttons in a string
 */
@Composable
fun ColumnMainScreen(
    onEndGame: () -> Unit,
    buttonsClicked: String,
    onButtonsClickedChange: (String) -> Unit,
    mainActivityViewModel: MainActivityViewModel)
{
    val scrollState = rememberScrollState(0)
    LaunchedEffect(buttonsClicked) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column (
        modifier =
            Modifier.fillMaxSize()
                .padding(5.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            userScrollEnabled = false
        ) {
            itemsIndexed(simonColors) {index, element ->
                SimonButton(element, simonLetters[index],
                    pressed = {
                        if(buttonsClicked != ""){
                            onButtonsClickedChange(", ${simonLetters[index]}")
                        }else{
                            onButtonsClickedChange ("${simonLetters[index]}")
                        }
                       // simon.press()
                    })
            }
        }

        LazyColumn(
           modifier = Modifier.fillMaxHeight(),
            userScrollEnabled = false,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                Text(
                    modifier = Modifier
                        .width(320.dp)
                        .height(85.dp)
                        .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.small)
                        .padding(5.dp)
                        .verticalScroll(scrollState, true),
                    text = buttonsClicked,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Button(
                        {
                            onButtonsClickedChange ("")
                            simon.resetPressed()
                        },
                        modifier =
                            Modifier.size(130.dp, 50.dp)
                                .padding(5.dp)
                                .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,

                        ) {
                        Text(
                            text = stringResource(R.string.delete_button_name),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Button(
                        {
                            simon.endGame(buttonsClicked)
                            onButtonsClickedChange("")
                            onEndGame()
                        },
                        modifier =
                            Modifier.size(130.dp, 50.dp)
                                .padding(5.dp)
                                .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,

                        ) {
                        Text(
                            text = stringResource( R.string.end_game_button_name),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }


        }

    }
            }


/**
 * This function is used to create the colored buttons of Simon game
 * @param color is the color of the button
 * @param letter is the capital starting letter corresponding to the color of the button
 * @param pressed is a lambda function used to add the letter to the string of the buttons pressed
 */
@Composable
fun SimonButton (color: Color, letter: Char, pressed: () -> Unit){
    Button(
        onClick = pressed,
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(4f/3f)
        ,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(color)

    ){
    }
}


