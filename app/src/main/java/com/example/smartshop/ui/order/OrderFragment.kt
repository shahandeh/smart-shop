package com.example.smartshop.ui.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.smartshop.R
import com.example.smartshop.data.CurrentUser.user_id
import com.example.smartshop.databinding.FragmentOrderBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.OrderAdapter
import com.example.smartshop.util.gone
import com.example.smartshop.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order), OrderClickListener {
    private val orderViewModer by viewModels<OrderViewModel>()
    lateinit var binding: FragmentOrderBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOrderBinding.bind(view)

        if (user_id != 0) binding.login.gone()

        val pendingOrderListAdapter = OrderAdapter(this)
        binding.recyclerView.adapter = pendingOrderListAdapter
        binding.orderComplete.setOnClickListener {
            val action = OrderFragmentDirections.actionGlobalOrderDetailFragment()
            findNavController().navigate(action)
        }

        orderViewModer.getOrder()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {

                launch {
                    orderViewModer.currentPendingOrder.collect {
                        when (it) {

                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                if (it.value.isNotEmpty()) {
                                    if (it.value[0].line_items.isNotEmpty()) {
                                        orderViewModer.setDataFromOrder(it.value)
                                        binding.empty.gone()
                                    }
                                } else binding.empty.visible()
                                binding.customView.onSuccess()
                            }

                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { orderViewModer.getOrder() }
                            }
                        }
                    }
                }

                launch {
                    orderViewModer.productList.collect {
                        when (it) {

                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                if (it.value.isEmpty()) binding.empty.visible()
                                else {
                                    val productInOrderList =
                                        orderViewModer.createProductInOrderList(it.value)
                                    orderViewModer.productInOrderList.clear()
                                    orderViewModer.productInOrderList.addAll(productInOrderList)
                                    binding.orderTotal.text = orderViewModer.totalPrice()
                                    pendingOrderListAdapter.submitList(orderViewModer.productInOrderList)
                                    pendingOrderListAdapter.notifyDataSetChanged()
                                }
                                binding.customView.onSuccess()
                            }

                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { orderViewModer.getOrder() }
                            }
                        }
                    }
                }

                launch {
                    orderViewModer.updateOrderResponse.collect {
                        when (it) {

                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                orderViewModer.setDataFromResponse(it.value)
                                binding.customView.onSuccess()
                            }

                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { orderViewModer.getOrder() }
                            }
                        }
                    }
                }
            }
        }

    }

    override fun minusCount(id: Int) {
        orderViewModer.funQuantityMinus(id)
    }

    override fun plusCount(id: Int) {
        orderViewModer.funQuantityPlus(id)
    }

    override fun clickItem(id: Int) {
        val action = OrderFragmentDirections.actionGlobalDetailFragment(id.toString())
        findNavController().navigate(action)
    }
}