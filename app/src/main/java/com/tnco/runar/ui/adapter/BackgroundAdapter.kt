package com.tnco.runar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.R
import com.tnco.runar.data.remote.BackgroundInfo
import com.tnco.runar.ui.fragment.GeneratorBackground

class BackgroundAdapter(val list: List<BackgroundInfo>, val fragment: GeneratorBackground) :
    RecyclerView.Adapter<BackgroundViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.backgroung_generato_selected, parent, false)
        val holder = BackgroundViewHolder(itemView)
        itemView.setOnClickListener {
            fragment.selectBackground(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int) {
        val img = holder.itemView.findViewById<ImageView>(R.id.backgroundImage)
        val checkBox = holder.itemView.findViewById<ImageView>(R.id.backgroundCheckBox)
        val frameLayout = holder.itemView.findViewById<FrameLayout>(R.id.background_framelayout)

        img.setImageBitmap(list[position].img)
        checkBox.visibility = if (list[position].isSelected) View.VISIBLE else View.GONE
        frameLayout.setBackgroundResource(
            if (list[position].isSelected) R.drawable.backround_background_selected
            else R.drawable.backround_background
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class BackgroundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}
