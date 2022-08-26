package com.tnco.runar.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tnco.runar.databinding.BottomSheetLayoutBinding

class BottomSheetFragment(
    private val tFragment: Fragment?,
    private val tHeading: String,
    private val tDescription: String,
    private val tDrawable: Drawable,
    private val bText: String
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLayoutBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return BottomSheetLayoutBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            heading.text = tHeading
            description.text = tDescription
            image.setImageDrawable(tDrawable)
            navigateButton.text = bText

            navigateButton.setOnClickListener {
                if (tFragment != null) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(this@BottomSheetFragment.id, tFragment)
                        .commit()
                }
            }

            crossButton.setOnClickListener {
                onDestroyView()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetFragment"
    }
}