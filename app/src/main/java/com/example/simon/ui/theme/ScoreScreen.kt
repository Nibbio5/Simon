package com.example.simon.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.simon.MainActivityViewModel

/**
 * The score screen is used in order to display the score
 * and the letter corresponding to the color pressed in sequence
 * @param historyList is a list of string corresponding to all the
 * played game since the start of the application
 * @param scoreList is a list of integer corresponding to all the
 * score gotten since the start of the application
 * both need to be rememberSavable in order to be saved when the
 * device change orientation
 */

/**
@Composable
fun ScoreScreen(historyList : MutableList<String> = rememberSaveable {
    //simon.getHistory()
                    mutableListOf()
                                                                     }
                , scoreList : MutableList<Int> = rememberSaveable{
                        mutableListOf()
                }) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(historyList) { index, element ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier
                  .padding(
                      20.dp
                  )
                  .background(
                      MaterialTheme.colorScheme.secondary,
                      MaterialTheme.shapes.small
                  )
                  .fillMaxWidth()
            ){
                Text(
                    modifier = Modifier
                        .padding(
                            10.dp
                        ),
                    text = "${scoreList[index]}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary

                )
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = element,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

        }
    }
}  **/
@Composable
fun ScoreScreen1(onStartGame : () -> Unit, mainActivityViewModel: MainActivityViewModel) {

    val gamesList by mainActivityViewModel.allGames.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(gamesList) { game ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(20.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.shapes.small
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "${game.score}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = game.sequence.subSequence(1, game.sequence.length - 1).toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Button(
                    onClick = {
                        onStartGame()
                    },
                    modifier = Modifier.padding(end = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = ">",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreScreen(onStartGame : () -> Unit,mainActivityViewModel: MainActivityViewModel) {

    val gamesList by mainActivityViewModel.allGames.collectAsState()

    // Lo Scaffold gestisce la struttura della schermata, incluso il FAB
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onStartGame()
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                // Icona a forma di controller
                Icon(
                    imageVector = Icons.Filled.VideogameAsset,
                    contentDescription = "Nuova Partita"
                )
            }
        }
    ) { innerPadding ->

        // Passiamo innerPadding alla LazyColumn per evitare che il FAB copra i contenuti
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding())
        ) {
            items(gamesList) { game ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(20.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.shapes.small
                        )
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "${game.score}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f),
                        text = game.sequence.subSequence(1, game.sequence.length - 1).toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Button(
                        onClick = {
                            // TODO: Azione per il bottone riga
                        },
                        modifier = Modifier.padding(end = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = ">",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

