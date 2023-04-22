package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentLayoutsBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.model.RunicDrawsAccessModel
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.viewmodel.LayoutViewModel
import com.tnco.runar.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class LayoutFragment : Fragment(R.layout.fragment_layouts), View.OnClickListener, HasVisibleNavBar {

    private val viewModel: LayoutViewModel by viewModels()

    private lateinit var purchaseHelper: PurchaseHelper

    private var _binding: FragmentLayoutsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLayoutsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.upperBanner.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            val bannerOnClick: () -> Unit = {
                val bottomNavigationBar =
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
                val menuItem = bottomNavigationBar.menu.findItem(R.id.generator)
                NavigationUI.onNavDestinationSelected(menuItem, findNavController())
            }
            setContent {
                UpperBanner(onClick = bannerOnClick)
            }
        }
        purchaseHelper = PurchaseHelper(requireActivity())
        purchaseHelper.reloadPurchase()

        showLimitsOnLayouts()
        return view
    }

    private fun showLimitsOnLayouts() = binding.apply {
        val count = mutableStateOf(SharedPreferencesRepository(requireContext()).runicLayoutsLimit)
        val time = mutableStateOf("")
        val hasSubs = purchaseHelper.consumeEnabled

        object : CounterUtil(requireContext()) {
            override fun onTimerTick(timeUntilFinished: String) {
                Log.d("TAG_LIMIT", "onTimerTick: $timeUntilFinished")
                time.value = timeUntilFinished
            }

            override fun onTimerFinish() {
                // TODO: Remove Limit on Layouts and Get User another 3 chances
            }
        }.startOrRefreshCounting()

        listOf(firstLayoutAccessCard, secondLayoutAccessCard, thirdLayoutAccessCard).forEach {
            it.setContent {
                AccessCard(accessType = RunicDrawsAccessModel.Free, hasSubs = hasSubs)
            }
        }

        val accessType = if (count.value > 0) {
            setForeground(color = null)
            RunicDrawsAccessModel.OpenWithLimit(count = count)
        } else {
            setForeground(color = R.color.close_layout_foreground)
            RunicDrawsAccessModel.ClosedForAWhile(time = time)
        }

        listOf(
            fourthLayoutAccessCard,
            fifthLayoutAccessCard,
            sixthLayoutAccessCard,
            seventhLayoutAccessCard,
            eightLayoutAccessCard
        ).forEach {
            it.setContent {
                AccessCard(accessType = accessType, hasSubs = hasSubs)
            }
        }
    }

    private fun setForeground(color: Int?) {
        binding.apply {
            val listOfView = listOf(
                fourthLayout,
                fifthLayout,
                sixthLayout,
                seventhLayout,
                eightLayout
            )

            listOfView.forEach {
                if (color != null) {
                    it.isEnabled = false
                    it.foreground = ResourcesCompat.getDrawable(resources, color, null)
                } else {
                    it.isEnabled = true
                    it.foreground = null
                }
            }
        }
    }

    @Composable
    fun AccessCard(accessType: RunicDrawsAccessModel, hasSubs: StateFlow<Boolean>) {
        val hasSubsState = hasSubs.collectAsState()
        val hasSubsRemember by remember {
            hasSubsState
        }

        if (!hasSubsRemember) {
            val image: Painter
            val counter: String
            val textColor: Color

            when (accessType) {
                is RunicDrawsAccessModel.Free -> {
                    image = painterResource(id = R.drawable.yellow_bag)
                    counter = "∞"
                    textColor = colorResource(id = R.color.run_draws_open_text_color)
                }
                is RunicDrawsAccessModel.Closed -> {
                    image = painterResource(id = R.drawable.lock_icon)
                    counter = ""
                    textColor = colorResource(id = R.color.run_draws_open_text_color)
                }
                is RunicDrawsAccessModel.OpenWithLimit -> {
                    image = painterResource(id = R.drawable.yellow_bag)
                    counter = accessType.count.value.toString()
                    textColor = colorResource(id = R.color.run_draws_open_text_color)
                }
                is RunicDrawsAccessModel.ClosedForAWhile -> {
                    image = painterResource(id = R.drawable.lock_with_time)
                    counter = accessType.time.value
                    textColor = colorResource(id = R.color.run_draws_time_color)
                }
            }

            Card(
                shape = RoundedCornerShape(topEnd = 8.dp, bottomStart = 8.dp),
                backgroundColor = colorResource(
                    id = R.color.shadow
                ),
                modifier = Modifier.wrapContentSize()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = image,
                        contentDescription = ""
                    )
                    if (counter.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = counter,
                            color = textColor,
                            fontFamily = FontFamily(Font(resId = R.font.roboto_regular)),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.SCREEN_VIEW_RUNIC_DRAWS)
        setClickListenerOnRuneLayouts()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setClickListenerOnRuneLayouts() {
        with(binding) {
            val listOfView = listOf(
                firstLayout,
                secondLayout,
                thirdLayout,
                fourthLayout,
                fifthLayout,
                sixthLayout,
                seventhLayout,
                eightLayout
            )
            listOfView.setOnCLickListenerForAll(this@LayoutFragment)
        }
    }

    override fun onClick(v: View?) {
        val idOfRune = when (v?.id) {
            R.id.first_layout -> 1
            R.id.second_layout -> 2
            R.id.third_layout -> 3
            R.id.fourth_layout -> 4
            R.id.fifth_layout -> 5
            R.id.sixth_layout -> 6
            R.id.seventh_layout -> 7
            else -> 8
        }
        viewModel.checkDescriptionStatus(idOfRune)
        val layoutName = AnalyticsUtils.convertLayoutIdToName(idOfRune)
        viewModel.analyticsHelper.sendEvent(
            AnalyticsEvent.DRAWS_SELECTED,
            Pair(AnalyticsConstants.DRAW_RUNE_LAYOUT, layoutName)
        )
        viewModel.showStatus.observe(viewLifecycleOwner) { needShowDescription ->
            if (needShowDescription) {
                val direction = LayoutFragmentDirections
                    .actionLayoutFragmentToLayoutDescriptionFragment(idOfRune)
                findNavController().navigate(direction)
            } else {
                val direction = LayoutFragmentDirections
                    .actionLayoutFragmentToLayoutInitFragment(idOfRune)
                findNavController().navigate(direction)
            }
        }
    }
}

@Preview
@Composable
private fun UpperBanner(onClick: () -> Unit = {}) {
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3.28f)
                    .border(
                        border = BorderStroke(1.dp, colorResource(id = R.color.border)),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .rectShadow(
                        cornersRadius = 8.dp,
                        shadowBlurRadius = 8.dp,
                        offsetX = 8.dp,
                        offsetY = 8.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sacr_back_button),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.run_patterns)
                )
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onClick)
                ) {
                    val (image, title, description) = createRefs()
                    val topDescriptionHorizontalGuideline = createGuidelineFromTop(0.5f)
                    val bottomTitleHorizontalGuideline = createGuidelineFromTop(0.42f)
                    Image(
                        painter = painterResource(id = R.drawable.rune_pattern),
                        contentDescription = stringResource(id = R.string.run_patterns),
                        modifier = Modifier
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(start = 12.dp, top = 6.dp, bottom = 14.dp, end = 6.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.run_patterns),
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                        color = colorResource(R.color.neutrals_gray_100),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .constrainAs(title) {
                                width = Dimension.fillToConstraints
                                bottom.linkTo(bottomTitleHorizontalGuideline)
                                start.linkTo(image.end)
                                end.linkTo(parent.end)
                            }
                    )
                    Text(
                        text = stringResource(id = R.string.layout_upper_banner_description),
                        fontFamily = FontFamily(Font(resId = R.font.roboto_regular)),
                        color = colorResource(R.color.neutrals_gray_50),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(end = 28.dp)
                            .constrainAs(description) {
                                width = Dimension.fillToConstraints
                                top.linkTo(topDescriptionHorizontalGuideline)
                                start.linkTo(image.end)
                                end.linkTo(parent.end)
                            }
                    )
                }
            }
            Spacer(Modifier.height(38.dp))
            Text(
                text = stringResource(id = R.string.rune_layouts),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                color = colorResource(R.color.onboard_header_color),
                fontSize = 36.sp,
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun RuneAccessCardPreview(
    @PreviewParameter(PreviewParams::class)
    accessType: RunicDrawsAccessModel
) {
    MaterialTheme {
        LayoutFragment().AccessCard(accessType = accessType, hasSubs = MutableStateFlow(true))
    }
}

class PreviewParams : PreviewParameterProvider<RunicDrawsAccessModel> {
    override val values: Sequence<RunicDrawsAccessModel>
        get() = sequenceOf(
            RunicDrawsAccessModel.Free,
            RunicDrawsAccessModel.Closed,
            RunicDrawsAccessModel.OpenWithLimit(count = mutableStateOf(3)),
            RunicDrawsAccessModel.ClosedForAWhile(time = mutableStateOf("24:18:32"))
        )
}
