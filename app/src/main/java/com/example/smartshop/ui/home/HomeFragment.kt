package com.example.smartshop.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R
import com.example.smartshop.data.model.Product
import com.example.smartshop.databinding.FragmentHomeBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.DataModel
import com.example.smartshop.ui.adapter.HomeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeClickListener {
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dateListAdapter: HomeListAdapter
    private lateinit var popularityListAdapter: HomeListAdapter
    private lateinit var ratedListAdapter: HomeListAdapter
    private lateinit var dateRecyclerView: RecyclerView
    private lateinit var popularityRecyclerView: RecyclerView
    private lateinit var ratedRecyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        dateListAdapter = HomeListAdapter(this)
        popularityListAdapter = HomeListAdapter(this)
        ratedListAdapter = HomeListAdapter(this)

        binding.apply {
            dateRecyclerView = recyclerViewDate
            dateRecyclerView.addItemDecoration(HomeItemDecoration(16))
            dateRecyclerView.adapter = dateListAdapter
            dateRecyclerView.adapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            popularityRecyclerView = recyclerViewPopularity
            popularityRecyclerView.addItemDecoration(HomeItemDecoration(16))
            popularityRecyclerView.adapter = popularityListAdapter
            popularityRecyclerView.adapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            ratedRecyclerView = recyclerViewRated
            ratedRecyclerView.addItemDecoration(HomeItemDecoration(16))
            ratedRecyclerView.adapter = ratedListAdapter
            ratedRecyclerView.adapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        homeViewModel.getProductListByDate()
        homeViewModel.getProductListByPopularity()
        homeViewModel.getProductListByRating()

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
                                it.value?.let { productList ->
                                    dateListAdapter.submitList(
                                        productList(
                                            productList,
                                            "جدیدترین محصولات",
                                            "date"
                                        )
                                    )
                                }
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
                                it.value?.let { productList ->
                                    popularityListAdapter.submitList(
                                        productList(
                                            productList,
                                            "پر بازدیدترین محصولات",
                                            "popularity"
                                        )
                                    )
                                }
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
                                it.value?.let { productList ->
                                    ratedListAdapter.submitList(
                                        productList(
                                            productList,
                                            "بهترین محصولات",
                                            "rating")
                                    )
                                }
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
            }
        }
    }

    private fun productList(list: List<Product>, title: String, order: String): List<DataModel> {
        val temp = mutableListOf<DataModel>()
        temp.add(
            DataModel.Header(
                title = title,
                order = order
            )
        )

        for (i in list) {
            temp.add(
                DataModel.Data(
                    i
                )
            )
        }

        temp.add(
            DataModel.Footer(
                order = order
            )
        )
        return temp
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