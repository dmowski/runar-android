package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.FragmentLayoutsBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.viewmodel.LayoutViewModel
import com.tnco.runar.util.AnalyticsConstants
import com.tnco.runar.util.AnalyticsUtils
import com.tnco.runar.util.setOnCLickListenerForAll

class LayoutFragment : Fragment(R.layout.fragment_layouts), View.OnClickListener {

    private val viewModel: LayoutViewModel by viewModels()

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
            val bannerOnClick = {
                findNavController().navigate(
                    R.id.generator,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(findNavController().graph.startDestinationId, true)
                        .build()
                )
            }
            setContent {
                UpperBanner(onClick = bannerOnClick)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.sendEvent(AnalyticsEvent.SCREEN_VIEW_RUNIC_DRAWS)
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
        AnalyticsHelper.sendEvent(
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
            ConstraintLayout(
                modifier = Modifier
                    .paint(
                        painter = painterResource(id = R.drawable.layouts_upper_banner),
                        contentScale = ContentScale.FillBounds
                    )
                    .fillMaxWidth()
                    .aspectRatio(3.28f)
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
            Text(
                text = stringResource(id = R.string.rune_layouts),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                color = colorResource(R.color.onboard_header_color),
                fontSize = 36.sp,
                modifier = Modifier
                    .padding(top = 38.dp, bottom = 24.dp)
            )
        }
    }
}
