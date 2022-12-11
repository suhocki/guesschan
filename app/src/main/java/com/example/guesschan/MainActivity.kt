package com.example.guesschan

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val answers = listOf(
                    "https://i.pinimg.com/564x/ea/a7/ad/eaa7ad621f8922ba4cc568b8f9535b8a.jpg" to "pepsi",
                    "https://i.pinimg.com/564x/82/3d/c4/823dc43360361f8c36aaa083c17f3ea2.jpg" to "coke",
                    "https://i.pinimg.com/564x/25/27/31/25273185114afd744cb1e5ec34fe92fa.jpg" to "subway",
                )
                var i by remember {
                    mutableStateOf(0)
                }
                val (url, answer) = answers[i]
                Game(
                    url = url,
                    letters = answer.asSequence().shuffled().joinToString(separator = ""),
                ) { word ->
                    if (word == answer) {
                        i = (i + 1).rem(answers.size)
                    }
                }
            }
        }
    }

    @Composable
    private fun Game(
        url: String,
        letters: String,
        onWordEntered: (String) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = url,
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
            )
            PredefinedLettersMode(
                letters = letters,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 64.dp),
                onWordEntered = onWordEntered,
            )
        }
    }

    @Composable
    private fun PredefinedLettersMode(
        letters: String,
        modifier: Modifier,
        onWordEntered: (String) -> Unit,
    ) {
        var bottomLetters by remember(letters) {
            mutableStateOf(letters)
        }
        var topLetters by remember(letters) {
            mutableStateOf(String(letters.indices.map { ' ' }.toCharArray()))
        }
        Box(
            modifier = modifier
                .wrapContentWidth()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .defaultMinSize(minHeight = 132.dp)
            ) {
                Letters(
                    letters = topLetters,
                    onClickedAt = { position ->
                        val selected = topLetters[position]
                        topLetters = topLetters.replaceRange(position..position, " ")
                        bottomLetters = bottomLetters.replaceFirst(' ', selected)
                    }
                )
                Letters(
                    letters = bottomLetters,
                    onClickedAt = { position ->
                        val selected = bottomLetters[position]
                        bottomLetters = bottomLetters.replaceRange(position..position, " ")
                        topLetters = topLetters.replaceFirst(' ', selected)
                        if (topLetters.none { it == ' ' }) {
                            onWordEntered(topLetters)
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun Letters(letters: String, onClickedAt: (Int) -> Unit) {
        Row(
            modifier = Modifier.sizeIn(minHeight = 50.dp)
        ) {
            letters.forEachIndexed { index, letter ->
                Letter(letter) {
                    if (letter != ' ') {
                        onClickedAt(index)
                    }
                }
            }
        }
    }

    @Composable
    private fun Letter(char: Char, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .size(62.dp)
                .padding(8.dp)
                .clickable { onClick() },
            elevation = 4.dp,
        ) {
            Box {
                Text(
                    text = char.toString(),
                    fontSize = 22.sp,
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }
        }
    }
}