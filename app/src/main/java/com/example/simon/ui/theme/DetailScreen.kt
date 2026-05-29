package com.example.simon.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.simon.MainActivityViewModel
import com.example.simon.R


/**
 * This is the screen that is used to show the detail of the game
 * is is shown to the user after he click on a game in the score screen.
 *
 * This Screen take the game from the room database and it print on a text
 * on screen, the index after which the error had appen are colored in red.
 *
 * @param mainActivityViewModel is the view model of the main activity
 * @param gameId is the id of the game to show the detail of
 */

@Composable
fun DetailScreen(mainActivityViewModel: MainActivityViewModel, gameId: Int) {

    val game by mainActivityViewModel.getGameById(gameId).collectAsState(initial = null)

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
            item {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "${stringResource(R.string.score_text)} ${game?.score ?: 0}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                )

                val sequenceText = game?.sequence ?: ""
                val errorIndex = game?.errorIndex ?: -1

                Text(
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    text = buildAnnotatedString {
                        sequenceText.forEachIndexed { index, char ->

                            val letterColor = if (errorIndex != -1 && index >= errorIndex) {
                                Color.Red
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }

                            withStyle(style = SpanStyle(color = letterColor)) {
                                append(char.toString())
                            }

                            if (index < sequenceText.lastIndex) {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
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
