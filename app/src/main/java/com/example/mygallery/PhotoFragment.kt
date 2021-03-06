package com.example.mygallery


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo.*


private const val ARG_URI = "uri"

class PhotoFragment : Fragment() {
    private var uri : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(           // 프래그먼트에 표시될 뷰 생성
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   // 뷰가 생성된 직후에 호출됨
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(uri).into(imageView)
    }

    companion object {
        @JvmStatic
        fun newInstance(uri: String) =  // 프래그먼트 생성
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri)
                }
            }
    }
}
