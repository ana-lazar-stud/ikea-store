package com.ma.ikea.ui

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.ma.ikea.R
import com.ma.ikea.databinding.FragmentProductEditBinding
import com.ma.ikea.domain.Product
import com.ma.ikea.domain.ProductValidator
import com.ma.ikea.logd
import com.ma.ikea.showAlert
import com.ma.ikea.validateInteger

class ProductEditFragment : Fragment() {
    private var _binding: FragmentProductEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logd("onCreateView called")
        _binding = FragmentProductEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logd("onViewCreated called")
        val product = arguments?.get("product") as Product
        loadProductProperties(product)
        binding.editButton.setOnClickListener { run { updateProduct() } }
        binding.deleteButton.setOnClickListener { run { deleteProduct() } }
    }

    private fun loadProductProperties(product: Product) {
        binding.idText.text = product.id
        binding.nameText.setText(product.name)
        binding.descText.setText(product.description)
        binding.companyText.setText(product.company)
        binding.quantityText.setText(product.quantity.toString())
        binding.categoryText.setText(product.category)
        binding.isleText.setText(product.isle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logd("onDestroyView called")
        _binding = null
    }

    private fun getProduct(): Product {
        val id = binding.idText.text.toString()
        val name = binding.nameText.text.toString()
        val desc = binding.descText.text.toString()
        val company = binding.companyText.text.toString()
        val quantity = binding.quantityText.text.toString()
        val category = binding.categoryText.text.toString()
        val isle = binding.isleText.text.toString()
        return Product(id, name, desc, company, quantity.toInt(), category, isle)
    }

    private fun updateProduct() {
        if (!validateInteger(binding.quantityText.text.toString())) {
            activity?.let { showAlert(it, "Save error", "Quantity must be integer") }
            return
        }

        val product = getProduct()
        val message = ProductValidator.validate(product)

        if (message == "") {
            findNavController().navigate(
                R.id.action_ProductEditFragment_to_ProductListFragment,
                bundleOf("update" to product)
            )
        }
        else {
            activity?.let { showAlert(it, "Save error", message) }
        }
    }

    private fun deleteProduct() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Delete")
                setMessage("Are you sure you want to delete this product?")
                setPositiveButton("confirm"
                ) { _, _ ->
                    val productId = binding.idText.text
                    findNavController().navigate(
                        R.id.action_ProductEditFragment_to_ProductListFragment,
                        bundleOf("delete" to productId)
                    )
                }
                setNegativeButton("cancel") { _, _ -> }
            }
            builder.create()
            builder.show()
        }
    }
}