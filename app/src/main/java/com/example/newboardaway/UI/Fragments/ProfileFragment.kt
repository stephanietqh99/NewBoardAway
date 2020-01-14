package com.example.newboardaway.UI.Fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.newboardaway.R
import com.example.newboardaway.Utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var imageUri : Uri
    private val REQUEST_IMAGE_CAPTURE = 100 //track call back

    private val DEFAULT_IMAGE_URL = "https://ibb.co/8zdMV0c"

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser?.let { user->  //profile picture uri inside
            //downlload glide img lib and load img file
            Glide.with(this)
                .load(user.photoUrl)
                .into(image_view)

            edit_text_name.setText(user.displayName)

            text_email.text = user.email
            if(user.isEmailVerified){
                text_not_verified.visibility = View.INVISIBLE
            }
            else{
                text_not_verified.visibility = View.VISIBLE

            }
            text_phone.text =
                if(user.phoneNumber.isNullOrEmpty())
                    "Add Number"
                else
                    user.phoneNumber

        }

        image_view.setOnClickListener {
            takePictureIntent()
        }

        button_save.setOnClickListener{
            val photo = when{
                ::imageUri.isInitialized -> imageUri
                currentUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
                else ->currentUser.photoUrl
            }

            val name = edit_text_name.text.toString().trim()

            if(name.isEmpty()){
                edit_text_name.error = "Please enter your name"
                edit_text_name.requestFocus()
                return@setOnClickListener
            }

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name).setPhotoUri(photo).build()

            progressbar.visibility = View.VISIBLE
            currentUser?.updateProfile(updates)?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    context?.toast("Your details are saved")
                    progressbar.visibility = View.INVISIBLE
                }else{
                    context?.toast(task.exception?.message!!)
                }
            }
        }

        text_not_verified.setOnClickListener {
            currentUser?.sendEmailVerification()?.addOnCompleteListener {
                if(it.isSuccessful){
                    context?.toast("Verification email sent. Please check your mailbox.")
                }else{
                    context?.toast(it.exception?.message!!)
                }
            }
        }

        text_phone.setOnClickListener {
            val action = ProfileFragmentDirections.actionVerifyPhone()
            Navigation.findNavController(it).navigate(action)
        }

        text_email.setOnClickListener {
            val action = ProfileFragmentDirections.actionUpdateEmail()
            Navigation.findNavController(it).navigate(action)
        }

        text_password.setOnClickListener {
            val action = ProfileFragmentDirections.actionUpdatePassword()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun takePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent -> //capture image by camera
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            val imageBitmap = data?.extras?.get("data") as Bitmap

            uploadImageAndSaveUri(imageBitmap)

        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {

        val boas = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance().reference
            .child("profilePicture/${FirebaseAuth.getInstance().currentUser?.uid}")
        //save img in profilePicture storage

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,boas)
        val image = boas.toByteArray()

        val upload = storageRef.putBytes(image)

        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            progressbar_pic.visibility = View.INVISIBLE

            if(uploadTask.isSuccessful){
                //get downloaded uri successfully
                storageRef.downloadUrl.addOnCompleteListener { urlTask->
                    urlTask.result?.let {
                        imageUri = it
                        activity?.toast("Image Uploaded Successfully")

                        image_view.setImageBitmap(bitmap)
                    }
                }
            }else{
                uploadTask.exception?.let {
                    activity?.toast(it.message!!)
                }
            }

        }

    }


}
