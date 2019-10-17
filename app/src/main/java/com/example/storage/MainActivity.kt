package com.example.storage

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 112
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStoragePermissions()
        btn_save_image.setOnClickListener {
            val bitmap = getBitmapFromAssets("test_image.png")
            MediaStore.Images.Media.insertImage(contentResolver,bitmap,"test_image","test_image")
            Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_LONG).show()
        }
        btn_save_video.setOnClickListener {

        }
    }

    private fun getBitmapFromAssets(fileName: String): Bitmap {
        val assetManager = assets
        var inputStream: InputStream? = null
        try {
            inputStream = assetManager.open("$fileName")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun setStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Permission to access the external storage is required for this app to download media.")
                        .setTitle("Permission required")
                    builder.setPositiveButton("OK") { dialog, id ->
                        Log.i("MainActivity", "Clicked")
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
                    }
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
                }
            } else {
                return
            }
        } else {
           return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return
            } else {
                Toast.makeText(baseContext, "Permission not granted, so image cant be stored in storage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
