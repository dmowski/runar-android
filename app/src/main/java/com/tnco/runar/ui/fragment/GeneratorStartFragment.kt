package com.tnco.runar.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.tnco.runar.R
import com.tnco.runar.data.remote.NetworkResult
import com.tnco.runar.databinding.FragmentGeneratorStartBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.adapter.RunesGeneratorAdapter
import com.tnco.runar.ui.layouts.NoticeBottomSheetGenerator
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.GoogleAdUtils
import com.tnco.runar.util.InternalDeepLink
import com.tnco.runar.util.PurchaseHelper
import com.tnco.runar.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class GeneratorStartFragment : Fragment(), HasVisibleNavBar {

    private var _binding: FragmentGeneratorStartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var hasChance = false

    private lateinit var purchaseHelper: PurchaseHelper
    private var hasSubs = false
    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    private var listId: MutableList<Int> = mutableListOf()
    private var listAllIds: MutableList<Int> = mutableListOf()
    private val mAdapter: RunesGeneratorAdapter by lazy {
        RunesGeneratorAdapter(::onShowBottomSheet)
    }

    private val showBottomSheet = mutableStateOf(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGeneratorStartBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.arrowBackBlock.setOnClickListener {
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

        purchaseHelper = PurchaseHelper(requireActivity())
        purchaseHelper.billingSetup()

        binding.noticeBottomSheetGenerator.setContent {
            if (!hasSubs || !hasChance)
                ModalBottomSheet()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        purchaseHelper.consumeEnabled.asLiveData().observe(viewLifecycleOwner) {
            hasSubs = it
            Log.d("TAG_BILLING_TEST", "hasSubs: $hasSubs")
            if ((!hasSubs && !hasChance) || (!hasSubs && hasChance)) {
                binding.hasSubsRune.visibility = View.GONE
                binding.noSubsRune.visibility = View.VISIBLE
            } else {
                binding.hasSubsRune.visibility = View.VISIBLE
                binding.noSubsRune.visibility = View.GONE
            }
        }

        with(binding) {
            btnRandom.setOnClickListener {
                if (shimmerLayout.visibility == View.GONE)
                    randomRunes()
            }

            btnGenerate.setOnClickListener {
                if (shimmerLayout.visibility == View.GONE)
                    sentRunes()
            }

            noSubsRune.setOnClickListener {
                onBlockClick()
            }
            mAdapter.onClick = {
                if (!hasSubs)
                    showBottomSheet.value = !hasChance

                (hasSubs || hasChance)
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
        Log.d("TAG_SHIMMER", "onStartShimmering: Start Shimmering!")
        with(binding) {
            if (hasSubsRune.visibility == View.GONE) {
                runesRecyclerViewBlock.visibility = View.GONE
                shimmerLayoutBlock.visibility = View.VISIBLE
                shimmerLayoutBlock.startShimmer()
            } else {
                runesRecyclerView.visibility = View.GONE
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
            }
        }
    }

    private fun onStopShimmering() {
        Log.d("TAG_SHIMMER", "onStopShimmering: Stop Shimmering!")
        with(binding) {
            if (hasSubsRune.visibility == View.GONE) {
                shimmerLayoutBlock.stopShimmer()
                shimmerLayoutBlock.visibility = View.GONE
                runesRecyclerViewBlock.visibility = View.VISIBLE
            }
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
        val gridLayoutManagerBlock =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false)

        with(binding) {
            runesRecyclerView.layoutManager = gridLayoutManager
            runesRecyclerViewBlock.layoutManager = gridLayoutManagerBlock
            runesRecyclerView.adapter = mAdapter
            runesRecyclerViewBlock.adapter = mAdapter

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

        hasChance = false

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

        hasChance = false

        val direction = GeneratorStartFragmentDirections
            .actionGeneratorStartFragmentToGeneratorMagicRune()
        findNavController().navigate(direction)
    }

    private fun onBlockClick() {
        Log.d("TAG_SHIMMER", "Clicked:")
        if (!hasSubs || !hasChance)
            showBottomSheet.value = true
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ModalBottomSheet() {
        Log.d("TAG_SHIMMIT", "ModalBottomSheet: $showBottomSheet")
        val showBottomSheetRemember by remember {
            showBottomSheet
        }
        val coroutineScope = rememberCoroutineScope()

        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { it != ModalBottomSheetValue.Expanded }
        )

        NoticeBottomSheetGenerator(
            sheetState = bottomSheetState,
            coroutineScope = coroutineScope,
            fontSize = viewModel.fontSize.observeAsState().value!!,
            watchAD = {
                GoogleAdUtils(requireActivity()).apply {
                    onUserEarnedReward = {
                        binding.hasSubsRune.visibility = View.VISIBLE
                        binding.noSubsRune.visibility = View.GONE
                        showBottomSheet.value = false
                        hasChance = true
                    }
                    onAdFailedToLoad = {
                        Toast.makeText(requireContext(), "Something going wrong. Please try again!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            purchaseSubsBtnClicked = {
                val direction = GeneratorStartFragmentDirections.actionGeneratorStartFragmentToRunarSubs()
                findNavController().navigate(direction)
            }
        ) {
            showBottomSheet.value = false
            hideBottomSheet(bottomSheetState, coroutineScope)
        }

        LaunchedEffect(key1 = bottomSheetState.currentValue) {
            if (showBottomSheet.value && (bottomSheetState.currentValue == ModalBottomSheetValue.Hidden)) {
                showBottomSheet.value = false
            }
        }

        if (showBottomSheetRemember)
            showBottomSheet(bottomSheetState, coroutineScope)
        else
            hideBottomSheet(bottomSheetState, coroutineScope)
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun showBottomSheet(
        bottomSheetState: ModalBottomSheetState,
        coroutineScope: CoroutineScope
    ) = coroutineScope.launch {
        bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun hideBottomSheet(
        bottomSheetState: ModalBottomSheetState,
        coroutineScope: CoroutineScope
    ) = coroutineScope.launch {
        bottomSheetState.hide()
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
