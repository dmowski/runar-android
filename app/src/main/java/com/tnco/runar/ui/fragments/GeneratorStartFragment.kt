package com.tnco.runar.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.tnco.runar.adapters.RunesGeneratorAdapter
import com.tnco.runar.databinding.FragmentGeneratorStartBinding
import com.tnco.runar.extensions.observeOnce
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.presentation.viewmodel.GeneratorStartViewModel
import kotlinx.coroutines.launch
import java.util.*


class GeneratorStartFragment : Fragment() {

    private var _binding: FragmentGeneratorStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: GeneratorStartViewModel
    private val mAdapter: RunesGeneratorAdapter by lazy { RunesGeneratorAdapter() }
    private var listId: MutableList<Int> = mutableListOf()
    private var listAllIds: MutableList<Int> = mutableListOf()

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

        binding.btnRandom.setOnClickListener {
            randomRunes()
        }

        binding.btnGenerate.setOnClickListener {
            sentRunes()
        }

        return binding.root
    }


    private fun readDatabase() { // доделать считывание
        lifecycleScope.launch {
            mViewModel.readRunes.observeOnce(viewLifecycleOwner, { listRunes ->
                if (listRunes.isNotEmpty()) {
                    mAdapter.setData(listRunes)
                    listRunes.forEach {
                        listAllIds.add(it.id)
                    }
                } else requestApiData()
            })
        }
    }


    private fun requestApiData() {
        mViewModel.getRunes()
        mViewModel.runesResponse.observe(viewLifecycleOwner, { listRunes ->
            if (listRunes.isNotEmpty()) {
                mAdapter.setData(listRunes)
                listRunes.forEach {
                    listAllIds.add(it.id)
                }
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
                1 -> {
                    showRunes(binding.tvRune1, binding.rune1, binding.tvDescRune1, it[0])
                    binding.btnRandom.visibility = View.INVISIBLE
                    binding.btnGenerate.visibility = View.VISIBLE
                }
                2 -> {
                    showRunes(binding.tvRune2, binding.rune2, binding.tvDescRune2, it[1])
                }
                3 -> {
                    showRunes(binding.tvRune3, binding.rune3, binding.tvDescRune3, it[2])
                }
            }
        })

        binding.rune1.setOnClickListener {
            if (mAdapter.obsSelectedRunes.value?.size == 1) {
                clearRune(binding.tvRune1, binding.rune1, binding.tvDescRune1, 0)
                binding.btnGenerate.visibility = View.GONE
                binding.btnRandom.visibility = View.VISIBLE
            }
        }

        binding.rune2.setOnClickListener {
            if (mAdapter.obsSelectedRunes.value?.size == 2)
                clearRune(binding.tvRune2, binding.rune2, binding.tvDescRune2, 1)
        }


        binding.rune3.setOnClickListener {
            if (mAdapter.obsSelectedRunes.value?.size == 3)
                clearRune(binding.tvRune3, binding.rune3, binding.tvDescRune3, 2)
        }
    }

    private fun showRunes(
        tvRune: TextView,
        imgRune: ImageView,
        tvDescRune: TextView,
        runesItemsModel: RunesItemsModel
    ) {
        tvRune.visibility = View.INVISIBLE
        imgRune.visibility = View.VISIBLE
        imgRune.load(runesItemsModel.imgUrl)
        tvDescRune.visibility = View.VISIBLE
        if (Locale.getDefault().language == "ru")
            tvDescRune.text = runesItemsModel.ruTitle
        else tvDescRune.text = runesItemsModel.enTitle
    }

    private fun clearRune(
        tvRune: TextView,
        imgRune: ImageView,
        tvDescRune: TextView,
        index: Int
    ) {
        imgRune.visibility = View.INVISIBLE
        tvDescRune.visibility = View.INVISIBLE
        tvRune.visibility = View.VISIBLE
        mAdapter.obsSelectedRunes.value?.removeAt(index)
    }


    private fun randomRunes() {
        val count = (1..3).random()
        for (i in 0 until count) {
            listId.add(i, listAllIds.random())
            listAllIds.remove(listId[i])
        }
        listId.sort()
        listId.forEach {
            Log.i("TAG", "$count $it") // отправить в другой фрагмент
        }
    }

    private fun sentRunes() {
        mAdapter.obsSelectedRunes.value?.forEach {
            listId.add(it.id)
        }
        listId.sort()
        listId.forEach {
            Log.i("TAG", "$it") // отправить в другой фрагмент
        }
    }

}