package com.anandarh.storyapp.ui.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.anandarh.storyapp.R
import com.anandarh.storyapp.databinding.ActivityCameraBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CameraActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityCameraBinding

    var fotoapparat: Fotoapparat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the listener for take photo button
        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.btnBack.setOnClickListener { finish() }

        initializeCamera()
    }

    override fun onStart() {
        super.onStart()
        // Request camera permissions
        if (hasPermissionAll()) {
            fotoapparat?.start()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_rationale),
                REQUEST_CODE_PERMISSIONS,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
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
        fotoapparat?.stop()
    }

    private fun initializeCamera() {
        fotoapparat = Fotoapparat(
            context = this,
            view = binding.cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = logcat(),
            cameraErrorCallback = { Log.e(TAG, "Camera error: ", it) }
        )
    }

    private fun hasPermissionAll(): Boolean =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) EasyPermissions.hasPermissions(
            this,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) else EasyPermissions.hasPermissions(
            this,
            Manifest.permission.CAMERA
        )

    private fun takePhoto() {
        val photoResult = fotoapparat?.takePicture()

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())

        val sd = Environment.getExternalStorageDirectory()
        val destinationFile = File(sd.absolutePath + "/.StoryApp", name)

        // Asynchronously converts photo to bitmap and returns the result on the main thread
        photoResult
            ?.saveToFile(destinationFile)
    }

    companion object {
        private const val TAG = "GGWPS"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}