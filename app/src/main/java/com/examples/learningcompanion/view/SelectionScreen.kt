package com.examples.learningcompanion.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.examples.learningcompanion.singleton.InferenceSingleton
import com.examples.learningcompanion.model.LLMModel

@Composable
internal fun SelectionRoute(
    onModelSelected: () -> Unit = {},
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(LLMModel.entries) { model ->
            Button(
                onClick = {
                    InferenceSingleton.Companion.llmModel = model
                    onModelSelected()
                },
            ) {
                Text(model.toString())
            }
        }
    }
}
