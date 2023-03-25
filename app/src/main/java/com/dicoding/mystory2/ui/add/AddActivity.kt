package com.dicoding.mystory2.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystory2.R
import com.dicoding.mystory2.databinding.ActivityAddBinding
import com.dicoding.mystory2.ui.main.StoryActivity
import com.dicoding.mystory2.viewModel.ViewModelFactory
import java.io.File

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var getFile: File? = null
    private lateinit var addStoryUserViewModel: AddViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Access Denied.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

       binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addStoryUserViewModel =
            ViewModelProvider(this, ViewModelFactory(this))[AddViewModel::class.java]


        addStoryUserViewModel.change.observe(this) {
            uploaded(it)
        }

        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonUpload.setOnClickListener {
            addStoryUserViewModel.getDescription(binding.descriptionEditText.text.toString())
            addStoryUserViewModel.uploadImage()
            uploadImage()

        }
    }

    private fun uploadImage(){
        if (binding.descriptionEditText.text.isEmpty()) {
            val message = getString(R.string.please_fill_out)
            binding.descriptionEditText.error = message
            Toast.makeText(this@AddActivity, message, Toast.LENGTH_SHORT).show()
            return
        }

    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


    private fun uploaded(value: Boolean) {
        if (value) {
            val intent = Intent(this@AddActivity, StoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )


            binding.previewImage.setImageBitmap(result)
            addStoryUserViewModel.getFile(getFile)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddActivity)

            getFile = myFile
            addStoryUserViewModel.getFile(getFile)

            binding.previewImage.setImageURI(selectedImg)
        }
    }


}

