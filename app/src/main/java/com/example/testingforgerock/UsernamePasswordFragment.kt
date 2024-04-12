package com.example.testingforgerock

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import org.forgerock.android.auth.Node
import org.forgerock.android.auth.callback.NameCallback
import org.forgerock.android.auth.callback.PasswordCallback


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UsernamePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsernamePasswordFragment : DialogFragment() {
    private var listener: MainActivity? = null
    private var node: Node? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_username_password, container, false)

        node = requireArguments().getSerializable("NODE") as Node?

        val username = view.findViewById<AppCompatEditText>(R.id.inputUsername)
        val password = view.findViewById<AppCompatEditText>(R.id.inputPassword)
        val next = view.findViewById<Button>(R.id.buttonContinue)
        next.setOnClickListener { v: View? ->
            dismiss()
            node!!.getCallback(NameCallback::class.java)
                .setName(username.getText().toString())
            node!!.getCallback(PasswordCallback::class.java)
                .setPassword(password.getText().toString().toCharArray())
            node!!.next(context, listener)
        }
        val cancel = view.findViewById<Button>(R.id.buttonCancel)
        cancel.setOnClickListener { v: View? -> dismiss() }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            listener = context
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.setAttributes(params as WindowManager.LayoutParams)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(node: Node) =
            UsernamePasswordFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("NODE", node)
                }
            }
    }
}