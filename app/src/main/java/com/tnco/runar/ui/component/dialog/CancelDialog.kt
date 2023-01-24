package com.tnco.runar.ui.component.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.CancelDialogLayoutBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.AnalyticsConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CancelDialog : DialogFragment() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private var _binding: CancelDialogLayoutBinding? = null
    private val binding get() = _binding!!

    private val args: CancelDialogArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CancelDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = android.R.style.ThemeOverlay

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // requireDialog().requestWindowFeature(Window.FEATURE_NO_TITLE)
        requireDialog().setCancelable(false)
        requireDialog().window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        requireDialog().window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        requireDialog().window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        requireDialog().window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireDialog().window?.statusBarColor = requireContext().getColor(R.color.library_top_bar)
        requireDialog().window?.navigationBarColor = requireContext().getColor(R.color.library_top_bar)

        val buttonsFontSize = (args.fontSize * 0.85f)
        binding.dialogText.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, args.fontSize)
            text = args.dialogText
        }
        binding.buttonYes.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonsFontSize)
        binding.buttonNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonsFontSize)
        requireDialog().setOnKeyListener { _, keyCode, event ->
            var consumed = false
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    consumed = true
                    dismiss()
                }
            }
            consumed
        }
        binding.dialogElementLeft.setOnClickListener {
            dismiss()
        }
        binding.dialogElementRight.setOnClickListener {
            viewModel.cancelChildrenCoroutines()
            analyticsHelper.sendEvent(
                AnalyticsEvent.SCRIPT_INTERRUPTION,
                Pair(AnalyticsConstants.PAGE, args.page)
            )
            findNavController().popBackStack(args.destinationOnAgree, false)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
