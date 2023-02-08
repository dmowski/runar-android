package com.tnco.runar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager.widget.PagerAdapter
import com.tnco.runar.R
import com.tnco.runar.model.OnboardGuideElementModel

class OnboardViewPagerAdapter(
    private val models: List<OnboardGuideElementModel>,
    private val onStartMainActivity: () -> Unit,
    private val onChangePosition: (Int) -> Unit
) : PagerAdapter() {

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val view = layoutInflater.inflate(R.layout.onboard_card, container, false)
        val cardButton = view.findViewById<TextView>(R.id.card_button)
        val header = view.findViewById<TextView>(R.id.header)
        val info = view.findViewById<TextView>(R.id.info)
        val img = view.findViewById<ImageView>(R.id.image)
        cardButton.text = models[position].buttonText
        header.text = models[position].headerText
        info.text = models[position].infoText
        img.setImageResource(models[position].imgId)

        if (position == 5) {
            cardButton.background = AppCompatResources.getDrawable(
                container.context,
                R.drawable.onboarding_button_background_selected
            )
            cardButton.setTextColor(container.context.getColor(R.color.onboarding_button_text_deselected))
        }

        cardButton.setOnClickListener {
            if (position < models.size - 1) {
                onChangePosition(position)
            } else {
                onStartMainActivity()
            }
        }
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
