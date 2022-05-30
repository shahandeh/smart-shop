package com.example.smartshop.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.smartshop.R
import com.example.smartshop.data.model.Image
import com.example.smartshop.data.model.Product
import com.example.smartshop.databinding.FragmentDetailBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.ImageRecyclerAdapter
import com.example.smartshop.util.cleaner
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var binding: FragmentDetailBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility = View.GONE
        binding = FragmentDetailBinding.bind(view)

        detailViewModel.getProduct(args.id)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                detailViewModel.getProduct.collect { resultWrapper ->
                    when (resultWrapper) {
                        is ResultWrapper.Failure -> {
                            binding.customView.onFail(resultWrapper.message.toString())
                            binding.customView.click {
                                detailViewModel.getProduct(args.id)
                            }
                        }
                        ResultWrapper.Loading -> {
                            binding.customView.onLoading()
                        }
                        is ResultWrapper.Success -> {
                            binding.customView.onSuccess()
                            resultWrapper.value?.let { setData(it) }
                        }
                    }
                }
            }
        }
    }

    private fun setData(product: Product) {
        binding.apply {
            productName.text = product.name
            if (product.sale_price.isNotBlank()) productPrice.text = product.sale_price
            else productPrice.text = product.regular_price
            productRatingPoint.text = product.average_rating
            productDescription.text = product.description.cleaner()
        }
        showImage(product.images)
    }

    private fun showImage(list: List<Image>) {
        val imageRecyclerAdapter = ImageRecyclerAdapter(list)
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
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility = View.VISIBLE
    }
}