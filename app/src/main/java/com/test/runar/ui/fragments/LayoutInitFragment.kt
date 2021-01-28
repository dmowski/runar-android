package com.test.runar.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel
import com.test.runar.ui.dialogs.CancelDialog
import com.test.runar.ui.dialogs.DescriptionDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LayoutInitFragment : Fragment(R.layout.fragment_layout_init),
    View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var header: TextView
    private lateinit var buttonText: TextView
    private lateinit var headerText: String
    private lateinit var descriptionText: String
    private lateinit var layoutFrame : ConstraintLayout
    private var fontSize: Float = 0f
    private var runeTable : Array<Array<Int>> = Array(7) { Array(2) { 0 } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fontSize = arguments?.getFloat("descriptionFontSize")!!

        view.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.exit_button).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.info_button).setOnClickListener(this)
        header =
            view.findViewById<FrameLayout>(R.id.description_header_frame).getChildAt(0) as TextView
        buttonText = view.findViewById<FrameLayout>(R.id.description_button_frame).getChildAt(0) as TextView
        model.selectedLayout.observe(viewLifecycleOwner) {
            if (it != null) {
                header.text = it.layoutName
                headerText = it.layoutName.toString()
                descriptionText = it.layoutDescription.toString()
                layoutFrame = view.findViewById<ConstraintLayout>(R.id.layoutFrame)
                ((layoutFrame.getChildAt(0) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot1.toString()
                ((layoutFrame.getChildAt(1) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot2.toString()
                ((layoutFrame.getChildAt(2) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot3.toString()
                ((layoutFrame.getChildAt(3) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot4.toString()
                ((layoutFrame.getChildAt(4) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot5.toString()
                ((layoutFrame.getChildAt(5) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot6.toString()
                ((layoutFrame.getChildAt(6) as ConstraintLayout).getChildAt(0) as TextView).text = it.slot7.toString()
                for(i in 0..6){
                    var currentNumber = (((layoutFrame.getChildAt(i) as ConstraintLayout).getChildAt(0) as TextView).text as String).toInt()
                    if(currentNumber==0){
                        (layoutFrame.getChildAt(i) as ConstraintLayout).visibility=View.INVISIBLE
                    }
                    runeTable[i][0] = currentNumber
                }
                when(it.layoutId){
                    2,4->{
                        val constraintsSet = ConstraintSet()
                        constraintsSet.clone(layoutFrame)
                        constraintsSet.clear(R.id.third_rune,ConstraintSet.START)
                        constraintsSet.clear(R.id.seventh_rune,ConstraintSet.START)
                        constraintsSet.connect(R.id.third_rune,ConstraintSet.END,R.id.center_guideline,ConstraintSet.END,0)
                        constraintsSet.connect(R.id.seventh_rune,ConstraintSet.START,R.id.center_guideline,ConstraintSet.END,0)
                        constraintsSet.applyTo(layoutFrame)
                    }
                    5->{
                        val constraintsSet = ConstraintSet()
                        constraintsSet.clone(layoutFrame)
                        constraintsSet.clear(R.id.first_rune,ConstraintSet.TOP)
                        constraintsSet.clear(R.id.fourth_rune,ConstraintSet.BOTTOM)
                        constraintsSet.clear(R.id.fifth_rune,ConstraintSet.BOTTOM)
                        constraintsSet.clear(R.id.seventh_rune,ConstraintSet.TOP)
                        constraintsSet.clear(R.id.seventh_rune,ConstraintSet.BOTTOM)
                        constraintsSet.clear(R.id.sixth_layout,ConstraintSet.TOP)
                        constraintsSet.clear(R.id.sixth_layout,ConstraintSet.BOTTOM)
                        constraintsSet.connect(R.id.first_rune,ConstraintSet.TOP,R.id.support_top,ConstraintSet.BOTTOM,0)
                        constraintsSet.connect(R.id.fourth_rune,ConstraintSet.BOTTOM,R.id.support_bottom,ConstraintSet.TOP,0)
                        constraintsSet.connect(R.id.seventh_rune,ConstraintSet.TOP,R.id.support_big_top,ConstraintSet.BOTTOM,0)
                        constraintsSet.connect(R.id.seventh_rune,ConstraintSet.BOTTOM,R.id.support_big_bottom,ConstraintSet.TOP,0)
                        constraintsSet.connect(R.id.sixth_rune,ConstraintSet.TOP,R.id.seventh_rune,ConstraintSet.TOP,0)
                        constraintsSet.connect(R.id.sixth_rune,ConstraintSet.BOTTOM,R.id.seventh_rune,ConstraintSet.BOTTOM,0)
                        constraintsSet.applyTo(layoutFrame)
                    }
                    7->{
                        val constraintsSet = ConstraintSet()
                        constraintsSet.clone(layoutFrame)
                        constraintsSet.clear(R.id.first_rune,ConstraintSet.TOP)
                        constraintsSet.clear(R.id.fourth_rune,ConstraintSet.BOTTOM)
                        constraintsSet.clear(R.id.fifth_rune,ConstraintSet.BOTTOM)
                        constraintsSet.connect(R.id.first_rune,ConstraintSet.TOP,R.id.support_top,ConstraintSet.BOTTOM,0)
                        constraintsSet.connect(R.id.fourth_rune,ConstraintSet.BOTTOM,R.id.support_bottom,ConstraintSet.TOP,0)
                        constraintsSet.applyTo(layoutFrame)
                    }
                }
                Log.d("Log",runeTable.joinToString())
                firstSlotOpener()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        model.clearLayoutData()
    }

    override fun onClick(v: View?) {

        val navController = findNavController()
        when (v?.id) {
            R.id.exit_button -> {
                activity?.let { CancelDialog(navController, it) }?.showDialog()
            }
            R.id.description_button_frame -> {
                var result = slotChanger()
                if(result[1]){
                    buttonText.text = "Толковать"
                }
                if(!result[0]) navController.navigate(R.id.emptyFragment)
            }
            R.id.info_button -> {
                val info = DescriptionDialog(descriptionText, headerText, fontSize)
                activity?.let { info.showDialog(it) }
            }
        }
    }

    private fun slotChanger(): Array<Boolean>{
        var result = false
        var isLast = false
        for(i in 0..6){
            if(runeTable[i][1]==1){

                val slot = layoutFrame.getChildAt(i) as ConstraintLayout

                slot.setOnClickListener(null)
                slot.setBackgroundResource(R.drawable.slot_active_big)
                context?.let { (slot.getChildAt(0) as TextView).setTextColor(it.getColor(R.color.rune_number_color_selected)) }

                runeSetter(slot)

                //open it with animation
                runeTable[i][0]=0
                runeTable[i][1]=0
                var minSlot =10
                var minValue =10
                for(i in 0..6){
                    if(runeTable[i][0]<minValue&&runeTable[i][0]!=0){
                        minSlot = i
                        minValue=runeTable[i][0]
                    }
                }
                if(minSlot!=10){
                    runeTable[minSlot][1]=1
                    val slot = layoutFrame.getChildAt(minSlot) as ConstraintLayout
                    slot.setBackgroundResource(R.drawable.slot_active)
                    context?.let { (slot.getChildAt(0) as TextView).setTextColor(it.getColor(R.color.rune_number_color_selected)) }
                    slot.setOnClickListener {
                        var result = slotChanger()
                        if(result[1]){
                            buttonText.text = "Толковать"
                        }
                    }
                }
                else isLast = true
                result = true
                return arrayOf(result,isLast)
            }
        }
        result = false
        return arrayOf(result,isLast)
    }

    private fun firstSlotOpener(){
        var minElement = 10
        var minValue =10
        for(i in 0..6){
            if(minValue>runeTable[i][0]&&runeTable[i][0]!=0){
                minValue=runeTable[i][0]
                minElement=i
            }
        }

        if(minValue !=10){
            runeTable[minElement][1] = 1
            val slot = layoutFrame.getChildAt(minElement) as ConstraintLayout
            slot.setBackgroundResource(R.drawable.slot_active)
            context?.let { (slot.getChildAt(0) as TextView).setTextColor(it.getColor(R.color.rune_number_color_selected)) }
            slot.setOnClickListener {
                var result = slotChanger()
                if(result[1]){
                    buttonText.text = "Толковать"
                }
            }
        }
    }

    private fun runeSetter(slot: ConstraintLayout){
        lifecycleScope.launch {
            delay(500L)
            slot.setBackgroundResource(R.drawable.rune_sample)
            (slot.getChildAt(0) as TextView).visibility=View.INVISIBLE
        }
    }

}