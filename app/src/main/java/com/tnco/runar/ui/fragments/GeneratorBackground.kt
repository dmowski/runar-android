package com.tnco.runar.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.R
import com.tnco.runar.controllers.*
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.presentation.viewmodel.MainViewModel
import com.tnco.runar.ui.activity.MainActivity


class GeneratorBackground : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var backgroundImgRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var pointLayout: LinearLayout
    private lateinit var btn_next: TextView
    private lateinit var textSelectBackground: TextView
    private var hasSelected = false
    private val pointsList = mutableListOf<ImageView>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SELECTION_BACKGROUND)

        progressBar = view.findViewById(R.id.generatorProgressBar)
        pointLayout = view.findViewById(R.id.points)
        btn_next = view.findViewById(R.id.button_next)
        textSelectBackground = view.findViewById(R.id.textSelectBackground)
        textSelectBackground.visibility = View.GONE
        btn_next.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, GeneratorFinal())
                ?.commit()
        }

        if (viewModel.backgroundInfo.value!!.isEmpty()) {
            viewModel.getBackgroundInfo()
        }
        backgroundImgRecyclerView = view.findViewById(R.id.backgroundImgRecyclerView)
        val adapter = BackgroundAdapter(viewModel.backgroundInfo.value!!, this)
        val layoutManager = LinearLayoutManager(requireActivity())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        backgroundImgRecyclerView.adapter = adapter
        backgroundImgRecyclerView.layoutManager = layoutManager
        backgroundImgRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->

            var pos = layoutManager.findFirstCompletelyVisibleItemPosition()

            if (hasSelected) {
                pos = viewModel.backgroundInfo.value!!.indexOfFirst { it.isSelected }
            }

            if (pos >= 0) {
                for (i in pointsList.indices) {
                    pointsList[i].setImageResource(
                        if (i == pos) R.drawable.ic_point_selected
                        else R.drawable.ic_point_deselected
                    )
                }
                pointLayout.invalidate()
            }

        }


        viewModel.backgroundInfo.observe(activity as MainActivity) {
            if (viewModel.backgroundInfo.value!!.isNotEmpty()) {
                progressBar.visibility = View.GONE
                textSelectBackground.visibility = if (!hasSelected) View.VISIBLE else View.GONE
                pointsList.clear()
                pointLayout.removeAllViews()
                val inflater = LayoutInflater.from(view.context)
                for (i in 0 until viewModel.backgroundInfo.value!!.size) {
                    val point =
                        inflater.inflate(R.layout.point_image_view, pointLayout, false) as ImageView
                    if (!hasSelected) {
                        var pos = layoutManager.findFirstCompletelyVisibleItemPosition()
                        if (pos < 0) pos = 0
                        point.setImageResource(if (i == pos) R.drawable.ic_point_selected else R.drawable.ic_point_deselected)
                    } else {
                        point.setImageResource(if (viewModel.backgroundInfo.value!![i].isSelected) R.drawable.ic_point_selected else R.drawable.ic_point_deselected)
                    }

                    pointLayout.addView(point)
                    pointsList.add(point)
                    pointLayout.invalidate()
                }

            }
            backgroundImgRecyclerView.adapter?.notifyDataSetChanged()
        }
        (activity as MainActivity).hideBottomBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_generator_background, container, false)
    }

    fun selectBackground(position: Int) {
        val data = viewModel.backgroundInfo.value!!


        for (i in data.indices) {
            if (i != position) {
                data[i].isSelected = false
            } else {
                data[position].isSelected = !data[position].isSelected
            }
        }

        hasSelected = data.any { it.isSelected }
        if (hasSelected) {
            btn_next.visibility = View.VISIBLE
            textSelectBackground.visibility = View.GONE
        } else {
            btn_next.visibility = View.GONE
            textSelectBackground.visibility = View.VISIBLE
        }


        viewModel.backgroundInfo.value = data
    }

}
