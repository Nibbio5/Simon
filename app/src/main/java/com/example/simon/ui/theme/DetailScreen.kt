package com.example.simon.ui.theme

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.simon.MainActivityViewModel

@Composable
fun DetailScreen(onStartGame : () -> Unit,mainActivityViewModel: MainActivityViewModel) {

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
                Button (
                    // shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),

                    modifier = Modifier
                        .padding(20.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.shapes.small
                        ).wrapContentSize(),
                    onClick = {

                    },
                ) {


                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        //verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            //.padding(20.dp)
                            .background(
                                MaterialTheme.colorScheme.secondary,

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
                            textAlign = TextAlign.End,
                            text = game.sequence.subSequence(1, game.sequence.length - 1)
                                .toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        /**
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
                         **/
                    }
                }
            }
        }
    }
}
