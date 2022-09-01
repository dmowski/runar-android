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
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.FragmentGeneratorStartBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.ui.adapter.RunesGeneratorAdapter
import com.tnco.runar.util.observeOnce
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.util.*

class GeneratorStartFragment : Fragment() {

    private var _binding: FragmentGeneratorStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: MainViewModel
    private val mAdapter: RunesGeneratorAdapter by lazy { RunesGeneratorAdapter() }
    private var listId: MutableList<Int> = mutableListOf()
    private var listAllIds: MutableList<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).hideBottomBar()
        _binding = FragmentGeneratorStartBinding.inflate(inflater, container, false)
        binding.arrowBack.setOnClickListener {
            activity?.onBackPressed()
        }

        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SELECTED)
        mViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setupRecyclerView()
        readDatabase()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRandom.setOnClickListener {
                randomRunes()
            }

            btnGenerate.setOnClickListener {
                sentRunes()
            }
        }

        makeBottomSheet()
    }

    private fun makeBottomSheet() {
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false)

        with(binding) {
            runesRecyclerView.layoutManager = gridLayoutManager
            runesRecyclerView.adapter = mAdapter

            mAdapter.obsSelectedRunes.observe(viewLifecycleOwner) {
                val titles: MutableList<String?>? = null
                val descs: MutableList<String?>? = null
                val imageUrls: MutableList<String?>? = null

                val isRus = Locale.getDefault().language == "ru"
                val title1 = if (isRus) it[0].ruTitle else it[0].enTitle
                val desc1 = if (isRus) it[0].ruDesc else it[0].enDesc

                val title2 = if (isRus) it[1].ruTitle else it[1].enTitle
                val desc2 = if (isRus) it[1].ruDesc else it[1].enDesc

                val title3 = if (isRus) it[2].ruTitle else it[2].enTitle
                val desc3 = if (isRus) it[2].ruDesc else it[2].enDesc

                when {
                    rune1.visibility == View.VISIBLE -> {
                        titles?.set(0, title1)
                        descs?.set(0, desc1)
                        imageUrls?.set(0, it[0].imgUrl)
                    }
                    rune2.visibility == View.VISIBLE -> {
                        titles?.set(1, title2)
                        descs?.set(1, desc2)
                        imageUrls?.set(1, it[1].imgUrl)
                    }
                    rune3.visibility == View.VISIBLE -> {
                        titles?.set(2, title3)
                        descs?.set(2, desc3)
                        imageUrls?.set(2, it[2].imgUrl)
                    }
                    else -> {
                        return@observe
                    }
                }

                rune1.setOnLongClickListener {
                    if (titles?.get(0) != null) {
                        BottomSheetFragment(titles[0], descs?.get(0), imageUrls?.get(0)).show(
                            requireActivity().supportFragmentManager,
                            BottomSheetFragment.TAG
                        )
                    }
                    true
                }

                rune2.setOnLongClickListener {
                    if (titles?.get(1) != null) {
                        BottomSheetFragment(titles[1], descs?.get(1), imageUrls?.get(1)).show(
                            requireActivity().supportFragmentManager,
                            BottomSheetFragment.TAG
                        )
                    }
                    true
                }

                rune3.setOnLongClickListener {
                    if (titles?.get(2) != null) {
                        BottomSheetFragment(titles[2], descs?.get(2), imageUrls?.get(2)).show(
                            requireActivity().supportFragmentManager,
                            BottomSheetFragment.TAG
                        )
                    }
                    true
                }
            }
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
        mViewModel.getRunes()
        mViewModel.runesResponse.observe(viewLifecycleOwner) { listRunes ->
            if (listRunes.isNotEmpty()) {
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
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, GeneratorMagicRune())
            ?.commit()
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
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, GeneratorMagicRune())
            ?.commit()

    }
}