package com.test.runar.ui.fragments

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutInterpretationFragment : Fragment(R.layout.fragment_layout_interpretation),
        View.OnClickListener {
    private lateinit var model: MainViewModel
    private lateinit var header: TextView
    private lateinit var headerText: String
    private var runeHeight: Int =0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        header =
                ((view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(0) as FrameLayout).getChildAt(0) as TextView

        model.selectedLayout.observe(viewLifecycleOwner){
            if(it!=null){
                header.text = it.layoutName
            }
        }
        model.runeHeight.observe(viewLifecycleOwner){
            if(it!=null){
                runeHeight = it
                var runeView = (view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(1)
                var layoutParams = runeView.layoutParams
                layoutParams.height = 223
                layoutParams.width = (223/1.23).toInt()
                runeView.layoutParams = layoutParams
            }
        }
        val mainLayout = view.findViewById<ConstraintLayout>(R.id.main_layout)
        val backgroundLayout = (view.findViewById<ScrollView>(R.id.scroll_view).getChildAt(0) as ConstraintLayout).getChildAt(2) as ConstraintLayout
        val observer = mainLayout.viewTreeObserver
        observer.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                observer.removeOnGlobalLayoutListener(this)
                val screenHeight = mainLayout.height
                val minSize = screenHeight-backgroundLayout.top
                //Log.d("Log",backgroundLayout.height.toString())
                //Log.d("Log",minSize.toString())
                if(minSize>backgroundLayout.height){
                    val backLayout = backgroundLayout.getChildAt(1) as ConstraintLayout
                    val backLayoutParams = backLayout.layoutParams
                    backLayoutParams.height = minSize
                    backLayout.layoutParams = backLayoutParams
                }
                /*var bmp = BitmapFactory.decodeResource(resources,R.drawable.interpretation_background)
                var aspect = mainLayout.width/414
                var newBmp = Bitmap.createBitmap(bmp,0,0,414,backgroundLayout.height/aspect)
                val imageView = backgroundLayout.getChildAt(0) as ImageView
                imageView.setBackground(BitmapDrawable(resources,newBmp))*/
            }
        })





    }

    override fun onStop() {
        super.onStop()
        model.clearLayoutData()
    }
    override fun onClick(v: View?) {
        val navController = findNavController()
    }
}