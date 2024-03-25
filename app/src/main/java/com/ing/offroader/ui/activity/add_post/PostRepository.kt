package com.ing.offroader.ui.activity.add_post

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ing.offroader.ui.fragment.community.model.PostDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class PostRepository {
    private val TAG = "태그 : AddPostRepository"

    private var user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    // setPostItems
    private val _setPostItems: MutableLiveData<ArrayList<PostDTO?>?> = MutableLiveData()
    val setPostItems: LiveData<ArrayList<PostDTO?>?> = _setPostItems

    // myPostItems
    private val _myPostItems: MutableLiveData<ArrayList<PostDTO?>?> = MutableLiveData()
    val myPostItems: LiveData<ArrayList<PostDTO?>?> = _myPostItems

    /** 사진 전송 관련 */
    private val specifiedStorage = Firebase.storage("gs://offroader-event.appspot.com")
    private val storageRef = specifiedStorage.reference

    var myPostItemArray: ArrayList<PostDTO?> = arrayListOf()

    init {
        setPost()

    }


    fun setMyImage(post: PostDTO?) {


        val pathRef = storageRef.child("Offroader_res/post_image/${post?.post_id}.jpg")

        // 디비 스토리지에서 받아온 값을 메모리에 저장하려고 함. 앱 메모리보다 큰 사진을 불러오면 크래시가 나기 때문에 불러올 때 메모리의 크기 제한을 둠.
        val ONE_MEGABYTE: Long = 1024 * 1024

        pathRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // 바이트어레이를 비트맵으로 변환해주는 코드

            val image = BitmapFactory.decodeByteArray(it, 0, it.size)
            post?.images = image
            val item = myPostItemArray.find {
                it?.post_id == post?.post_id
            }
            if (item == null) {
                myPostItemArray.add(post)
            }


            // 비트맵을 바인딩해주는 코드
            val items = myPostItemArray
            _myPostItems.value = items

            Log.d(TAG, "setMyPost: ${myPostItems.value?.size}, ${post?.title}")

        }.addOnFailureListener {
            Log.d(TAG, "onBindViewHolder: 사진 받아오는거 실패함. 알아서 하셈.")
        }
    }


    fun setMyPost() {
        Log.d(TAG, "setMyPost: ")
        if (user == null) {
            Log.e(TAG, "유저 없음.")
        }
        try {
            db.collection("Community").whereEqualTo("uid", user?.uid.toString())
                .addSnapshotListener { value, error ->
                    myPostItemArray = arrayListOf()
                    if (error != null) {
                        Log.e(TAG, "Failed with ${error.message}.", error)
                        return@addSnapshotListener
                    }
                    value?.documents?.forEach {
                        val post = it.toObject(PostDTO::class.java)
                        setMyImage(post)
                    }

                }
        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }
    }

    fun setPost() {
        Log.d(TAG, "setPost: ")
        try {

            db.collection("Community").orderBy("upload_date", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    myPostItemArray = arrayListOf()
                    // 에러가 있을경우 다시 받아오도록 코드 작성
                    if (error != null) {
                        Log.e(TAG, "Failed with ${error.message}.", error)
                        return@addSnapshotListener
                    }

                    // 다큐먼츠 돌려서 모든 포스팅 정보를 postItems에 저장
                    value?.documents?.forEach {
                        val post = it.toObject(PostDTO::class.java)
                        Log.d(TAG, "setPost: ${post?.title}")
                        setMyImage(post)
                    }

                }
        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }
    }

    fun editPost(title: String, content: String?, image: ByteArray?, postId: String?) {
        db.collection("Community").document(postId.toString()).get().addOnSuccessListener {
            val postDto = it.toObject(PostDTO::class.java)
            deleteImage(postId)
            saveImage(image, postId)

            val newPost = hashMapOf(
                "uid" to user!!.uid,
                "title" to title,
                "contents" to content,
                "like" to 0,
                "post_id" to postId,
                "san" to null,
                "upload_date" to postDto?.upload_date
            )

            try {
                db.collection("Community").document(newPost["post_id"].toString()).set(newPost)
                    .addOnSuccessListener {
                        // 온 석세스가 되었을 때 디비에서 값을 다시 받아와서 옵져빙 해줄 수 있도록 해주기
                        Log.d(TAG, "addPost: 게시물 디비에 저장하기 success 이제 setPost 함수 실행")

                    }.addOnFailureListener {
                        Log.d(TAG, "addPost: 게시물 올리기 Fail !")

                    }.addOnCompleteListener {
                        Log.d(TAG, "addPost: 온콤플릿 리스너는 업로드 실행이 끝날때까지 기다렸다가 실행되는건감?")
                        // 아니네 전에 실행되네...
                        myPostItemArray = arrayListOf()
                    }

            } catch (e: Exception) {
                Log.e(TAG, "FireStore Error: $e")
            }
        }


    }

    private fun deleteImage(postId: String?) {
        val ref = storageRef.child("Offroader_res/post_image/${postId}.jpg")
        ref.delete().addOnSuccessListener {
            Log.d(TAG, "delete successful")
        }.addOnFailureListener {
            Log.d(TAG, "delete Failed")
        }

    }

    private fun saveImage(image: ByteArray?, postId: String?) {
// 이미지를 저장할 위치를 지정
        val ref = storageRef.child("Offroader_res/post_image/${postId}.jpg")

        //putBytes 함수를 사용해 전달받은 이미지를 firebase에 업로드
        val uploadTask = ref.putBytes(image!!)
        //성공,실패 여부 확인 로그
        uploadTask.addOnFailureListener {
            Log.d(TAG, "사진 업로드 성공여부: 실패")

        }.addOnSuccessListener { taskSnapshot ->
            Log.d(TAG, "사진 업로드 성공여부: 성공")

        }.addOnCompleteListener {
            setPost()

        }
    }


    fun addPost(title: String, content: String?, image: ByteArray?) {
        Log.d(TAG, "addPost uid: ${user?.uid}")


        val postId = UUID.randomUUID().toString()

        saveImage(image, postId)
        val dateToLong = getDateTimeNow()


        Log.d(TAG, "addPost: $dateToLong")

        val newPost = hashMapOf(
            "uid" to user!!.uid,
            "title" to title,
            "contents" to content,
            "like" to 0,
            "post_id" to postId,
            "san" to null,
            "upload_date" to dateToLong
        )

        try {
            db.collection("Community").document(newPost["post_id"].toString()).set(newPost)
                .addOnSuccessListener {
                    // 온 석세스가 되었을 때 디비에서 값을 다시 받아와서 옵져빙 해줄 수 있도록 해주기
                    Log.d(TAG, "addPost: 게시물 디비에 저장하기 success 이제 setPost 함수 실행")

                }.addOnFailureListener {
                    Log.d(TAG, "addPost: 게시물 올리기 Fail !")

                }.addOnCompleteListener {
                    Log.d(TAG, "addPost: 온콤플릿 리스너는 업로드 실행이 끝날때까지 기다렸다가 실행되는건감?")
                    // 아니네 전에 실행되네...

                }

        } catch (e: Exception) {
            Log.e(TAG, "FireStore Error: $e")
        }
    }


    private fun getDateTimeNow(): Long {
        val date = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return date.format(formatter).toLong()
    }

}
