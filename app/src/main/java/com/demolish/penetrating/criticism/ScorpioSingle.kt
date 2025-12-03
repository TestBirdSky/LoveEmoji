package com.demolish.penetrating.criticism

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.demolish.penetrating.criticism.databinding.ScorpioSingleBinding

class ScorpioSingle : AppCompatActivity() {
    private val binding by lazy { ScorpioSingleBinding.inflate(layoutInflater) }
    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    private var isPlaying = false
    private var isLooping = false
    private var selectedTimerDelay = 0 // in seconds
    private val timerHandler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null
    private var countdownRunnable: Runnable? = null
    private var remainingSeconds = 0
    
    private var vibrator: Vibrator? = null
    private var cameraManager: CameraManager? = null
    private var cameraId: String? = null
    private var isFlashlightOn = false
    private var flashlightRunnable: Runnable? = null
    
    private lateinit var audioItem: AudioItem
    
    companion object {
        const val EXTRA_AUDIO_ITEM_NAME = "audio_item_name"
        const val EXTRA_AUDIO_ITEM_IMAGE = "audio_item_image"
        const val EXTRA_AUDIO_ITEM_AUDIO = "audio_item_audio"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scorpio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Get audio data from intent
        val name = intent.getStringExtra(EXTRA_AUDIO_ITEM_NAME) ?: "Audio"
        val imageResId = intent.getIntExtra(EXTRA_AUDIO_ITEM_IMAGE, R.drawable.ic_ghost_1)
        val audioResId = intent.getIntExtra(EXTRA_AUDIO_ITEM_AUDIO, R.raw.sounds_ghost_1)
        
        audioItem = AudioItem(name, imageResId, audioResId, AudioType.GHOST)
        
        setupUI()
        setupControls()
        initializeAudio()
    }
    
    private fun setupUI() {
        binding.tvTitle.text = audioItem.name
        binding.imgAudio.setImageResource(audioItem.imageResId)
    }
    
    private fun setupControls() {
        // Back button
        binding.btnBack.setOnClickListener {
            // Ensure everything is stopped before finishing
            if (isPlaying) {
                stopAudio()
            }
            finish()
        }
        
        // Play/Stop button
        binding.btnPlay.setOnClickListener {
            togglePlayStop()
        }
        
        // Timer button - toggle dropdown
        binding.btnTimer.setOnClickListener {
            toggleTimerDropdown()
        }
        
        // Timer options
        binding.timer5s.setOnClickListener { selectTimer(5, binding.timer5s) }
        binding.timer10s.setOnClickListener { selectTimer(10, binding.timer10s) }
        binding.timer15s.setOnClickListener { selectTimer(15, binding.timer15s) }
        binding.timer20s.setOnClickListener { selectTimer(20, binding.timer20s) }
        binding.timerOff.setOnClickListener { selectTimer(0, binding.timerOff) }
        
        // Loop switch
        binding.switchLoop.setOnCheckedChangeListener { _, isChecked ->
            isLooping = isChecked
            mediaPlayer?.isLooping = isChecked
        }
        
        // Volume control
        setupVolumeControl()
        
        // Close dropdown when clicking outside
        binding.scorpio.setOnClickListener {
            if (binding.timerOptionsLayout.visibility == View.VISIBLE) {
                binding.timerOptionsLayout.visibility = View.GONE
            }
        }
    }
    
    private fun initializeAudio() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        
        // Initialize vibrator
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        
        // Initialize flashlight
        try {
            cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraId = cameraManager?.cameraIdList?.get(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        try {
            mediaPlayer = MediaPlayer.create(this, audioItem.audioResId)
            mediaPlayer?.isLooping = isLooping
            
            // Set completion listener
            mediaPlayer?.setOnCompletionListener {
                if (!isLooping) {
                    isPlaying = false
                    binding.imgPlayStop.setImageResource(R.drawable.ic_play)
                    stopRippleAnimation()
                    stopVibration()
                    stopFlashlight()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun togglePlayStop() {
        // Prevent multiple clicks during countdown or playing
        if (remainingSeconds > 0) {
            // Already in countdown, ignore
            return
        }
        
        if (isPlaying) {
            stopAudio()
        } else {
            if (selectedTimerDelay > 0) {
                startDelayedAudio()
            } else {
                startAudio()
            }
        }
    }
    
    private fun startAudio() {
        // Prevent starting audio if already playing
        if (isPlaying) {
            return
        }
        
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, audioItem.audioResId)
                mediaPlayer?.isLooping = isLooping
                mediaPlayer?.setOnCompletionListener {
                    if (!isLooping) {
                        isPlaying = false
                        binding.imgPlayStop.setImageResource(R.drawable.ic_play)
                        stopRippleAnimation()
                        stopVibration()
                        stopFlashlight()
                    }
                }
            }
            
            mediaPlayer?.start()
            isPlaying = true
            binding.imgPlayStop.setImageResource(R.drawable.ic_stop)
            startRippleAnimation()
            
            // Start vibration if enabled
            if (SettingsManager.isVibrationEnabled(this)) {
                startVibration()
            }
            
            // Start flashlight if enabled
            if (SettingsManager.isFlashlightEnabled(this)) {
                startFlashlight()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun stopAudio() {
        try {
            mediaPlayer?.pause()
            mediaPlayer?.seekTo(0)
            isPlaying = false
            binding.imgPlayStop.setImageResource(R.drawable.ic_play)
            stopRippleAnimation()
            stopVibration()
            stopFlashlight()
            
            // Cancel any pending timer and countdown
            timerRunnable?.let { timerHandler.removeCallbacks(it) }
            countdownRunnable?.let { timerHandler.removeCallbacks(it) }
            
            // Reset timer button if countdown was active
            if (remainingSeconds > 0) {
                resetTimerButton()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun startDelayedAudio() {
        // Initialize remaining seconds
        remainingSeconds = selectedTimerDelay
        
        // Update UI to show countdown
        binding.btnTimer.text = "${remainingSeconds}s"
        binding.btnTimer.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        
        // Start countdown
        countdownRunnable = object : Runnable {
            override fun run() {
                remainingSeconds--
                
                if (remainingSeconds > 0) {
                    binding.btnTimer.text = "${remainingSeconds}s"
                    timerHandler.postDelayed(this, 1000)
                } else {
                    // Start audio when countdown finishes
                    startAudio()
                    resetTimerButton()
                }
            }
        }
        
        timerHandler.postDelayed(countdownRunnable!!, 1000)
    }
    
    private fun toggleTimerDropdown() {
        if (binding.timerOptionsLayout.visibility == View.VISIBLE) {
            binding.timerOptionsLayout.visibility = View.GONE
        } else {
            // Position dropdown below timer button
            binding.timerOptionsLayout.post {
                binding.timerOptionsLayout.visibility = View.VISIBLE
            }
        }
    }
    
    private fun selectTimer(seconds: Int, selectedView: TextView) {
        selectedTimerDelay = seconds
        
        // If switching to off during countdown, cancel countdown and play immediately
        if (seconds == 0 && remainingSeconds > 0) {
            // Cancel ongoing countdown
            countdownRunnable?.let { timerHandler.removeCallbacks(it) }
            remainingSeconds = 0
            
            // Play audio immediately if not already playing
            if (!isPlaying) {
                startAudio()
            }
        }
        
        // Update button text and color
        if (seconds == 0) {
            binding.btnTimer.text = "off"
            binding.btnTimer.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        } else {
            binding.btnTimer.text = "${seconds}s"
            binding.btnTimer.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }
        
        // Reset all options
        resetTimerOptions()
        
        // Highlight selected option with appropriate background
        when (selectedView.id) {
            R.id.timer5s -> selectedView.setBackgroundResource(R.drawable.bg_timer_option_selected_top)
            R.id.timerOff -> selectedView.setBackgroundResource(R.drawable.bg_timer_option_selected_bottom)
            else -> selectedView.setBackgroundResource(R.drawable.bg_timer_option_selected)
        }
        
        // Hide dropdown
        binding.timerOptionsLayout.visibility = View.GONE
    }
    
    private fun resetTimerOptions() {
        binding.timer5s.setBackgroundResource(R.drawable.bg_timer_option_top)
        binding.timer10s.setBackgroundResource(R.drawable.bg_timer_option)
        binding.timer15s.setBackgroundResource(R.drawable.bg_timer_option)
        binding.timer20s.setBackgroundResource(R.drawable.bg_timer_option)
        binding.timerOff.setBackgroundResource(R.drawable.bg_timer_option_bottom)
    }
    
    private fun setupVolumeControl() {
        val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 15
        val currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 7
        
        // Use a larger range (1000) for better precision
        val seekBarMax = 1000
        binding.seekBarVolume.max = seekBarMax
        
        // Convert current volume to seekbar range
        val initialProgress = (currentVolume * seekBarMax) / maxVolume
        binding.seekBarVolume.progress = initialProgress
        
        binding.seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update volume when user drags the seekbar
                if (fromUser) {
                    // Map seekbar progress (0-1000) to actual volume (0-maxVolume)
                    val actualVolume = (progress * maxVolume) / seekBarMax
                    audioManager?.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        actualVolume,
                        0
                    )
                }
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        // Volume icon clicks
        binding.imgVolumeOff.setOnClickListener {
            binding.seekBarVolume.progress = 0
            audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        }
        
        binding.imgVolumeOn.setOnClickListener {
            binding.seekBarVolume.progress = seekBarMax
            audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Force stop playback state
        isPlaying = false
        
        // Stop and release media player
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Stop vibration and flashlight
        stopVibration()
        stopFlashlight()
        
        // Cancel any pending timers and countdown
        timerRunnable?.let { timerHandler.removeCallbacks(it) }
        countdownRunnable?.let { timerHandler.removeCallbacks(it) }
        flashlightRunnable?.let { timerHandler.removeCallbacks(it) }
        
        // Clear all handler callbacks
        timerHandler.removeCallbacksAndMessages(null)
    }
    
    override fun onPause() {
        super.onPause()
        // Pause audio when activity is paused
        if (isPlaying) {
            mediaPlayer?.pause()
            stopRippleAnimation()
            stopVibration()
            stopFlashlight()
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Resume audio if it was playing
        if (isPlaying) {
            mediaPlayer?.start()
            startRippleAnimation()
            
            // Resume vibration and flashlight if enabled
            if (SettingsManager.isVibrationEnabled(this)) {
                startVibration()
            }
            if (SettingsManager.isFlashlightEnabled(this)) {
                startFlashlight()
            }
        }
    }
    
    private fun startRippleAnimation() {
        // Show ripple circles
        binding.rippleCircle1.visibility = View.VISIBLE
        binding.rippleCircle2.visibility = View.VISIBLE
        binding.rippleCircle0.visibility = View.GONE

        // Animate ripple circle 1
        val scaleX1 = ObjectAnimator.ofFloat(binding.rippleCircle1, "scaleX", 1f, 1.8f)
        val scaleY1 = ObjectAnimator.ofFloat(binding.rippleCircle1, "scaleY", 1f, 1.8f)
        val alpha1 = ObjectAnimator.ofFloat(binding.rippleCircle1, "alpha", 1f, 0f)
        
        val animSet1 = AnimatorSet()
        animSet1.playTogether(scaleX1, scaleY1, alpha1)
        animSet1.duration = 1500
        animSet1.interpolator = AccelerateInterpolator()
        
        // Animate ripple circle 2 with delay
        val scaleX2 = ObjectAnimator.ofFloat(binding.rippleCircle2, "scaleX", 1f, 1.8f)
        val scaleY2 = ObjectAnimator.ofFloat(binding.rippleCircle2, "scaleY", 1f, 1.8f)
        val alpha2 = ObjectAnimator.ofFloat(binding.rippleCircle2, "alpha", 1f, 0f)
        
        val animSet2 = AnimatorSet()
        animSet2.playTogether(scaleX2, scaleY2, alpha2)
        animSet2.duration = 1500
        animSet2.interpolator = AccelerateInterpolator()
        animSet2.startDelay = 750
        
        // Repeat animations
        val handler = Handler(Looper.getMainLooper())
        val repeatRunnable = object : Runnable {
            override fun run() {
                if (isPlaying && binding.rippleCircle1.visibility == View.VISIBLE) {
                    animSet1.start()
                    animSet2.start()
                    handler.postDelayed(this, 1500)
                }
            }
        }
        
        animSet1.start()
        animSet2.start()
        handler.postDelayed(repeatRunnable, 1500)
    }
    
    private fun stopRippleAnimation() {
        binding.rippleCircle1.visibility = View.GONE
        binding.rippleCircle2.visibility = View.GONE
        binding.rippleCircle0.visibility = View.VISIBLE

        binding.rippleCircle1.clearAnimation()
        binding.rippleCircle2.clearAnimation()
        binding.rippleCircle1.scaleX = 1f
        binding.rippleCircle1.scaleY = 1f
        binding.rippleCircle2.scaleX = 1f
        binding.rippleCircle2.scaleY = 1f
        binding.rippleCircle1.alpha = 0f
        binding.rippleCircle2.alpha = 0f
    }
    
    private fun resetTimerButton() {
        if (selectedTimerDelay > 0) {
            binding.btnTimer.text = "${selectedTimerDelay}s"
            binding.btnTimer.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        } else {
            binding.btnTimer.text = "off"
            binding.btnTimer.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        }
        remainingSeconds = 0
    }
    
    private fun startVibration() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Continuous vibration pattern: vibrate for 500ms, pause 100ms, repeat
                val timings = longArrayOf(0, 500, 100)
                val amplitudes = intArrayOf(0, 255, 0)
                val vibrationEffect = VibrationEffect.createWaveform(timings, amplitudes, 0)
                vibrator?.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 500, 100), 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun stopVibration() {
        try {
            vibrator?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun startFlashlight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Stop any existing flashlight task first
                stopFlashlight()
                
                cameraId?.let {
                    cameraManager?.setTorchMode(it, true)
                    isFlashlightOn = true
                    // Flash pattern: on/off every 500ms
                    flashlightHandler()
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
    
    private fun flashlightHandler() {
        // Remove any pending flashlight tasks
        flashlightRunnable?.let { timerHandler.removeCallbacks(it) }
        
        if (isPlaying && SettingsManager.isFlashlightEnabled(this)) {
            flashlightRunnable = Runnable {
                try {
                    cameraId?.let {
                        isFlashlightOn = !isFlashlightOn
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cameraManager?.setTorchMode(it, isFlashlightOn)
                        }
                    }
                    flashlightHandler()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            timerHandler.postDelayed(flashlightRunnable!!, 500)
        } else {
            stopFlashlight()
        }
    }
    
    private fun stopFlashlight() {
        // Remove any pending flashlight tasks
        flashlightRunnable?.let { timerHandler.removeCallbacks(it) }
        flashlightRunnable = null
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraId?.let {
                    cameraManager?.setTorchMode(it, false)
                    isFlashlightOn = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}