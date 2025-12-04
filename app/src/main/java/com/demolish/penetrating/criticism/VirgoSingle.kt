package com.demolish.penetrating.criticism

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.demolish.penetrating.criticism.databinding.VirgoSingleBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.delay

class VirgoSingle : BaseLoginHelper() {
    val binding by lazy { VirgoSingleBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.virgo)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.tvSkip.setOnClickListener {
            next()
        }
        binding.tvLogin.setOnClickListener {
            loginGoogle()
        }
        onBackPressedDispatcher.addCallback(this) {
        }
        lifecycleScope.launchWhenCreated {
            delay(1500)
            if (SettingsManager.isSkip) {
                next()
            } else {
                binding.layoutPro.visibility = View.GONE
                binding.tvSkip.visibility = View.VISIBLE
                binding.tvLogin.visibility = View.VISIBLE
            }
        }
    }

    private fun next() {
        SettingsManager.isSkip = true
        startActivity(Intent(this@VirgoSingle, LibraSingle::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        checkPrankLoginStatus()
    }

    override fun fetchLogin(): LoginListener {
        return object : LoginListener {
            override fun updateUi(account: GoogleSignInAccount) {
                SettingsManager.isSkip = true
                next()
            }

            override fun logout() {

            }
        }
    }
}