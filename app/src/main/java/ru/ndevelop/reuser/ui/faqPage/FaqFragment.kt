package ru.ndevelop.reuser.ui.faqPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.ndevelop.reuser.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [faq1.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var fragmentType: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentType = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return when(fragmentType){
            1-> inflater.inflate(R.layout.fragment_faq1, container, false)
            2 ->  inflater.inflate(R.layout.fragment_faq2, container, false)
            3 ->  inflater.inflate(R.layout.fragment_faq3, container, false)
            else -> inflater.inflate(R.layout.fragment_faq1, container, false)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            FaqFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}
