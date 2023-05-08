package com.tnco.runar.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.tnco.runar.R
import com.tnco.runar.data.remote.NetworkResult
import com.tnco.runar.databinding.FragmentGenBlockBinding
import com.tnco.runar.databinding.FragmentGeneratorStartBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.ui.adapter.RunesGeneratorAdapter
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.InternalDeepLink
import com.tnco.runar.util.PurchaseHelper
import com.tnco.runar.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class GeneratorStartFragment : Fragment(), HasVisibleNavBar {

    private var _binding: FragmentGeneratorStartBinding? = null
    private var binding2: FragmentGenBlockBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var purchaseHelper: PurchaseHelper
    private var hasSubs = false

    private var listId: MutableList<Int> = mutableListOf()
    private var listAllIds: MutableList<Int> = mutableListOf()
    private val mAdapter: RunesGeneratorAdapter by lazy {
        RunesGeneratorAdapter(::onShowBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGeneratorStartBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SELECTED)
        setupRecyclerView()

        viewModel.isNetworkAvailable.observeOnce(viewLifecycleOwner) { status ->
            if (status) {
                requestApiData()
            } else {
                showInternetConnectionError()
            }
        }
//        purchaseHelper.consumeEnabled.asLiveData().observe(viewLifecycleOwner) {
//            hasSubs = it
//            Log.d("TAG_BILLING_TEST", "hasSubs: $hasSubs")
//            if (!hasSubs)
//            else
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRandom.setOnClickListener {
                if (shimmerLayout.visibility == View.GONE)
                    randomRunes()
            }

            btnGenerate.setOnClickListener {
                if (shimmerLayout.visibility == View.GONE)
                    sentRunes()
            }
        }

        makeBottomSheet()
    }

    private fun makeBottomSheet() {
        with(binding) {
            mAdapter.obsSelectedRunes.observe(viewLifecycleOwner) { runes ->
                when (runes.size) {
                    1 -> {
                        rune1.setOnLongClickListener {
                            onShowBottomSheet(runes[0])
                            true
                        }
                    }
                    2 -> {
                        rune2.setOnLongClickListener {
                            onShowBottomSheet(runes[1])
                            true
                        }
                    }
                    3 -> {
                        rune3.setOnLongClickListener {
                            onShowBottomSheet(runes[2])
                            true
                        }
                    }
                }
            }
        }
    }

    private fun onShowBottomSheet(rune: RunesItemsModel) {
        var title = ""
        var desc = ""
        if (viewModel.languageRepository.currentAppLanguage() == "ru") {
            title = rune.ruTitle.toString()
            desc = rune.ruDesc.toString()
        } else {
            title = rune.enTitle.toString()
            desc = rune.enDesc.toString()
        }

        val direction = GeneratorStartFragmentDirections
            .actionGeneratorStartFragmentToBottomSheetFragment(title, desc, rune.imgUrl)
        findNavController().navigate(direction)
    }

    private fun onStartShimmering() {
        with(binding) {
            runesRecyclerView.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
        }
    }

    private fun onStopShimmering() {
        with(binding) {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            runesRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun readDatabase() { // TODO доделать считывание
        listAllIds.clear()
        lifecycleScope.launch {
            viewModel.readRunes.observeOnce(viewLifecycleOwner) { listRunes ->
                if (listRunes.isNotEmpty()) {
                    mAdapter.setData(listRunes)
                    listRunes.forEach {
                        listAllIds.add(it.id)
                    }
                } else requestApiData()
            }
        }
    }

    private fun requestApiData() {
        listAllIds.clear()
        viewModel.getRunes()
        viewModel.runesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (response.data!!.isNotEmpty()) {
                        onStopShimmering()
                        mAdapter.setData(response.data)
                        response.data.forEach {
                            listAllIds.add(it.id)
                        }
                    }
                }
                is NetworkResult.Error -> showInternetConnectionError()
                is NetworkResult.Loading -> onStartShimmering()
            }
        }
    }

    private fun setupRecyclerView() {
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false)

        with(binding) {
            runesRecyclerView.layoutManager = gridLayoutManager
            runesRecyclerView.adapter = mAdapter

            mAdapter.obsSelectedRunes.observe(viewLifecycleOwner) {
                when (it.size) {
                    1 -> {
                        showRunes(tvRune1, rune1, tvDescRune1, it[0])
                        btnRandom.visibility = View.INVISIBLE
                        btnGenerate.visibility = View.VISIBLE
                    }
                    2 -> {
                        showRunes(tvRune2, rune2, tvDescRune2, it[1])
                    }
                    3 -> {
                        showRunes(tvRune3, rune3, tvDescRune3, it[2])
                    }
                }
            }

            rune1.setOnClickListener {
                if (mAdapter.obsSelectedRunes.value?.size == 1) {
                    clearRune(tvRune1, rune1, tvDescRune1, 0)
                    btnGenerate.visibility = View.GONE
                    btnRandom.visibility = View.VISIBLE
                }
            }

            rune2.setOnClickListener {
                if (mAdapter.obsSelectedRunes.value?.size == 2)
                    clearRune(tvRune2, rune2, tvDescRune2, 1)
            }

            rune3.setOnClickListener {
                if (mAdapter.obsSelectedRunes.value?.size == 3)
                    clearRune(tvRune3, rune3, tvDescRune3, 2)
            }
        }
    }

    private fun showRunes(
        tvRune: TextView,
        imgRune: ImageView,
        tvDescRune: TextView,
        runesItemsModel: RunesItemsModel
    ) {
        tvRune.visibility = View.INVISIBLE
        imgRune.visibility = View.VISIBLE
        imgRune.load(runesItemsModel.imgUrl)
        tvDescRune.visibility = View.VISIBLE
        if (Locale.getDefault().language == "ru")
            tvDescRune.text = runesItemsModel.ruTitle
        else tvDescRune.text = runesItemsModel.enTitle
    }

    private fun clearRune(
        tvRune: TextView,
        imgRune: ImageView,
        tvDescRune: TextView,
        index: Int
    ) {
        imgRune.visibility = View.INVISIBLE
        tvDescRune.visibility = View.INVISIBLE
        tvRune.visibility = View.VISIBLE
        mAdapter.obsSelectedRunes.value?.removeAt(index)
        mAdapter.updateItem(index)
    }

    private fun randomRunes() {
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_RANDOM_RUNES)
        val count = (1..3).random()
        listId.clear()

        for (i in 0 until count) {
            listId.add(i, listAllIds.random())
            listAllIds.remove(listId[i])
        }
        listId.sort()

        var idsString = ""
        when (count) {
            1 -> {
                idsString = listId[0].toString()
            }
            2 -> {
                idsString = "${listId[0]}_${listId[1]}"
            }
            3 -> {
                idsString = "${listId[0]}_${listId[1]}_${listId[2]}"
            }
        }
        viewModel.runesSelected = idsString

        val direction = GeneratorStartFragmentDirections
            .actionGeneratorStartFragmentToGeneratorMagicRune()
        findNavController().navigate(direction)
    }

    private fun sentRunes() {
        listId.clear()
        mAdapter.obsSelectedRunes.value?.forEach {
            listId.add(it.id)
        }
        listId.sort()
        var idsString = ""
        val count = listId.count()

        when (count) {
            1 -> {
                idsString = listId[0].toString()
            }
            2 -> {
                idsString = "${listId[0]}_${listId[1]}"
            }
            3 -> {
                idsString = "${listId[0]}_${listId[1]}_${listId[2]}"
            }
        }

        viewModel.runesSelected = idsString

        val direction = GeneratorStartFragmentDirections
            .actionGeneratorStartFragmentToGeneratorMagicRune()
        findNavController().navigate(direction)
    }

    private fun showInternetConnectionError() {
        val uri = Uri.parse(
            InternalDeepLink.ConnectivityErrorFragment
                .withArgs("${R.id.generatorStartFragment}")
        )
        findNavController().navigate(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter.clearData()
        _binding = null
    }
}
