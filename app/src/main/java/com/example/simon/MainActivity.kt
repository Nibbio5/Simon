package com.example.simon

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simon.database.GamesDatabase
import com.example.simon.database.GamesRepository
import com.example.simon.screens.DetailScreen
import com.example.simon.screens.ScoreScreen
import com.example.simon.ui.theme.SimonTheme
import com.example.simon.ui.theme.simonLetters

class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val database = GamesDatabase.getDatabase(applicationContext)
                val repository = GamesRepository(database.gamesDao())

                MainActivityViewModel(repository, savedStateHandle)
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

                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "score-screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("game-screen"){
                            MainScreen(onEndGame = {
                                navController.navigate("score-screen") {
                                    popUpTo("game-screen") { inclusive = true }
                                }
                            }, mainActivityViewModel)
                        }

                        composable("score-screen") {
                            ScoreScreen(
                                onStartGame = {
                                    navController.navigate("game-screen") {
                                        popUpTo("score-screen") { inclusive = true }
                                    }
                                },
                                onDetail = { passedId ->
                                    navController.navigate("detail-screen/$passedId")
                                },
                                mainActivityViewModel
                            )
                        }

                        composable(
                            "detail-screen/{gameId}",
                            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
                            DetailScreen(mainActivityViewModel, gameId)
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
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        RowMainScreen(
            onEndGame,
            mainActivityViewModel
        )
    } else {
        ColumnMainScreen(
            onEndGame,
            mainActivityViewModel)
    }
}

/**
 * This function along the ColumnMainScreen are the layout
 * for the Landscape orientation and the Portrait orientation
 * The structure is a little different for the Landscape orientation
 * since is use a Row and a LazyHorizontalGrid for the buttons and
 * the main widget distribution.
 *
 * Landscape screen
 *
 * @param onEndGame is used to navigate to the score screen
 * @param mainActivityViewModel is the view model of the activity
 */
@Composable
fun RowMainScreen(
    onEndGame: () -> Unit,
    mainActivityViewModel: MainActivityViewModel) {


    val isGameOver = mainActivityViewModel.isGameOver
    val pressedText = mainActivityViewModel.pressedText
    val isPaused = mainActivityViewModel.isPaused

    BackHandler {
        if (isGameOver) {
            onEndGame()
        } else {
            mainActivityViewModel.endGameWithButton()
            onEndGame()
        }
    }

    val scrollState = rememberScrollState(0)
    LaunchedEffect(pressedText) {
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
                mainActivityViewModel.colors) { index, _ ->
                SimonButton(
                    mainActivityViewModel.colors[index],
                    pressed = {
                        mainActivityViewModel.press(simonLetters[index])
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
                            mainActivityViewModel.startGame()
                        },
                        modifier = Modifier.size(130.dp, 50.dp).padding(5.dp),
                        shape = MaterialTheme.shapes.small,

                        ) {
                        Text(
                            text = stringResource(R.string.start_game_button_name),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            if(!isPaused)
                                mainActivityViewModel.pause()
                            else mainActivityViewModel.resume()
                        },
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ) {
                        Icon(
                            imageVector =
                                if(isPaused) {
                                    Icons.Filled.PlayCircle
                                }
                                else{
                                    Icons.Filled.PauseCircle
                                },
                            contentDescription = "Start/Pause"
                        )
                    }
                    Button(
                        {
                            mainActivityViewModel.endGameWithButton()
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
 * Portrait screen
 *
 * @param onEndGame is used to navigate to the score screen
 * @param mainActivityViewModel is the view model of the activity
 */
@Composable
fun ColumnMainScreen(
    onEndGame: () -> Unit,
    mainActivityViewModel: MainActivityViewModel
) {
    val isGameOver = mainActivityViewModel.isGameOver
    val pressedText = mainActivityViewModel.pressedText
    val isPaused = mainActivityViewModel.isPaused
    val isGameStart = mainActivityViewModel.isGameStart

    BackHandler {
        if (isGameOver) {
            onEndGame()
        } else {
            mainActivityViewModel.endGameWithButton()
            onEndGame()
        }
    }

    val scrollState = rememberScrollState(0)
    LaunchedEffect(pressedText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    // Reordering the indexes of the color to match the vertical grid layout
    val portraitOrderIndices = listOf(0, 3, 1, 4, 2, 5)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            userScrollEnabled = false,
            modifier = Modifier.padding(8.dp)
        ) {
            items(portraitOrderIndices.size) { i ->

                val actualIndex = portraitOrderIndices[i]

                SimonButton(
                    color = mainActivityViewModel.colors[actualIndex],
                    pressed = {
                        mainActivityViewModel.press(simonLetters[actualIndex])
                    }
                )
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
                        onClick = {
                            mainActivityViewModel.startGame()
                        },
                        modifier = Modifier
                            .size(130.dp, 50.dp)
                            .padding(5.dp)
                            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        enabled = !isGameStart
                        ) {
                        Text(
                            text = stringResource(R.string.start_game_button_name),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(
                        onClick = {
                            if(!isPaused) mainActivityViewModel.pause()
                            else mainActivityViewModel.resume()
                        },
                        enabled = mainActivityViewModel.isRobotPlaying && !isGameOver,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Icon(
                            imageVector =
                                if(isPaused) Icons.Filled.PlayCircle
                                else Icons.Filled.PauseCircle,
                            contentDescription = "Pause/Resume",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Button(
                        onClick = {
                            mainActivityViewModel.endGameWithButton()
                            onEndGame()
                        },
                        modifier = Modifier
                            .size(130.dp, 50.dp)
                            .padding(5.dp)
                            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        enabled = !isGameOver

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
 * @param pressed is a lambda function used to add the letter to the string of the buttons pressed
 */
@Composable
fun SimonButton (color: Color, pressed: () -> Unit){
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