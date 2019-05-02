package com.example.visitor.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.visitor.interfaces.Callbacks
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

object ImageUtils {

    private lateinit var compressedImage: File
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    @SuppressLint("CheckResult")
    fun uploadImage(context: Context, imagePath: String, callbacks: Callbacks){
        Compressor(context)
            .compressToFileAsFlowable(File(imagePath))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { file ->
                    compressedImage = file
                    firebaseUpload(callbacks)
                },
                { throwable ->
                    throwable.printStackTrace()
//                        toast(throwable.message)
                })
    }

    private fun firebaseUpload(callbacks: Callbacks) {
        val ref = storageRef.child("images/${compressedImage.name}")
        val uploadTask = ref.putFile(Uri.fromFile(compressedImage))
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                if (downloadUri != null) {
                    callbacks.getImageURI(downloadUri)
                }
                Log.d("download",downloadUri.toString())
            } else {

            }
        }
    }
}