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
        val header = view.findViewById<TextView>(R.id.header)
        val info = view.findViewById<TextView>(R.id.info)
        val img = view.findViewById<ImageView>(R.id.image)
        cardButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*1.4).toFloat()) //1.2 actually
        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*1.8).toFloat())
        info.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*0.8).toFloat())
        cardButton.text = models[position].buttonText
        header.text = models[position].headerText
        info.text = models[position].infoText
        img.setImageResource(models[position].imgId)
        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}