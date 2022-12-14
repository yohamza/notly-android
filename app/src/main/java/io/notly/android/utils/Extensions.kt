package io.notly.android.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import org.json.JSONObject


fun View.snack(message: String?, length: Int = Snackbar.LENGTH_SHORT) {
    val snack = Snackbar.make(this, message ?: "Oops something went wrong", length)
    snack.show()
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.show() {
    if(!this.isVisible) this.visibility = View.VISIBLE
}

fun View.hide() {
    if(this.isVisible) this.visibility = View.GONE
}

fun ResponseBody.getErrorMessage(): String{
    return JSONObject(this.charStream().readText()).getString("message")
}