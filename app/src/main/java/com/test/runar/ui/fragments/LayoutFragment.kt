package com.test.runar.ui.fragments

import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutFragment : Fragment(R.layout.fragment_layouts), View.OnClickListener {
    private lateinit var model: MainViewModel
    private var fontSize: Float =0f

    private var scroll_view: ScrollView? = null
    private var arrow_down: ImageView? = null
    private var arrow_up: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        fontSize = correctFontSize()
        model.setFontSize(fontSize)
        model.clearLayoutData()
        model.clearAusp()
        model.clearAffirm()
        model.clearInterpretation()
        Log.d("DebugData","First fragment recreated")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ConstraintLayout>(R.id.first_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.second_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.third_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.fourth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.fifth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.sixth_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.seventh_layout).setOnClickListener(this)
        view.findViewById<ConstraintLayout>(R.id.eight_layout).setOnClickListener(this)

        arrow_down = view.findViewById(R.id.arrow_down)
        arrow_up = view.findViewById(R.id.arrow_up)
        scroll_view = view.findViewById(R.id.scroll_view)
        //screen size and aspect ratio
        var metrics: DisplayMetrics = context?.resources!!.displayMetrics
        var ratio = metrics.heightPixels.toFloat() / metrics.widthPixels.toFloat()
        if (ratio >= 2.1) {
            arrow_down?.visibility = View.GONE
        } else {
            arrow_down?.visibility = View.VISIBLE
            arrow_down?.setOnClickListener {
                scroll_view?.post(Runnable {
                    scroll_view?.fullScroll(ScrollView.FOCUS_DOWN)
                })
            }
            arrowUp()
        }
    }

    private fun arrowUp() {
        scroll_view?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            //  determine if the end of the list has been reached
            val bottom = (scroll_view?.getChildAt(scroll_view?.childCount!! - 1))?.height!! - scroll_view?.height!! - scrollY
            if (bottom == 0) {
                //bottom detected
                arrow_down?.visibility = View.GONE

                arrow_up?.visibility = View.VISIBLE
                //  scrolling up after clicking
                arrow_up?.setOnClickListener {
                    scroll_view?.post(Runnable {
                        scroll_view?.fullScroll(ScrollView.FOCUS_UP)
                    })
                }
            } else {
                arrow_up?.visibility = View.GONE
                arrow_down?.visibility = View.VISIBLE
            }
        }
    }


    override fun onClick(v: View?) {
        val dest = when (v?.id) {
            R.id.first_layout -> 1
            R.id.second_layout -> 2
            R.id.third_layout -> 3
            R.id.fourth_layout -> 4
            R.id.fifth_layout -> 5
            R.id.sixth_layout -> 6
            R.id.seventh_layout -> 7
            else -> 8
        }
        val navController = findNavController()
        model.descriptionCheck(requireContext(), dest)
        model.getLayoutDescription(requireContext(), dest)
        model.showStatus.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    navController.navigate(R.id.action_layoutFragment_to_layoutInitFragment)
                    model.clearShowStatus()
                }
                1 -> {
                    navController.navigate(R.id.action_layoutFragment_to_layoutDescriptionFragment2)
                    model.clearShowStatus()
                }
            }
        }
    }
    private fun correctFontSize(): Float {
        val text = "Простое гадание на рунах, однако оно"
        val paint = Paint()
        val bounds = Rect()
        val maxWidth = Resources.getSystem().displayMetrics.widthPixels * 0.84
        paint.typeface = context?.let { ResourcesCompat.getFont(it,R.font.roboto_light) }
        var textSize = 1f
        paint.textSize = 1f
        paint.getTextBounds(text, 0, text.length, bounds)
        var currentWidth = bounds.width()
        while (currentWidth < maxWidth) {
            textSize++
            paint.textSize = textSize
            paint.getTextBounds(text, 0, text.length, bounds)
            currentWidth = bounds.width()
        }
        return textSize - 2f
    }
}