package com.mit.offroader.ui.fragment.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mit.offroader.R
import com.mit.offroader.databinding.ItemHomeCardBinding
import com.mit.offroader.ui.activity.sandetail.SanDetailActivity

class HomeHoriAdapter(private val items: ArrayList<HomeUiState> = ArrayList()) :
    RecyclerView.Adapter<HomeHoriAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemHomeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val item = items[position]
            val paddingValue = holder.parentView.paddingStart

            title.text = item.name
            des.text = item.address

            //텍스트가 담기는 뷰에 블러 효과 부여
            with(blur) {
                setupWith(mainCard)
                    .setBlurEnabled(true)
                    .setBlurRadius(10f)
            }

            if (item.images != null) {
                Glide.with(itemView)
                    .load(item.images[0])
                    .placeholder(R.drawable.ic_launcher_foreground) //로딩 중, 로띠 학습하면 사용해 보기
                    .into(img)
            }

            //마지막 항목에 padding을 부여해 좌우가 같도록 함, 자세한건 xml 확인 요
            if (position == items.size - 1) parentView.setPadding(parentView.paddingLeft, parentView.paddingTop, parentView.paddingRight + paddingValue, parentView.paddingBottom)

            //아이템 클릭 시 해당 위치의 이름을 산 디테일로 전달
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, SanDetailActivity::class.java)

                //산 디테일에 item name을 전달하고
                intent.putExtra("name", item.name)
                //시작
                context.startActivity(intent)
            }
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