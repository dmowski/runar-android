package com.tnco.runar.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentLayoutInterpretationFavBinding
import com.tnco.runar.ui.viewmodel.InterpretationFavViewModel
import com.tnco.runar.util.InterTagHandler
import com.tnco.runar.util.OnSwipeTouchListener
import com.tnco.runar.util.setOnCLickListenerForAll

class LayoutInterpretationFavFragment : Fragment(R.layout.fragment_layout_interpretation_fav),
    View.OnClickListener {

    private val viewModel: InterpretationFavViewModel by viewModels()
    private val args: LayoutInterpretationFavFragmentArgs by navArgs()

    private lateinit var bottomRunesNav: ConstraintLayout
    private lateinit var headerFrame: TextView
    private lateinit var runesLayout: ConstraintLayout

    private var runesViewList: ArrayList<FrameLayout> = arrayListOf()
    private var runesPositionsList: ArrayList<String?> = arrayListOf()
    private var runesDotsList: ArrayList<ImageView> = arrayListOf()

    private var runeHeight: Int = 0
    private var runeWidth: Int = 0
    private var fontSize: Float = 0f
    private var layoutId: Int = 0
    private var affirmId: Int = 0

    private var defaultConstraintSet = ConstraintSet()
    private var baseSize = 0
    private var screenHeight: Int = 0
    private var currentRunePosition = 0
    private var newUserLayout = arrayListOf<Int>()

    private var _binding: FragmentLayoutInterpretationFavBinding? = null
    private val binding
        get() = _binding!!

    private var readyToReturn = true
    private var backBlock = true

    private var affirmText = ""
    private var singleRuneAusp = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId = args.layoutId
        newUserLayout = args.userLayout.toCollection(ArrayList())
        affirmId = args.affirmId
        viewModel.setCurrentUserLayout(newUserLayout)
        viewModel.getLayoutDescription(layoutId)
        viewModel.getRuneDataFromDB()
        viewModel.getAffirmationsDataFromDB()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutInterpretationFavBinding.bind(view)

        //set necessary views
        headerFrame = binding.descriptionHeaderFrame
        runesLayout = binding.runesLayout
        bottomRunesNav = binding.bottomRunesNavBar
        //get data about current layout
        viewModel.fontSize.observe(viewLifecycleOwner) { interpretation ->
            if (interpretation != null) {

                fontSize = interpretation
                val headerTextSize = (interpretation * 3.0).toFloat()
                val runeNameTextSize = (interpretation * 1.2).toFloat()
                val littleTextSize = (interpretation * 0.75).toFloat()
                with(binding) {
                    descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, interpretation)
                    runeName.setTextSize(TypedValue.COMPLEX_UNIT_PX, runeNameTextSize)
                    runePosition.setTextSize(TypedValue.COMPLEX_UNIT_PX, littleTextSize)
                    runeAusf.setTextSize(TypedValue.COMPLEX_UNIT_PX, littleTextSize)
                    helperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, littleTextSize)
                    singleRuneName.setTextSize(TypedValue.COMPLEX_UNIT_PX, runeNameTextSize)
                }
                viewModel.selectedLayout.observe(viewLifecycleOwner) { selectedLayout ->
                    if (selectedLayout != null) {
                        runeHeight = runeHeightCalculator()
                        runeWidth = (runeHeight / 1.23).toInt()
                        binding.descriptionHeaderFrame.text = selectedLayout.layoutName

                        val firstRune = FrameLayout(requireContext())
                        val secondRune = FrameLayout(requireContext())
                        val thirdRune = FrameLayout(requireContext())
                        val fourthRune = FrameLayout(requireContext())
                        val fifthRune = FrameLayout(requireContext())
                        val sixthRune = FrameLayout(requireContext())
                        val seventhRune = FrameLayout(requireContext())
                        runesPositionsList.addAll(
                            arrayListOf(
                                selectedLayout.slotMeaning1,
                                selectedLayout.slotMeaning2,
                                selectedLayout.slotMeaning3,
                                selectedLayout.slotMeaning4,
                                selectedLayout.slotMeaning5,
                                selectedLayout.slotMeaning6,
                                selectedLayout.slotMeaning7
                            )
                        )
                        when (selectedLayout.layoutId) {
                            1 -> {
                                firstRune.id = View.generateViewId()
                                val ims = context?.assets?.open("runes/${newUserLayout[1]}.png")
                                val firstRuneImage = Drawable.createFromStream(ims, null)
                                firstRune.background = firstRuneImage
                                val firstRuneLayoutParams =
                                    ConstraintLayout.LayoutParams(runeWidth, runeHeight)
                                firstRune.layoutParams = firstRuneLayoutParams

                                this.runesLayout.addView(firstRune)
                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.applyTo(runesLayout)
                            }
                            2 -> {
                                runesViewList.addAll(arrayListOf(firstRune, secondRune))

                                runesInit(runesViewList, runesLayout)

                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    R.id.center_guideline,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.START,
                                    R.id.center_guideline,
                                    ConstraintSet.END,
                                    0
                                )
                                set.applyTo(runesLayout)
                                dotsCreator(2)
                            }
                            3 -> {
                                runesViewList.addAll(arrayListOf(thirdRune, secondRune, firstRune))

                                runesInit(runesViewList, runesLayout)

                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.BOTTOM,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    secondRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    secondRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.applyTo(runesLayout)
                                dotsCreator(3)
                            }
                            4 -> {
                                runesViewList.addAll(
                                    arrayListOf(fourthRune, secondRune, thirdRune, firstRune)
                                )

                                runesInit(runesViewList, runesLayout)

                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    R.id.center_guideline,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    firstRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.END,
                                    R.id.center_guideline,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    secondRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.END,
                                    secondRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.START,
                                    secondRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.BOTTOM,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.applyTo(runesLayout)
                                dotsCreator(4)
                            }
                            5 -> {
                                runesViewList.addAll(
                                    arrayListOf(fourthRune, secondRune, thirdRune, firstRune)
                                )

                                runesInit(runesViewList, runesLayout)

                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.TOP,
                                    firstRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    fourthRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    fourthRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.applyTo(runesLayout)
                                dotsCreator(4)
                            }
                            6 -> {
                                runesViewList.addAll(
                                    arrayListOf(
                                        fourthRune,
                                        secondRune,
                                        fifthRune,
                                        firstRune,
                                        thirdRune
                                    )
                                )

                                runesInit(runesViewList, runesLayout)

                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    firstRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.END,
                                    secondRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.BOTTOM,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.START,
                                    secondRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.BOTTOM,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.applyTo(runesLayout)
                                dotsCreator(5)
                            }
                            7 -> {
                                runesViewList.addAll(
                                    arrayListOf(
                                        sixthRune,
                                        thirdRune,
                                        fifthRune,
                                        fourthRune,
                                        secondRune,
                                        firstRune
                                    )
                                )

                                runesInit(runesViewList, runesLayout)

                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    firstRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.TOP,
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.END,
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.BOTTOM,
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.TOP,
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.connect(
                                    sixthRune.id,
                                    ConstraintSet.START,
                                    thirdRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    sixthRune.id,
                                    ConstraintSet.BOTTOM,
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    sixthRune.id,
                                    ConstraintSet.TOP,
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.applyTo(runesLayout)
                                dotsCreator(6)
                            }
                            8 -> {
                                runesViewList.addAll(
                                    arrayListOf(
                                        thirdRune,
                                        seventhRune,
                                        sixthRune,
                                        fifthRune,
                                        fourthRune,
                                        secondRune,
                                        firstRune
                                    )
                                )
                                runesInit(runesViewList, runesLayout)

                                val set = ConstraintSet()
                                set.clone(runesLayout)
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.END,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    firstRune.id,
                                    ConstraintSet.TOP,
                                    R.id.divider1,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.TOP,
                                    firstRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    secondRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    secondRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    thirdRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.TOP,
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    fourthRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.TOP,
                                    fourthRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.START,
                                    firstRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    fifthRune.id,
                                    ConstraintSet.END,
                                    firstRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    sixthRune.id,
                                    ConstraintSet.END,
                                    thirdRune.id,
                                    ConstraintSet.START,
                                    0
                                )
                                set.connect(
                                    sixthRune.id,
                                    ConstraintSet.BOTTOM,
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    sixthRune.id,
                                    ConstraintSet.TOP,
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.connect(
                                    seventhRune.id,
                                    ConstraintSet.START,
                                    thirdRune.id,
                                    ConstraintSet.END,
                                    0
                                )
                                set.connect(
                                    seventhRune.id,
                                    ConstraintSet.BOTTOM,
                                    thirdRune.id,
                                    ConstraintSet.BOTTOM,
                                    0
                                )
                                set.connect(
                                    seventhRune.id,
                                    ConstraintSet.TOP,
                                    thirdRune.id,
                                    ConstraintSet.TOP,
                                    0
                                )
                                set.applyTo(runesLayout)
                                dotsCreator(7)
                            }
                        }
                        //**runes description
                        //runes click listeners
                        for (i in 0 until this.runesLayout.childCount) {
                            this.runesLayout.getChildAt(i).setOnClickListener(this)
                        }
                        runesDotsList.setOnCLickListenerForAll(this)

                        //runes description**
                        viewModel.getLuckPercentForCurrentLayout()
                        viewModel.currentAusp.observe(viewLifecycleOwner) { ausp ->
                            if (ausp != null) {
                                val fortune = getLuckLevel(ausp)
                                binding.text.text =
                                    "${requireContext().resources.getString(R.string.layout_interpretation_ausf)}: $fortune"
                                singleRuneAusp = ausp
                                viewModel.getAffimForCurrentLayout(ausp)
                            }
                        }
                        viewModel.currentAffirm.observe(viewLifecycleOwner) { affirm ->
                            if (affirm.isNotBlank()) {
                                affirmText = "\n" + affirm
                                viewModel.getInterpretation()
                            }
                        }
                        viewModel.currentInterpretation.observe(viewLifecycleOwner) { interpretation ->
                            if (!interpretation.isNullOrBlank()) {
                                val newFontSize = (fontSize * 0.95).toFloat()
                                binding.interpretationText.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    newFontSize
                                )
                                binding.interpretationText.typeface = ResourcesCompat.getFont(
                                    requireContext(),
                                    R.font.roboto_light
                                )
                                binding.interpretationText.text = interpretation + affirmText
                                if (layoutId == 1) {
                                    viewModel.getSingleRuneData(newUserLayout[1])
                                    viewModel.singleRune.observe(viewLifecycleOwner) { name ->
                                        binding.singleRuneName.text = name
                                    }
                                }


                                val observer = binding.root.viewTreeObserver
                                defaultConstraintSet.clone(runesLayout)
                                observer.addOnGlobalLayoutListener(object :
                                    ViewTreeObserver.OnGlobalLayoutListener {
                                    override fun onGlobalLayout() {
                                        observer.removeOnGlobalLayoutListener(this)
                                        if (layoutId == 1) {
                                            binding.helperText.isVisible = false
                                            binding.divider0.isVisible = false
                                            val constraintsSet = ConstraintSet()
                                            constraintsSet.clone(binding.interpretationLayout)
                                            constraintsSet.clear(R.id.divider4, ConstraintSet.TOP)
                                            constraintsSet.clear(R.id.text, ConstraintSet.TOP)
                                            constraintsSet.connect(
                                                R.id.divider4,
                                                ConstraintSet.TOP,
                                                R.id.text,
                                                ConstraintSet.BOTTOM
                                            )
                                            constraintsSet.connect(
                                                R.id.text,
                                                ConstraintSet.TOP,
                                                R.id.single_rune_name,
                                                ConstraintSet.BOTTOM
                                            )

                                            constraintsSet.clear(R.id.text, ConstraintSet.END)
                                            constraintsSet.connect(
                                                R.id.text,
                                                ConstraintSet.END,
                                                R.id.right_text_guideline,
                                                ConstraintSet.END
                                            )
                                            with(binding) {
                                                text.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                                text.gravity = Gravity.CENTER
                                                text.setTextSize(
                                                    TypedValue.COMPLEX_UNIT_PX,
                                                    littleTextSize
                                                )
                                            }
                                            val secondFont = ResourcesCompat.getFont(
                                                requireContext(),
                                                R.font.roboto_medium
                                            )
                                            binding.text.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.interpretation_runes_position
                                                )
                                            )
                                            val fortune = getLuckLevel(singleRuneAusp)
                                            binding.text.text = Html.fromHtml(
                                                "${requireContext().resources.getString(R.string.layout_interpretation_ausf)}: $fortune",
                                                FROM_HTML_MODE_LEGACY,
                                                null,
                                                InterTagHandler(secondFont!!)
                                            )

                                            binding.text.isVisible = false
                                            constraintsSet.applyTo(binding.interpretationLayout)
                                        } else {
                                            binding.singleRuneName.isVisible = false
                                        }
                                        screenHeight = binding.root.height
                                        val minSize = screenHeight - binding.interFrame.top
                                        if (minSize > binding.interFrame.height) {
                                            val backLayoutParams =
                                                binding.interpretationLayout.layoutParams
                                            backLayoutParams.height =
                                                minSize - binding.divider3.height
                                            binding.interpretationLayout.layoutParams =
                                                backLayoutParams
                                        }
                                        baseSize = firstRune.bottom - binding.divider1.height
                                        binding.loadHelper.isVisible = false
                                    }
                                })
                            }
                        }
                    }
                }
                val listOfView = listOf(binding.leftArrow, binding.rightArrow, binding.exitButton)
                listOfView.setOnCLickListenerForAll(this)

                viewModel.selectedRune.observe(viewLifecycleOwner) {
                    if (it != null) {
                        with(binding) {
                            runeName.text = it.runeName
                            runeDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize - 3f)
                            runeDescription.text = "\n" + it.fullDescription
//                            val secondFont =
//                                ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
//                            runeAusf.text = Html.fromHtml(
//                                "${requireContext().resources.getString(R.string.layout_interpretation_ausf)} - <bf>${it.ausp} %</bf>",
//                                null,
//                                InterTagHandler(secondFont!!)
//                            )
                        }
                    }
                }
            }
        }

        val swipeListener = object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeRight() {
                if (currentRunePosition == 0) {
                    showDescriptionOfSelectedRune(runesViewList.last())
                } else showDescriptionOfSelectedRune(runesViewList[currentRunePosition - 1])
                super.onSwipeRight()
            }

            override fun onSwipeLeft() {
                if (currentRunePosition == runesViewList.size - 1) {
                    showDescriptionOfSelectedRune(runesViewList[0])
                } else showDescriptionOfSelectedRune(runesViewList[currentRunePosition + 1])
                super.onSwipeLeft()
            }

        }

        binding.exitButtonMain.setOnClickListener(this)
        binding.runeDescriptionScroll.setOnTouchListener(swipeListener)
        binding.runeDescriptionBack.setOnTouchListener(swipeListener)

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (readyToReturn) {
                        backBlock = false
                    } else {
                        hideRuneDescription()
                        readyToReturn = true
                        backBlock = true
                    }
                } else backBlock = false
            }
            backBlock
        }
    }

    /**
     * Percentage intervals are based on runes having luck between 0 and 100
     */
    private fun getLuckLevel(percent: Int): String {
        var luckLevel = ""
        when (percent) {
            in 0..20 -> luckLevel = requireContext().resources.getString(R.string.luck_next_time)
            in 21..40 -> luckLevel = requireContext().resources.getString(R.string.luck_is_close)
            in 41..69 -> luckLevel = requireContext().resources.getString(R.string.today_your_day)
            in 70..100 -> luckLevel = requireContext().resources.getString(R.string.lucky)
        }
        return luckLevel
    }

    override fun onClick(v: View?) {
        val runeIdList = arrayListOf<Int>()
        val runeDotsIdList = arrayListOf<Int>()
        for (rune in runesViewList) runeIdList.add(rune.id)
        for (runeDot in runesDotsList) runeDotsIdList.add(runeDot.id)
        when (v?.id) {
            R.id.exit_button_main -> {
                findNavController().popBackStack()
            }
            in runeIdList -> {
                showDescriptionOfSelectedRune(v)
            }
            in runeDotsIdList -> {
                showDescriptionOfSelectedRune(runesViewList[runeDotsIdList.indexOf(v?.id)])
            }
            R.id.left_arrow -> {
                if (currentRunePosition == 0) {
                    showDescriptionOfSelectedRune(runesViewList.last())
                } else showDescriptionOfSelectedRune(runesViewList[currentRunePosition - 1])
            }
            R.id.right_arrow -> {
                if (currentRunePosition == runesViewList.size - 1) {
                    showDescriptionOfSelectedRune(runesViewList[0])
                } else showDescriptionOfSelectedRune(runesViewList[currentRunePosition + 1])
            }
            R.id.exit_button -> {
                hideRuneDescription()
            }
        }
    }

    private fun hideRuneDescription() {
        readyToReturn = true
        defaultConstraintSet.applyTo(runesLayout)
        binding.descriptionHeaderBackground.visibility = View.GONE
        binding.interFrame.visibility = View.VISIBLE
        for (rune in runesViewList) {
            rune.foreground = ColorDrawable(Color.TRANSPARENT)
        }
        binding.runeDescriptionBack.isVisible = false
    }

    private fun showDescriptionOfSelectedRune(v: View?) {
        readyToReturn = false
        defaultConstraintSet.applyTo(runesLayout)
        if (runesViewList.size > 1) {
            binding.descriptionHeaderBackground.isVisible = true
            binding.interFrame.isVisible = false
            for (rune in runesViewList) {
                if (rune.id != v?.id) {
                    rune.foreground =
                        ContextCompat.getDrawable(requireContext(), R.drawable.rune_foreground)
                } else rune.foreground = ColorDrawable(Color.TRANSPARENT)
            }
            for (rune in runesViewList) {
                if (rune.id == v?.id) {
                    for (runeDot in runesDotsList) {
                        runeDot.setImageResource(R.drawable.ic_circle_deselected)
                        runeDot.setOnClickListener(this)
                    }
                    viewModel.getSelectedRuneData(runesViewList.indexOf(rune) + 1)
                    binding.runePosition.text = runesPositionsList[runesViewList.indexOf(rune)]
                    binding.runeDescriptionScroll.scrollTo(0, 0)
                    runesDotsList[runesViewList.indexOf(rune)].setImageResource(R.drawable.ic_circle_selected)
                    runesDotsList[runesViewList.indexOf(rune)].setOnClickListener(null)
                    currentRunePosition = runesViewList.indexOf(rune)

                    val constraintsSet = ConstraintSet()
                    constraintsSet.clone(runesLayout)
                    constraintsSet.connect(
                        R.id.rune_description_back,
                        ConstraintSet.TOP,
                        rune.id,
                        ConstraintSet.BOTTOM
                    )
                    constraintsSet.applyTo(runesLayout)
                }
            }
        }

        when (layoutId) {
            2 -> {
                val set = ConstraintSet()
                set.clone(runesLayout)
                set.clear(runesViewList[0].id, ConstraintSet.TOP)
                set.connect(
                    runesViewList[0].id,
                    ConstraintSet.TOP,
                    headerFrame.id,
                    ConstraintSet.BOTTOM
                )
                set.clear(runesViewList[1].id, ConstraintSet.TOP)
                set.connect(
                    runesViewList[1].id,
                    ConstraintSet.TOP,
                    headerFrame.id,
                    ConstraintSet.BOTTOM
                )
                set.applyTo(runesLayout)
            }

            3 -> {
                val set = ConstraintSet()
                set.clone(runesLayout)
                set.clear(runesViewList[1].id, ConstraintSet.TOP)
                set.connect(
                    runesViewList[1].id,
                    ConstraintSet.TOP,
                    headerFrame.id,
                    ConstraintSet.BOTTOM
                )
                set.applyTo(runesLayout)
            }

            4 -> {
                when (v?.id) {
                    runesViewList[3].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.TOP,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }

                    runesViewList[1].id, runesViewList[0].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[2].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id)
                        set.clear(runesViewList[1].id, ConstraintSet.BOTTOM)
                        set.clear(runesViewList[1].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[1].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                        runesViewList[3].isVisible = false
                    }
                }
            }
            5 -> {
                when (v?.id) {
                    runesViewList[3].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.TOP,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[1].id, runesViewList[2].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id, ConstraintSet.TOP)
                        set.clear(runesViewList[3].id, ConstraintSet.BOTTOM)
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.TOP,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[0].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                }
            }
            6 -> {
                when (v?.id) {
                    runesViewList[3].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.TOP,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[1].id, runesViewList[0].id, runesViewList[2].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[3].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[4].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[3].id)
                        set.clear(runesViewList[1].id, ConstraintSet.BOTTOM)
                        set.clear(runesViewList[1].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[1].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                        runesViewList[3].isVisible = false
                    }
                }
            }
            7 -> {
                when (v?.id) {
                    runesViewList[5].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[5].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[5].id,
                            ConstraintSet.TOP,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[4].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[5].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[5].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }

                    runesViewList[1].id, runesViewList[2].id, runesViewList[0].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[5].id)
                        set.clear(runesViewList[4].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[4].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                        runesViewList[5].isVisible = false
                    }
                    runesViewList[3].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        val removeList = arrayListOf(runesViewList[4], runesViewList[5])
                        viewRemover(removeList, set)
                        set.clear(runesViewList[1].id, ConstraintSet.BOTTOM)
                        set.clear(runesViewList[1].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[1].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                }
            }
            8 -> {
                when (v?.id) {
                    runesViewList[6].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[6].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[6].id,
                            ConstraintSet.TOP,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[5].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[6].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[6].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }

                    runesViewList[0].id, runesViewList[2].id, runesViewList[1].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        set.clear(runesViewList[6].id)
                        set.clear(runesViewList[5].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[5].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                        runesViewList[6].isVisible = false
                    }
                    runesViewList[4].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        val removeList = arrayListOf(runesViewList[6], runesViewList[5])
                        viewRemover(removeList, set)
                        set.clear(runesViewList[0].id, ConstraintSet.BOTTOM)
                        set.clear(runesViewList[0].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[0].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                    runesViewList[3].id -> {
                        val set = ConstraintSet()
                        set.clone(runesLayout)
                        val removeList = arrayListOf(
                            runesViewList[6],
                            runesViewList[5],
                            runesViewList[0],
                            runesViewList[1],
                            runesViewList[2]
                        )
                        viewRemover(removeList, set)
                        set.clear(runesViewList[4].id, ConstraintSet.BOTTOM)
                        set.clear(runesViewList[4].id, ConstraintSet.TOP)
                        set.connect(
                            runesViewList[4].id,
                            ConstraintSet.BOTTOM,
                            headerFrame.id,
                            ConstraintSet.BOTTOM
                        )
                        set.applyTo(runesLayout)
                    }
                }
            }
        }

        val descriptionBack = binding.runeDescriptionBack
        val backLayoutParams = descriptionBack.layoutParams
        backLayoutParams.height = screenHeight - baseSize
        descriptionBack.layoutParams = backLayoutParams
        descriptionBack.isVisible = true
    }

    private fun <T : View> viewRemover(list: ArrayList<T>, parentSet: ConstraintSet) {
        for (element in list) {
            parentSet.clear(element.id)
            element.isVisible = false
        }
    }


    private fun <T : View> viewIdGenerator(list: ArrayList<T>) {
        for (element in list) element.id = View.generateViewId()
    }

    private fun runesParametersSetter(list: ArrayList<FrameLayout>) {
        for (element in list) element.layoutParams =
            ConstraintLayout.LayoutParams(runeWidth, runeHeight)
    }

    private fun <T : View, S : ViewGroup> addViewHelper(list: ArrayList<T>, viewGroup: S) {
        for (element in list) viewGroup.addView(element)
    }

    private fun runesImgSetter(runes: ArrayList<FrameLayout>) {
        runes.forEachIndexed { index, element ->
            val i = context?.assets?.open("runes/${newUserLayout[index + 1]}.png")
            val runeImage = Drawable.createFromStream(i, null)
            element.background = runeImage
        }
    }

    private fun dotsInit(list: ArrayList<ImageView>, parent: ConstraintLayout) {
        for (dot in list) {
            dot.id = View.generateViewId()
            dot.adjustViewBounds = true
            dot.setImageResource(R.drawable.ic_circle_deselected)
            parent.addView(dot)
        }
    }

    private fun runesInit(runes: ArrayList<FrameLayout>, parent: ConstraintLayout) {
        viewIdGenerator(runes)
        runesImgSetter(runes)
        runesParametersSetter(runes)
        addViewHelper(runes, parent)
    }

    private fun runeHeightCalculator(): Int {
        val statusBarResource =
            requireContext().resources.getIdentifier("status_bar_height", "dimen", "android")
        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels
        val statusBarHeight = requireContext().resources.getDimensionPixelSize(statusBarResource)
        val fragmentHeight = screenHeight - statusBarHeight
        val headerHeight = screenWidth * 0.72 / 3.5
        val buttonHeight = screenWidth * 0.62 / 4.55
        val indentHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120f, displayMetrics)
        val calculatedFrameHeight = fragmentHeight - headerHeight - buttonHeight - indentHeight
        return (calculatedFrameHeight / 5).toInt()
    }

    private fun dotsCreator(n: Int) {
        val allDots = arrayListOf<ImageView>()
        for (i in 0..6) {
            allDots.add(ImageView(requireContext()))
        }
        when (n) {
            2 -> runesDotsList.addAll(arrayListOf(allDots[1], allDots[0]))
            3 -> runesDotsList.addAll(arrayListOf(allDots[1], allDots[0], allDots[2]))
            4 -> runesDotsList.addAll(arrayListOf(allDots[3], allDots[1], allDots[0], allDots[2]))
            5 -> runesDotsList.addAll(
                arrayListOf(
                    allDots[3],
                    allDots[1],
                    allDots[0],
                    allDots[2],
                    allDots[4]
                )
            )
            6 -> runesDotsList.addAll(
                arrayListOf(
                    allDots[5],
                    allDots[3],
                    allDots[1],
                    allDots[0],
                    allDots[2],
                    allDots[4]
                )
            )
            7 -> runesDotsList.addAll(
                arrayListOf(
                    allDots[5],
                    allDots[3],
                    allDots[1],
                    allDots[0],
                    allDots[2],
                    allDots[4],
                    allDots[6]
                )
            )
        }
        dotsInit(runesDotsList, bottomRunesNav)
        val bottomNavSet = ConstraintSet()
        bottomNavSet.clone(bottomRunesNav)
        bottomNavSet.connect(allDots[0].id, ConstraintSet.TOP, R.id.left_arrow, ConstraintSet.TOP)
        bottomNavSet.connect(
            allDots[0].id,
            ConstraintSet.BOTTOM,
            R.id.left_arrow,
            ConstraintSet.BOTTOM
        )
        bottomNavSet.connect(
            allDots[0].id,
            ConstraintSet.START,
            R.id.bottom_runes_nav_center,
            ConstraintSet.START
        )
        bottomNavSet.connect(
            allDots[0].id,
            ConstraintSet.END,
            R.id.bottom_runes_nav_center,
            ConstraintSet.END
        )
        bottomNavSet.connect(
            allDots[1].id,
            ConstraintSet.END,
            allDots[0].id,
            ConstraintSet.START,
            30
        )

        if (n % 2 == 0) {
            bottomNavSet.clear(allDots[0].id, ConstraintSet.END)
        }
        if (n > 2) {
            bottomNavSet.connect(
                allDots[2].id,
                ConstraintSet.START,
                allDots[0].id,
                ConstraintSet.END,
                30
            )
        }
        if (n > 3) {
            bottomNavSet.connect(
                allDots[3].id,
                ConstraintSet.END,
                allDots[1].id,
                ConstraintSet.START,
                30
            )
        }
        if (n > 4) {
            bottomNavSet.connect(
                allDots[4].id,
                ConstraintSet.START,
                allDots[2].id,
                ConstraintSet.END,
                30
            )
        }
        if (n > 5) {
            bottomNavSet.connect(
                allDots[5].id,
                ConstraintSet.END,
                allDots[3].id,
                ConstraintSet.START,
                30
            )
        }
        if (n > 6) {
            bottomNavSet.connect(
                allDots[6].id,
                ConstraintSet.START,
                allDots[4].id,
                ConstraintSet.END,
                30
            )
        }
        for (i in 1 until n) {
            bottomNavSet.connect(allDots[i].id, ConstraintSet.TOP, allDots[0].id, ConstraintSet.TOP)
            bottomNavSet.connect(
                allDots[i].id,
                ConstraintSet.BOTTOM,
                allDots[0].id,
                ConstraintSet.BOTTOM
            )
        }
        bottomNavSet.applyTo(bottomRunesNav)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}