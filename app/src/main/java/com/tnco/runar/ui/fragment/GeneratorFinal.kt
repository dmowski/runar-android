package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.databinding.GeneratorFinalBinding
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.screenCompose.GeneratorFinalScreen
import com.tnco.runar.ui.viewmodel.MainViewModel

class GeneratorFinal : Fragment() {

    private var _binding: GeneratorFinalBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private companion object {
        const val QUALITY = 100
        const val AUTHORITY = "com.tnco.runar.provider"
        const val IMAGE_NAME = "image.jpeg"
        const val CHILD = "images"
        const val PAGE = "generator_final"
        const val TYPE = "image/jpeg"
    }
//    lateinit var twitter: ImageView
//    lateinit var facebook: ImageView
//    lateinit var instagram: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showCancelDialog()
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GeneratorFinalBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return ComposeView(requireContext()).apply {
            setContent {
                GeneratorFinalScreen(findNavController())
            }
        }
    }
    private fun showCancelDialog() {
        CancelDialog(
            requireContext(),
            viewModel.fontSize.value!!,
            PAGE,
            getString(R.string.description_generator_popup)
        ) {
            requireActivity().viewModelStore.clear()
            val direction = GeneratorFinalDirections.actionGlobalGeneratorFragment()
            findNavController().navigate(direction)
        }
            .showDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
