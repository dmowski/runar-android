package com.tnco.runar.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tnco.runar.R
import com.tnco.runar.ui.Navigator

class CancelDialog(private val context: Context,private val fontSize:Float) {

    fun showDialog() {
        val dialog = Dialog(context, android.R.style.ThemeOverlay)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.cancel_dialog_layout)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show()
        val buttonsFontSize = (fontSize*0.85).toFloat()
        dialog.findViewById<TextView>(R.id.dialog_text).setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
        dialog.findViewById<TextView>(R.id.button_yes).setTextSize(TypedValue.COMPLEX_UNIT_PX,buttonsFontSize)
        dialog.findViewById<TextView>(R.id.button_no).setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonsFontSize)
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
        dialog.findViewById<ConstraintLayout>(R.id.dialog_element_left).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<ConstraintLayout>(R.id.dialog_element_right).setOnClickListener {
            dialog.dismiss()
            (context as Navigator).agreeWithDialog()
        }
    }
}