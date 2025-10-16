package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

/* ---------- Утилиты / логика ---------- */

/** Разрешаем только цифры и , . ; запятую превращаем в точку. */
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


// безопасный парсинг
private fun parseIntSafe(s: String): Int =
    try { java.lang.Integer.parseInt(s) } catch (_: Exception) { 0 }


/** Скидка в % по количеству блюд. */
private fun discountFor(dishes: Int): Int =
    if (dishes <= 0) 0
    else if (dishes <= 2) 3
    else if (dishes <= 5) 5
    else if (dishes <= 10) 7
    else 10

/* ---------- UI ---------- */

@Composable
fun TipCalcScreen(modifier: Modifier = Modifier) {
    var sumText by rememberSaveable { mutableStateOf("") }     // сумма заказа (строкой)
    var dishesText by rememberSaveable { mutableStateOf("") }  // кол-во блюд (строкой)

    // Слайдер чаевых: храним 0..1 и маппим в 0..25 (%)
    var tipT by rememberSaveable { mutableFloatStateOf(0f) }   // 0..1
    val tip = tipT * 25f                                       // 0..25 — пригодится на шаге 5

    val dishes = parseIntSafe(dishesText)
    val discount = discountFor(dishes) // программно определяем выбранную скидку

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Поля ввода
        LabeledField("Сумма заказа:", sumText) { sumText = keepDigitsCommaDot(it) }
        LabeledField("Количество блюд:", dishesText) { dishesText = keepDigitsCommaDot(it) }

        // Слайдер чаевых
        Text("Чаевые:", fontSize = 16.sp)
        Slider(
            value = tipT,
            onValueChange = { tipT = it },                // 0..1
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("0"); Text("25")
        }

        // Радиокнопки (некликабельные — выбор только программно)
        Text("Скидка:", fontSize = 22.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DiscountRadio(discount == 3, 3)
            DiscountRadio(discount == 5, 5)
            DiscountRadio(discount == 7, 7)
            DiscountRadio(discount == 10, 10)
        }
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

@Composable
private fun DiscountRadio(isSelected: Boolean, percent: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RadioButton(selected = isSelected, onClick = null, enabled = false)
        Text("$percent%", fontSize = 18.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewTipCalc() {
    TipCalcApp()
}
