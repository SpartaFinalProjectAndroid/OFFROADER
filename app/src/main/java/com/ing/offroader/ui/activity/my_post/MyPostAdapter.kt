package com.ing.offroader.ui.activity.my_post

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ing.offroader.data.model.addpost.PostModel
import com.ing.offroader.databinding.ItemPostBinding
import java.net.URLEncoder

class MyPostAdapter(private val context: Context, private val viewModel: MyPostViewModel) :
    ListAdapter<PostModel, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostItemViewHolder(binding)
    }

    private var attached = true
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        attached = false
    }

    interface ItemMoreClick {
        fun itemMoreClick(user: String?, item: PostModel?)
    }

    interface ItemHeartClick {
        fun itemHeartClick()
    }

    var moreClick: ItemMoreClick? = null
    var heartClick: ItemHeartClick? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)


        if (attached) {
            (holder as PostItemViewHolder).apply {


                // 비트맵을 바인딩해주는 코드
//            postImage.setImageBitmap(item.images)

                Glide.with(context).load(item.images).into(postImage)
                Log.d(TAG, "onBindViewHolder: ${item.userName}")
                userid.text = item.userName

                
                Glide.with(context).load(item.userProfileImage).into(profileImage)
                title.text = item.title.toString()
                content.text = (item.contents ?: "").toString()
                likeCount.text = item.like.toString()
                userLevel.visibility = View.INVISIBLE
                moreButton.setOnClickListener {
                    moreClick?.itemMoreClick(item.uid, item)
                }


                val ds = item.upload_date.toString()
                val formattedDate =
                    ds[0].toString() + ds[1].toString() + ds[2].toString() + ds[3].toString() + "." + ds[4].toString() + ds[5].toString() + "." + ds[6].toString() + ds[7].toString()
                date.text = formattedDate


            }
        }
    }

    inner class PostItemViewHolder(binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var postItem: ConstraintLayout = binding.clItemPost

        val profileImage = binding.ivProfileImage
        val userid = binding.tvId
        val userLevel = binding.tvLevel
        val title = binding.tvTitle
        val content = binding.tvContent
        val postImage = binding.ivUploadedImage
        val likeCount = binding.tvLikeCount
        val date = binding.tvDate
        val moreButton = binding.ivMore
        val heartButton = binding.ivHeart

    }


    companion object {

        private const val TAG = "태그 : CommunityAdapter"
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//                Log.d(TAG, "areItemsTheSame: ${oldItem.post_id}, ${newItem.post_id}")
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//                Log.d(TAG, "areItemsTheSame: ${oldItem}, ${newItem}")
                return oldItem == newItem
            }
        }
    }


}