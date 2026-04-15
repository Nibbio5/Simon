package com.example.simon.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ScoreScreen(historyList : MutableList<String> = rememberSaveable()  { simon.getHistory()}, scoreList : MutableList<Int> = rememberSaveable(){ simon.getScoreHistory() }) {
    var scrollState = rememberScrollState(0)
    LazyColumn(
        modifier = Modifier.fillMaxSize() //.verticalScroll(scrollState)
    ) {
        itemsIndexed(historyList) { index, element ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,

                //Takes the background color from the scheme, the padding is nedded in order to
                // mantain a good margin from the borders
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
}

