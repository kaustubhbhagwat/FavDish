package com.example.favdish.view.activites

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateFavDishBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

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

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){
                        Toast.makeText(this@AddUpdateFavDishActivity,"All permissions granted",Toast.LENGTH_LONG).show()
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

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){
                        Toast.makeText(this@AddUpdateFavDishActivity,"All have Gallery permissions!! All good to go",Toast.LENGTH_LONG).show()
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

    private fun showRationalDialigforPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permissions required for this feature")
            .setPositiveButton("Go to settings")
            {_,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e : ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }.show()

    }
}