package com.example.smartshop.ui.productlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R
import com.example.smartshop.databinding.FragmentProductListBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {
    private lateinit var binding: FragmentProductListBinding
    private val productListViewModel by viewModels<ProductListViewModel>()
    private val args by navArgs<ProductListFragmentArgs>()
    private lateinit var productListAdapter: ProductListAdapter
    private var isLoading = false
    private var pageNumber = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductListBinding.bind(view)


        productListAdapter = ProductListAdapter{
            showDetail(it)
        }
        binding.productListRecyclerView.adapter = productListAdapter
        binding.productListRecyclerView.addItemDecoration(ProductItemDecoration(2))

        binding.productListRecyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        productListViewModel.getProductListByCategory(pageNumber, args.category.toString())
        productListViewModel.getProductListByOrder(pageNumber, args.orderBy.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (args.category != null) {
                    binding.productListRecyclerView.scrollListener {
                        productListViewModel.getProductListByCategory(pageNumber, args.category.toString())
                    }
                    productListViewModel.getProductListByCategory
                        .collect { resultWrapper ->
                            when (resultWrapper) {
                                is ResultWrapper.Failure -> {
                                    binding.customView.onFail(resultWrapper.message.toString())
                                    binding.customView.click {
                                        productListViewModel.getProductListByCategory(
                                            pageNumber,
                                            args.category.toString())
                                    }
                                }
                                ResultWrapper.Loading -> {
                                    binding.customView.onLoading()
                                }
                                is ResultWrapper.Success -> {
                                    pageNumber++
                                    isLoading = false
                                    binding.customView.onSuccess()
                                    resultWrapper.value?.let {
                                        productListViewModel.cachedList.addAll(it)
                                    }
                                    val pos = productListViewModel.cachedList.size
                                    productListAdapter.submitList(productListViewModel.cachedList)
                                    productListAdapter.notifyItemInserted(pos)
                                }
                            }
                        }
                } else {
                    binding.productListRecyclerView.scrollListener {
                        productListViewModel.getProductListByOrder(pageNumber, args.orderBy.toString())
                    }
                    productListViewModel.getProductListByOrder
                        .collect { resultWrapper ->
                            when (resultWrapper) {
                                is ResultWrapper.Failure -> {
                                    binding.customView.onFail(resultWrapper.message.toString())
                                    binding.customView.click {
                                        productListViewModel.getProductListByOrder(
                                            pageNumber,
                                            args.orderBy.toString()
                                        )
                                    }
                                }
                                ResultWrapper.Loading -> {
                                    binding.customView.onLoading()
                                }
                                is ResultWrapper.Success -> {
                                    pageNumber++
                                    isLoading = false
                                    binding.customView.onSuccess()
                                    resultWrapper.value?.let {
                                        productListViewModel.cachedList.addAll(it)
                                    }
                                    val pos = productListViewModel.cachedList.size
                                    productListAdapter.submitList(productListViewModel.cachedList)
                                    productListAdapter.notifyItemInserted(pos)
                                }
                            }
                        }
                }
            }
        }

    }

    private fun showDetail(id: String){
        val action = ProductListFragmentDirections.actionGlobalDetailFragment(id)
        findNavController().navigate(action)
    }

    private fun RecyclerView.scrollListener(fn: () -> Unit) {
        var isScrolling = false
        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && isScrolling && !isLoading) {
                    fn()
                    isLoading = true
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = newState >= 1
            }
        })
    }

}