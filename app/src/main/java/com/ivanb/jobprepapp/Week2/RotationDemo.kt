package com.ivanb.jobprepapp.Week2

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// BEZ ViewModela - obični state u composable-u
@Composable
fun CounterWithoutViewModel() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Bez ViewModela: $count")
    }
}

// ViewModel s vlastitim state-om (plain mutableStateOf za sad, StateFlow dolazi sutra)
class CounterViewModel : ViewModel() {
    var count by mutableStateOf(0)
        private set

    fun increment() {
        count++
    }
}

@Composable
fun CounterWithViewModel(viewModel: CounterViewModel = viewModel()) {
    Button(onClick = { viewModel.increment() }) {
        Text("S ViewModelom: ${viewModel.count}")
    }
}

@Composable
fun RotationDemoScreen(modifier: Modifier = Modifier) {
    Column (modifier = modifier) {
        CounterWithoutViewModel()
        CounterWithViewModel()
    }
}