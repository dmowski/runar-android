package com.test.runar.ui.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.databinding.FragmentLayoutInitBinding
import com.test.runar.extensions.setOnCLickListenerForAll
import com.test.runar.presentation.viewmodel.InitViewModel
import com.test.runar.ui.Navigator
import com.test.runar.ui.dialogs.DescriptionDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class LayoutInitFragment : Fragment(R.layout.fragment_layout_init), View.OnClickListener {

    private val viewModel: InitViewModel by viewModels()
    private lateinit var headerText: String
    private lateinit var descriptionText: String
    private lateinit var layoutFrame: ConstraintLayout
    private var fontSize: Float = 0f
    private var runeTable: Array<Array<Int>> = Array(7) { Array(2) { 0 } }
    private var runesList: Array<Array<Int>> = Array(25) { Array(2) { 0 } }
    private var layoutTable: Array<Int> = Array(9) { 0 }
    private var layoutId: Int = 0
    private var threadCounter = 0

    private var navigator: Navigator? = null

    private var _binding: FragmentLayoutInitBinding? = null
    private val binding
        get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId = requireArguments().getInt(KEY_ID)
        viewModel.getLayoutDescription(layoutId)
        runesArrayInit()
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutInitBinding.bind(view)

        val listOfView = listOf(binding.descriptionButtonFrame, binding.exitButton, binding.infoButton)
        listOfView.setOnCLickListenerForAll(this)

        viewModel.fontSize.observe(viewLifecycleOwner) {textSize->
            fontSize = textSize
            val headerTextSize = (textSize*3).toFloat()
            val buttonTextSize = (textSize*1.65).toFloat()
            binding.descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
            binding.descriptionButtonFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize)
        }

        viewModel.selectedLayout.observe(viewLifecycleOwner) {
            when(it.layoutId){
                1,2,3,4-> layoutTable[0] = it.layoutId!!
                else-> layoutTable[0] = it.layoutId!!-1
            }
            binding.descriptionHeaderFrame.text = it.layoutName
            headerText = it.layoutName.toString()
            descriptionText = it.layoutDescription.toString()
            layoutFrame = binding.layoutFrame
            binding.firstRuneNumber.text = it.slot1.toString()
            binding.secondRuneNumber.text = it.slot2.toString()
            binding.thirdRuneNumber.text = it.slot3.toString()
            binding.fourthRuneNumber.text = it.slot4.toString()
            binding.fifthRuneNumber.text = it.slot5.toString()
            binding.sixthRuneNumber.text = it.slot6.toString()
            binding.seventhRuneNumber.text = it.slot7.toString()
            for (i in 0..6) {
                val currentNumber = (((layoutFrame.getChildAt(i) as ConstraintLayout).getChildAt(0) as TextView).text as String).toInt()
                if (currentNumber == 0) {
                    (layoutFrame.getChildAt(i) as ConstraintLayout).visibility = View.INVISIBLE
                }
                runeTable[i][0] = currentNumber
            }
            when (it.layoutId) {
                2, 4 -> {
                    val constraintsSet = ConstraintSet()
                    constraintsSet.clone(layoutFrame)
                    constraintsSet.clear(R.id.third_rune, ConstraintSet.START)
                    constraintsSet.clear(R.id.seventh_rune, ConstraintSet.START)
                    constraintsSet.connect(R.id.third_rune, ConstraintSet.END, R.id.center_guideline, ConstraintSet.END, 0)
                    constraintsSet.connect(R.id.seventh_rune, ConstraintSet.START, R.id.center_guideline, ConstraintSet.END, 0)
                    constraintsSet.applyTo(layoutFrame)
                }
                5 -> {
                    val constraintsSet = ConstraintSet()
                    constraintsSet.clone(layoutFrame)
                    constraintsSet.clear(R.id.first_rune, ConstraintSet.TOP)
                    constraintsSet.clear(R.id.fourth_rune, ConstraintSet.BOTTOM)
                    constraintsSet.clear(R.id.fifth_rune, ConstraintSet.BOTTOM)
                    constraintsSet.clear(R.id.seventh_rune, ConstraintSet.TOP)
                    constraintsSet.clear(R.id.seventh_rune, ConstraintSet.BOTTOM)
                    constraintsSet.clear(R.id.sixth_layout, ConstraintSet.TOP)
                    constraintsSet.clear(R.id.sixth_layout, ConstraintSet.BOTTOM)
                    constraintsSet.connect(R.id.first_rune, ConstraintSet.TOP, R.id.support_top, ConstraintSet.BOTTOM, 0)
                    constraintsSet.connect(R.id.fourth_rune, ConstraintSet.BOTTOM, R.id.support_bottom, ConstraintSet.TOP, 0)
                    constraintsSet.connect(R.id.seventh_rune, ConstraintSet.TOP, R.id.support_big_top, ConstraintSet.BOTTOM, 0)
                    constraintsSet.connect(R.id.seventh_rune, ConstraintSet.BOTTOM, R.id.support_big_bottom, ConstraintSet.TOP, 0)
                    constraintsSet.connect(R.id.sixth_rune, ConstraintSet.TOP, R.id.seventh_rune, ConstraintSet.TOP, 0)
                    constraintsSet.connect(R.id.sixth_rune, ConstraintSet.BOTTOM, R.id.seventh_rune, ConstraintSet.BOTTOM, 0)
                    constraintsSet.applyTo(layoutFrame)
                }
                7 -> {
                    val constraintsSet = ConstraintSet()
                    constraintsSet.clone(layoutFrame)
                    constraintsSet.clear(R.id.first_rune, ConstraintSet.TOP)
                    constraintsSet.clear(R.id.fourth_rune, ConstraintSet.BOTTOM)
                    constraintsSet.clear(R.id.fifth_rune, ConstraintSet.BOTTOM)
                    constraintsSet.connect(R.id.first_rune, ConstraintSet.TOP, R.id.support_top, ConstraintSet.BOTTOM, 0)
                    constraintsSet.connect(R.id.fourth_rune, ConstraintSet.BOTTOM, R.id.support_bottom, ConstraintSet.TOP, 0)
                    constraintsSet.applyTo(layoutFrame)
                }
            }
            firstSlotOpener()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.exit_button -> {
                navigator?.showDialog()
            }
            R.id.description_button_frame -> {
                val result = slotChanger()
                if (result[1]) {
                    binding.infoButton.isVisible = false
                    binding.descriptionButtonFrame.text = getString(R.string.layout_init_button_text2)
                } else if (!result[0]) {
                    val userLayout = layoutTable.toIntArray()
                    navigator?.navigateToLayoutProcessingFragment(layoutId, userLayout)
                }

            }
            R.id.info_button -> {
                val info = DescriptionDialog(descriptionText, headerText, fontSize)
                info.showDialog(requireActivity())
            }
        }
    }

    private fun slotChanger(): Array<Boolean> {
        var result = false
        var isLast = false //is exist slots to open
        for (i in 0..6) {
            if (runeTable[i][1] == 1) {

                val slot = layoutFrame.getChildAt(i) as ConstraintLayout

                slot.setOnClickListener(null)
                slot.setBackgroundResource(R.drawable.slot_active_big)
                context?.let { (slot.getChildAt(0) as TextView).setTextColor(it.getColor(R.color.rune_number_color_selected)) }

                //open it with animation
                runeTable[i][0] = 0
                runeTable[i][1] = 0
                var minSlot = 10
                var minValue = 10
                for (i in 0..6) {
                    if (runeTable[i][0] < minValue && runeTable[i][0] != 0) {
                        minSlot = i
                        minValue = runeTable[i][0]
                    }
                }
                var activeSlot: ConstraintLayout? = null
                if (minSlot != 10) {
                    runeTable[minSlot][1] = 1
                    activeSlot = layoutFrame.getChildAt(minSlot) as ConstraintLayout

                    activeSlot.setOnClickListener {
                        val result = slotChanger()
                        if (result[1]) {
                            binding.descriptionButtonFrame.text = requireContext().resources.getString(R.string.layout_init_button_text2)
                            binding.infoButton.isVisible = false
                        }
                    }
                } else isLast = true
                result = true
                runeSetter(slot, activeSlot, minValue)
                return arrayOf(result, isLast)
            }
        }
        result = false
        return arrayOf(result, isLast)
    }

    private fun firstSlotOpener() {
        var minElement = 10
        var minValue = 10
        for (i in 0..6) {
            if (minValue > runeTable[i][0] && runeTable[i][0] != 0) {
                minValue = runeTable[i][0]
                minElement = i
            }
        }

        if (minValue != 10) {
            val slot = layoutFrame.getChildAt(minElement) as ConstraintLayout
            lifecycleScope.launch {
                runeTable[minElement][1] = 1
                slot.setBackgroundResource(R.drawable.slot_active)
                context?.let { (slot.getChildAt(0) as TextView).setTextColor(it.getColor(R.color.rune_number_color_selected)) }
            }
            slot.setOnClickListener {
                val result = slotChanger()
                if (result[1]) {
                    binding.descriptionButtonFrame.text = requireContext().resources.getString(R.string.layout_init_button_text2)
                    binding.infoButton.isVisible = false
                }
            }
        }
    }

    private fun runeSetter(slot: ConstraintLayout, activeSlot: ConstraintLayout?, childNumber: Int) {
        lifecycleScope.launch {
            threadCounter++
            blockButton(false)
            delay(500L)
            val runeId = getUniqueRune()
            val ims = context?.assets?.open("runes/${runeId}.png")
            val runeImage = Drawable.createFromStream(ims, null)
            slot.setBackgroundDrawable(runeImage)
            (slot.getChildAt(0) as TextView).visibility = View.INVISIBLE
            if (activeSlot != null) {
                activeSlot.setBackgroundResource(R.drawable.slot_active)
                context?.let { (activeSlot.getChildAt(0) as TextView).setTextColor(it.getColor(R.color.rune_number_color_selected)) }
            }
            if(childNumber==10) layoutTable[layoutTable[0]] = runeId
            else layoutTable[childNumber-1] = runeId
            blockButton(true)
            threadCounter--
        }
    }

    private fun blockButton(state: Boolean) {
        binding.descriptionButtonFrame.isClickable = state
    }

    private fun runesArrayInit() {
        runesList[0] = arrayOf(1, 2)
        runesList[1] = arrayOf(3, 4)
        runesList[2] = arrayOf(5, 6)
        runesList[3] = arrayOf(7, 8)
        runesList[4] = arrayOf(9, 10)
        runesList[5] = arrayOf(11, 12)
        runesList[6] = arrayOf(13, 0)
        runesList[7] = arrayOf(14, 15)
        runesList[8] = arrayOf(16, 0)
        runesList[9] = arrayOf(17, 18)
        runesList[10] = arrayOf(19, 0)
        runesList[11] = arrayOf(20, 0)
        runesList[12] = arrayOf(21, 0)
        runesList[13] = arrayOf(22, 23)
        runesList[14] = arrayOf(24, 25)
        runesList[15] = arrayOf(26, 0)
        runesList[16] = arrayOf(27, 28)
        runesList[17] = arrayOf(29, 30)
        runesList[18] = arrayOf(31, 32)
        runesList[19] = arrayOf(33, 34)
        runesList[20] = arrayOf(35, 36)
        runesList[21] = arrayOf(37, 0)
        runesList[22] = arrayOf(38, 0)
        runesList[23] = arrayOf(39, 40)
        runesList[24] = arrayOf(41, 0)
    }

    private fun getUniqueRune(): Int {
        while (true) {
            val randomNumber = Random.nextInt(1, 42)
            if (layoutId == 2) {
                for (i in 0..24) {
                    if (runesList[i][0] == randomNumber) {
                        runesList[i] = arrayOf(0, 0)
                        return randomNumber
                    }
                }
            } else {
                for (i in 0..24) {
                    for (i2 in 0..1) {
                        if (runesList[i][i2] == randomNumber) {
                            runesList[i] = arrayOf(0, 0)
                            return randomNumber
                        }
                    }
                }
            }
            continue
        }
    }

    companion object {
        private const val KEY_ID = "KEY_ID"

        fun newInstance(id: Int): LayoutInitFragment {
            return LayoutInitFragment().apply { arguments = bundleOf(KEY_ID to id) }
        }
    }
}