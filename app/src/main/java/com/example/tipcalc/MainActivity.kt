package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TipCalcApp() }
    }
}

@Composable
fun TipCalcApp() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { inner ->
            TipCalcScreen(Modifier.padding(inner).padding(16.dp))
        }
    }
}

@Composable
fun TipCalcScreen(modifier: Modifier = Modifier) {
    var sumText by rememberSaveable { mutableStateOf("") }
    var dishesText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LabeledField("Сумма заказа:", sumText) { sumText = keepDigitsCommaDot(it) }
        LabeledField("Количество блюд:", dishesText) { dishesText = keepDigitsCommaDot(it) }
    }
}

@Composable
private fun LabeledField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// утилита: разрешаем только цифры и , .
private fun keepDigitsCommaDot(src: String): String {
    val sb = StringBuilder(src.length)
    var i = 0
    while (i < src.length) {
        val ch = src[i]
        if ((ch in '0'..'9') || ch == '.' || ch == ',') {
            sb.append(if (ch == ',') '.' else ch)
        }
        i++
    }
    return sb.toString()
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewTipCalc() { TipCalcApp() }
