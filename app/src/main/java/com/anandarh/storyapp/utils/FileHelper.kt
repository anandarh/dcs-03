package com.anandarh.storyapp.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class FileHelper {
    companion object {
        fun fileFromUri(context: Context, uri: Uri): File? {
            try {

                val name = SimpleDateFormat("yyyy-MM-dd-HHmmssSSS", Locale.US)
                    .format(System.currentTimeMillis())
                val imgFile = File.createTempFile(name, ".jpg", context.filesDir)


                val contentResolver: ContentResolver = context.contentResolver

                val inputStream: InputStream =
                    contentResolver.openInputStream(uri) ?: return null
                val outputStream: OutputStream = FileOutputStream(imgFile)

                val buf = ByteArray(1024)
                var len: Int
                while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)

                outputStream.close()
                inputStream.close()

                return imgFile
            } catch (e: Exception) {
                return null
            }
        }
    }
}