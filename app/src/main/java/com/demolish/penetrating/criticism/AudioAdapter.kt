package com.demolish.penetrating.criticism

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demolish.penetrating.criticism.databinding.ItemAudioBinding

class AudioAdapter(
    private var audioList: List<AudioItem>,
    private val onItemClick: (AudioItem, Int) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    private val backgroundColors = listOf(
        R.color.card_yellow,
        R.color.card_blue,
        R.color.card_purple,
        R.color.card_pink_light,
        R.color.card_pink_magenta,
        R.color.card_green
    )

    private val backgroundColorsF = listOf(
        R.color.card_yellow_f,
        R.color.card_blue_f,
        R.color.card_purple_f,
        R.color.card_pink_light_f,
        R.color.card_pink_magenta_f,
        R.color.card_green_f
    )

    inner class AudioViewHolder(private val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AudioItem, position: Int) {
            binding.audioImage.setImageResource(item.imageResId)
            binding.audioName.text = item.name
            
            // Set cyclic background color
            val colorIndex = position % backgroundColors.size
            val color = ContextCompat.getColor(binding.root.context, backgroundColors[colorIndex])
            val colorF = ContextCompat.getColor(binding.root.context, backgroundColorsF[colorIndex])

            binding.cardView.setCardBackgroundColor(color)
            binding.cardViewFrame.setCardBackgroundColor(colorF)

            binding.root.setOnClickListener {
                onItemClick(item, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = ItemAudioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(audioList[position], position)
    }

    override fun getItemCount(): Int = audioList.size

    fun updateList(newList: List<AudioItem>) {
        audioList = newList
        notifyDataSetChanged()
    }
}
