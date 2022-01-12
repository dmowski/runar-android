package com.tnco.runar.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.databinding.RuneItemBinding
import com.tnco.runar.extensions.RunesDiffUtil
import com.tnco.runar.model.RunesItemsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf
import java.util.*
import kotlin.collections.HashMap

class RunesGeneratorAdapter : RecyclerView.Adapter<RunesGeneratorAdapter.RunesGeneratorHolder>() {


    private val selectedRunes: MutableList<RunesItemsModel> = mutableListOf()
    var obsSelectedRunes: MutableLiveData<MutableList<RunesItemsModel>> = MutableLiveData()
    private var mListRunes = emptyList<RunesItemsModel>()

    class RunesGeneratorHolder(private val binding: RuneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(rune: RunesItemsModel) {
            binding.rune = rune
            binding.executePendingBindings()
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

    }

    override fun getItemCount(): Int = mListRunes.size

    fun setData(list: List<RunesItemsModel>) {
        val runesDiffUtil = RunesDiffUtil(mListRunes, list)
        val diffUtilResult = DiffUtil.calculateDiff(runesDiffUtil)
        mListRunes = list
        diffUtilResult.dispatchUpdatesTo(this)
    }
}