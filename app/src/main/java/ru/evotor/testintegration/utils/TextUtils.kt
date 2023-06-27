package ru.evotor.testintegration.utils

import android.text.Editable
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import ru.evotor.testintegration.R
import java.math.BigDecimal

fun Editable.toStringIfNotEmpty(): String? =
    if (isEmpty()) {
        null
    } else {
        toString()
    }

fun Editable.toBigDecimalIfNotEmpty(): BigDecimal? =
    toStringIfNotEmpty()?.toBigDecimal()

fun Editable?.toBigDecimal(): BigDecimal =
    if (isNullOrEmpty()) {
        BigDecimal.ZERO
    } else {
        toString().toBigDecimal()
    }

fun MaterialAutoCompleteTextView.initAdapter(values: List<String>, onValueClick: (String) -> Unit) {
    val arrayAdapter =
        ArrayAdapter(context, R.layout.item_drop_down_menu, values)
    setAdapter(arrayAdapter)
    setOnItemClickListener { _, _, position, _ ->
        arrayAdapter.getItem(position)?.let { value ->
            onValueClick(value)
        }
    }
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            hideKeyboard()
        }
    }
}