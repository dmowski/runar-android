package com.test.runar.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.View
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
import com.test.runar.CustomClasses.OnSwipeTouchListener
import com.test.runar.CustomView.OffsetImageView
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutInterpretationFragment : Fragment(R.layout.fragment_layout_interpretation),
        View.OnClickListener {
    private lateinit var model: MainViewModel


    private lateinit var header: TextView
    private lateinit var runePosition: TextView
    private lateinit var runeAusf: TextView
    private lateinit var bottomRunesNav: ConstraintLayout
    private lateinit var interpretationFrame: ConstraintLayout
    private lateinit var mainConstraintLayout: ConstraintLayout
    private lateinit var interpretationLayout: ConstraintLayout
    private lateinit var runeDescriptionSV: ScrollView

    private lateinit var buttonFrame: FrameLayout
    private lateinit var checkBox: CheckBox

    private lateinit var headerFrame: FrameLayout
    private lateinit var headerBackgroundFrame: FrameLayout
    private lateinit var runesLayout: ConstraintLayout

    private var runesViewList: ArrayList<FrameLayout> = arrayListOf()
    private var runesPositionsList: ArrayList<String?> = arrayListOf()
    private var runesDotsList: ArrayList<ImageView> = arrayListOf()
    private var runeHeight: Int = 0
    private var runeWidth: Int = 0
    private var fontSize: Float = 0f
    private var layoutId: Int =0

    private var defaultConstraintSet = ConstraintSet()

    private var firsOpening = true
    private var baseSize = 0

    private var screenHeight: Int = 0

    private var lastUserLayoutId =0

    private var ausfText =""

    private var currentRunePosition =0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        Log.d("DebugData","Last fragment recreated")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fontSize = arguments?.getFloat("descriptionFontSize")!!
        header = view.findViewById(R.id.header)
        interpretationFrame = view.findViewById(R.id.inter_frame)
        headerFrame = view.findViewById(R.id.description_header_frame)
        headerBackgroundFrame = view.findViewById(R.id.description_header_background)
        runesLayout = view.findViewById(R.id.runes_layout)
        mainConstraintLayout = view.findViewById(R.id.main_layout)
        buttonFrame = view.findViewById(R.id.description_button_frame)
        checkBox = view.findViewById(R.id.checkbox)
        interpretationLayout = view.findViewById(R.id.interpretation_layout)
        runeDescriptionSV = view.findViewById(R.id.rune_description_scroll)
        bottomRunesNav = view.findViewById(R.id.bottom_runes_nav_bar)

        model.lastUserLayoutId.observe(viewLifecycleOwner){
            if(it!=null){
                lastUserLayoutId = it
            }
        }

        model.layoutInterpretationData.observe(viewLifecycleOwner) {
            var currentId = 0
            if(it.second!=null){
                for(i in it.second) currentId+=i
                if (it != null && it.second[8] == it.first.layoutId&&lastUserLayoutId!=currentId) {
                    model.setLastUserLayout(currentId)
                    runeHeight = it.second[7]
                    runeWidth = (runeHeight / 1.23).toInt()
                    var userLayout = it.second
                    var selectedLayout = it.first
                    layoutId = selectedLayout.layoutId!!
                    header.text = selectedLayout.layoutName
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

                                this.runesLayout.addView(firstRune)
                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                                set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                                set.connect(firstRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                                set.applyTo(runesLayout)
                            }
                        }
                        2 -> {
                            val firstRune = FrameLayout(requireContext())
                            val secondRune = FrameLayout(requireContext())
                            runesViewList.addAll(arrayListOf(firstRune, secondRune))
                            runesPositionsList.addAll(arrayListOf(selectedLayout.slotMeaning3, selectedLayout.slotMeaning7))

                            viewIdGenerator(runesViewList)

                            val firstRuneId = userLayout[2]
                            val secondRuneId = userLayout[6]

                            val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                            val img2 = context?.assets?.open("runes/${secondRuneId}.png")

                            firstRune.setBackgroundDrawable(Drawable.createFromStream(img1, null))
                            secondRune.setBackgroundDrawable(Drawable.createFromStream(img2, null))

                            firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                            runesLayout.addView(firstRune)
                            runesLayout.addView(secondRune)

                            val set = ConstraintSet()
                            set.clone(runesLayout)
                            set.connect(firstRune.id, ConstraintSet.END, R.id.center_guideline, ConstraintSet.START, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.START, R.id.center_guideline, ConstraintSet.END, 0)
                            set.applyTo(runesLayout)

                            val firstDot = ImageView(requireContext())
                            val secondDot = ImageView(requireContext())
                            runesDotsList.addAll(arrayListOf(firstDot,secondDot))

                            for(runeDot in runesDotsList){
                                runeDot.id = View.generateViewId()
                                runeDot.adjustViewBounds = true
                                runeDot.setImageResource(R.drawable.ic_circle_deselected)
                                bottomRunesNav.addView(runeDot)
                            }

                            val bottomNavSet = ConstraintSet()
                            bottomNavSet.clone(bottomRunesNav)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.TOP,R.id.left_arrow,ConstraintSet.TOP,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.BOTTOM,R.id.left_arrow,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.END,R.id.bottom_runes_nav_center,ConstraintSet.END,15)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.TOP,firstDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.BOTTOM,firstDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.START,R.id.bottom_runes_nav_center,ConstraintSet.START,15)
                            bottomNavSet.applyTo(bottomRunesNav)
                        }
                        3 -> {
                            val firstRune = FrameLayout(requireContext())
                            val secondRune = FrameLayout(requireContext())
                            val thirdRune = FrameLayout(requireContext())
                            runesViewList.addAll(arrayListOf(thirdRune, secondRune, firstRune))
                            runesPositionsList.addAll(arrayListOf(selectedLayout.slotMeaning7, selectedLayout.slotMeaning3, selectedLayout.slotMeaning6))

                            viewIdGenerator(runesViewList)

                            val firstRuneId = userLayout[5]
                            val secondRuneId = userLayout[2]
                            val thirdRuneId = userLayout[6]

                            val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                            val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                            val img3 = context?.assets?.open("runes/${thirdRuneId}.png")

                            firstRune.setBackgroundDrawable(Drawable.createFromStream(img1, null))
                            secondRune.setBackgroundDrawable(Drawable.createFromStream(img2, null))
                            thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3, null))

                            firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                            runesLayout.addView(firstRune)
                            runesLayout.addView(secondRune)
                            runesLayout.addView(thirdRune)

                            val set = ConstraintSet()
                            set.clone(runesLayout)
                            set.connect(secondRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(secondRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(secondRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                            set.connect(firstRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(thirdRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                            set.connect(thirdRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(firstRune.id, ConstraintSet.END, secondRune.id, ConstraintSet.START, 0)
                            set.connect(thirdRune.id, ConstraintSet.START, secondRune.id, ConstraintSet.END, 0)
                            set.applyTo(runesLayout)

                            val firstDot = ImageView(requireContext())
                            val secondDot = ImageView(requireContext())
                            val thirdDot = ImageView(requireContext())

                            runesDotsList.addAll(arrayListOf(firstDot,secondDot,thirdDot))

                            for(runeDot in runesDotsList){
                                runeDot.id = View.generateViewId()
                                runeDot.adjustViewBounds = true
                                runeDot.setImageResource(R.drawable.ic_circle_deselected)
                                bottomRunesNav.addView(runeDot)
                            }

                            val bottomNavSet = ConstraintSet()
                            bottomNavSet.clone(bottomRunesNav)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.END,secondDot.id,ConstraintSet.START,15)

                            bottomNavSet.connect(thirdDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.START,secondDot.id,ConstraintSet.END,15)

                            bottomNavSet.connect(secondDot.id,ConstraintSet.TOP,R.id.left_arrow,ConstraintSet.TOP,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.BOTTOM,R.id.left_arrow,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,15)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,15)
                            bottomNavSet.applyTo(bottomRunesNav)
                        }
                        4 -> {
                            val firstRune = FrameLayout(requireContext())
                            val secondRune = FrameLayout(requireContext())
                            val thirdRune = FrameLayout(requireContext())
                            val fourthRune = FrameLayout(requireContext())
                            runesViewList.addAll(arrayListOf(fourthRune,secondRune,thirdRune,firstRune))
                            runesPositionsList.addAll(arrayListOf(selectedLayout.slotMeaning7, selectedLayout.slotMeaning3, selectedLayout.slotMeaning4, selectedLayout.slotMeaning2))

                            viewIdGenerator(runesViewList)

                            val firstRuneId = userLayout[1]
                            val secondRuneId = userLayout[2]
                            val thirdRuneId = userLayout[3]
                            val fourthRuneId = userLayout[6]

                            val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                            val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                            val img3 = context?.assets?.open("runes/${thirdRuneId}.png")
                            val img4 = context?.assets?.open("runes/${fourthRuneId}.png")

                            firstRune.setBackgroundDrawable(Drawable.createFromStream(img1, null))
                            secondRune.setBackgroundDrawable(Drawable.createFromStream(img2, null))
                            thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3, null))
                            fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4, null))

                            firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                            runesLayout.addView(firstRune)
                            runesLayout.addView(secondRune)
                            runesLayout.addView(thirdRune)
                            runesLayout.addView(fourthRune)

                            val set = ConstraintSet()
                            set.clone(runesLayout)
                            set.connect(firstRune.id, ConstraintSet.END, R.id.center_guideline, ConstraintSet.END, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(secondRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(thirdRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(thirdRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(thirdRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(fourthRune.id, ConstraintSet.START, secondRune.id, ConstraintSet.END, 0)
                            set.connect(fourthRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(fourthRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                            set.applyTo(runesLayout)

                            val firstDot = ImageView(requireContext())
                            val secondDot = ImageView(requireContext())
                            val thirdDot = ImageView(requireContext())
                            val fourthDot = ImageView(requireContext())

                            runesDotsList.addAll(arrayListOf(firstDot,secondDot,thirdDot,fourthDot))

                            for(runeDot in runesDotsList){
                                runeDot.id = View.generateViewId()
                                runeDot.adjustViewBounds = true
                                runeDot.setImageResource(R.drawable.ic_circle_deselected)
                                bottomRunesNav.addView(runeDot)
                            }

                            val bottomNavSet = ConstraintSet()
                            bottomNavSet.clone(bottomRunesNav)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.END,secondDot.id,ConstraintSet.START,30)

                            bottomNavSet.connect(secondDot.id,ConstraintSet.TOP,R.id.left_arrow,ConstraintSet.TOP,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.BOTTOM,R.id.left_arrow,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.END,R.id.bottom_runes_nav_center,ConstraintSet.START,15)

                            bottomNavSet.connect(thirdDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.START,R.id.bottom_runes_nav_center,ConstraintSet.END,15)

                            bottomNavSet.connect(fourthDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.START,thirdDot.id,ConstraintSet.END,30)


                            bottomNavSet.applyTo(bottomRunesNav)
                        }
                        5 -> {
                            val firstRune = FrameLayout(requireContext())
                            val secondRune = FrameLayout(requireContext())
                            val thirdRune = FrameLayout(requireContext())
                            val fourthRune = FrameLayout(requireContext())
                            runesViewList.addAll(arrayListOf(fourthRune, secondRune, thirdRune, firstRune))
                            runesPositionsList.addAll(arrayListOf(selectedLayout.slotMeaning3, selectedLayout.slotMeaning6, selectedLayout.slotMeaning7, selectedLayout.slotMeaning1))

                            viewIdGenerator(runesViewList)

                            val firstRuneId = userLayout[1]
                            val secondRuneId = userLayout[5]
                            val thirdRuneId = userLayout[6]
                            val fourthRuneId = userLayout[2]

                            val img1 = context?.assets?.open("runes/${firstRuneId}.png")
                            val img2 = context?.assets?.open("runes/${secondRuneId}.png")
                            val img3 = context?.assets?.open("runes/${thirdRuneId}.png")
                            val img4 = context?.assets?.open("runes/${fourthRuneId}.png")

                            firstRune.setBackgroundDrawable(Drawable.createFromStream(img1, null))
                            secondRune.setBackgroundDrawable(Drawable.createFromStream(img2, null))
                            thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3, null))
                            fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4, null))

                            firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                            runesLayout.addView(firstRune)
                            runesLayout.addView(secondRune)
                            runesLayout.addView(thirdRune)
                            runesLayout.addView(fourthRune)

                            val set = ConstraintSet()
                            set.clone(runesLayout)
                            set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(fourthRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(fourthRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(fourthRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.START, 0)
                            set.connect(secondRune.id, ConstraintSet.BOTTOM, fourthRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.TOP, 0)
                            set.connect(thirdRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.END, 0)
                            set.connect(thirdRune.id, ConstraintSet.BOTTOM, fourthRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(thirdRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.TOP, 0)
                            set.applyTo(runesLayout)

                            val firstDot = ImageView(requireContext())
                            val secondDot = ImageView(requireContext())
                            val thirdDot = ImageView(requireContext())
                            val fourthDot = ImageView(requireContext())

                            runesDotsList.addAll(arrayListOf(firstDot,secondDot,thirdDot,fourthDot))

                            for(runeDot in runesDotsList){
                                runeDot.id = View.generateViewId()
                                runeDot.adjustViewBounds = true
                                runeDot.setImageResource(R.drawable.ic_circle_deselected)
                                bottomRunesNav.addView(runeDot)
                            }

                            val bottomNavSet = ConstraintSet()
                            bottomNavSet.clone(bottomRunesNav)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.END,secondDot.id,ConstraintSet.START,30)

                            bottomNavSet.connect(secondDot.id,ConstraintSet.TOP,R.id.left_arrow,ConstraintSet.TOP,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.BOTTOM,R.id.left_arrow,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.END,R.id.bottom_runes_nav_center,ConstraintSet.START,15)

                            bottomNavSet.connect(thirdDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.START,R.id.bottom_runes_nav_center,ConstraintSet.END,15)

                            bottomNavSet.connect(fourthDot.id,ConstraintSet.TOP,secondDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.BOTTOM,secondDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.START,thirdDot.id,ConstraintSet.END,30)


                            bottomNavSet.applyTo(bottomRunesNav)
                        }
                        6 -> {
                            val firstRune = FrameLayout(requireContext())
                            val secondRune = FrameLayout(requireContext())
                            val thirdRune = FrameLayout(requireContext())
                            val fourthRune = FrameLayout(requireContext())
                            val fifthRune = FrameLayout(requireContext())
                            runesViewList.addAll(arrayListOf(fourthRune,secondRune,fifthRune,firstRune,thirdRune))
                            runesPositionsList.addAll(arrayListOf(selectedLayout.slotMeaning6, selectedLayout.slotMeaning3, selectedLayout.slotMeaning7, selectedLayout.slotMeaning2, selectedLayout.slotMeaning4))

                            viewIdGenerator(runesViewList)

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

                            firstRune.setBackgroundDrawable(Drawable.createFromStream(img1, null))
                            secondRune.setBackgroundDrawable(Drawable.createFromStream(img2, null))
                            thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3, null))
                            fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4, null))
                            fifthRune.setBackgroundDrawable(Drawable.createFromStream(img5, null))

                            firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fifthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                            runesLayout.addView(firstRune)
                            runesLayout.addView(secondRune)
                            runesLayout.addView(thirdRune)
                            runesLayout.addView(fourthRune)
                            runesLayout.addView(fifthRune)

                            val set = ConstraintSet()
                            set.clone(runesLayout)
                            set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(secondRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(thirdRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(thirdRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(thirdRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(fourthRune.id, ConstraintSet.END, secondRune.id, ConstraintSet.START, 0)
                            set.connect(fourthRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(fourthRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                            set.connect(fifthRune.id, ConstraintSet.START, secondRune.id, ConstraintSet.END, 0)
                            set.connect(fifthRune.id, ConstraintSet.BOTTOM, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(fifthRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.TOP, 0)
                            set.applyTo(runesLayout)

                            val firstDot = ImageView(requireContext())
                            val secondDot = ImageView(requireContext())
                            val thirdDot = ImageView(requireContext())
                            val fourthDot = ImageView(requireContext())
                            val fifthDot = ImageView(requireContext())

                            runesDotsList.addAll(arrayListOf(firstDot,secondDot,thirdDot,fourthDot,fifthDot))

                            for(runeDot in runesDotsList){
                                runeDot.id = View.generateViewId()
                                runeDot.adjustViewBounds = true
                                runeDot.setImageResource(R.drawable.ic_circle_deselected)
                                bottomRunesNav.addView(runeDot)
                            }

                            val bottomNavSet = ConstraintSet()
                            bottomNavSet.clone(bottomRunesNav)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.END,secondDot.id,ConstraintSet.START,15)

                            bottomNavSet.connect(secondDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.END,thirdDot.id,ConstraintSet.START,15)

                            bottomNavSet.connect(thirdDot.id,ConstraintSet.TOP,R.id.left_arrow,ConstraintSet.TOP,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.BOTTOM,R.id.left_arrow,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,15)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,15)

                            bottomNavSet.connect(fourthDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.START,thirdDot.id,ConstraintSet.END,15)

                            bottomNavSet.connect(fifthDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fifthDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fifthDot.id,ConstraintSet.START,fourthDot.id,ConstraintSet.END,15)


                            bottomNavSet.applyTo(bottomRunesNav)
                        }
                        7 -> {
                            val firstRune = FrameLayout(requireContext())
                            val secondRune = FrameLayout(requireContext())
                            val thirdRune = FrameLayout(requireContext())
                            val fourthRune = FrameLayout(requireContext())
                            val fifthRune = FrameLayout(requireContext())
                            val sixthRune = FrameLayout(requireContext())
                            runesViewList.addAll(arrayListOf(sixthRune,thirdRune,fifthRune,fourthRune,secondRune,firstRune))
                            runesPositionsList.addAll(arrayListOf(selectedLayout.slotMeaning7, selectedLayout.slotMeaning3, selectedLayout.slotMeaning6, selectedLayout.slotMeaning4, selectedLayout.slotMeaning2, selectedLayout.slotMeaning1))

                            viewIdGenerator(runesViewList)

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

                            firstRune.setBackgroundDrawable(Drawable.createFromStream(img1, null))
                            secondRune.setBackgroundDrawable(Drawable.createFromStream(img2, null))
                            thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3, null))
                            fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4, null))
                            fifthRune.setBackgroundDrawable(Drawable.createFromStream(img5, null))
                            sixthRune.setBackgroundDrawable(Drawable.createFromStream(img6, null))

                            firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fifthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            sixthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                            runesLayout.addView(firstRune)
                            runesLayout.addView(secondRune)
                            runesLayout.addView(thirdRune)
                            runesLayout.addView(fourthRune)
                            runesLayout.addView(fifthRune)
                            runesLayout.addView(sixthRune)

                            val set = ConstraintSet()
                            set.clone(runesLayout)
                            set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(secondRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(thirdRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(thirdRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(thirdRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(fourthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(fourthRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(fourthRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(fifthRune.id, ConstraintSet.END, thirdRune.id, ConstraintSet.START, 0)
                            set.connect(fifthRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(fifthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                            set.connect(sixthRune.id, ConstraintSet.START, thirdRune.id, ConstraintSet.END, 0)
                            set.connect(sixthRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(sixthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                            set.applyTo(runesLayout)

                            val firstDot = ImageView(requireContext())
                            val secondDot = ImageView(requireContext())
                            val thirdDot = ImageView(requireContext())
                            val fourthDot = ImageView(requireContext())
                            val fifthDot = ImageView(requireContext())
                            val sixthDot = ImageView(requireContext())

                            runesDotsList.addAll(arrayListOf(firstDot,secondDot,thirdDot,fourthDot,fifthDot,sixthDot))

                            for(runeDot in runesDotsList){
                                runeDot.id = View.generateViewId()
                                runeDot.adjustViewBounds = true
                                runeDot.setImageResource(R.drawable.ic_circle_deselected)
                                bottomRunesNav.addView(runeDot)
                            }

                            val bottomNavSet = ConstraintSet()
                            bottomNavSet.clone(bottomRunesNav)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.END,secondDot.id,ConstraintSet.START,30)

                            bottomNavSet.connect(secondDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.END,thirdDot.id,ConstraintSet.START,30)

                            bottomNavSet.connect(thirdDot.id,ConstraintSet.TOP,R.id.left_arrow,ConstraintSet.TOP,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.BOTTOM,R.id.left_arrow,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.END,R.id.bottom_runes_nav_center,ConstraintSet.START,15)

                            bottomNavSet.connect(fourthDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.START,R.id.bottom_runes_nav_center,ConstraintSet.END,15)

                            bottomNavSet.connect(fifthDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fifthDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fifthDot.id,ConstraintSet.START,fourthDot.id,ConstraintSet.END,30)

                            bottomNavSet.connect(sixthDot.id,ConstraintSet.TOP,thirdDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(sixthDot.id,ConstraintSet.BOTTOM,thirdDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(sixthDot.id,ConstraintSet.START,fifthDot.id,ConstraintSet.END,30)


                            bottomNavSet.applyTo(bottomRunesNav)
                        }
                        8 -> {
                            val firstRune = FrameLayout(requireContext())
                            val secondRune = FrameLayout(requireContext())
                            val thirdRune = FrameLayout(requireContext())
                            val fourthRune = FrameLayout(requireContext())
                            val fifthRune = FrameLayout(requireContext())
                            val sixthRune = FrameLayout(requireContext())
                            val seventhRune = FrameLayout(requireContext())
                            runesViewList.addAll(arrayListOf(thirdRune,seventhRune,sixthRune,fifthRune,fourthRune,secondRune,firstRune))
                            runesPositionsList.addAll(arrayListOf(selectedLayout.slotMeaning3, selectedLayout.slotMeaning7, selectedLayout.slotMeaning6, selectedLayout.slotMeaning5, selectedLayout.slotMeaning4, selectedLayout.slotMeaning2, selectedLayout.slotMeaning1))

                            viewIdGenerator(runesViewList)

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

                            firstRune.setBackgroundDrawable(Drawable.createFromStream(img1, null))
                            secondRune.setBackgroundDrawable(Drawable.createFromStream(img2, null))
                            thirdRune.setBackgroundDrawable(Drawable.createFromStream(img3, null))
                            fourthRune.setBackgroundDrawable(Drawable.createFromStream(img4, null))
                            fifthRune.setBackgroundDrawable(Drawable.createFromStream(img5, null))
                            sixthRune.setBackgroundDrawable(Drawable.createFromStream(img6, null))
                            seventhRune.setBackgroundDrawable(Drawable.createFromStream(img7, null))

                            firstRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            secondRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            thirdRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fourthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            fifthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            sixthRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                            seventhRune.layoutParams = ConstraintLayout.LayoutParams(runeWidth, runeHeight)

                            runesLayout.addView(firstRune)
                            runesLayout.addView(secondRune)
                            runesLayout.addView(thirdRune)
                            runesLayout.addView(fourthRune)
                            runesLayout.addView(fifthRune)
                            runesLayout.addView(sixthRune)
                            runesLayout.addView(seventhRune)

                            val set = ConstraintSet()
                            set.clone(runesLayout)
                            set.connect(firstRune.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
                            set.connect(firstRune.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                            set.connect(firstRune.id, ConstraintSet.TOP, R.id.description_header_frame, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.TOP, firstRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(secondRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(secondRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(thirdRune.id, ConstraintSet.TOP, secondRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(thirdRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(thirdRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(fourthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(fourthRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(fourthRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(fifthRune.id, ConstraintSet.TOP, fourthRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(fifthRune.id, ConstraintSet.START, firstRune.id, ConstraintSet.START, 0)
                            set.connect(fifthRune.id, ConstraintSet.END, firstRune.id, ConstraintSet.END, 0)
                            set.connect(sixthRune.id, ConstraintSet.END, thirdRune.id, ConstraintSet.START, 0)
                            set.connect(sixthRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(sixthRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                            set.connect(seventhRune.id, ConstraintSet.START, thirdRune.id, ConstraintSet.END, 0)
                            set.connect(seventhRune.id, ConstraintSet.BOTTOM, thirdRune.id, ConstraintSet.BOTTOM, 0)
                            set.connect(seventhRune.id, ConstraintSet.TOP, thirdRune.id, ConstraintSet.TOP, 0)
                            set.applyTo(runesLayout)

                            val firstDot = ImageView(requireContext())
                            val secondDot = ImageView(requireContext())
                            val thirdDot = ImageView(requireContext())
                            val fourthDot = ImageView(requireContext())
                            val fifthDot = ImageView(requireContext())
                            val sixthDot = ImageView(requireContext())
                            val seventhDot = ImageView(requireContext())

                            runesDotsList.addAll(arrayListOf(firstDot,secondDot,thirdDot,fourthDot,fifthDot,sixthDot,seventhDot))

                            for(runeDot in runesDotsList){
                                runeDot.id = View.generateViewId()
                                runeDot.adjustViewBounds = true
                                runeDot.setImageResource(R.drawable.ic_circle_deselected)
                                bottomRunesNav.addView(runeDot)
                            }

                            val bottomNavSet = ConstraintSet()
                            bottomNavSet.clone(bottomRunesNav)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.TOP,fourthDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.BOTTOM,fourthDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(firstDot.id,ConstraintSet.END,secondDot.id,ConstraintSet.START,15)

                            bottomNavSet.connect(secondDot.id,ConstraintSet.TOP,fourthDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.BOTTOM,fourthDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(secondDot.id,ConstraintSet.END,thirdDot.id,ConstraintSet.START,15)

                            bottomNavSet.connect(thirdDot.id,ConstraintSet.TOP,fourthDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.BOTTOM,fourthDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(thirdDot.id,ConstraintSet.END,fourthDot.id,ConstraintSet.START,15)

                            bottomNavSet.connect(fourthDot.id,ConstraintSet.TOP,R.id.left_arrow,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.BOTTOM,R.id.left_arrow,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,15)
                            bottomNavSet.connect(fourthDot.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,15)

                            bottomNavSet.connect(fifthDot.id,ConstraintSet.TOP,fourthDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(fifthDot.id,ConstraintSet.BOTTOM,fourthDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(fifthDot.id,ConstraintSet.START,fourthDot.id,ConstraintSet.END,15)

                            bottomNavSet.connect(sixthDot.id,ConstraintSet.TOP,fourthDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(sixthDot.id,ConstraintSet.BOTTOM,fourthDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(sixthDot.id,ConstraintSet.START,fifthDot.id,ConstraintSet.END,15)

                            bottomNavSet.connect(seventhDot.id,ConstraintSet.TOP,fourthDot.id,ConstraintSet.TOP,0)
                            bottomNavSet.connect(seventhDot.id,ConstraintSet.BOTTOM,fourthDot.id,ConstraintSet.BOTTOM,0)
                            bottomNavSet.connect(seventhDot.id,ConstraintSet.START,sixthDot.id,ConstraintSet.END,15)


                            bottomNavSet.applyTo(bottomRunesNav)
                        }
                    }
                    //**runes description
                    //runes click listeners
                    for (i in 0 until this.runesLayout.childCount) {
                        this.runesLayout.getChildAt(i).setOnClickListener(this)
                    }
                    for (runeDot in runesDotsList){
                        runeDot.setOnClickListener(this)
                    }
                    //runes description**
                    model.getAuspForCurrentLayout()
                    model.currentAusp.observe(viewLifecycleOwner) {
                        var testText = interpretationLayout.findViewById<TextView>(R.id.text)
                        interpretationLayout.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener(this)
                        if (it != null) {
                            ausfText = "Благоприятность - $it %"
                            testText.text = ausfText
                            if (it <= 50) {
                                model.getAffimForCurrentLayout(it)
                            }
                        }
                    }
                    model.currentAffirm.observe(viewLifecycleOwner) {
                        if (it != "" || it != null) {
                            val affimTextView = interpretationLayout.findViewById<TextView>(R.id.text_affim)
                            affimTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                            affimTextView.text = it
                            model.getInterpretation(requireContext())
                        }
                    }
                    model.currentInterpretation.observe(viewLifecycleOwner) {
                        if (it != null) {
                            if (it.isNotEmpty()) {
                                var interpretationTextView = interpretationLayout.findViewById<TextView>(R.id.interpretation_text)
                                interpretationTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

                                val secondFont = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
                                val interpretationText = it
                                interpretationTextView.text = Html.fromHtml(interpretationText, null, InterTagHandler(secondFont!!))


                                val backgroundLayout = interpretationFrame
                                val backLayout = interpretationLayout
                                val interLayout = backgroundLayout.findViewById<ConstraintLayout>(R.id.interpretation_layout)
                                val bottomSupportFrame = interLayout.findViewById<FrameLayout>(R.id.bottom_support_frame)
                                val observer = mainConstraintLayout.viewTreeObserver

                                defaultConstraintSet.clone(runesLayout)

                                observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                                    override fun onGlobalLayout() {
                                        observer.removeOnGlobalLayoutListener(this)
                                        screenHeight = mainConstraintLayout.height
                                        val minSize = screenHeight - backgroundLayout.top
                                        var flag = false
                                        if (minSize > backgroundLayout.height) {
                                            val backLayoutParams = backLayout.layoutParams
                                            backLayoutParams.height = minSize
                                            backLayout.layoutParams = backLayoutParams
                                            flag = true
                                        }
                                        if (bottomSupportFrame.bottom < screenHeight && flag) {
                                            val constraintsSet = ConstraintSet()
                                            constraintsSet.clone(backLayout)
                                            constraintsSet.clear(R.id.bottom_support_frame, ConstraintSet.TOP)
                                            constraintsSet.connect(R.id.bottom_support_frame, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                                            constraintsSet.clear(R.id.description_button_frame, ConstraintSet.TOP)
                                            constraintsSet.connect(R.id.description_button_frame, ConstraintSet.BOTTOM, R.id.bottom_support_frame, ConstraintSet.TOP)
                                            constraintsSet.clear(R.id.checkbox, ConstraintSet.TOP)
                                            constraintsSet.connect(R.id.checkbox, ConstraintSet.BOTTOM, R.id.description_button_frame, ConstraintSet.TOP)
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

        view.findViewById<ImageView>(R.id.exit_button).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.left_arrow).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.right_arrow).setOnClickListener(this)

        var runeName = view.findViewById<TextView>(R.id.rune_name)
        runePosition = view.findViewById<TextView>(R.id.rune_position)
        runeAusf = view.findViewById<TextView>(R.id.rune_ausf)
        var runeDescription = view.findViewById<TextView>(R.id.rune_description)
        model.selectedRune.observe(viewLifecycleOwner){
            if(it!=null){
                runeName.text = it.runeName
                runeDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize-3f)
                runeDescription.text = it.fullDescription
                val secondFont = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
                runeAusf.text = Html.fromHtml("Благоприятность - <bf>${it.ausp} %</bf>", null, InterTagHandler(secondFont!!))
            }
        }
        view.findViewById<ConstraintLayout>(R.id.rune_description_back).setOnTouchListener(object : OnSwipeTouchListener(requireContext()){
            override fun onSwipeRight() {
                if(currentRunePosition==runesViewList.size-1){
                    showDescriptionOfSelectedRune(runesViewList[0])
                }
                else showDescriptionOfSelectedRune(runesViewList[currentRunePosition+1])
                super.onSwipeRight()
            }

            override fun onSwipeLeft() {
                if(currentRunePosition==0){
                    showDescriptionOfSelectedRune(runesViewList.last())
                }
                else showDescriptionOfSelectedRune(runesViewList[currentRunePosition-1])
                super.onSwipeLeft()
            }

        })

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(v: View?) {
        val runeIdList = arrayListOf<Int>()
        val runeDotsIdList = arrayListOf<Int>()
        for (rune in runesViewList) runeIdList.add(rune.id)
        for (runeDot in runesDotsList) runeDotsIdList.add(runeDot.id)
        val navController = findNavController()
        when (v?.id) {
            R.id.description_button_frame -> {
                if (checkBox.isChecked) model.saveUserLayout(requireContext())
                navController.navigate(R.id.action_layoutInterpretationFragment_to_layoutFragment)
            }
            in runeIdList -> {
                showDescriptionOfSelectedRune(v)
            }
            in runeDotsIdList ->{
                showDescriptionOfSelectedRune(runesViewList[runeDotsIdList.indexOf(v?.id)])
            }
            R.id.left_arrow ->{
                if(currentRunePosition==0){
                    showDescriptionOfSelectedRune(runesViewList.last())
                }
                else showDescriptionOfSelectedRune(runesViewList[currentRunePosition-1])
            }
            R.id.right_arrow->{
                if(currentRunePosition==runesViewList.size-1){
                    showDescriptionOfSelectedRune(runesViewList[0])
                }
                else showDescriptionOfSelectedRune(runesViewList[currentRunePosition+1])
            }
            R.id.exit_button ->{
                hideRuneDescription()
            }
        }
    }

    private fun hideRuneDescription(){
        defaultConstraintSet.applyTo(runesLayout)
        headerBackgroundFrame.visibility = View.GONE
        interpretationFrame.visibility = View.VISIBLE
        for (rune in runesViewList) {
            rune.foreground = ColorDrawable(Color.TRANSPARENT)
        }
        var descriptionBack = runesLayout.findViewById<ConstraintLayout>(R.id.rune_description_back)
        descriptionBack.visibility = View.GONE
    }

    private fun showDescriptionOfSelectedRune(v: View?){
        var size =0
        defaultConstraintSet.applyTo(runesLayout)
        if (runesViewList != null && runesViewList.size > 1) {
            headerBackgroundFrame.visibility = View.VISIBLE
            interpretationFrame.visibility = View.GONE
            for (rune in runesViewList) {
                if (rune.id != v?.id) {
                    rune.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.rune_foreground)
                } else rune.foreground = ColorDrawable(Color.TRANSPARENT)
            }
            for (rune in runesViewList) {
                if (rune.id == v?.id) {


                    for(runeDot in runesDotsList){
                        runeDot.setImageResource(R.drawable.ic_circle_deselected)
                        runeDot.setOnClickListener(this)
                    }

                    model.getSelectedRuneData(runesViewList.indexOf(rune))
                    runePosition.text =runesPositionsList[runesViewList.indexOf(rune)]
                    runeDescriptionSV.scrollTo(0,0)
                    runesDotsList[runesViewList.indexOf(rune)].setImageResource(R.drawable.ic_circle_selected)
                    runesDotsList[runesViewList.indexOf(rune)].setOnClickListener(null)
                    currentRunePosition = runesViewList.indexOf(rune)

                    val constraintsSet = ConstraintSet()
                    constraintsSet.clone(runesLayout)
                    constraintsSet.connect(R.id.rune_description_back, ConstraintSet.TOP, rune.id, ConstraintSet.BOTTOM)
                    constraintsSet.applyTo(runesLayout)
                }
            }
        }

        when(layoutId){
            1,2,3-> if(firsOpening){
                firsOpening = false
                size = runesViewList[0].bottom
                baseSize = size
            }
            else size = baseSize
            4->{
                if(firsOpening){
                    firsOpening = false
                    size = runesViewList[3].bottom
                    baseSize = size
                }
                else size = baseSize
                when(v?.id){
                    runesViewList[1].id,runesViewList[0].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id,ConstraintSet.TOP)
                        set.connect(runesViewList[3].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                    }
                    runesViewList[2].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id)
                        set.clear(runesViewList[1].id,ConstraintSet.BOTTOM)
                        set.clear(runesViewList[1].id,ConstraintSet.TOP)
                        set.connect(runesViewList[1].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                    }
                }
            }
            5->{
                if(firsOpening){
                    firsOpening = false
                    size = runesViewList[3].bottom
                    baseSize = size
                }
                else size = baseSize
                when(v?.id){
                    runesViewList[1].id,runesViewList[2].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.connect(runesViewList[3].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                    }
                    runesViewList[0].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id,ConstraintSet.TOP)
                        set.connect(runesViewList[3].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                    }
                }
            }
            6->{
                if(firsOpening){
                    firsOpening = false
                    size = runesViewList[3].bottom
                    baseSize = size
                }
                else size = baseSize
                when(v?.id){
                    runesViewList[1].id,runesViewList[0].id,runesViewList[2].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id,ConstraintSet.TOP)
                        set.connect(runesViewList[3].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                    }
                    runesViewList[4].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id)
                        set.clear(runesViewList[1].id,ConstraintSet.BOTTOM)
                        set.clear(runesViewList[1].id,ConstraintSet.TOP)
                        set.connect(runesViewList[1].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                        runesViewList[3].visibility= View.GONE
                    }
                }
            }
            7->{
                if(firsOpening){
                    firsOpening = false
                    size = runesViewList[5].bottom
                    baseSize = size
                }
                else size = baseSize
                when(v?.id){
                    runesViewList[4].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[5].id,ConstraintSet.TOP)
                        set.connect(runesViewList[5].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                    }

                    runesViewList[1].id,runesViewList[2].id,runesViewList[0].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[5].id)
                        set.clear(runesViewList[4].id,ConstraintSet.TOP)
                        set.connect(runesViewList[4].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                        runesViewList[5].visibility= View.GONE
                    }
                    runesViewList[3].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[5].id)
                        set.clear(runesViewList[4].id)
                        set.clear(runesViewList[1].id,ConstraintSet.BOTTOM)
                        set.clear(runesViewList[1].id,ConstraintSet.TOP)
                        set.connect(runesViewList[1].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                        runesViewList[4].visibility= View.GONE
                        runesViewList[5].visibility= View.GONE
                    }
                }
            }

            8->{
                if(firsOpening){
                    firsOpening = false
                    size = runesViewList[6].bottom
                    baseSize = size
                }
                else size = baseSize
                when(v?.id){
                    runesViewList[5].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[6].id,ConstraintSet.TOP)
                        set.connect(runesViewList[6].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                    }

                    runesViewList[0].id,runesViewList[2].id,runesViewList[1].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[6].id)
                        set.clear(runesViewList[5].id,ConstraintSet.TOP)
                        set.connect(runesViewList[5].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                        runesViewList[6].visibility= View.GONE
                    }
                    runesViewList[4].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[6].id)
                        set.clear(runesViewList[5].id)
                        set.clear(runesViewList[0].id,ConstraintSet.BOTTOM)
                        set.clear(runesViewList[0].id,ConstraintSet.TOP)
                        set.connect(runesViewList[0].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                        runesViewList[5].visibility= View.GONE
                        runesViewList[6].visibility= View.GONE
                    }
                    runesViewList[3].id->{
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[6].id)
                        set.clear(runesViewList[5].id)
                        set.clear(runesViewList[0].id)
                        set.clear(runesViewList[2].id)
                        set.clear(runesViewList[1].id)
                        set.clear(runesViewList[4].id,ConstraintSet.BOTTOM)
                        set.clear(runesViewList[4].id,ConstraintSet.TOP)
                        set.connect(runesViewList[4].id,ConstraintSet.BOTTOM,headerFrame.id,ConstraintSet.BOTTOM)
                        set.applyTo(runesLayout)
                        runesViewList[5].visibility= View.GONE
                        runesViewList[6].visibility= View.GONE
                        runesViewList[0].visibility= View.GONE
                        runesViewList[1].visibility= View.GONE
                        runesViewList[2].visibility= View.GONE
                    }
                }
            }
        }

        var descriptionBack = runesLayout.findViewById<ConstraintLayout>(R.id.rune_description_back)
        var backLayoutParams = descriptionBack.layoutParams
        backLayoutParams.height = screenHeight - size
        descriptionBack.layoutParams = backLayoutParams
        descriptionBack.visibility = View.VISIBLE
    }

    private fun <T : View>viewIdGenerator(list: ArrayList<T>){
        for( element in list) element.id = View.generateViewId()
    }
}