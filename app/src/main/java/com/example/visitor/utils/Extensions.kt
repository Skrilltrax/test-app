package com.example.visitor.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class Extensions {

    companion object {
        fun TextInputEditText.doAfterTextChanged (cb: (String) -> Unit) {
            this.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
}