package com.ing.offroader.ui.activity.add_post

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ing.offroader.data.model.addpost.EditPostDTO
import com.ing.offroader.databinding.ActivityAddPostBinding
import com.ing.offroader.ui.fragment.community.MyApplication
import com.ing.offroader.ui.fragment.community.adapter.CommunityAdapter
import org.apache.commons.io.output.ByteArrayOutputStream

private const val TAG = "AddPostActivity"

class AddPostActivity : AppCompatActivity() {
    private var _binding: ActivityAddPostBinding? = null
    private val binding get() = _binding!!
    private val addPostViewModel by viewModels<AddPostViewModel> {
        AddPostViewModelFactory((this.application as MyApplication).postRepository)
    }
    private var editPostInfo : EditPostDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddPostBinding.inflate(layoutInflater)

        editPostInfo = intent.getParcelableExtra("POST_INFO")

        setContentView(binding.root)
        initView()
        initObserver()
    }

    private fun initObserver() {
        addPostViewModel.addPostUiState.observe(this) {
            if (it == null) {
                Log.d(TAG, "initObserver: homeuistate가 널")
            } else {
                if (it.errorMessage == null) {
                    Log.d(TAG, "initObserver: finish")
                    finish()
                } else {
                    if (it.errorMessage.isEmpty().not()) {
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initView() {
        setUpEditActivity()
        setupListener()
    }

    private fun setUpEditActivity() = with(binding){
        if (editPostInfo != null) {
            etTitle.setText(editPostInfo?.title)
            if (editPostInfo?.content == "null") {
                Log.d(TAG, "setUpEditActivity: ${editPostInfo?.content}")
                etContent.text = Editable.Factory.getInstance().newEditable(" ")
            } else {
                etContent.setText(editPostInfo?.content)
            }
            val storage = Firebase.storage("gs://offroader-event.appspot.com")
            val storageRef =
                storage.reference.child("Offroader_res/post_image/${editPostInfo?.postId}.jpg")

            // 디비 스토리지에서 받아온 값을 메모리에 저장하려고 함. 앱 메모리보다 큰 사진을 불러오면 크래시가 나기 때문에 불러올 때 메모리의 크기 제한을 둠.
            val ONE_MEGABYTE: Long = 1024 * 1024
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // 바이트어레이를 비트맵으로 변환해주는 코드
                val image = BitmapFactory.decodeByteArray(it, 0, it.size)
                // 비트맵을 바인딩해주는 코드
                ivSelectedImg.setImageBitmap(image)
            }.addOnFailureListener {
                Log.d(TAG, "onBindViewHolder: 사진 받아오는거 실패함. 알아서 하셈.")
            }

        }
    }

    private fun setupListener() = with(binding) {
        etTitle.addTextChangedListener { addPostViewModel.titleChangedListener(etTitle.text.toString()) }
        etContent.addTextChangedListener { addPostViewModel.contentChangedListener(etContent.text.toString()) }
        tvComplete.setOnClickListener { addPostViewModel.setOnCompleteButton() }
        ivAddImage.setOnClickListener { imgPicker() }
        // TODO : 2. 카메라로 이동해서 사진 찍는 코드
    }

    //이미지 픽커 라이브러리를 사용해 간단하게 편집할 수 있고, 사진의 최대 크기와 용량을 제한함
    private fun imgPicker() {
        ImagePicker.with(this)
            .galleryOnly() //갤러리에서만 사진을 불러올 수 있도록 지정
            .compress(1024) //일메가로압축
            .maxResultSize(1080, 1080) //또는 이미지크기 가세로 1080 제한
            .cropSquare() //사진의 비율을 1:1로 지정
            .createIntent { intent -> imageResult.launch(intent) } //class 처음에 작성한 이미지 선택 Intent 실행
    }

    //가져온 사진 uri를 이미지로 변경하고 byteArray로 사진을 전달
    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                //정상적으로 이미지를 가져온 경우
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!

                    //이미지를 포스트 액티비티의 사진 부분에 적용하고
                    binding.ivSelectedImg.setImageURI(fileUri)

                    //가져온 사진 uri를 이미지로 변경해
                    val bitmap = (binding.ivSelectedImg.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    //addPostViewModel를 통해 Firebase에 전달
                    addPostViewModel.addImageUri(data)
                }

                //에러가 발생한 경우 어떤 에러인지 토스트로 알림
                ImagePicker.RESULT_ERROR -> { Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show() }

                //선택을 취소한 경우 토스트로 알림
                else -> { Toast.makeText(this, "선택을 취소하셨습니다.", Toast.LENGTH_SHORT).show() }
            }
        }
}