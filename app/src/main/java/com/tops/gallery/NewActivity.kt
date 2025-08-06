package com.tops.gallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tops.gallery.databinding.ActivityNewBinding
import java.io.File

class NewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBinding

    // Pic Image from Gallery
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>


    //Take Image By Camera
    private lateinit var photoUri: Uri
    val takeCameraPick = registerForActivityResult(ActivityResultContracts.TakePicture()) { sucess ->
        if (sucess) {
            Log.d("PicturePicker", "Selected URI: $sucess")
            binding.cameraimgview.setImageURI(photoUri)
        } else {
            Log.d("camera", "Image capture cancelled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGallery.setOnClickListener {
            // Pic Image from Gallery
            pickImageFromGallery()
        }

        binding.btnCamera.setOnClickListener {
            //Take Image By Camera
            CaptureImageFromCamera()
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.galleryimgview.setImageURI(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }



        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    // Pic Image from Gallery
    private fun pickImageFromGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    //Take Image By Camera
    private fun CaptureImageFromCamera() {
        //Create temporary file
        val imageFile = File.createTempFile("IMG_", ".jpg", cacheDir)

        //Get content URI using FileProvider
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            imageFile
        )

        //Launch the camera
        takeCameraPick.launch(photoUri)
    }
}