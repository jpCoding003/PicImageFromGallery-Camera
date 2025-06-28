package com.tops.gallery

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputBinding
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tops.gallery.databinding.ActivityMainBinding
import java.io.File
import java.net.URI


class MainActivity : AppCompatActivity() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var photoUri: Uri

    private lateinit var binding: ActivityMainBinding

    val takeCameraPick = registerForActivityResult(ActivityResultContracts.TakePicture()) { sucess ->
        if (sucess) {
            Log.d("PicturePicker", "Selected URI: $sucess")
            binding.imgageView1.setImageURI(photoUri)
        } else {
            Log.d("camera", "Image capture cancelled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnimage.setOnClickListener {
            pickImageFromGallery()
//           chooseFromWhereToPick()
        }

        binding.btnimage1.setOnClickListener {
            CaptureImageFromCamera()
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                    binding.imageView.setImageURI(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
        }
    }

    private fun chooseFromWhereToPick() {
//        AlertDialog.Builder()
    }

    private fun CaptureImageFromCamera() {
        // Step 3: Create temporary file
        val imageFile = File.createTempFile("IMG_", ".jpg", cacheDir)

        // Step 4: Get content URI using FileProvider
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            imageFile
        )

        // Step 5: Launch the camera
        takeCameraPick.launch(photoUri)
    }

    private fun pickImageFromGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}