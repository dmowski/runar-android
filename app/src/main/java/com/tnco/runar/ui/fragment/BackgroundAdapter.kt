package com.tnco.runar.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.R
import com.tnco.runar.data.remote.BackgroundInfo

class BackgroundAdapter(
    val selectBackground: (Int) -> Unit
) : RecyclerView.Adapter<BackgroundViewHolder>() {

    private val list = mutableListOf<BackgroundInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.backgroung_generato_selected, parent, false)
        val holder = BackgroundViewHolder(itemView)
        itemView.setOnClickListener {
            if (list[holder.adapterPosition].img != null) {
                selectBackground(holder.adapterPosition)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int) {
        val img = holder.itemView.findViewById<ImageView>(R.id.backgroundImage)
        val checkBox = holder.itemView.findViewById<ImageView>(R.id.backgroundCheckBox)
        val frameLayout = holder.itemView.findViewById<FrameLayout>(R.id.background_framelayout)
        val progressBar = holder.itemView.findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = if (list[position].img != null) View.GONE else View.VISIBLE
        img.setImageBitmap(list[position].img)
        checkBox.visibility =
            if (list[position].isSelected && list[position].img != null) View.VISIBLE else View.GONE
        frameLayout.setBackgroundResource(
            if (list[position].isSelected && list[position].img != null) {
                R.drawable.backround_background_selected
            } else {
                R.drawable.backround_background
            }
        )
    }

    fun updateData(list: List<BackgroundInfo>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class BackgroundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
