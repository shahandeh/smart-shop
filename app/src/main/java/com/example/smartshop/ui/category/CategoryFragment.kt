package com.example.smartshop.ui.category

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
import com.example.smartshop.databinding.FragmentCategoryBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.CategoryListAdapter
import com.example.smartshop.ui.adapter.OrderHistoryItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {
    private lateinit var binding: FragmentCategoryBinding
    private val categoryViewModel by viewModels<CategoryViewModel>()
    private lateinit var categoryListAdapter: CategoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryBinding.bind(view)
        categoryListAdapter = CategoryListAdapter {
            click(it)
        }
        binding.recyclerView.adapter = categoryListAdapter
        binding.recyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerView.addItemDecoration(OrderHistoryItemDecoration(16))
        categoryViewModel.getCategoryList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.getCategoryList.collect { resultWrapper ->
                    when (resultWrapper) {
                        ResultWrapper.Loading -> {
                            binding.customView.onLoading()
                        }
                        is ResultWrapper.Success -> {
                            binding.customView.onSuccess()
                            resultWrapper.value?.let { categoryListAdapter.submitList(it) }
                        }
                        is ResultWrapper.Failure -> {
                            binding.customView.onFail(resultWrapper.message.toString())
                            binding.customView.click {
                                categoryViewModel.getCategoryList()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun click(id: Int) {
        val action = CategoryFragmentDirections.actionGlobalProductListFragment(null, id.toString())
        findNavController().navigate(action)
    }

}