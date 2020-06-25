package com.pedolu.smkcodingchallenge2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedolu.smkcodingchallenge2.R
import com.pedolu.smkcodingchallenge2.data.model.indonesia.ProvinsiItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.provinsi_indonesia_item.view.*


class IndonesiaAdapter(
    private val context: Context, private val items:
    List<ProvinsiItem>, private val listener: (ProvinsiItem) -> Unit
) :
    RecyclerView.Adapter<IndonesiaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            context, LayoutInflater.from(context).inflate(
                R.layout.provinsi_indonesia_item,
                parent, false
            )
        )

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)
    }

    class ViewHolder(val context: Context, override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val txtProvinsi = containerView.txtProvinsi
        private val txtRecovered = containerView.txtRecovered
        private val txtConfirmed = containerView.txtConfirmed
        private val txtDeath = containerView.txtDeath
        fun bindItem(item: ProvinsiItem, listener: (ProvinsiItem) -> Unit) {
            txtProvinsi.text = item.attributes.provinsi
            txtConfirmed.text = item.attributes.kasusPosi.toString()
            txtRecovered.text = item.attributes.kasusSemb.toString()
            txtDeath.text = item.attributes.kasusMeni.toString()
            containerView.setOnClickListener { listener(item) }
        }
    }
}
