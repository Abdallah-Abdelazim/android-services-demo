package com.abdallah_abdelazim.calculator.util

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackbar(@StringRes msgStrResId: Int) {
    view?.let {
        Snackbar.make(it, msgStrResId, Snackbar.LENGTH_LONG).show()
    }
}