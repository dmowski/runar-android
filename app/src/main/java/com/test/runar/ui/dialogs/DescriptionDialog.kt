package com.test.runar.ui.dialogs

import android.R
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

class DescriptionDialog(description: String, header: String, fontSize: Float) :
    View.OnClickListener {

    private lateinit var dialog: Dialog
    private val descriptionText = description
    private val headerText = header
    private val fontSize = fontSize-1

    fun showDialog(activity: FragmentActivity) {
        dialog = Dialog(activity, R.style.ThemeOverlay)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(com.test.runar.R.layout.description_dialog_layout)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val header = dialog.findViewById<FrameLayout>(com.test.runar.R.id.description_header_frame)
            .getChildAt(0) as TextView
        val description = dialog.findViewById<TextView>(com.test.runar.R.id.description_text_view)
        header.text = headerText
        description.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        description.text = descriptionText
        dialog.show()
        dialog.findViewById<ImageView>(com.test.runar.R.id.exit_button).setOnClickListener(this)
        dialog.findViewById<FrameLayout>(com.test.runar.R.id.description_button_frame)
            .setOnClickListener(this)
        dialog.setOnKeyListener { dialog, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
            }
            true
        }
    }

    override fun onClick(v: View?) {
        dialog.dismiss()
    }
}