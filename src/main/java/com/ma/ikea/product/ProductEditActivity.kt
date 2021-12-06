package com.ma.ikea.product

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ma.ikea.R
import com.ma.ikea.data.Product
import com.ma.ikea.databinding.ActivityProductEditBinding
import com.ma.ikea.isIntegerValid
import com.ma.ikea.logd

class ProductEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductEditBinding
    private lateinit var viewModel: ProductViewModel

    private var productId: Int? = null
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            productId = bundle.getInt("PRODUCT_ID", 0)
        }

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel.productFormState.observe(this, { state ->
            validateForm(state)
        })
        viewModel.error.observe(this, { exception ->
            if (exception != null) {
                logd("action error")
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.completed.observe(this, { completed ->
            if (completed) {
                logd("action completed")
                val response = Intent()
                response.putExtra("MESSAGE", "Action successful")
                setResult(Activity.RESULT_OK, response)
                finish()
            }
        })
        val id = productId
        if (id !== null) {
            viewModel.getById(id).observe(this, {
                logd("update items")
                if (it != null) {
                    product = it
                    loadProductProperties()
                }
            })
        }
        else {
            binding.deleteButton.visibility = View.GONE
        }

        binding.editButton.setOnClickListener { run { editProduct() } }
        binding.deleteButton.setOnClickListener { run { deleteProduct() } }
        binding.cancelButton.setOnClickListener {
            val response = Intent()
            response.putExtra("MESSAGE", "Action cancelled")
            setResult(Activity.RESULT_OK, response)
            finish()
        }
    }

    private fun validateForm(state: ProductFormState) {
        if (state.nameError != null) {
            binding.nameText.error = getString(state.nameError)
        }
        if (state.descriptionError != null) {
            binding.descText.error = getString(state.descriptionError)
        }
        if (state.companyError != null) {
            binding.companyText.error = getString(state.companyError)
        }
        if (state.categoryError != null) {
            binding.categoryText.error = getString(state.categoryError)
        }
        if (state.isleError != null) {
            binding.isleText.error = getString(state.isleError)
        }
    }

    private fun loadProductProperties() {
        binding.nameText.setText(product?.name)
        binding.descText.setText(product?.description)
        binding.companyText.setText(product?.company)
        binding.quantityText.setText(product?.quantity.toString())
        binding.categoryText.setText(product?.category)
        binding.isleText.setText(product?.isle)
    }

    private fun getProduct(): Product {
        val name = binding.nameText.text.toString()
        val desc = binding.descText.text.toString()
        val company = binding.companyText.text.toString()
        val quantity = binding.quantityText.text.toString()
        val category = binding.categoryText.text.toString()
        val isle = binding.isleText.text.toString()
        return Product(productId, name, desc, company, quantity.toInt(), category, isle)
    }

    private fun editProduct() {
        if (!isIntegerValid(binding.quantityText.text.toString())) {
            binding.quantityText.error = getString(R.string.invalid_quantity)
            return
        }
        val product = getProduct()
        viewModel.saveOrUpdate(product)
    }

    private fun deleteProduct() {
        this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Delete")
                setMessage("Are you sure you want to delete this product?")
                setPositiveButton("confirm"
                ) { _, _ ->
                    val id = productId
                    if (id !== null) {
                        viewModel.remove(id)
                    }
                }
                setNegativeButton("cancel") { _, _ -> }
            }
            builder.create()
            builder.show()
        }
    }
}
