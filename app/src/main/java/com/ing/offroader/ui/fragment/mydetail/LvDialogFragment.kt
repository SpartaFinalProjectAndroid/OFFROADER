package com.ing.offroader.ui.fragment.mydetail

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.ing.offroader.R
import com.ing.offroader.databinding.DialogLvBinding

class LvDialogFragment : DialogFragment() {
    private var _binding : DialogLvBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLvBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.ivBack.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

//        context?.dialogFragmentResize(this, 0.9f, 0.65f)
        //추후 가로 넓이가 긴 디바이스의 경우 max width를 적당한 크기로 지정해 주는 것을 고려해야 함
        dialog?.window?.setLayout((resources.displayMetrics.widthPixels * 0.9f).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        context?.dialogRoundedBackground(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Dialog 크기 비율 조절
    private fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }

    private fun Context.dialogRoundedBackground(dialogFragment: DialogFragment) {

        dialog?.window?.setBackgroundDrawable(
            context?.let { ContextCompat.getDrawable(it, R.drawable.ic_rounded_background_dialog) }
        )
    }
}