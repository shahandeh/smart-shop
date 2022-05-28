package com.example.smartshop.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R
import com.example.smartshop.databinding.FragmentSearchBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.ProductListAdapter
import com.example.smartshop.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchListAdapter: ProductListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        searchListAdapter = ProductListAdapter {

        }

        binding.recyclerView.adapter = searchListAdapter
        binding.recyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotBlank()) {
                    searchViewModel.searchProduct(query)
                    searchViewModel.param = query
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                TODO("Not yet implemented")
                return false
            }
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchResult.collect {
                    searchViewModel.searchResult.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                if (searchViewModel.param.isBlank())
                                    binding.customView.onSuccess()

                                else binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                it.value?.let { productList ->
                                    searchListAdapter.submitList(productList)
                                }
                            }

                            is ResultWrapper.Failure -> {
                                it.message?.let { message ->
                                    binding.customView.onFail(message)
                                }
                                binding.customView.click {
                                    searchViewModel.searchProduct(searchViewModel.param)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}