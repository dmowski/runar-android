package com.tnco.runar.ui.component.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.tnco.runar.R

class DescriptionDialog(
    private val description: String,
    private val headerText: String,
    private val fontSize: Float
) : View.OnClickListener {

    private lateinit var dialog: Dialog

    fun showDialog(activity: FragmentActivity) {
        dialog = Dialog(activity, android.R.style.ThemeOverlay)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.description_dialog_layout)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog.window?.statusBarColor = activity.getColor(R.color.library_top_bar)
        dialog.window?.navigationBarColor = activity.getColor(R.color.library_top_bar)
        val header = dialog.findViewById<FrameLayout>(R.id.description_header_frame)
            .getChildAt(0) as TextView
        val descriptionView = dialog.findViewById<TextView>(R.id.description_text_view)
        header.text = headerText
        val headerFontSize = (fontSize * 2)
        val buttonFontSize = (fontSize * 1.5).toFloat()
        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerFontSize)
        descriptionView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        dialog.findViewById<TextView>(R.id.button_text)
            .setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonFontSize)
        descriptionView.text = description
        dialog.show()
        dialog.findViewById<ImageView>(R.id.exit_button).setOnClickListener(this)
        dialog.findViewById<FrameLayout>(R.id.description_button_frame)
            .setOnClickListener(this)
        dialog.setOnKeyListener { _, keyCode, event ->
            var consumed = false
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    consumed = true
                    dialog.dismiss()
                }
            }
            consumed
        }
    }

    override fun onClick(v: View?) {
        dialog.dismiss()
    }
}