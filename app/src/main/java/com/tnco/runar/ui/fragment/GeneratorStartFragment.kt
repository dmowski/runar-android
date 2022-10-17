package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.FragmentGeneratorStartBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.adapter.RunesGeneratorAdapter
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.observeOnce
import kotlinx.coroutines.launch
import java.util.*

class GeneratorStartFragment : Fragment() {

    private var _binding: FragmentGeneratorStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: MainViewModel
    private val mAdapter: RunesGeneratorAdapter by lazy { RunesGeneratorAdapter(::onShowBottomSheet) }
    private var listId: MutableList<Int> = mutableListOf()
    private var listAllIds: MutableList<Int> = mutableListOf()
    private val sharedPreferences by lazy { SharedPreferencesRepository.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratorStartBinding.inflate(inflater, container, false)

        onStartShimmering()

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SELECTED)
        mViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setupRecyclerView()
        //readDatabase()
        requestApiData()
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

        if (sharedPreferences.language == "ru") {
            title = rune.ruTitle.toString()
            desc = rune.ruDesc.toString()
        } else if (sharedPreferences.language == "en") {
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

    private fun readDatabase() { // доделать считывание
        listAllIds.clear()
        lifecycleScope.launch {
            mViewModel.readRunes.observeOnce(viewLifecycleOwner) { listRunes ->
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
        mViewModel.getRunes()
        mViewModel.runesResponse.observe(viewLifecycleOwner) { listRunes ->
            if (listRunes.isNotEmpty()) {
                onStopShimmering()
                mAdapter.setData(listRunes)
                listRunes.forEach {
                    listAllIds.add(it.id)
                }
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
    }

    private fun randomRunes() {
        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_RANDOM_RUNES)
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
        mViewModel.runesSelected = idsString

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

        mViewModel.runesSelected = idsString

        val direction = GeneratorStartFragmentDirections
            .actionGeneratorStartFragmentToGeneratorMagicRune()
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}