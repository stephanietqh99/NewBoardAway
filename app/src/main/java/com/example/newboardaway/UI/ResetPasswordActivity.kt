package com.example.newboardaway.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.newboardaway.R
import com.example.newboardaway.Utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        button_reset_password.setOnClickListener {
            val email = text_email.text.toString().trim()

            if (email.isEmpty()) {
                text_email.error = "Please enter your email"
                text_email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                text_email.error = "Your email format is not valid. Please enter again"
                text_email.requestFocus()
                return@setOnClickListener
            }

            progressbar.visibility = View.VISIBLE
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task->
                progressbar.visibility = View.GONE
                if(task.isSuccessful){
                    this.toast("Password reset request is sent, please check your email.")
                }else{
                    this.toast(task.exception?.message!!)
                }
            }
        }
    }




}
