package com.tnco.runar.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tnco.runar.R
import com.tnco.runar.data.remote.NetworkResult
import com.tnco.runar.databinding.FragmentGeneratorBackgroundBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.InternalDeepLink
import com.tnco.runar.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneratorBackground : Fragment() {

    private var _binding: FragmentGeneratorBackgroundBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    lateinit var backgroundImgRecyclerView: RecyclerView
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
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SELECTION_BACKGROUND)
        binding.textSelectBackground.visibility = View.GONE
        binding.buttonNext.setOnClickListener {
            val direction = GeneratorBackgroundDirections
                .actionGeneratorBackgroundToGeneratorFinal()
            findNavController().navigate(direction)
        }

        binding.backArrow.setOnClickListener {
            showCancelDialog()
        }

        if (viewModel.backgroundInfo.isEmpty()) {
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
                pos = viewModel.backgroundInfo.indexOfFirst { it.isSelected }
            }

            if (pos >= 0) {
                for (i in pointsList.indices) {
                    pointsList[i].setImageResource(
                        if (i == pos) R.drawable.ic_point_selected
                        else R.drawable.ic_point_deselected
                    )
                }
                binding.points.invalidate()
            }
        }

        viewModel.isNetworkAvailable.observeOnce(viewLifecycleOwner) { status ->
            if (status) {
                observeBackgroundImages(layoutManager)
            } else {
                showInternetConnectionError()
            }
        }
    }

    private fun observeBackgroundImages(layoutManager: LinearLayoutManager) {
        viewModel.backgroundInfoResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val backgroundInfo = response.data!!
                    if (backgroundInfo.isNotEmpty()) {
                        with(binding) {
                            generatorProgressBar.visibility = View.GONE
                            textSelectBackground.visibility =
                                if (!hasSelected) View.VISIBLE else View.GONE
                            points.removeAllViews()
                        }
                        pointsList.clear()
                        val inflater = LayoutInflater.from(requireView().context)
                        for (i in backgroundInfo.indices) {
                            val point = inflater.inflate(
                                R.layout.point_image_view,
                                binding.points,
                                false
                            ) as ImageView
                            if (!hasSelected) {
                                var pos = layoutManager.findFirstCompletelyVisibleItemPosition()
                                if (pos < 0) pos = 0
                                point.setImageResource(
                                    if (i == pos) R.drawable.ic_point_selected
                                    else R.drawable.ic_point_deselected
                                )
                            } else {
                                point.setImageResource(
                                    if (backgroundInfo[i].isSelected) R.drawable.ic_point_selected
                                    else R.drawable.ic_point_deselected
                                )
                            }

                            binding.points.addView(point)
                            pointsList.add(point)
                            binding.points.invalidate()
                        }
                        adapter.updateData(backgroundInfo)
                    }
                }
                is NetworkResult.Error -> {
                    if (viewModel.backgroundInfo.all { it.img == null }) {
                        showInternetConnectionError()
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneratorBackgroundBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }

    private fun selectBackground(position: Int) {
        val data = viewModel.backgroundInfo

        for (i in data.indices) {
            if (i != position) {
                data[i].isSelected = false
            } else {
                data[position].isSelected = !data[position].isSelected
            }
        }

        hasSelected = data.filter { it.isSelected }.count() > 0
        if (hasSelected) {
            with(binding) {
                buttonNext.visibility = View.VISIBLE
                textSelectBackground.visibility = View.GONE
            }
        } else {
            with(binding) {
                buttonNext.visibility = View.GONE
                textSelectBackground.visibility = View.VISIBLE
            }
        }

        adapter.updateData(data)
    }

    private fun showCancelDialog() {
        val uri = Uri.parse(
            InternalDeepLink.CancelDialog
                .withArgs(
                    "${viewModel.fontSize.value!!}",
                    "generator_background",
                    getString(R.string.description_runic_draws_popup),
                    "${R.id.generatorStartFragment}"
                )
        )
        findNavController().navigate(uri)
    }

    private fun showInternetConnectionError() {
        requireActivity().viewModelStore.clear()
        val uri = Uri.parse(
            InternalDeepLink.ConnectivityErrorFragment
                .withArgs("${R.id.generatorStartFragment}")
        )
        findNavController().navigate(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
