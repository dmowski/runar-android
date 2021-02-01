package com.test.runar.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutInterpretationFragment : Fragment(R.layout.fragment_layout_interpretation),
        View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var header: TextView
    private lateinit var headerText: String
    private var runeHeight: Int = 0
    private var runeWidth: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                }
            }
        }

        val mainLayout = view.findViewById<ConstraintLayout>(R.id.main_layout)
        val backgroundLayout = (view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(2) as ConstraintLayout
        val observer = mainLayout.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                observer.removeOnGlobalLayoutListener(this)
                val screenHeight = mainLayout.height
                val minSize = screenHeight - backgroundLayout.top
                if (minSize > backgroundLayout.height) {
                    val backLayout = backgroundLayout.getChildAt(1) as ConstraintLayout
                    val backLayoutParams = backLayout.layoutParams
                    backLayoutParams.height = minSize
                    backLayout.layoutParams = backLayoutParams
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(v: View?) {
        val navController = findNavController()
    }
}