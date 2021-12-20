package com.tnco.runar.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.databinding.RuneItemBinding
import com.tnco.runar.model.RunesItemsModel

class RunesGeneratorAdapter: RecyclerView.Adapter<RunesGeneratorAdapter.RunesGeneratorHolder>() {

    private var mListRunes = emptyList<RunesItemsModel>()

    class RunesGeneratorHolder(private val binding:RuneItemBinding): RecyclerView.ViewHolder(binding.root){

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
    }

    override fun getItemCount(): Int = mListRunes.size

    fun setData(list: List<RunesItemsModel>) {
        mListRunes = list
        Log.i("TAG","${list[2].imgUrl} Adapter")
        notifyDataSetChanged()
    }
}