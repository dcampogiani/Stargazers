package com.danielecampogiani.demo

import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager

fun Fragment.hideKeyBoard() {

    activity?.let {

        val imm = it.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(it.currentFocus.windowToken, 0)
        }

    }
}