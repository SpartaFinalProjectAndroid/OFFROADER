package com.ing.offroader.ui.activity.add_post

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ing.offroader.databinding.ActivityAddPostBinding
import com.ing.offroader.ui.fragment.community.MyApplication
import org.apache.commons.io.output.ByteArrayOutputStream

private const val TAG = "AddPostActivity"

class AddPostActivity : AppCompatActivity() {
    private var _binding: ActivityAddPostBinding? = null
    private val binding get() = _binding!!
    private val addPostViewModel by viewModels<AddPostViewModel> {
        AddPostViewModelFactory((this.application as MyApplication).postRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddPostBinding.inflate(layoutInflater)
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
        setupListener()
    }

    private fun setupListener() = with(binding) {
        etTitle.addTextChangedListener { addPostViewModel.titleChangedListener(etTitle.text.toString()) }
        etContent.addTextChangedListener { addPostViewModel.contentChangedListener(etContent.text.toString()) }
        tvComplete.setOnClickListener { addPostViewModel.setOnCompleteButton() }
        ivAddImage.setOnClickListener { imgPicker() }
        ivAddPhoto.setOnClickListener { imgPickerCamera() }
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

    private fun imgPickerCamera() {
        ImagePicker.with(this)
            .cameraOnly() //카메라에서만 사진을 불러올 수 있도록 지정
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