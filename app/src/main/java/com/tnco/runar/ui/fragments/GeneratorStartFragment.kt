package com.tnco.runar.ui.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tnco.runar.R
import com.tnco.runar.RunarLogger
import com.tnco.runar.adapters.RunesGeneratorAdapter
import com.tnco.runar.databinding.FragmentGeneratorStartBinding
import com.tnco.runar.presentation.viewmodel.GeneratorStartViewModel
import com.tnco.runar.retrofit.RunesResponse


class GeneratorStartFragment : Fragment() {

    private var _binding:FragmentGeneratorStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel:GeneratorStartViewModel
    private val mAdapter: RunesGeneratorAdapter by lazy { RunesGeneratorAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratorStartBinding.inflate(inflater,container,false)
        binding.arrowBack.setOnClickListener {
            activity?.onBackPressed()
        }
        mViewModel = ViewModelProvider(requireActivity()).get(GeneratorStartViewModel::class.java)
        mViewModel.getRunes()
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(),3,GridLayoutManager.HORIZONTAL,false)

        binding.runesRecyclerView.layoutManager = gridLayoutManager
        binding.runesRecyclerView.adapter = mAdapter

        mViewModel.runesResponse.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){
                mAdapter.setData(it)
//                for (i in it)
//                Log.i("TAG",i.imgUrl.toString())
            }
        })
    }


}