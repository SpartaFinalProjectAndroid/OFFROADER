package com.mit.offroader.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mit.offroader.R
import com.mit.offroader.databinding.ItemHomeCardBinding

class HomeHoriAdapter(private val items: ArrayList<HomeUiState> = ArrayList()) :
    RecyclerView.Adapter<HomeHoriAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_card, parent, false)
        val view = ItemHomeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val item = items[position]
            val paddingValue = holder.parentView.paddingStart

            title.text = item.name
            des.text = item.address
            with(blur) {
                setupWith(mainCard)
                    .setBlurEnabled(true)
                    .setBlurRadius(10f)
            }
            if (item.images != null) {
            Glide.with(itemView)
                .load(item.images[0])
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(img)
            }

            if (position == items.size - 1) parentView.setPadding(parentView.paddingLeft, parentView.paddingTop, parentView.paddingRight + paddingValue, parentView.paddingBottom)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(binding: ItemHomeCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.cardTitle
        val des = binding.cardDes
        val blur = binding.i1FkView
        val img = binding.ivMainImage
        val mainCard = binding.mainCard
        val parentView = binding.clCardParent
    }
}