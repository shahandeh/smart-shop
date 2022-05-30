package com.example.smartshop.ui.orderhistory

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R
import com.example.smartshop.databinding.FragmentOrderHistoryBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.OrderHistoryAdapter
import com.example.smartshop.ui.adapter.OrderHistoryItemDecoration
import com.example.smartshop.ui.adapter.ProductItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryFragment : Fragment(R.layout.fragment_order_history) {
    private val orderHistoryViewModel by viewModels<OrderHistoryViewModel>()
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderHistoryBinding.bind(view)

        orderHistoryAdapter = OrderHistoryAdapter()
        binding.recyclerView.adapter = orderHistoryAdapter
        binding.recyclerView.addItemDecoration(OrderHistoryItemDecoration(16))

        orderHistoryViewModel.getOrderHistory(orderHistoryViewModel.pageNumber)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.recyclerView.scrollListener {
                    orderHistoryViewModel.getOrderHistory(
                        orderHistoryViewModel.pageNumber
                    )
                }
                orderHistoryViewModel.orderHistory.collect {
                    when (it) {
                        ResultWrapper.Loading -> {
                            binding.customView.onLoading()
                        }

                        is ResultWrapper.Success -> {
                            orderHistoryViewModel.pageNumber++
                            orderHistoryViewModel.isLoading = false
                            binding.customView.onSuccess()
                            it.value.let { orderHistory ->
                                orderHistoryViewModel.cachedList.addAll(orderHistory)
                            }

                            val pos = orderHistoryViewModel.cachedList.size
                            orderHistoryAdapter.submitList(orderHistoryViewModel.cachedList)
                            orderHistoryAdapter.notifyItemInserted(pos)
                        }

                        is ResultWrapper.Failure -> {
                            binding.customView.onFail(it.toString())
                            binding.customView.click {
                                orderHistoryViewModel.getOrderHistory(orderHistoryViewModel.pageNumber)
                            }
                        }
                    }
                }
            }

        }
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

                if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && isScrolling && !orderHistoryViewModel.isLoading) {
                    fn()
                    orderHistoryViewModel.isLoading = true
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = newState >= 1
            }
        })
    }

}