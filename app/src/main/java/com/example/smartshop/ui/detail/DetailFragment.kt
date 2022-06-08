package com.example.smartshop.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.smartshop.R
import com.example.smartshop.databinding.FragmentDetailBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.DetailReviewListAdapter
import com.example.smartshop.ui.adapter.ImageRecyclerAdapter
import com.example.smartshop.ui.adapter.OrderHistoryItemDecoration
import com.example.smartshop.util.cleaner
import com.example.smartshop.util.gone
import com.example.smartshop.util.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.N)
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailReviewListAdapter: DetailReviewListAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.gone()
        binding = FragmentDetailBinding.bind(view)

        detailViewModel.productId = args.id

        detailReviewListAdapterInit()

        binding.suggestCard.setOnClickListener {
            val action = DetailFragmentDirections.actionGlobalCreateReviewFragment(detailViewModel.productId, null)
            findNavController().navigate(action)
        }

        binding.buy.setOnClickListener {
            if (detailViewModel.orderIsEmpty) detailViewModel.createOrder()
            else detailViewModel.addToOrder()
        }

        detailViewModel.getProduct()
        detailViewModel.getProductReviewList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    detailViewModel.getProduct.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                detailViewModel.product = it.value
                                collectOrder()
                            }

                            is ResultWrapper.Failure -> {
                                hideBuyButton()
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click {
                                    detailViewModel.getOrder()
                                }
                            }
                        }
                    }
                }

                launch {
                    detailViewModel.createOrder.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                setData()
                                showImage()
                            }

                            is ResultWrapper.Failure -> {
                                hideBuyButton()
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click {
                                    detailViewModel.getOrder()
                                }
                            }
                        }
                    }
                }

                launch {
                    detailViewModel.updateOrderResponse.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }

                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                detailViewModel.orderContainProduct = true
                                setData()
                            }

                            is ResultWrapper.Failure -> {
                                hideBuyButton()
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click {
                                    detailViewModel.getOrder()
                                }
                            }
                        }
                    }
                }

                launch {
                    detailViewModel.productReviewList.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                            }
                            is ResultWrapper.Success -> {
                                detailViewModel.reviewList = it.value
                                detailReviewListAdapter.submitList(detailViewModel.createReviewDataList())
                                binding.reviewRecyclerView.adapter?.notifyDataSetChanged()
                            }
                            is ResultWrapper.Failure -> {
                                detailViewModel.getOrder()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun detailReviewListAdapterInit() {
        detailReviewListAdapter = DetailReviewListAdapter{
            showReviewList(it)
        }
        binding.reviewRecyclerView.adapter = detailReviewListAdapter
        binding.reviewRecyclerView.addItemDecoration(OrderHistoryItemDecoration(8))
    }

    private fun showReviewList(productId: String) {
        val action = DetailFragmentDirections.actionGlobalReviewListFragment(productId)
        findNavController().navigate(action)
    }

    private fun CoroutineScope.collectOrder() {
        launch {
            detailViewModel.getOrder.collect {
                when (it) {
                    ResultWrapper.Loading -> {
                        binding.customView.onLoading()
                    }

                    is ResultWrapper.Success -> {
                        binding.customView.onSuccess()
                        if (it.value.isEmpty()) {
                            detailViewModel.orderIsEmpty = true
                            setData()
                            showImage()
                            showBuyButton()
                        } else {
                            hideBuyButton()
                            detailViewModel.order = it.value[0]
                            orderContainProduct()
                        }
                    }

                    is ResultWrapper.Failure -> {
                        hideBuyButton()
                        binding.customView.onFail(it.message.toString())
                        binding.customView.click {
                            detailViewModel.getOrder()
                        }
                    }
                }
            }
        }
    }

    private fun orderContainProduct() {
        for (i in detailViewModel.order.line_items) {
            if (i.product_id == detailViewModel.product.id) {
                detailViewModel.orderContainProduct = true
            }
        }
        setData()
        showImage()
    }

    private fun showBuyButton() {
        binding.apply {
            buy.visible()
        }
    }

    private fun hideBuyButton() {
        binding.apply {
            buy.gone()
        }
    }

    private fun setData() {
        binding.apply {
            productName.text = detailViewModel.product.name
            productRatingPoint.text = detailViewModel.product.average_rating
            productDescription.text = detailViewModel.product.description.cleaner()
            if (detailViewModel.product.sale_price.isNotBlank())
                productPrice.text = detailViewModel.product.sale_price
            else productPrice.text = detailViewModel.product.regular_price
            buy.isGone = detailViewModel.orderContainProduct
        }
    }

    private fun showImage() {
        val imageRecyclerAdapter = ImageRecyclerAdapter(detailViewModel.product.images)
        binding.viewPager.apply {
            adapter = imageRecyclerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            currentItem = 0
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
        }.attach()
    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visible()
    }
}