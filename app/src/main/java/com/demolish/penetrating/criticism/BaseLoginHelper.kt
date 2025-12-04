package com.demolish.penetrating.criticism

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

/**
 * Date：2025/12/4
 * Describe:
 */
abstract class BaseLoginHelper : AppCompatActivity() {

    abstract fun fetchLogin(): LoginListener


    protected fun checkPrankLoginStatus() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            fetchLogin().updateUi(account)
        }
    }

    protected fun loginGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private val RC_GOOGLE_SIGN_IN = 12345

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            delUi(task)
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun delUi(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                fetchLogin().updateUi(account)
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}