package com.midland.ynote.Fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.midland.ynote.R
import com.midland.ynote.Utilities.ChatGPT

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OpenAI.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpenAI : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var glowRel: RelativeLayout? = null
    private var snackBarRel: RelativeLayout? = null
    var animatorSet: AnimatorSet? = null
    lateinit var responseTV: TextView
    private lateinit var questionTV: TextView
    private lateinit var queryEdt: TextInputEditText
    private var chatGPTAPI: ChatGPT? = null


    var url = "https://api.openai.com/v1/engines/davinci/jobs"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_open_a_i, container, false)
        chatGPTAPI = ChatGPT(context)
        snackBarRel = v.findViewById(R.id.snackBarRel)
        glowRel = v.findViewById(R.id.glowRel)
        responseTV = v.findViewById(R.id.idTVResponse)
        questionTV = v.findViewById(R.id.idTVQuestion)
        queryEdt = v.findViewById(R.id.idEdtQuery)


        animatorSet = AnimatorSet()
        val fadeOut: android.animation.ObjectAnimator = android.animation.ObjectAnimator.ofFloat(glowRel, "alpha", 0.5f, 0.1f)
        fadeOut.duration = 500
        val fadeIn: android.animation.ObjectAnimator = android.animation.ObjectAnimator.ofFloat(glowRel, "alpha", 0.1f, 0.5f)
        fadeIn.duration = 500
        animatorSet!!.play(fadeIn).after(fadeOut)
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatorSet!!.start()
                super.onAnimationEnd(animation)
            }
        })
        animatorSet!!.start()


        // adding editor action listener for edit text on below line.

        queryEdt.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                // setting response tv on below line.
                glowRel!!.visibility = View.VISIBLE
                responseTV.text = "Please wait.."
                // validating text
                if (queryEdt.text.toString().isNotEmpty()) {
                    // calling get response to get the response.
                    chatGPTAPI!!.getResponse(queryEdt.text.toString(), object :
                        ChatGPT.ChatGPTResponseListener {
                        override fun onResponse(response: String) {
                            Log.d("ChatGPT Response", response)
                            glowRel!!.visibility = View.GONE
                            responseTV.text = response
                        }

                        override fun onError(error: String) {
                            Log.e("ChatGPT Error", error)
                            glowRel!!.visibility = View.GONE
                            responseTV.text = error
                        }
                    })
                } else {
                    glowRel!!.visibility = View.GONE
                    responseTV.text = "No response"
                    Toast.makeText(context, "Please enter your query..", Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false
        })

        return v
    }

    companion object Companion{
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OpenAI.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OpenAI().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}



