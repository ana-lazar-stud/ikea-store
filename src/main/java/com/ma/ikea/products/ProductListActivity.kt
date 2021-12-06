package com.ma.ikea.products

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ma.ikea.R
import com.ma.ikea.databinding.ActivityProductListBinding
import com.ma.ikea.logd
import com.ma.ikea.product.ProductEditActivity

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var productsViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logd("onCreate called")

        setContentView(R.layout.activity_product_list)

        productListAdapter = ProductListAdapter(this)
        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)

        val productList = findViewById<RecyclerView>(R.id.productList)
        productList.adapter = productListAdapter
        productList.layoutManager = LinearLayoutManager(this)

        val listMessage = findViewById<TextView>(R.id.list_message)
        productsViewModel.products.observe(this, { value ->
            if (value.isEmpty()) {
                listMessage.visibility = View.VISIBLE
            }
            else {
                listMessage.visibility = View.INVISIBLE
            }
            productListAdapter.products = value
        })

        val addButton = findViewById<FloatingActionButton>(R.id.add_button)
        addButton.setOnClickListener {
            val intent = Intent(this@ProductListActivity, ProductEditActivity::class.java)
            productEditActivityLauncher.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logd("onDestroy")
    }

    val productEditActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val message = data.getStringExtra("MESSAGE")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            }
        }
}
