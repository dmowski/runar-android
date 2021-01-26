package com.test.runar.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutFragment : Fragment(R.layout.fragment_layouts), View.OnClickListener {
    private lateinit var model: MainViewModel

    private var scroll_view:ScrollView?=null
    private var arrow_down:ImageView?=null
    private var arrow_up:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
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

        arrowUpDown()
    }

    private fun arrowUpDown() {

        var scrollBounds = Rect()
        scroll_view?.getHitRect(scrollBounds)

        scroll_view?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//if at least one pixel of the picture is visible and scrolling down
            if (scroll_view?.getLocalVisibleRect(scrollBounds) == true && oldScrollY < scrollY) {
                arrow_down?.visibility = View.VISIBLE

                // if yes - scrolling down after clicking
                arrow_down?.setOnClickListener {
                    scroll_view?.post(Runnable {
                        scroll_view?.fullScroll(ScrollView.FOCUS_DOWN)
                    })
                }
            } else {
                arrow_down?.visibility = View.GONE
            }

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
        val bundle = bundleOf("id" to dest)
        model.descriptionCheck(requireContext(), dest)
        model.showStatus.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    navController.navigate(R.id.emptyFragment)
                    model.clearShowStatus()
                }
                1 -> {
                    navController.navigate(R.id.layoutDescriptionFragment, bundle)
                    model.clearShowStatus()
                }
            }
        }
    }
}