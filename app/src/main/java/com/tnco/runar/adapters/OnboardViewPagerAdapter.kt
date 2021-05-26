package com.tnco.runar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.tnco.runar.R
import com.tnco.runar.model.OnboardGuideElementModel

class OnboardViewPagerAdapter(var models: List<OnboardGuideElementModel>,var context:Context): PagerAdapter() {
    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.onboard_card, container, false)
        var back = view.findViewById<ImageView>(R.id.back)
        back.setBackgroundColor(models.get(position).colorId)
        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}