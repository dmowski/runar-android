package com.test.runar.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import com.test.runar.R

class CancelDialog<T : Activity>(
    private val navController: NavController,
    private val activity: T
) {

    fun showDialog() {
        val dialog = Dialog(activity as Activity, android.R.style.ThemeOverlay)
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
        dialog.setOnKeyListener { dialog, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
            }
            true
        }
        dialog.findViewById<ConstraintLayout>(R.id.dialog_element_left).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<ConstraintLayout>(R.id.dialog_element_right).setOnClickListener {
            navController.navigate(R.id.layoutFragment)
            dialog.dismiss()
        }
    }
}