package com.example.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, error: Boolean) {
    textInputLayout.error = if (error) {
        textInputLayout.context.getString(R.string.wrong_name)
    } else {
        null
    }
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, error: Boolean) {
    textInputLayout.error = if (error) {
        textInputLayout.context.getString(R.string.wrong_count)
    } else {
        null
    }
}

//можно использовать в разметке String.valueOf(number) вместо этого адаптера
@BindingAdapter("numberAsText")
fun bindNumberAsText(textInputEditText: TextInputEditText, number: Int) {
    val text = if (number == 0) {
        textInputEditText.context.getString(R.string.default_one_count)
    } else {
        number.toString()
    }
    textInputEditText.setText(text)
}