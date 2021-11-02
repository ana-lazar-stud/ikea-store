package com.ma.ikea.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ma.ikea.R
import com.ma.ikea.databinding.FragmentProductListBinding
import com.ma.ikea.domain.Product
import com.ma.ikea.logd
import com.ma.ikea.viewmodels.ProductsViewModel


class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var productsViewModel: ProductsViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logd("onCreateView called")
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logd("onViewCreated called")
        setUpProductList()
        doActions()
        binding.addButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_ProductListFragment_to_ProductSaveFragment)
        }
    }

    private fun setUpProductList() {
        productListAdapter = ProductListAdapter(this)
        binding.productList.adapter = productListAdapter
        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        productsViewModel.products.observe(viewLifecycleOwner, { value ->
            productListAdapter.products = value
        })
    }

    private fun doActions() {
        if (arguments?.containsKey("save") == true) {
            val product = arguments?.get("save") as Product
            productsViewModel.saveProduct(product)
            Toast.makeText(activity,"Product saved successfully", Toast.LENGTH_LONG).show()
        }
        if (arguments?.containsKey("update") == true) {
            val product = arguments?.get("update") as Product
            productsViewModel.updateProduct(product)
            Toast.makeText(activity,"Product updated successfully", Toast.LENGTH_LONG).show()
        }
        if (arguments?.containsKey("delete") == true) {
            val id = arguments?.get("delete") as String
            productsViewModel.removeProduct(id)
            Toast.makeText(activity,"Product deleted successfully", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logd("onDestroyView called")
        _binding = null
    }
}