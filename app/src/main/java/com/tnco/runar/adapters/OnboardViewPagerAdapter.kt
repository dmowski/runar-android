package com.tnco.runar.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager.widget.PagerAdapter
import com.tnco.runar.R
import com.tnco.runar.RunarLogger
import com.tnco.runar.model.OnboardGuideElementModel
import com.tnco.runar.presentation.viewmodel.OnboardViewModel

class OnboardViewPagerAdapter(var models: List<OnboardGuideElementModel>,var context:Context, var textSize: Float, var viewModel: OnboardViewModel): PagerAdapter() {
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
        cardButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*1.4*1.2).toFloat()) //1.2 actually
        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*1.8*1.2).toFloat())
        info.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*0.8*1.2).toFloat())
        cardButton.text = models[position].buttonText
        header.text = models[position].headerText
        info.text = models[position].infoText
        img.setImageResource(models[position].imgId)

        if(position==4) {
            cardButton.background = AppCompatResources.getDrawable(context,R.drawable.onboarding_button_background_selected)
            cardButton.setTextColor(context.getColor(R.color.onboarding_button_text_deselected))
        }

        cardButton.setOnClickListener {
            if(position<models.size-1){
                viewModel.changeCurrentPosition(position+1)
            }
            else{
                viewModel.nextActivity(true)
            }
        }

        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}