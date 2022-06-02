package com.example.smartshop.ui.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.smartshop.R
import com.example.smartshop.data.CurrentUser
import com.example.smartshop.databinding.FragmentOrderBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.OrderAdapter
import com.example.smartshop.util.gone
import com.example.smartshop.util.snack
import com.example.smartshop.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
const val TAG = "majid"
@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order), OrderClickListener {
    private val orderViewModel by viewModels<OrderViewModel>()
    private lateinit var binding: FragmentOrderBinding
    private lateinit var orderAdapter: OrderAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderBinding.bind(view)

        Log.d("majid", "onViewCreated order fragment: ${CurrentUser.user_id}")

        binding.orderComplete.setOnClickListener { orderViewModel.orderComplete() }

//        orderAdapter = OrderAdapter(this)
//        binding.recyclerView.adapter = orderAdapter

        orderViewModel.getOrder()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    orderViewModel.order.collect {
                        when (it) {

                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                if (it.value.isNotEmpty()) {
                                    getProductCollect()
                                    orderViewModel.currentOrder = it.value[0]
                                    orderViewModel.getProductInOrder()
                                }
                            }

                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { orderViewModel.getOrder() }
                            }
                        }
                    }
                }

                launch {
                    orderViewModel.updateOrderResponse.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }
                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                getProductCollect()
                                orderViewModel.currentResponse = it.value
                                orderViewModel.listUpdated = true
                                orderViewModel.getProductInResponse()
                            }
                            is ResultWrapper.Failure -> {
                                binding.root.snack(it.message.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun CoroutineScope.getProductCollect() {
        launch {
            orderViewModel.getProduct.collect {
                when (it) {

                    ResultWrapper.Loading -> {
                        binding.customView.onLoading()
                    }

                    is ResultWrapper.Success -> {
                        binding.customView.onSuccess()
                        orderViewModel.productList.add(it.value)
                        if (orderViewModel.listUpdated) submitProductFromResponse()
                        else submitProductFromOrder()
                        Log.d(TAG, "getProductCollect: ${orderViewModel.listUpdated}")
                    }

                    is ResultWrapper.Failure -> {
                        binding.customView.onFail(it.message.toString())
                        binding.customView.click { orderViewModel.getOrder() }
                    }
                }
            }
        }
    }

    private fun submitProductFromResponse() {
        Log.d(TAG, "submitProductFromResponse: " +
                "${orderViewModel.productList.size}     " +
                "${orderViewModel.currentResponse.line_items.size}")
        if (orderViewModel.productList.size == orderViewModel.currentResponse.line_items.size) {
            showView()
            val temp = orderViewModel.createProductListFromResponse()
            orderAdapter = OrderAdapter(this)
            binding.recyclerView.adapter = orderAdapter
            orderAdapter.submitList(temp)
            binding.recyclerView.adapter?.notifyItemChanged(0, temp.size)
        }
    }

    private fun submitProductFromOrder() {
        Log.d(TAG, "submitProductFromOrder: " +
                "${orderViewModel.productList.size}     " +
                "${orderViewModel.currentOrder.line_items.size}")
        if (orderViewModel.productList.size == orderViewModel.currentOrder.line_items.size) {
            showView()
            val temp = orderViewModel.createProductListFromOrder()
            orderAdapter = OrderAdapter(this)
            binding.recyclerView.adapter = orderAdapter
            orderAdapter.submitList(temp)

//            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun showView() {
        binding.apply {
            orderTotalText.visible()
            orderComplete.visible()
            recyclerView.visible()
            bottomView.visible()
            orderTotal.visible()
        }
    }

    private fun hideView() {
        binding.apply {
            orderTotalText.gone()
            orderComplete.gone()
            recyclerView.gone()
            bottomView.gone()
            orderTotal.gone()
        }
    }

    override fun minusCount(id: Int) {
        orderViewModel.productCount(id, false)
    }

    override fun plusCount(id: Int) {
        orderViewModel.productCount(id, true)
    }

    override fun clickItem(id: Int) {
        val action = OrderFragmentDirections.actionGlobalDetailFragment(id.toString())
        findNavController().navigate(action)
    }

}