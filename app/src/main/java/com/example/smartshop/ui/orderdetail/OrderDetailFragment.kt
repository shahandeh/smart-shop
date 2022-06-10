package com.example.smartshop.ui.orderdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartshop.R
import com.example.smartshop.data.CurrentUser.first_name
import com.example.smartshop.data.CurrentUser.last_name
import com.example.smartshop.databinding.FragmentOrderDetailBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.gone
import com.example.smartshop.util.snack
import com.example.smartshop.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailFragment : Fragment(R.layout.fragment_order_detail) {
    private val orderViewModel by viewModels<OrderDetailViewModel>()
    private lateinit var binding: FragmentOrderDetailBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderDetailBinding.bind(view)

        binding.done.setOnClickListener { orderViewModel.updateOrder() }
        binding.coupon.setOnClickListener {
            orderViewModel.couponCode = binding.couponCode.text.toString()
            orderViewModel.validateCoupon()
        }

        orderViewModel.getOrder()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {

                launch {
                    orderViewModel.currentOrder.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }
                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                if (it.value.isNotEmpty()) {
                                    orderViewModel.order = it.value[0]
                                    orderViewModel.setDateFromOrder()
                                    showData()
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
                    orderViewModel.orderResponse.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }
                            is ResultWrapper.Success -> {
                                binding.data.gone()
                                binding.customView.onSuccess()
                                binding.root.snack(getString(R.string.your_request_status_is_complete))
                                binding.success.visible()
                            }
                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { orderViewModel.updateOrder() }
                            }
                        }
                    }
                }

                launch {
                    orderViewModel.validateCouponResponse.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }
                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                if (orderViewModel.couponCode == it.value[0].code && orderViewModel.couponAmount == it.value[0].amount) {
                                    binding.root.snack(getString(R.string.coupon_code_already_used))
                                } else orderViewModel.setCoupon()
                                orderViewModel.couponAmount = it.value[0].amount
                            }
                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { orderViewModel.validateCoupon() }
                            }
                        }
                    }
                }

                launch {
                    orderViewModel.updateOrderCoupon.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }
                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                orderViewModel.orderUpdate = it.value
                                orderViewModel.setDataFromUpdate()
                                binding.root.snack(getString(R.string.coupon_added))
                            }
                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { orderViewModel.setCoupon() }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showData() {
        binding.apply {
            data.visible()
            firstName.text = first_name
            lastName.text = last_name
            orderId.text = orderViewModel.orderId
            price.text = orderViewModel.price
            discount.text = orderViewModel.discount
            finalPrice.text = orderViewModel.finalPrice
        }
    }

}