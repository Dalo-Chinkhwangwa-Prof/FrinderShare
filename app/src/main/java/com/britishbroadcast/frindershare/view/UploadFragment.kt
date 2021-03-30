package com.britishbroadcast.frindershare.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.britishbroadcast.frindershare.R
import com.britishbroadcast.frindershare.model.data.FrinderPost
import com.britishbroadcast.frindershare.viewmodel.FrinderViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.post_item_layout.*
import kotlinx.android.synthetic.main.upload_fragment_layout.*
import java.io.ByteArrayOutputStream

class UploadFragment : Fragment(R.layout.upload_fragment_layout) {


    var bitMap: Bitmap? = null

    private val viewModel: FrinderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upload_imageview.setOnClickListener {

            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 777)
        }

        upload_button.setOnClickListener {
            bitMap?.let {

                val outputStream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                val imageByteArray = outputStream.toByteArray()
                val storageReference = FirebaseStorage.getInstance()
                        .reference
                        .child("UPLOADS/${FirebaseAuth.getInstance().currentUser.uid}")

                val uploadTask = storageReference.putBytes(imageByteArray)
                uploadTask.addOnCompleteListener{
                    task ->
                     if(task.isSuccessful){
                        storageReference.downloadUrl.addOnCompleteListener { uriTask ->
                            if(uriTask.isSuccessful){

                                val imageLink = uriTask.result?.toString() ?:""
                                val userId = FirebaseAuth.getInstance().currentUser.uid

                                val post = FrinderPost().also {
                                    it.userId = userId
                                    it.imageUrl = imageLink
                                    it.description = caption_edittext.text.toString()
                                }

                                Log.d("TAG_X", imageLink)

                                viewModel.postThePost(post)

                            } else {
                                Log.d("TAG_X", "Error: ${uriTask.exception}")
                            }
                        }

                     } else {
                         Log.d("TAG_X", "Error: ${task.exception}")
                     }

                }


            } ?: Toast.makeText(requireContext(), "Image cannot be empty", Toast.LENGTH_SHORT).show()


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 777) {

             bitMap = data?.extras?.get("data") as Bitmap
            Glide.with(this)
                    .setDefaultRequestOptions(RequestOptions().centerCrop())
                    .load(bitMap)
                    .into(upload_imageview)

        }
    }
}