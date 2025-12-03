package com.demolish.penetrating.criticism

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.demolish.penetrating.criticism.databinding.VirgoSingleBinding
import kotlinx.coroutines.delay

class VirgoSingle : AppCompatActivity() {
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
        onBackPressedDispatcher.addCallback(this) {
        }
        lifecycleScope.launchWhenCreated {
            delay(1500)
            startActivity(Intent(this@VirgoSingle, LibraSingle::class.java))
            finish()
        }
    }
}