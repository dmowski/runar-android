package com.tnco.runar.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.tnco.runar.R
import com.tnco.runar.model.OnboardGuideElementModel

class OnboardViewPagerAdapter(var models: List<OnboardGuideElementModel>,var context:Context, var textSize: Float): PagerAdapter() {
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
        val cardButton = view.findViewById<TextView>(R.id.card_button)
        cardButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*1.4).toFloat()) //1.2 actually
        cardButton.text = models[position].buttonText
        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}