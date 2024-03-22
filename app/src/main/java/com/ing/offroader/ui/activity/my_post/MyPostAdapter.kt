package com.ing.offroader.ui.activity.my_post

import android.graphics.BitmapFactory
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val user = FirebaseAuth.getInstance().currentUser
        val item = getItem(position)

        (holder as PostItemViewHolder).apply {


            val storage = Firebase.storage("gs://offroader-event.appspot.com")
            // 저장소 위치 참조
            val storageRef = storage.reference
            // 파일 위치 참조
            val pathRef = storageRef.child("Offroader_res/post_image/${item.post_id}.jpg")

            // 디비 스토리지에서 받아온 값을 메모리에 저장하려고 함. 앱 메모리보다 큰 사진을 불러오면 크래시가 나기 때문에 불러올 때 메모리의 크기 제한을 둠.
            val ONE_MEGABYTE: Long = 1024*1024
            pathRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // 바이트어레이를 비트맵으로 변환해주는 코드
                val image = BitmapFactory.decodeByteArray(it,0,it.size)
                // 비트맵을 바인딩해주는 코드
                postImage.setImageBitmap(image)
            }.addOnFailureListener {
                Log.d(TAG, "onBindViewHolder: 사진 받아오는거 실패함. 알아서 하셈.")
            }


            userid.text = user!!.displayName
            Log.d(TAG, "onBindViewHolder: providerData: ${user.providerData}")
            title.text = item.title.toString()
            content.text = (item.contents?: "").toString()
            likeCount.text = item.like.toString()
            userLevel.visibility = View.INVISIBLE

            Glide.with(holder.profileImage.context).load(user.photoUrl).into(holder.profileImage)

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

        private const val TAG = "태그 : CommunityAdapter"
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostDTO>() {
            override fun areItemsTheSame(oldItem: PostDTO, newItem: PostDTO): Boolean {
                Log.d(TAG, "areItemsTheSame: ${oldItem.post_id}, ${newItem.post_id}")
                return oldItem.post_id == newItem.post_id
            }

            override fun areContentsTheSame(oldItem: PostDTO, newItem: PostDTO): Boolean {
                Log.d(TAG, "areItemsTheSame: ${oldItem}, ${newItem}")
                return oldItem == newItem
            }
        }
    }


}