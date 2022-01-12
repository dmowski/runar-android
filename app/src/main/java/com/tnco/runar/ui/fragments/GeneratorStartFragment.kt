package com.tnco.runar.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.tnco.runar.adapters.RunesGeneratorAdapter
import com.tnco.runar.databinding.FragmentGeneratorStartBinding
import com.tnco.runar.extensions.observeOnce
import com.tnco.runar.presentation.viewmodel.GeneratorStartViewModel
import kotlinx.coroutines.launch
import java.util.*


class GeneratorStartFragment : Fragment() {

    private var _binding: FragmentGeneratorStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: GeneratorStartViewModel
    private val mAdapter: RunesGeneratorAdapter by lazy { RunesGeneratorAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratorStartBinding.inflate(inflater, container, false)
        binding.arrowBack.setOnClickListener {
            activity?.onBackPressed()
        }
        mViewModel = ViewModelProvider(requireActivity()).get(GeneratorStartViewModel::class.java)

        setupRecyclerView()
        readDatabase()

        return binding.root
    }

    private fun readDatabase() { // доделать считывание
        lifecycleScope.launch {
            mViewModel.readRunes.observeOnce(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    mAdapter.setData(it)
                    //Log.i("TAG", "DATABASE")
                } else requestApiData()
            })
        }
    }


    private fun requestApiData() {
        mViewModel.getRunes()
        mViewModel.runesResponse.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                mAdapter.setData(it)
                //Log.i("TAG", "SERVER")
            }
        })
    }

    private fun setupRecyclerView() {
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false)

        binding.runesRecyclerView.layoutManager = gridLayoutManager
        binding.runesRecyclerView.adapter = mAdapter

        mAdapter.obsSelectedRunes.observe(viewLifecycleOwner, {
            when (it.size) {
                0 -> {
                    binding.btnGenerate.visibility = View.GONE
                    binding.btnRandom.visibility = View.VISIBLE
                }
                1 -> {
                    binding.tvRune1.visibility = View.INVISIBLE
                    binding.rune1.visibility = View.VISIBLE
                    binding.tvDescRune1.visibility = View.VISIBLE
                    binding.rune1.load(it[0].imgUrl)
                    if (Locale.getDefault().language == "ru")
                        binding.tvDescRune1.text = it[0].ruTitle
                    else binding.tvDescRune1.text = it[0].enTitle
                    binding.btnRandom.visibility = View.INVISIBLE
                    binding.btnGenerate.visibility = View.VISIBLE
                }
                2 -> {
                    binding.tvRune2.visibility = View.INVISIBLE
                    binding.rune2.visibility = View.VISIBLE
                    binding.rune2.load(it[1].imgUrl)
                    binding.tvDescRune2.visibility = View.VISIBLE
                    if (Locale.getDefault().language == "ru")
                        binding.tvDescRune2.text = it[1].ruTitle
                    else binding.tvDescRune2.text = it[1].enTitle
                }
                3 -> {
                    binding.tvRune3.visibility = View.INVISIBLE
                    binding.rune3.visibility = View.VISIBLE
                    binding.rune3.load(it[2].imgUrl)
                    binding.tvDescRune3.visibility = View.VISIBLE
                    if (Locale.getDefault().language == "ru")
                        binding.tvDescRune3.text = it[2].ruTitle
                    else binding.tvDescRune3.text = it[2].enTitle
                }
            }
        })

    }


}