package com.danielecampogiani.demo

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager

fun androidx.fragment.app.Fragment.hideKeyBoard() {
    val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isAcceptingText) {
        imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
    }
}