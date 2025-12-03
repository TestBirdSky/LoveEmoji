package com.demolish.penetrating.criticism

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.demolish.penetrating.criticism.databinding.SagittariusSingleBinding

class SagittariusSingle : AppCompatActivity() {
    private val binding by lazy { SagittariusSingleBinding.inflate(layoutInflater) }
    private lateinit var prefs: SharedPreferences
    
    companion object {
        const val PREFS_NAME = "app_settings"
        const val KEY_VIBRATION = "vibration_enabled"
        const val KEY_FLASHLIGHT = "flashlight_enabled"
        const val PRIVACY_URL = "https://www.example.com/privacy" // TODO Replace with actual URL
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sagittarius)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        setupViews()
        loadSettings()
    }
    
    private fun setupViews() {
        // Back button
        binding.imgBack.setOnClickListener {
            finish()
        }
        
        // Vibration switch
        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            saveVibrationSetting(isChecked)
        }
        
        // Flashlight switch
        binding.switchFlashlight.setOnCheckedChangeListener { _, isChecked ->
            saveFlashlightSetting(isChecked)
        }
        
        // Share
        binding.layoutShare.setOnClickListener {
            shareApp()
        }
        
        // Privacy Policy
        binding.layoutPrivacy.setOnClickListener {
            openPrivacyPolicy()
        }
    }
    
    private fun loadSettings() {
        // Load vibration setting
        val vibrationEnabled = prefs.getBoolean(KEY_VIBRATION, false)
        binding.switchVibration.isChecked = vibrationEnabled
        
        // Load flashlight setting
        val flashlightEnabled = prefs.getBoolean(KEY_FLASHLIGHT, false)
        binding.switchFlashlight.isChecked = flashlightEnabled
    }
    
    private fun saveVibrationSetting(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VIBRATION, enabled).apply()
    }
    
    private fun saveFlashlightSetting(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_FLASHLIGHT, enabled).apply()
    }
    
    private fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, "Check out this awesome app: ${getString(R.string.app_name)}\n" +
                    "https://play.google.com/store/apps/details?id=${packageName}")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
    
    private fun openPrivacyPolicy() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}