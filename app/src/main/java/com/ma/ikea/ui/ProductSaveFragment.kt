package com.ma.ikea.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.ma.ikea.R
import com.ma.ikea.databinding.FragmentProductSaveBinding
import com.ma.ikea.domain.Product
import com.ma.ikea.domain.ProductValidator
import com.ma.ikea.logd
import com.ma.ikea.showAlert
import com.ma.ikea.validateInteger

class ProductSaveFragment : Fragment() {
    private var _binding: FragmentProductSaveBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logd("onCreateView called")
        _binding = FragmentProductSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logd("onViewCreated called")

        binding.saveButton.setOnClickListener { run { saveProduct() } }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logd("onDestroyView called")
        _binding = null
    }

    private fun getProduct(): Product {
        val name = binding.nameText.text.toString()
        val desc = binding.descText.text.toString()
        val company = binding.companyText.text.toString()
        val quantity = binding.quantityText.text.toString()
        val category = binding.categoryText.text.toString()
        val isle = binding.isleText.text.toString()

        return Product("null", name, desc, company, quantity.toInt(), category, isle)
    }

    private fun saveProduct() {
        if (!validateInteger(binding.quantityText.text.toString())) {
            activity?.let { showAlert(it, "Save error", "Quantity must be integer") }
            return
        }

        val product = getProduct()
        val message = ProductValidator.validate(product)

        if (message == "") {
            findNavController().navigate(
                R.id.action_ProductSaveFragment_to_ProductListFragment,
                bundleOf("save" to product)
            )
        }
        else {
            activity?.let { showAlert(it, "Save error", message) }
        }
    }
}
