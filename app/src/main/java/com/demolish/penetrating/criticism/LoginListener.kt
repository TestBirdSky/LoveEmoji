package com.demolish.penetrating.criticism

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * Date：2025/12/4
 * Describe:
 */
interface LoginListener {

    fun updateUi(account: GoogleSignInAccount)

    fun logout()

}