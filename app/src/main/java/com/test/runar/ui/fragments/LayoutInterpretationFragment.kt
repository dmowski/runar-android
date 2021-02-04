package com.test.runar.ui.fragments

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.CustomClasses.InterTagHandler
import com.test.runar.CustomView.OffsetImageView
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutInterpretationFragment : Fragment(R.layout.fragment_layout_interpretation),
        View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var header: TextView
    private lateinit var headerText: String
    private lateinit var checkBox: CheckBox
    private var runeHeight: Int = 0
    private var runeWidth: Int = 0
    private var fontSize: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fontSize = arguments?.getFloat("descriptionFontSize")!!
        header =
                ((view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(0) as FrameLayout).getChildAt(0) as TextView

        model.layoutInterpretationData.observe(viewLifecycleOwner) {
            if (it!=null&& it.second[8]==it.first.layoutId) {
                runeHeight = it.second[7]
                runeWidth = (runeHeight / 1.23).toInt()
                var userLayout = it.second
                var selectedLayout = it.first
                header.text = selectedLayout.layoutName

                var runeLayout = (view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(1) as ConstraintLayout
                when (selectedLayout.layoutId) {
                    1 -> {
                        val firstRune = context?.let { it1 -> FrameLayout(it1) }
                        if (firstRune != null) {
                            firstRune.id = View.generateViewId()
                            val firstRuneId = userLayout[2]

                            val ims = context?.assets?.open("runes/${firstRuneId}.png")
                            var firstRuneImage = Drawable.createFromStream(ims, null)
                            firstRune.setBackgroundDrawable(firstRuneImage)
                            var firstRuneLayoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            firstRune.layoutParams = firstRuneLayoutParams

                            runeLayout.addView(firstRune)
                            val set = ConstraintSet()
                            set.clone(runeLayout)
                            set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                            set.applyTo(runeLayout)
                        }
                    }
                    2->{
                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())

                        firstRune.id = View.generateViewId()
                        secondRune.id = View.generateViewId()

                        val firstRuneId = userLayout[2]
                        val secondRuneId = userLayout[6]

                        val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                        val img2 = context?.assets?.open("runes/${secondRuneId}.png")

                        firstRune.setBackgroundDrawable(Drawable.createFromStream(img1,null))
                        secondRune.setBackgroundDrawable(Drawable.createFromStream(img2,null))

                        firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                        runeLayout.addView(firstRune)
                        runeLayout.addView(secondRune)

                        val set = ConstraintSet()
                        set.clone(runeLayout)
                        set.connect(firstRune.id, ConstraintSet.END, R.id.center_guideline, ConstraintSet.START, 0)
                        set.connect(firstRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(secondRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(secondRune.id, ConstraintSet.START, R.id.center_guideline, ConstraintSet.END, 0)
                        set.applyTo(runeLayout)
                    }
                    3->{
                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())
                        val thirdRune = FrameLayout(requireContext())

                        firstRune.id = View.generateViewId()
                        secondRune.id = View.generateViewId()
                        thirdRune.id = View.generateViewId()

                        val firstRuneId = userLayout[5]
                        val secondRuneId = userLayout[2]
                        val thirdRuneId = userLayout[6]

                        val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                        val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                        val img3 = context?.assets?.open("runes/${thirdRuneId}.png")

                        firstRune.setBackgroundDrawable(Drawable.createFromStream(img1,null))
                        secondRune.setBackgroundDrawable(Drawable.createFromStream(img2,null))
                        thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3,null))

                        firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                        runeLayout.addView(firstRune)
                        runeLayout.addView(secondRune)
                        runeLayout.addView(thirdRune)

                        val set = ConstraintSet()
                        set.clone(runeLayout)
                        set.connect(secondRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                        set.connect(secondRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                        set.connect(secondRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(firstRune.id,ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                        set.connect(firstRune.id,ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(thirdRune.id,ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                        set.connect(thirdRune.id,ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(firstRune.id,ConstraintSet.END, secondRune.id, ConstraintSet.START, 0)
                        set.connect(thirdRune.id,ConstraintSet.START, secondRune.id, ConstraintSet.END, 0)
                        set.applyTo(runeLayout)
                    }
                    4->{
                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())
                        val thirdRune = FrameLayout(requireContext())
                        val fourthRune = FrameLayout(requireContext())

                        firstRune.id = View.generateViewId()
                        secondRune.id = View.generateViewId()
                        thirdRune.id = View.generateViewId()
                        fourthRune.id = View.generateViewId()

                        val firstRuneId = userLayout[1]
                        val secondRuneId = userLayout[2]
                        val thirdRuneId = userLayout[3]
                        val fourthRuneId = userLayout[6]

                        val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                        val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                        val img3 = context?.assets?.open("runes/${thirdRuneId}.png")
                        val img4 = context?.assets?.open("runes/${fourthRuneId}.png")

                        firstRune.setBackgroundDrawable(Drawable.createFromStream(img1,null))
                        secondRune.setBackgroundDrawable(Drawable.createFromStream(img2,null))
                        thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3,null))
                        fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4,null))

                        firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                        runeLayout.addView(firstRune)
                        runeLayout.addView(secondRune)
                        runeLayout.addView(thirdRune)
                        runeLayout.addView(fourthRune)

                        val set = ConstraintSet()
                        set.clone(runeLayout)
                        set.connect(firstRune.id, ConstraintSet.END, R.id.center_guideline, ConstraintSet.END, 0)
                        set.connect(firstRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(secondRune.id,ConstraintSet.TOP,firstRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(secondRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(secondRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(thirdRune.id,ConstraintSet.TOP,secondRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(thirdRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(thirdRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(fourthRune.id, ConstraintSet.START, secondRune.id, ConstraintSet.END, 0)
                        set.connect(fourthRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(fourthRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                        set.applyTo(runeLayout)
                    }
                    5->{
                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())
                        val thirdRune = FrameLayout(requireContext())
                        val fourthRune = FrameLayout(requireContext())

                        firstRune.id = View.generateViewId()
                        secondRune.id = View.generateViewId()
                        thirdRune.id = View.generateViewId()
                        fourthRune.id = View.generateViewId()

                        val firstRuneId = userLayout[1]
                        val secondRuneId = userLayout[5]
                        val thirdRuneId = userLayout[6]
                        val fourthRuneId = userLayout[2]

                        val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                        val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                        val img3 = context?.assets?.open("runes/${thirdRuneId}.png")
                        val img4 = context?.assets?.open("runes/${fourthRuneId}.png")

                        firstRune.setBackgroundDrawable(Drawable.createFromStream(img1,null))
                        secondRune.setBackgroundDrawable(Drawable.createFromStream(img2,null))
                        thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3,null))
                        fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4,null))

                        firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                        runeLayout.addView(firstRune)
                        runeLayout.addView(secondRune)
                        runeLayout.addView(thirdRune)
                        runeLayout.addView(fourthRune)

                        val set = ConstraintSet()
                        set.clone(runeLayout)
                        set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                        set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                        set.connect(firstRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(fourthRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                        set.connect(fourthRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                        set.connect(fourthRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(secondRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.START, 0)
                        set.connect(secondRune.id, ConstraintSet.BOTTOM, fourthRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(secondRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.TOP, 0)
                        set.connect(thirdRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.END, 0)
                        set.connect(thirdRune.id, ConstraintSet.BOTTOM, fourthRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(thirdRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.TOP, 0)
                        set.applyTo(runeLayout)
                    }
                    6->{
                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())
                        val thirdRune = FrameLayout(requireContext())
                        val fourthRune = FrameLayout(requireContext())
                        val fifthRune = FrameLayout(requireContext())

                        firstRune.id = View.generateViewId()
                        secondRune.id = View.generateViewId()
                        thirdRune.id = View.generateViewId()
                        fourthRune.id = View.generateViewId()
                        fifthRune.id = View.generateViewId()

                        val firstRuneId = userLayout[1]
                        val secondRuneId = userLayout[2]
                        val thirdRuneId = userLayout[3]
                        val fourthRuneId = userLayout[5]
                        val fifthRuneId = userLayout[6]

                        val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                        val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                        val img3 = context?.assets?.open("runes/${thirdRuneId}.png")
                        val img4 = context?.assets?.open("runes/${fourthRuneId}.png")
                        val img5 = context?.assets?.open("runes/${fifthRuneId}.png")

                        firstRune.setBackgroundDrawable(Drawable.createFromStream(img1,null))
                        secondRune.setBackgroundDrawable(Drawable.createFromStream(img2,null))
                        thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3,null))
                        fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4,null))
                        fifthRune.setBackgroundDrawable(Drawable.createFromStream(img5,null))

                        firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fifthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                        runeLayout.addView(firstRune)
                        runeLayout.addView(secondRune)
                        runeLayout.addView(thirdRune)
                        runeLayout.addView(fourthRune)
                        runeLayout.addView(fifthRune)

                        val set = ConstraintSet()
                        set.clone(runeLayout)
                        set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                        set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                        set.connect(firstRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(secondRune.id,ConstraintSet.TOP,firstRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(secondRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(secondRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(thirdRune.id,ConstraintSet.TOP,secondRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(thirdRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(thirdRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(fourthRune.id, ConstraintSet.END, secondRune.id, ConstraintSet.START, 0)
                        set.connect(fourthRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(fourthRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                        set.connect(fifthRune.id, ConstraintSet.START, secondRune.id, ConstraintSet.END, 0)
                        set.connect(fifthRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(fifthRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                        set.applyTo(runeLayout)
                    }
                    7->{
                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())
                        val thirdRune = FrameLayout(requireContext())
                        val fourthRune = FrameLayout(requireContext())
                        val fifthRune = FrameLayout(requireContext())
                        val sixthRune = FrameLayout(requireContext())

                        firstRune.id = View.generateViewId()
                        secondRune.id = View.generateViewId()
                        thirdRune.id = View.generateViewId()
                        fourthRune.id = View.generateViewId()
                        fifthRune.id = View.generateViewId()
                        sixthRune.id = View.generateViewId()

                        val firstRuneId = userLayout[0]
                        val secondRuneId = userLayout[1]
                        val thirdRuneId = userLayout[2]
                        val fourthRuneId = userLayout[3]
                        val fifthRuneId = userLayout[5]
                        val sixthRuneId = userLayout[6]

                        val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                        val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                        val img3 = context?.assets?.open("runes/${thirdRuneId}.png")
                        val img4 = context?.assets?.open("runes/${fourthRuneId}.png")
                        val img5 = context?.assets?.open("runes/${fifthRuneId}.png")
                        val img6 = context?.assets?.open("runes/${sixthRuneId}.png")

                        firstRune.setBackgroundDrawable(Drawable.createFromStream(img1,null))
                        secondRune.setBackgroundDrawable(Drawable.createFromStream(img2,null))
                        thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3,null))
                        fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4,null))
                        fifthRune.setBackgroundDrawable(Drawable.createFromStream(img5,null))
                        sixthRune.setBackgroundDrawable(Drawable.createFromStream(img6,null))

                        firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fifthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        sixthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                        runeLayout.addView(firstRune)
                        runeLayout.addView(secondRune)
                        runeLayout.addView(thirdRune)
                        runeLayout.addView(fourthRune)
                        runeLayout.addView(fifthRune)
                        runeLayout.addView(sixthRune)

                        val set = ConstraintSet()
                        set.clone(runeLayout)
                        set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                        set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                        set.connect(firstRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(secondRune.id,ConstraintSet.TOP,firstRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(secondRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(secondRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(thirdRune.id,ConstraintSet.TOP,secondRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(thirdRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(thirdRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(fourthRune.id,ConstraintSet.TOP,thirdRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(fourthRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(fourthRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(fifthRune.id, ConstraintSet.END, thirdRune.id, ConstraintSet.START, 0)
                        set.connect(fifthRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(fifthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                        set.connect(sixthRune.id, ConstraintSet.START, thirdRune.id, ConstraintSet.END, 0)
                        set.connect(sixthRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(sixthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                        set.applyTo(runeLayout)
                    }
                    8->{
                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())
                        val thirdRune = FrameLayout(requireContext())
                        val fourthRune = FrameLayout(requireContext())
                        val fifthRune = FrameLayout(requireContext())
                        val sixthRune = FrameLayout(requireContext())
                        val seventhRune = FrameLayout(requireContext())

                        firstRune.id = View.generateViewId()
                        secondRune.id = View.generateViewId()
                        thirdRune.id = View.generateViewId()
                        fourthRune.id = View.generateViewId()
                        fifthRune.id = View.generateViewId()
                        sixthRune.id = View.generateViewId()
                        seventhRune.id = View.generateViewId()

                        val firstRuneId = userLayout[0]
                        val secondRuneId = userLayout[1]
                        val thirdRuneId = userLayout[2]
                        val fourthRuneId = userLayout[3]
                        val fifthRuneId = userLayout[4]
                        val sixthRuneId = userLayout[5]
                        val seventhRuneId = userLayout[6]

                        val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                        val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                        val img3 = context?.assets?.open("runes/${thirdRuneId}.png")
                        val img4 = context?.assets?.open("runes/${fourthRuneId}.png")
                        val img5 = context?.assets?.open("runes/${fifthRuneId}.png")
                        val img6 = context?.assets?.open("runes/${sixthRuneId}.png")
                        val img7 = context?.assets?.open("runes/${seventhRuneId}.png")

                        firstRune.setBackgroundDrawable(Drawable.createFromStream(img1,null))
                        secondRune.setBackgroundDrawable(Drawable.createFromStream(img2,null))
                        thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3,null))
                        fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4,null))
                        fifthRune.setBackgroundDrawable(Drawable.createFromStream(img5,null))
                        sixthRune.setBackgroundDrawable(Drawable.createFromStream(img6,null))
                        seventhRune.setBackgroundDrawable(Drawable.createFromStream(img7,null))

                        firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        fifthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        sixthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                        seventhRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                        runeLayout.addView(firstRune)
                        runeLayout.addView(secondRune)
                        runeLayout.addView(thirdRune)
                        runeLayout.addView(fourthRune)
                        runeLayout.addView(fifthRune)
                        runeLayout.addView(sixthRune)
                        runeLayout.addView(seventhRune)

                        val set = ConstraintSet()
                        set.clone(runeLayout)
                        set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                        set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                        set.connect(firstRune.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
                        set.connect(secondRune.id,ConstraintSet.TOP,firstRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(secondRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(secondRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(thirdRune.id,ConstraintSet.TOP,secondRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(thirdRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(thirdRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(fourthRune.id,ConstraintSet.TOP,thirdRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(fourthRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(fourthRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(fifthRune.id,ConstraintSet.TOP,fourthRune.id,ConstraintSet.BOTTOM,0)
                        set.connect(fifthRune.id,ConstraintSet.START,firstRune.id,ConstraintSet.START,0)
                        set.connect(fifthRune.id,ConstraintSet.END,firstRune.id,ConstraintSet.END,0)
                        set.connect(sixthRune.id, ConstraintSet.END, thirdRune.id, ConstraintSet.START, 0)
                        set.connect(sixthRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(sixthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                        set.connect(seventhRune.id, ConstraintSet.START, thirdRune.id, ConstraintSet.END, 0)
                        set.connect(seventhRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                        set.connect(seventhRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                        set.applyTo(runeLayout)
                    }
                }
                //**runes description
                //runes click listeners
                for(i in 0..runeLayout.childCount-1){
                    runeLayout.getChildAt(i).setOnClickListener(this)
                }
                //runes description**
                model.getAuspForCurrentLayout()
                model.currentAusp.observe(viewLifecycleOwner){
                    var interpretationLayout = ((view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(2) as ConstraintLayout).getChildAt(1) as ConstraintLayout
                    var testText = interpretationLayout.getChildAt(0) as TextView
                    interpretationLayout.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener(this)
                    if(it!=null){
                        testText.text = "Благоприятность - $it %"
                        if(it<=50){
                            model.getAffimForCurrentLayout(it)
                        }
                    }
                }
                model.currentAffirm.observe(viewLifecycleOwner){
                    if(it!=""||it!=null){
                        var interpretationLayout = ((view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(2) as ConstraintLayout).getChildAt(1) as ConstraintLayout
                        val affimTextView = interpretationLayout.findViewById<TextView>(R.id.text_affim)
                        affimTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                        affimTextView.text=it
                        model.getInterpretation(requireContext())
                    }
                }
                model.currentInterpretation.observe(viewLifecycleOwner){
                    if(it!=null){
                        if(it.isNotEmpty()){

                            var interpretationLayout = ((view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(2) as ConstraintLayout).getChildAt(1) as ConstraintLayout
                            var interpretationTextView =interpretationLayout.findViewById<TextView>(R.id.interpretation_text)
                            interpretationTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

                            val secondFont = ResourcesCompat.getFont(requireContext(),R.font.roboto_medium)
                            val interpretationText =it
                            interpretationTextView.text = Html.fromHtml(interpretationText,null,InterTagHandler(secondFont!!))


                            val mainLayout = view.findViewById<ConstraintLayout>(R.id.main_layout)
                            val backgroundLayout = (view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(2) as ConstraintLayout
                            val backLayout = backgroundLayout.getChildAt(1) as ConstraintLayout
                            val interLayout = backgroundLayout.findViewById<ConstraintLayout>(R.id.interpretation_layout)
                            val button = interLayout.findViewById<FrameLayout>(R.id.description_button_frame)
                            checkBox = interLayout.findViewById<CheckBox>(R.id.checkbox)
                            val bottomSupportFrame = interLayout.findViewById<FrameLayout>(R.id.bottom_support_frame)
                            val observer = mainLayout.viewTreeObserver
                            observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    observer.removeOnGlobalLayoutListener(this)
                                    val screenHeight = mainLayout.height
                                    val minSize = screenHeight - backgroundLayout.top
                                    var flag=false
                                    if (minSize > backgroundLayout.height) {
                                        val backLayoutParams = backLayout.layoutParams
                                        backLayoutParams.height = minSize
                                        backLayout.layoutParams = backLayoutParams
                                        flag=true
                                    }
                                    if(bottomSupportFrame.bottom<screenHeight&&flag){
                                        val constraintsSet = ConstraintSet()
                                        constraintsSet.clone(backLayout)
                                        constraintsSet.clear(R.id.bottom_support_frame,ConstraintSet.TOP)
                                        constraintsSet.connect(R.id.bottom_support_frame,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM)
                                        constraintsSet.clear(R.id.description_button_frame,ConstraintSet.TOP)
                                        constraintsSet.connect(R.id.description_button_frame,ConstraintSet.BOTTOM,R.id.bottom_support_frame,ConstraintSet.TOP)
                                        constraintsSet.clear(R.id.checkbox,ConstraintSet.TOP)
                                        constraintsSet.connect(R.id.checkbox,ConstraintSet.BOTTOM,R.id.description_button_frame,ConstraintSet.TOP)
                                        constraintsSet.applyTo(backLayout)
                                    }
                                    val supportView = view.findViewById<OffsetImageView>(R.id.load_helper)
                                    supportView.visibility = View.GONE
                                }
                            })
                        }
                    }
                }
                //logic here
            }
        }

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(v: View?) {
        val navController = findNavController()
        if (checkBox.isChecked) model.saveUserLayout(requireContext())
        navController.navigate(R.id.action_layoutInterpretationFragment_to_layoutFragment)
    }
}