package ru.ndevelop.reuser.ui.scan

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.ndevelop.reuser.MainActivity
import ru.ndevelop.reuser.R


class ScanFragment : Fragment(){

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mContext: Context
    private lateinit var infoText:TextView
    private lateinit var ivNFC:ImageView
    private lateinit var d: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_scan, container, false)
        initView(root)

        return root
    }

    @SuppressLint("NewApi")
    fun initView(root: View){
        infoText = root.findViewById(R.id.tv_start_scan)
        ivNFC = root.findViewById(R.id.iv_nfc)
        d = ivNFC.drawable
        if (d is AnimatedVectorDrawable) {
            (d as AnimatedVectorDrawable).registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    (d as AnimatedVectorDrawable).start()
                }
            })
            (d as AnimatedVectorDrawable).start()
        }

    }




}