package com.wikkot.drzewkav1

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.Locale

data class Transfer(val senders: List<String>, val recipients: List<String>, val amount: Double, val name: String)
data class SingleTransfer(val sender: String, val recipient: String, val amount: String)
data class SingleTransferByIds(val senderId: Int, val recipientId: Int, val amount: String)

object Constants {
    const val grosz = 0.01
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun doubleToAmount(d: Double) : String {
    return String.format(Locale.ENGLISH, "%.2f", d)
}