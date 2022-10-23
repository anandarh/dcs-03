package com.anandarh.storyapp.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.FileProvider.getUriForFile
import com.anandarh.storyapp.databinding.ActivityAddStoryBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class PostStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var tvSelect: TextView
    private lateinit var ivPhoto: ImageView
    private lateinit var btnCamera: MaterialButton
    private lateinit var btnGallery: MaterialButton
    private lateinit var btnPost: AppCompatButton
    private lateinit var etDescription: TextInputEditText

    private var fileName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null)
            fileName = savedInstanceState.getString("fileName")

        initializeUI()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("fileName", fileName)
    }

    private fun initializeUI() {
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.btnGallery.setOnClickListener { galleryLauncher.launch("image/*") }

        tvSelect = binding.tvSelect
        ivPhoto = binding.ivPhoto
        btnCamera = binding.btnCamera
        btnGallery = binding.btnGallery
        btnPost = binding.btnPost
        etDescription = binding.etDescription
    }

    private fun openCamera() {
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val imgFile = File(this.filesDir, "$name.jpg");
        val contentUri = getUriForFile(this, "$packageName.provider", imgFile)
        fileName = contentUri.toString()

        cameraLauncher.launch(contentUri)

    }


    private val cameraLauncher = registerForActivityResult(TakePicture()) { success ->
        if (success) {
            Picasso.get().load(fileName).into(ivPhoto)
            tvSelect.visibility = View.GONE
        } else
            tvSelect.visibility = View.VISIBLE
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                fileName = uri.toString()
                Picasso.get().load(fileName).into(ivPhoto)
                tvSelect.visibility = View.GONE
            } else
                tvSelect.visibility = View.VISIBLE
        }


    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HHmmssSSS"
    }


}