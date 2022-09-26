package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.tnco.runar.databinding.ConnectivityErrorLayoutBinding

class ConnectivityErrorFragment : Fragment() {
    private var _binding: ConnectivityErrorLayoutBinding? = null
    private val binding get() = _binding!!
    private val args: ConnectivityErrorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ConnectivityErrorLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRetry.setOnClickListener {
            findNavController().navigate(
                args.fragmentIdToRetry,
                null,
                navOptions {
                    popUpTo(args.fragmentIdToRetry) {
                        inclusive = true
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}