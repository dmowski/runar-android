package com.tnco.runar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.databinding.RuneItemBinding
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.util.RunesDiffUtil

class RunesGeneratorAdapter(
    private val onShowBottomSheet: (RunesItemsModel) -> Unit
) : RecyclerView.Adapter<RunesGeneratorAdapter.RunesGeneratorHolder>() {


    private val selectedRunes: MutableList<RunesItemsModel> = mutableListOf()
    var obsSelectedRunes: MutableLiveData<MutableList<RunesItemsModel>> = MutableLiveData()
    private var mListRunes = emptyList<RunesItemsModel>()
    class RunesGeneratorHolder(private val binding: RuneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rune: RunesItemsModel) {
            binding.rune = rune
        }

        companion object {
            fun from(parent: ViewGroup): RunesGeneratorHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RuneItemBinding.inflate(layoutInflater, parent, false)
                return RunesGeneratorHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunesGeneratorHolder {
        return RunesGeneratorHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RunesGeneratorHolder, position: Int) {
        val currentRune = mListRunes[position]
        holder.bind(currentRune)
        holder.itemView.setOnClickListener {
            if (selectedRunes.size < 3 && !selectedRunes.contains(currentRune))
                selectedRunes.add(currentRune)
            obsSelectedRunes.value = selectedRunes
        }

        holder.itemView.setOnLongClickListener {
            onShowBottomSheet(currentRune)
            true
        }
    }

    override fun getItemCount(): Int = mListRunes.size

    fun setData(list: List<RunesItemsModel>) {
        val runesDiffUtil = RunesDiffUtil(mListRunes, list)
        val diffUtilResult = DiffUtil.calculateDiff(runesDiffUtil)
        mListRunes = list
        diffUtilResult.dispatchUpdatesTo(this)
    }
}