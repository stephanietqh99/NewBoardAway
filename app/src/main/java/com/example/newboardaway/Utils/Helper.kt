package com.example.newboardaway.Utils


import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.newboardaway.UI.Fragments.ProfileFragment
import com.example.newboardaway.UI.HomeActivity
import com.example.newboardaway.UI.LoginActivity
import com.example.newboardaway.UI.home.EventFragment


fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.login() {
    val intent = Intent(this, HomeActivity::class.java).apply {
        //start fresh activity by closing all of previous activity
        //clear the previous task to prevent user see the previous page when press 'back' in phone keyboard
        //exp: User press back then accidently saw the register activity
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}



fun Context.logout() {
    val intent = Intent(this, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}