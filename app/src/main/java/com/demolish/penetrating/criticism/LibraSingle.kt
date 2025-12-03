package com.demolish.penetrating.criticism

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.demolish.penetrating.criticism.databinding.LibraSingleBinding

class LibraSingle : AppCompatActivity() {
    private val binding by lazy { LibraSingleBinding.inflate(layoutInflater) }
    private lateinit var audioAdapter: AudioAdapter
    private var currentType = AudioType.GHOST
    
    private val allAudioItems = mutableListOf<AudioItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Enable immersive status bar
        setupImmersiveMode()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.libra)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 50, systemBars.right, systemBars.bottom)
            insets
        }

        initializeAudioData()
        setupRecyclerView()
        setupTabs()
        
        // Set default selected tab
        selectTab(binding.tabGhost)
    }
    
    private fun setupImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.red_primary)
    }

    private fun initializeAudioData() {
        // Ghost items
        allAudioItems.add(AudioItem("Ghost 1", R.drawable.ic_ghost_1, R.raw.sounds_ghost_1, AudioType.GHOST))
        allAudioItems.add(AudioItem("Ghost 2", R.drawable.ic_ghost_2, R.raw.sounds_ghost_2, AudioType.GHOST))
        allAudioItems.add(AudioItem("Ghost 3", R.drawable.ic_ghost_3, R.raw.sounds_ghost_3, AudioType.GHOST))
        allAudioItems.add(AudioItem("Ghost 4", R.drawable.ic_ghost_4, R.raw.sounds_ghost_4, AudioType.GHOST))

        // Bomb items
        allAudioItems.add(AudioItem("Bomb 1", R.drawable.ic_bomb_1, R.raw.sounds_bomb_1, AudioType.BOMB))
        allAudioItems.add(AudioItem("Bomb 2", R.drawable.ic_bomb_2, R.raw.sounds_bomb_2, AudioType.BOMB))
        allAudioItems.add(AudioItem("Bomb 3", R.drawable.ic_bomb_3, R.raw.sounds_bomb_3, AudioType.BOMB))
        allAudioItems.add(AudioItem("Bomb 4", R.drawable.ic_bomb_4, R.raw.sounds_bomb_4, AudioType.BOMB))

        // Gun items
        allAudioItems.add(AudioItem("Gun 1", R.drawable.ic_gun_1, R.raw.sounds_gun_1, AudioType.GUN))
        allAudioItems.add(AudioItem("Gun 2", R.drawable.ic_gun_2, R.raw.sounds_gun_2, AudioType.GUN))
        allAudioItems.add(AudioItem("Gun 3", R.drawable.ic_gun_3, R.raw.sounds_gun_3, AudioType.GUN))
        allAudioItems.add(AudioItem("Gun 4", R.drawable.ic_gun_4, R.raw.sounds_gun_4, AudioType.GUN))

        // Air Horn items
        allAudioItems.add(AudioItem("Air Horn 1", R.drawable.ic_air_horn_1, R.raw.sounds_horn_1, AudioType.AIR_HORN))
        allAudioItems.add(AudioItem("Air Horn 2", R.drawable.ic_air_horn_2, R.raw.sounds_horn_2, AudioType.AIR_HORN))
        allAudioItems.add(AudioItem("Air Horn 3", R.drawable.ic_air_horn_3, R.raw.sounds_horn_3, AudioType.AIR_HORN))
        allAudioItems.add(AudioItem("Air Horn 4", R.drawable.ic_air_horn_4, R.raw.sounds_horn_4, AudioType.AIR_HORN))

        // Fart items
        allAudioItems.add(AudioItem("Fart 1", R.drawable.ic_fart_1, R.raw.sounds_fart_1, AudioType.FART))
        allAudioItems.add(AudioItem("Fart 2", R.drawable.ic_fart_2, R.raw.sounds_fart_2, AudioType.FART))
        allAudioItems.add(AudioItem("Fart 3", R.drawable.ic_fart_3, R.raw.sounds_fart_3, AudioType.FART))
        allAudioItems.add(AudioItem("Fart 4", R.drawable.ic_fart_4, R.raw.sounds_fart_4, AudioType.FART))
    }

    private fun setupRecyclerView() {
        audioAdapter = AudioAdapter(getFilteredList(currentType)) { item, position ->
            onAudioItemClick(item, position)
        }
        
        binding.audioRecyclerView.apply {
            layoutManager = GridLayoutManager(this@LibraSingle, 3)
            adapter = audioAdapter
        }
    }

    private fun setupTabs() {
        binding.imgSetting.setOnClickListener {
            startActivity(Intent(this, SagittariusSingle::class.java))
        }
        binding.tabGhost.setOnClickListener {
            switchToType(AudioType.GHOST)
            selectTab(binding.tabGhost)
        }

        binding.tabBomb.setOnClickListener {
            switchToType(AudioType.BOMB)
            selectTab(binding.tabBomb)
        }

        binding.tabGun.setOnClickListener {
            switchToType(AudioType.GUN)
            selectTab(binding.tabGun)
        }

        binding.tabAirHorn.setOnClickListener {
            switchToType(AudioType.AIR_HORN)
            selectTab(binding.tabAirHorn)
        }

        binding.tabFart.setOnClickListener {
            switchToType(AudioType.FART)
            selectTab(binding.tabFart)
        }
    }

    private fun switchToType(type: AudioType) {
        currentType = type
        audioAdapter.updateList(getFilteredList(type))
    }

    private fun getFilteredList(type: AudioType): List<AudioItem> {
        return allAudioItems.filter { it.type == type }
    }

    private fun selectTab(selectedTab: AppCompatTextView) {
        // Reset all tabs
        listOf(
            binding.tabGhost,
            binding.tabBomb,
            binding.tabGun,
            binding.tabAirHorn,
            binding.tabFart
        ).forEach { tab ->
            tab.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected))
            tab.setBackgroundResource(R.drawable.bg_tab_unselected)
        }

        // Highlight selected tab
        selectedTab.apply {
            setTextColor(ContextCompat.getColor(this@LibraSingle, R.color.black))
            setBackgroundResource(R.drawable.bg_tab_selected)
        }
    }

    private fun onAudioItemClick(item: AudioItem, position: Int) {
        // Navigate to ScorpioSingle detail page
        val intent = Intent(this, ScorpioSingle::class.java).apply {
            putExtra(ScorpioSingle.EXTRA_AUDIO_ITEM_NAME, item.name)
            putExtra(ScorpioSingle.EXTRA_AUDIO_ITEM_IMAGE, item.imageResId)
            putExtra(ScorpioSingle.EXTRA_AUDIO_ITEM_AUDIO, item.audioResId)
        }
        startActivity(intent)
    }
}