package com.example.favdish.view.activites

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateFavDishBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class AddUpdateFavDishActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityAddUpdateFavDishBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateFavDishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolBar()
        binding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.iv_add_dish_image -> customImageSelectionDialog()
            }
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        // GALLERY
        binding.tvGallery.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, GALLERY)

//            Dexter.withContext(this).withPermission(
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//            ).withListener(object : PermissionListener {
//
//                override fun onPermissionGranted(report: PermissionGrantedResponse?) {
//                    val galleryIntent = Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                        )
//                    startActivityForResult(galleryIntent, GALLERY)
//                }
//
//                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
//                    Toast.makeText(
//                        this@AddUpdateFavDishActivity,
//                        "You don't have the gallery permission to select an image",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    p0: PermissionRequest?,
//                    p1: PermissionToken?
//                ) {
//                    showRationalDialigforPermissions()
//                }
//
//            }).onSameThread().check()
            dialog.dismiss()
        }

        // CAMERA
        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialigforPermissions()
                }

            }).onSameThread().check()
            dialog.dismiss()


        }
        dialog.show()
    }

    private fun showRationalDialigforPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions required for this feature")
            .setPositiveButton("Go to settings")
            { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                data?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    binding.ivDishImage.setImageBitmap(thumbnail)
                    binding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.baseline_edit_24))
                }
            }else if (requestCode == GALLERY){
                data?.let {
                    val selectedPhototUri = data.data
                    binding.ivDishImage.setImageURI(selectedPhototUri)
                    binding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.baseline_edit_24))
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this@AddUpdateFavDishActivity,"Cancelled",Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val CAMERA = 1
        const val GALLERY = 2
    }
}


