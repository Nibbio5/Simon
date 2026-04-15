package com.example.simon.ui.theme

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreScreen(simon: Simon, ScoreList : MutableList<String> = simon.getScores()) {
    var scrollState = rememberScrollState(0)
    LazyColumn(
        modifier = Modifier.fillMaxSize() //.verticalScroll(scrollState)
    ) {
        items(ScoreList) { element ->
            Row(
              //  Modifier.background()
            ){
                Text(
                    text = element,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
  /*  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.strings.end_game_button_name),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onStartClicked) {
            Text(text = stringResource(R.strings.start))
        }
    }*/
}

