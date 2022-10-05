package com.tnco.runar.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.viewmodel.MainViewModel


class GeneratorBackground : Fragment() {
    private lateinit var viewModel: MainViewModel
    lateinit var backgroundImgRecyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var pointLayout: LinearLayout
    lateinit var btn_next: TextView
    lateinit var textSelectBackground: TextView
    lateinit var backArrow: ImageView
    var hasSelected = false
    val pointsList = mutableListOf<ImageView>()
    val adapter by lazy { BackgroundAdapter(::selectBackground) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showCancelDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SELECTION_BACKGROUND)
        progressBar = view.findViewById(R.id.generatorProgressBar)
        pointLayout = view.findViewById(R.id.points)
        btn_next = view.findViewById<TextView>(R.id.button_next)
        textSelectBackground = view.findViewById<TextView>(R.id.textSelectBackground)
        backArrow = view.findViewById(R.id.backArrow)
        textSelectBackground.visibility = View.GONE
        btn_next.setOnClickListener {
            val direction = GeneratorBackgroundDirections
                .actionGeneratorBackgroundToGeneratorFinal()
            findNavController().navigate(direction)
        }

        backArrow.setOnClickListener {
            showCancelDialog()
        }

        if (viewModel.backgroundInfo.value!!.isEmpty()) {
            viewModel.getBackgroundInfo()
        }
        backgroundImgRecyclerView = view.findViewById(R.id.backgroundImgRecyclerView)
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


        viewModel.backgroundInfo.observe(activity as MainActivity, Observer {
            if (viewModel.backgroundInfo.value!!.isNotEmpty()) {
                progressBar.visibility = View.GONE
                textSelectBackground.visibility = if (!hasSelected) View.VISIBLE else View.GONE
                pointsList.clear()
                pointLayout.removeAllViews()
                val inflater = LayoutInflater.from(view.context)
                for (i in 0..viewModel.backgroundInfo.value!!.size - 1) {
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
                adapter.updateData(it)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_generator_background, container, false)
    }

    private fun selectBackground(position: Int) {
        val data = viewModel.backgroundInfo.value!!


        for (i in data.indices) {
            if (i != position) {
                data[i].isSelected = false
            } else {
                data[position].isSelected = !data[position].isSelected
            }
        }

        hasSelected = data.filter { it.isSelected }.count() > 0
        if (hasSelected) {
            btn_next.visibility = View.VISIBLE
            textSelectBackground.visibility = View.GONE
        } else {
            btn_next.visibility = View.GONE
            textSelectBackground.visibility = View.VISIBLE
        }


        viewModel.backgroundInfo.value = data
    }

    private fun showCancelDialog() {
        CancelDialog(
            requireContext(),
            viewModel.fontSize.value!!,
            "generator_background",
            getString(R.string.description_generator_popup)
        ) {
            val direction = GeneratorBackgroundDirections.actionGlobalGeneratorFragment()
            findNavController().navigate(direction)
        }
            .showDialog()
    }
}
