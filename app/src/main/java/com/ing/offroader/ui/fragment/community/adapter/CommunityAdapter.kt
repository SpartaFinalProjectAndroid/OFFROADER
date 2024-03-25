package com.ing.offroader.ui.fragment.community.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ing.offroader.R
import com.ing.offroader.data.model.userInfo.UserData
import com.ing.offroader.databinding.ItemPostBinding
import com.ing.offroader.ui.fragment.community.model.PostDTO
import com.ing.offroader.ui.fragment.community.viewmodel.CommunityViewModel
import okhttp3.internal.wait

private const val TAG = "태그 : CommunityAdapter"

class CommunityAdapter(private val viewModel: CommunityViewModel) :
    ListAdapter<PostDTO, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    interface ItemMoreClick { fun itemMoreClick(item: PostDTO?) }
    interface ItemHeartClick { fun itemHeartClick() }

    var moreClick : ItemMoreClick ?= null
    var heartClick : ItemHeartClick ?= null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)
        val loggedInUser = FirebaseAuth.getInstance().currentUser


        (holder as PostItemViewHolder).apply {



//            postImage.setImageBitmap(item.images)
            Log.d(TAG, "onBindViewHolder: ${item.images}")
//            postImage.setImageURI(item.images)
            Glide.with(holder.postImage.context).load(item.images).into(postImage)

            item.uid.toString().let{
                FirebaseFirestore.getInstance().collection("User").document(it).get().addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(UserData::class.java)
                    userid.text = user?.user_name.toString()
                    Glide.with(holder.profileImage.context).load(user?.photo_Url).into(holder.profileImage)
                }
            }

            moreButton.setOnClickListener {
                moreClick?.itemMoreClick(item)

            }

            title.text = item.title.toString()
            content.text = (item.contents ?: "").toString()
            likeCount.text = item.like.toString()
            userLevel.visibility = View.INVISIBLE



            val ds = item.upload_date.toString()
            val formattedDate =
                ds[0].toString() + ds[1].toString() + ds[2].toString() + ds[3].toString() + "." + ds[4].toString() + ds[5].toString() + "." + ds[6].toString() + ds[7].toString()
            date.text = formattedDate

        }
    }

    inner class PostItemViewHolder(binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

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

        init {
            postItem.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = getItem(position)

//            viewModel.getSelectedItem(item)
        }
    }

    companion object {

//        private const val TAG = "태그 : CommunityAdapter"
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostDTO>() {
            override fun areItemsTheSame(oldItem: PostDTO, newItem: PostDTO): Boolean {
//                Log.d(TAG, "areItemsTheSame: ${oldItem.post_id}, ${newItem.post_id}")
                return oldItem.post_id == newItem.post_id
            }

            override fun areContentsTheSame(oldItem: PostDTO, newItem: PostDTO): Boolean {
//                Log.d(TAG, "areItemsTheSame: ${oldItem}, ${newItem}")
                return oldItem == newItem
            }
        }
    }


}