package com.ing.offroader.ui.activity.my_post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ing.offroader.data.model.userInfo.UserData
import com.ing.offroader.databinding.ItemPostBinding
import com.ing.offroader.ui.fragment.community.model.PostDTO

class MyPostAdapter(private val viewModel: MyPostViewModel) :
    ListAdapter<PostDTO, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostItemViewHolder(binding)
    }

    interface ItemMoreClick { fun itemMoreClick(user: FirebaseUser?, item: PostDTO?) }
    interface ItemHeartClick { fun itemHeartClick() }

    var moreClick : ItemMoreClick ?= null
    var heartClick : ItemHeartClick ?= null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val user = FirebaseAuth.getInstance().currentUser
        val item = getItem(position)


        (holder as PostItemViewHolder).apply {

            // 비트맵을 바인딩해주는 코드
//            postImage.setImageBitmap(item.images)
            Glide.with(holder.postImage.context).load(item.images).into(postImage)

            item.uid.toString().let {
                FirebaseFirestore.getInstance().collection("User").document(it).get().addOnSuccessListener { documentSnapShot ->
                    val user = documentSnapShot.toObject(UserData::class.java)
                    userid.text = user?.user_name.toString()
                    Glide.with(holder.profileImage.context).load(user?.photo_Url).into(profileImage)
                }
            }

            title.text = item.title.toString()
            content.text = (item.contents?: "").toString()
            likeCount.text = item.like.toString()
            userLevel.visibility = View.INVISIBLE
            moreButton.setOnClickListener {
                moreClick?.itemMoreClick(user, item)
            }


            val ds = item.upload_date.toString()
            val formattedDate =
                ds[0].toString() + ds[1].toString() + ds[2].toString() + ds[3].toString() + "." + ds[4].toString() + ds[5].toString() + "." + ds[6].toString() + ds[7].toString()
            date.text = formattedDate

        }
    }

    inner class PostItemViewHolder(binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root){

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