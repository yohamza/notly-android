package io.notly.android.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import io.notly.android.utils.Constants.PREFS_TOKEN_FILE
import io.notly.android.utils.Constants.USER_TOKEN

class TokenManager @Inject constructor(@ApplicationContext val context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun geToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}