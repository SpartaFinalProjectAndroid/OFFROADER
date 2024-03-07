package com.mit.offroader.ui.fragment.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mit.offroader.R
import com.mit.offroader.databinding.ItemHomeAttributeBinding
import com.mit.offroader.databinding.ItemHomeCardTitleBinding
import com.mit.offroader.databinding.ItemHomeEventBinding
import com.mit.offroader.databinding.ItemHomeEventTitleBinding
import com.mit.offroader.databinding.LayoutHomeHoriRvBinding


class HomeMultiViewTypeAdapter(private val context: Context, private val mList : ArrayList<HomeUiState>) :
    ListAdapter<HomeUiData, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<HomeUiData>() {
            override fun areItemsTheSame(oldItem: HomeUiData, newItem: HomeUiData): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: HomeUiData, newItem: HomeUiData): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is HomeUiData.First -> FIRST
            is HomeUiData.Second -> SECOND
            is HomeUiData.Third -> THIRD
            is HomeUiData.Fourth -> FOURTH
            else -> ATTRIBURE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FIRST -> {
                CardTitleViewHolder(
                    ItemHomeCardTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            SECOND -> {
                CardViewHolder(
                    LayoutHomeHoriRvBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            THIRD -> {
                EventTitleViewHolder(
                    ItemHomeEventTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            FOURTH -> {
                EventViewHolder(
                    ItemHomeEventBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                AttributeViewHolder(
                    ItemHomeAttributeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)) {
            is HomeUiData.First -> (holder as CardTitleViewHolder).bind(currentList[position] as HomeUiData.First)
            is HomeUiData.Second -> (holder as CardViewHolder).bind(HomeHoriAdapter())
            is HomeUiData.Third -> (holder as EventTitleViewHolder).bind(currentList[position] as HomeUiData.Third)
            is HomeUiData.Fourth -> (holder as EventViewHolder).bind(currentList[position] as HomeUiData.Fourth)
            else -> (holder as AttributeViewHolder).bind(currentList[position] as HomeUiData.Attribute)
        }
    }

    // 카드 타이틀 영역 뷰홀더
    inner class CardTitleViewHolder(private val binding: ItemHomeCardTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiData.First) = with(binding) {

            // 카드 타이틀 뷰홀더

        }
    }

    // 카드 영역 뷰홀더
//    inner class CardViewHolder(private val binding: LayoutHomeHoriRvBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        @RequiresApi(Build.VERSION_CODES.Q)
//        fun bind(item: HomeUiData.Second) = with(binding) {
////            val vBlur = i1FkView
////            val title = cardTitle
////            val des = cardDes
////
////            /* 기존 사용부
////                        context.let {
////                            vBlur.setBlurCB(it, vBlur, 20, object : BlurCompletionListener {
////                                override fun onCompleted() {
////                                    title.visibility = View.VISIBLE
////                                    des.visibility = View.VISIBLE
////                                }
////                            }
////            */
////
////            with(vBlur) {
////                setupWith(mainCard)
////                    .setBlurEnabled(true)
////                    .setBlurRadius(10f)
////            }
//
//            //구현해야 하는 것 가로 리사이클러 뷰 xml, 그 리사이클러 뷰 어답터
//            val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
//            binding.rvHori.layoutManager = layoutManager
//
//            // 리사이클러 뷰에 대한 어댑터 설정
//            // 여기서 YourAdapter는 가로 스크롤 리사이클러 뷰를 위한 어댑터의 인스턴스입니다.
//            val adapter = HomeHoriAdapter()
//            binding.rvHori.adapter = adapter
//
//
//
//
//        }
//    }

    inner class CardViewHolder(private val binding: LayoutHomeHoriRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(homeHoriAdapter: HomeHoriAdapter) = with(binding) {

            val adapter = HomeHoriAdapter(mList)
            binding.rvHori.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvHori.adapter = adapter

//            val adapter = MyAdapter(dataList)
//            binding.recyclerView.adapter = adapter
//            binding.recyclerView.layoutManager = LinearLayoutManager(this)



        }
    }

    // 이벤트 타이틀 영역 뷰홀더
    inner class EventTitleViewHolder(private val binding: ItemHomeEventTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiData.Third) = with(binding) {

            // 이벤트 타이틀 뷰홀더

        }
    }

    // 이벤트 영역 뷰홀더
    inner class EventViewHolder(private val binding: ItemHomeEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiData.Fourth) = with(binding) {
            tvEventTitle.text = item.title
            tvEventDes.text = item.des
            tvEventDate.text = item.date.toString()

//            //임시 테스트
//            root.setOnClickListener {
//                val dialog = AlertDialog.Builder(context)
//                    .setTitle(item.title)
//                    .setMessage(item.des)
//                    .setPositiveButton("OK", null)
//                    .create()
//                dialog.show()
//            }

            //자유로운 창 조절을 위해 Dialog Fragment로 추후 전환 고려
            root.setOnClickListener {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_home_event, null)
                val title = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
                val des = dialogView.findViewById<TextView>(R.id.tv_dialog_des)
                val date = dialogView.findViewById<TextView>(R.id.tv_dialog_date)
                val img = dialogView.findViewById<ImageView>(R.id.iv_dialog_img)
                val btn = dialogView.findViewById<CardView>(R.id.cv_btn_close)

                title.text = item.title
                des.text = item.des
                date.text = item.date.toString()
                Glide.with(context)
                    .load(item.image)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(img)

                val dialog = AlertDialog.Builder(context).apply {
                    setView(dialogView)
//                    setPositiveButton("확인", null)
//                    setNegativeButton("취소", null)
                }.create()

                val displayMetrics = DisplayMetrics()
                dialog.window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                val maxHeight = (displayMetrics.heightPixels - 100).toInt() //배율 사용시 Double -> toInt 필요
                val maxWidth = (displayMetrics.widthPixels - 100).toInt() //배율 사용시 Double -> toInt 필요

                dialog.show()
                dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.apply_corner_radius_20))
                dialog.window?.setLayout(maxWidth, maxHeight)

                //다이얼로그 크기를 고정했기 때문에 ScrollView 안의 ConstraintLayout의 최소 높이를 강제해야 함, 딱 맞추기 위해 - margin 해줌
                val dialogCl = dialogView.findViewById<ConstraintLayout>(R.id.cl_dialog)
                val dialogSV = dialogView.findViewById<ScrollView>(R.id.sv_dialog)
                dialogCl.minHeight = maxHeight - dialogSV.marginTop

                btn.setOnClickListener {
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.cancel()
                    }, 300)  // 1초 후에 실행
                }




                /*
                // 다이얼로그 창 높이를 화면 크기의 80% 초과 시 제한하려 했으나 dialogHeight를 측정할 수 없음
                // 디버그 모드 브레이크 포인트로 확인해 봐도 ?:0 출력함
                dialog.setOnShowListener {
                    val displayMetrics = DisplayMetrics()
                        dialog.window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

                    val dialogHeight = dialog.window?.decorView?.height ?:0
                    val maxHeight = (displayMetrics.heightPixels * 0.7).toInt()
                    if (dialogHeight > maxHeight) {
                            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, maxHeight)
                        } else {
                            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                        }
                }
                */

            }

            // 안쪽 when에 absoluteAdapterPosition을 사용하다 뷰 홀더의 앞 뒤 아이템의 개수가 바뀌면 코드를 수정해야 하는 문제를 해당 뷰 홀더의 아이템 개수를 카운트함
            val checkPosition = 1 + currentList.subList(0, absoluteAdapterPosition)
                .count { it is HomeUiData.Fourth }
            val checkSize = currentList.count { it is HomeUiData.Fourth }
            when (checkSize) {
                //아이템이 1개인 경우 모든 모서리가 둥근 배경을 사용
                1 -> {
                    binding.root.background = ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.apply_recycler_bg_single
                    )
                }
                //아이템이 2개인 경우 위 아래 다른 배경을 사용
                2 -> {
                    when (checkPosition) {
                        1 ->
                            binding.root.background = ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.apply_recycler_bg_top
                            )

                        checkSize ->
                            binding.root.background = ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.apply_recycler_bg_bot
                            )
                    }
                }
                //아이템이 3개 이상인 경우 첫 번째와 마지막을 제외하고 같은 배경을 사용
                else -> {
                    when (checkPosition) {
                        //해당 뷰 홀더의 위치를 가져옴
                        1 ->
                            binding.root.background = ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.apply_recycler_bg_top
                            )

                        in 2 until checkSize ->
                            binding.root.background = ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.apply_recycler_bg_mid
                            )

                        checkSize ->
                            binding.root.background = ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.apply_recycler_bg_bot
                            )
                        //해당 뷰 홀더의 위치에 아이템 개수를 더함
                    }
                }
            }
//            Log.d("ppoppo","개수:$checkSize, ${currentList.size} $checkPosition")
        }
    }

    inner class AttributeViewHolder(private val binding: ItemHomeAttributeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeUiData.Attribute) = with(binding) {

            /*//단일 클릭 액션
            llFlatIcon.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/uicons"))
                context.startActivity(intent)
            }
            */

            /*//카운팅 할 Boolean을 만들어서 두번 클릭을 감지
            var clickCounter = false

            llFlatIcon.setOnClickListener {
                //두번 클릭 = clickCounter true 상태에서 클릭
                //출처 웹사이트로 이동
                if (clickCounter) {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/uicons"))
                    context.startActivity(intent)
                //clickCounter를 ture로 카운팅하고 한번 더 눌러야 열림을 토스트로 알림
                } else {
                    clickCounter = true
                    Toast.makeText(context, "한번 더 눌러서 열기", Toast.LENGTH_SHORT).show()
                    //clickCounter를 ture로 카운팅하고 2초 동안 클릭하지 않은 경우 false로 원복
                    Handler(Looper.getMainLooper()).postDelayed({
                        clickCounter = false
                    }, 2000)
                }
            }
            */

            //핸들러 사용 프로그래스 바 적용함 제일 나음
            llFlatIcon.setOnClickListener {
                //root인 프레그먼트에서 rootView로 액티비티까지 이동한 후 ProgressBar 참조, binding 안되니까 변수로 할당
                val pBar = root.rootView.findViewById<ProgressBar>(R.id.pb_goto)
                val customToast = root.rootView.findViewById<ConstraintLayout>(R.id.cl_customToast)
                val btCancel = root.rootView.findViewById<TextView>(R.id.tv_goto_cancel)
                val time: Long = 2000
                val interval: Long = 1 // .001초 단위로 업데이트
                val increment = 100f / (time / interval)
                var currentPos = 0f
                //각 위치에 Handler()를 사용했는데 removeCallbacksAndMessages가 같이 안먹는 문제가 있어 변수화로 변경
                //추측으로는 remov~~ 에 사용한 Handler()만 초기화 된게 아닐까 생각함
                val handler = Handler()

                customToast.visibility = View.VISIBLE

                handler.postDelayed(object : Runnable {
                    override fun run() {
                        currentPos += increment
                        pBar.progress = currentPos.toInt()

                        // 진행 상태가 100%가 아니면 계속해서 업데이트
                        if (currentPos < 100) {
                            handler.postDelayed(this, interval)
                        } else {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.flaticon.com/uicons")
                            )
                            customToast.visibility = View.GONE
                            context.startActivity(intent)
                        }
                    }
                }, interval)

                btCancel.setOnClickListener {
                    customToast.visibility = View.GONE
                    handler.removeCallbacksAndMessages(null)
                }
            }

            /* //애니메이션 사용 취소하면 웹사이트 열리는 문제
            llFlatIcon.setOnClickListener {
                //root인 프레그먼트에서 rootView로 액티비티까지 이동한 후 ProgressBar 참조, binding 안되니까 변수로 할당
                val pBar = root.rootView.findViewById<ProgressBar>(R.id.pb_goto)
                val customToast = root.rootView.findViewById<ConstraintLayout>(R.id.cl_customToast)
                val btCancel = root.rootView.findViewById<TextView>(R.id.tv_goto_cancel)
                val time: Long = 5000 // 애니메이션 지속 시간을 5초로 설정

                customToast.visibility = View.VISIBLE

                // ObjectAnimator를 사용하여 pBar의 progress 속성을 0에서 100까지 변경
                val animation = ObjectAnimator.ofInt(pBar, "progress", 0, 100)
                animation.duration = time // 애니메이션 지속 시간 설정
//                animation.interpolator = DecelerateInterpolator() // 애니메이션 속도를 점차 느리게 하는 인터폴레이터 설정
                animation.start()

                animation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        // 애니메이션이 끝날 때 실행할 동작
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/uicons"))
                        context.startActivity(intent)
                    }
                })

                btCancel.setOnClickListener {
                    customToast.visibility = View.GONE
                    animation.cancel() // 애니메이션 취소
                }
            }
*/


        }
    }


    companion object {
        private const val FIRST = 1
        private const val SECOND = 2
        private const val THIRD = 3
        private const val FOURTH = 4
        private const val ATTRIBURE = 5
    }
}

