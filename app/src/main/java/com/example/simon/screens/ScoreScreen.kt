package com.example.simon.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.simon.MainActivityViewModel

/**
 * The score screen is used in order to display the score
 * and the letter corresponding to the color pressed in sequence
 * @param onStartGame is used to navigate to the game screen
 * @param onDetail is used to navigate to the detail screen
 * @param mainActivityViewModel is the view model of the main activity
 */
@Composable
fun ScoreScreen(
    onStartGame: () -> Unit,
    onDetail: (id: Int) -> Unit,
    mainActivityViewModel: MainActivityViewModel
) {

    //var of the games list
    val gamesList by mainActivityViewModel.allGames.collectAsState()

    Scaffold(
        topBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text = "Simon Score Board",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onStartGame() },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(
                    imageVector = Icons.Filled.VideogameAsset,
                    contentDescription = "Play Game"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            items(gamesList) { game ->
                Button(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .padding(20.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.shapes.small
                        )
                        .wrapContentSize(),
                    onClick = {
                        onDetail(game.id)
                    },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "${game.score}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        val sequenceText = game.sequence
                        val errorIndex = game.errorIndex

                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f),
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = buildAnnotatedString { //print the letter with te ', ' and add the color from the error point
                                sequenceText.forEachIndexed { index, char ->
                                    val letterColor = if (errorIndex != -1 && index >= errorIndex) {
                                        Color.Red
                                    } else {
                                        MaterialTheme.colorScheme.onSecondary
                                    }
                                    withStyle(style = SpanStyle(color = letterColor)) {
                                        append(char.toString())
                                    }
                                    if (index < sequenceText.lastIndex) {
                                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSecondary)) {
                                            append(", ")
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}