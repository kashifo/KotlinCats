package com.github.kotlincats.favorite

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import android.view.WindowManager


private const val ARG_PARAM1 = "param1"

class ImageDialogFragment : DialogFragment() {

    private var param1: String? = null
    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!
            .setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.github.kotlincats.R.layout.fragment_image_dialog, container, false)

        Log.e(TAG, "param1=$param1")
        if(!param1.isNullOrEmpty()) {
            val iv:PhotoView = view.findViewById<PhotoView>(com.github.kotlincats.R.id.imgDialog)
            Glide.with(this).load(param1).placeholder(com.github.kotlincats.R.drawable.loading).into(iv)
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ImageDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
