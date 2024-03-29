package com.tnco.runar.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.tnco.runar.R
import com.tnco.runar.databinding.ConnectivityErrorLayoutBinding
import com.tnco.runar.util.InternalDeepLink
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectivityErrorFragment : Fragment() {
    private var _binding: ConnectivityErrorLayoutBinding? = null
    private val binding get() = _binding!!
    private val args: ConnectivityErrorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {

            val uri = Uri.parse(
                InternalDeepLink.LayoutFragment
                    .getRoute()

            )

            findNavController().navigate(
                uri,
                navOptions {
                    popUpTo(R.id.layoutFragment) {
                        inclusive = true
                    }
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ConnectivityErrorLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRetry.setOnClickListener {
            findNavController().popBackStack(args.topMostDestinationToRetry, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
