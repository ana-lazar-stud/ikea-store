package com.ma.ikea.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ma.ikea.R
import com.ma.ikea.domain.Product
import com.ma.ikea.logd

class ProductListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    var products = emptyList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onProductClick: View.OnClickListener = View.OnClickListener { view ->
        val product = view.tag as Product
        fragment.findNavController().navigate(
            R.id.action_ProductListFragment_to_ProductEditFragment,
            bundleOf("product" to product)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        logd("onCreateViewHolder called")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        logd("onBindViewHolder called position $position")

        val product = products[position]

        holder.itemView.setOnClickListener(onProductClick)
        holder.itemView.tag = product
        holder.nameTextView.text = product.name
        holder.companyTextView.text = product.company
        holder.quantityTextView.text = product.quantity.toString()
    }

    override fun getItemCount() = products.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name)
        val companyTextView: TextView = view.findViewById(R.id.company)
        val quantityTextView: TextView = view.findViewById(R.id.quantity)
    }
}
