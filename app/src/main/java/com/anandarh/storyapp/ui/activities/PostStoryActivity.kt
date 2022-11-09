package com.anandarh.storyapp.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import com.anandarh.storyapp.R
import com.anandarh.storyapp.databinding.ActivityAddStoryBinding
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.FileHelper.Companion.fileFromUri
import com.anandarh.storyapp.utils.exceptionResponse
import com.anandarh.storyapp.viewmodels.PostStoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PostStoryActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel by viewModels<PostStoryViewModel>()

    private var fileName: String? = null
    private var permissionType: Int? = null
    private var lat: Double = -6.914744
    private var lon: Double = 107.609810

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null)
            fileName = savedInstanceState.getString("fileName")

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initializeAction()
        subscribeUI()
        getMyLocation()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("fileName", fileName)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "onPermissionsDenied: $requestCode :${perms.size}")

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (permissionType == 1)
            openCamera()
        else if (permissionType == 2)
            openExplorer()
    }

    private fun requestPermissionAll() {
        permissionType = 1
        // Ask for one permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_rationale),
                REQUEST_CODE_PERMISSIONS,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        else
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_rationale),
                REQUEST_CODE_PERMISSIONS,
                Manifest.permission.CAMERA
            )
    }

    private fun requestPermissionStorage() {
        permissionType = 2
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.permission_rationale),
            REQUEST_CODE_PERMISSIONS,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun hasPermissionCamera(): Boolean =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) EasyPermissions.hasPermissions(
            this,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) else EasyPermissions.hasPermissions(
            this,
            Manifest.permission.CAMERA
        )

    private fun hasPermissionStorage(): Boolean =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) EasyPermissions.hasPermissions(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) else true

    private fun initializeAction() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.btnGallery.setOnClickListener { openExplorer() }
        binding.btnPost.setOnClickListener { postStory() }
    }

    private fun subscribeUI() {
        viewModel.postState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Loading -> isLoading(true)
                is DataState.Success -> successHandler()
                is DataState.Error -> errorHandler(dataState.exception)
            }
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun isLoading(boolean: Boolean) {
        if (boolean) {
            binding.btnPost.isEnabled = false
            binding.btnCamera.isEnabled = false
            binding.btnGallery.isEnabled = false
            binding.etDescription.isEnabled = false
            binding.btnPost.text = this.getText(R.string.posting)
        } else {
            binding.btnPost.isEnabled = true
            binding.btnCamera.isEnabled = true
            binding.btnGallery.isEnabled = true
            binding.etDescription.isEnabled = true
            binding.btnPost.text = this.getText(R.string.post_story)
        }
    }

    private fun successHandler() {
        isLoading(false)
        setResult(RESULT_OK)
        finish()
    }

    private fun errorHandler(exception: Exception) {
        isLoading(false)
        val response = exceptionResponse(exception)
        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
    }

    private fun openCamera() {
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val imgFile = File(this.filesDir, "$name.jpg")
        val contentUri = getUriForFile(this, "$packageName.provider", imgFile)
        fileName = contentUri.toString()

        // Request camera permissions
        if (hasPermissionCamera()) {
            cameraLauncher.launch(contentUri)
        } else {
            // Ask for one permission
            requestPermissionAll()
        }
    }

    private fun openExplorer() {
        // Request camera permissions
        if (hasPermissionStorage()) {
            explorerLauncher.launch("image/*") // only display image type
        } else {
            // Ask for one permission
            requestPermissionStorage()
        }

    }

    private val cameraLauncher = registerForActivityResult(TakePicture()) { success ->
        if (success) {
            Picasso.get().load(fileName).into(binding.ivPhoto)
            binding.tvSelect.visibility = View.GONE
        } else
            binding.tvSelect.visibility = View.VISIBLE
    }

    private val explorerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                fileName = uri.toString()

                Picasso.get().load(fileName).into(binding.ivPhoto)
                binding.tvSelect.visibility = View.GONE

            } else
                binding.tvSelect.visibility = View.VISIBLE
        }

    private fun postStory() {
        binding.tilDescription.isErrorEnabled = binding.etDescription.text.isNullOrBlank()
        if (binding.etDescription.text.isNullOrBlank()) {
            binding.tilDescription.error = getString(R.string.required_field_message)
            return
        }

        if (fileName == null) {
            Toast.makeText(
                this,
                getString(R.string.no_image_selected),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        viewModel.postStory(
            description = binding.etDescription.text.toString(),
            photo = fileFromUri(this, Uri.parse(fileName))!!,
            lat = lat,
            lon = lon
        )
    }


    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HHmmssSSS"
        private const val TAG = "GGWPS"
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


}