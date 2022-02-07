package com.tnco.runar.ui.fragments


import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.tnco.runar.R
import com.tnco.runar.adapters.PagerAdapter
import com.tnco.runar.databinding.FragmentSampleLibraryBinding
import com.tnco.runar.extensions.observeOnce
import com.tnco.runar.presentation.viewmodel.LibraryViewModel
import com.google.android.material.tabs.TabLayout
import java.lang.Exception
import java.lang.reflect.Field


class SampleLibraryFragment : Fragment() {

    private var _binding: FragmentSampleLibraryBinding? = null
    private val binding get() = _binding!!

    val viewModel: LibraryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSampleLibraryBinding.inflate(inflater, container, false)

        viewModel.fontSize.observeOnce(this, {
            binding.tvToolbar.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it * 1.35).toFloat())
        })


        initLibraryPager()

        return binding.root
    }


    private fun initLibraryPager() {

        val fragments = ArrayList<Fragment>()
        fragments.add(LibraryFragment())
        fragments.add(AudioLibraryFragment())

        val titles = ArrayList<String>()
        titles.add(getString(R.string.library_tab_books))
        titles.add(getString(R.string.library_tab_audio))

        val pagerAdapter = PagerAdapter(fragments, requireActivity())

        // binding.viewPager.isUserInputEnabled = false
        binding.viewPager.apply {
            adapter = pagerAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}