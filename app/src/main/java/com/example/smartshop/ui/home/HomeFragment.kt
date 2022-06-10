package com.example.smartshop.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.smartshop.R
import com.example.smartshop.data.model.product.Image
import com.example.smartshop.databinding.FragmentHomeBinding
import com.example.smartshop.safeapi.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeClickListener {
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dateListAdapter: HomeListAdapter
    private lateinit var popularityListAdapter: HomeListAdapter
    private lateinit var ratedListAdapter: HomeListAdapter
    private lateinit var handler: Handler
    private lateinit var adapter: ImageSliderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val runnable =
            Runnable { binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1 }

        dateListAdapter = HomeListAdapter(this)
        popularityListAdapter = HomeListAdapter(this)
        ratedListAdapter = HomeListAdapter(this)

        listAdapterInit()

        binding.searchImage.setOnClickListener { searchFragment() }
        binding.cardView.setOnClickListener { searchFragment() }

        homeViewModel.getProductListByDate()
        homeViewModel.getProductListByPopularity()
        homeViewModel.getProductListByRating()
        homeViewModel.getImageSliderProduct()

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 3000)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.getProductListByDate.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.dateCustomView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.dateCustomView.onSuccess()
                                dateListAdapter.submitList(
                                    homeViewModel.createDataList(it.value)
                                )
                            }

                            is ResultWrapper.Failure -> {
                                it.message?.let { message ->
                                    binding.dateCustomView.onFail(message)
                                }
                                binding.dateCustomView.click {
                                    homeViewModel.getProductListByDate()
                                }
                            }
                        }
                    }
                }

                launch {
                    homeViewModel.getProductListByPopularity.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.popularityCustomView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.popularityCustomView.onSuccess()
                                popularityListAdapter.submitList(
                                    homeViewModel.createPopularityList(it.value)
                                )
                            }

                            is ResultWrapper.Failure -> {
                                it.message?.let { message ->
                                    binding.popularityCustomView.onFail(message)
                                }
                                binding.popularityCustomView.click {
                                    homeViewModel.getProductListByPopularity()
                                }
                            }
                        }
                    }
                }

                launch {
                    homeViewModel.getProductListByRating.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.rateCustomView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.rateCustomView.onSuccess()
                                ratedListAdapter.submitList(
                                    homeViewModel.createRatedList(it.value)
                                )
                            }

                            is ResultWrapper.Failure -> {
                                it.message?.let { message ->
                                    binding.rateCustomView.onFail(message)
                                }
                                binding.rateCustomView.click {
                                    homeViewModel.getProductListByRating()
                                }
                            }
                        }
                    }
                }

                launch {
                    homeViewModel.getImageSlider.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customViewImageSlider.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customViewImageSlider.onSuccess()
                                imageSliderInit(it.value.images)
                            }

                            is ResultWrapper.Failure -> {
                                binding.customViewImageSlider.onFail(it.message.toString())
                                binding.customViewImageSlider.click {
                                    homeViewModel.getImageSliderProduct()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun imageSliderInit(list: List<Image>) {
        val imageList = mutableListOf<String>()
        for (i in list) {
            imageList.add(i.src)
        }
        handler = Handler(Looper.myLooper()!!)

        adapter = ImageSliderAdapter(imageList, binding.viewPager2)

        binding.apply {
            viewPager2.adapter = adapter
            viewPager2.clipToPadding = false
            viewPager2.clipChildren = false
            viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

    }

    private fun listAdapterInit() {
        binding.apply {
            recyclerViewDate.addItemDecoration(HomeItemDecoration(16))
            recyclerViewDate.adapter = dateListAdapter
            recyclerViewDate.adapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            recyclerViewPopularity.addItemDecoration(HomeItemDecoration(16))
            recyclerViewPopularity.adapter = popularityListAdapter
            recyclerViewPopularity.adapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            recyclerViewRated.addItemDecoration(HomeItemDecoration(16))
            recyclerViewRated.adapter = ratedListAdapter
            recyclerViewRated.adapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        }
    }


    private fun searchFragment() {
        val action = HomeFragmentDirections.actionGlobalSearchFragment()
        findNavController().navigate(action)
    }

    override fun product(id: String) {
        val action = HomeFragmentDirections.actionGlobalDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun moreProduct(order: String) {
        val action = HomeFragmentDirections.actionGlobalProductListFragment(order, null)
        findNavController().navigate(action)
    }

}