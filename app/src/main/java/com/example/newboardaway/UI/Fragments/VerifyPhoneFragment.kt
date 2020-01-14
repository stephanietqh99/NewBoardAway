package com.example.newboardaway.UI.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.newboardaway.R
import com.example.newboardaway.Utils.toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_verify_phone.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class VerifyPhoneFragment : Fragment() {

    private var verificationId:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPhone.visibility =  View.VISIBLE
        layoutVerification.visibility = View.GONE

        button_send_verification.setOnClickListener {
            val phone = edit_text_phone.text.toString().trim()//remove space

            if(phone.isEmpty()||phone.length!=9){
                edit_text_phone.error = "Please enter a valid phone"
                edit_text_phone.requestFocus()
                return@setOnClickListener
            }

            val phoneNumber = '+' + ccp.selectedCountryCode + phone

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60,TimeUnit.SECONDS,requireActivity(),phoneAuthCallbacks
                //phone Number, how much to wait for time out, the unit for calculate the time out
                //pass activity, callback
            )

            layoutPhone.visibility =  View.GONE
            layoutVerification.visibility = View.VISIBLE
        }

        button_verify.setOnClickListener {
            val code = edit_text_code.text.toString().trim()

            if(code.isEmpty()){
                edit_text_code.error = "Please send Mr.VerifcationCode here"
                edit_text_code.requestFocus()
                return@setOnClickListener
            }

            verificationId?.let {
                val credential = PhoneAuthProvider.getCredential(it,code)
                addPhoneNum(credential)
            }
        }

    }

    val phoneAuthCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential?) {
            //verification successful and perform automatically
            phoneAuthCredential?.let {
                addPhoneNum(phoneAuthCredential)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException?) {
            context?.toast(exception?.message!!)
        }

        override fun onCodeSent(verifcationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
            //if cannot automatic detect the code sent, user enter manually
            super.onCodeSent(verifcationId, token)
            this@VerifyPhoneFragment.verificationId = verificationId
        }

    }
    private fun addPhoneNum(phoneAuthCredential: PhoneAuthCredential){
        FirebaseAuth.getInstance().currentUser?.updatePhoneNumber(phoneAuthCredential)
            ?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    context?.toast("Phone added successfully")
                    val action = VerifyPhoneFragmentDirections.actionPhoneVerified()
                    Navigation.findNavController(button_verify).navigate(action)
                }else{
                  context?.toast(task.exception?.message!!)
                }
            }
    }
}
