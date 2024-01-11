package com.example.favdish.view.activites

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.R
import com.example.favdish.utils.Constants
import com.example.favdish.databinding.ActivityAddUpdateFavDishBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding
import com.example.favdish.databinding.DialogCustomListBinding
import com.example.favdish.view.adapters.ListItemAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class AddUpdateFavDishActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityAddUpdateFavDishBinding
    private var imagePath: String = ""

    private lateinit var mCustomImageSelectionDialog: Dialog
    private lateinit var mCustomListDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateFavDishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolBar()
        binding.ivAddDishImage.setOnClickListener(this)
        binding.etCategory.setOnClickListener(this)
        binding.etCookingTime.setOnClickListener(this)
        binding.etDirectionToCook.setOnClickListener(this)
        binding.etType.setOnClickListener(this)
        binding.btnAddDish.setOnClickListener(this)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }

                R.id.et_type -> {
                    customItemsDialog(
                        "Type",
                        Constants.dishTypes(),
                        Constants.DISH_TYPE
                    )
                    return
                }

                R.id.et_category -> {
                    customItemsDialog(
                        "Category",
                        Constants.dishCategories(),
                        Constants.DISH_CATEGORY
                    )
                    return
                }

                R.id.et_cooking_time -> {

                    customItemsDialog(
                        "Cooking Time",
                        Constants.dishCookingTime(),
                        Constants.DISH_COOKING
                    )
                    return
                }

                R.id.btn_add_dish -> {
                    val title = binding.etTitle.text.toString().trim { it <= ' ' }
                    val type = binding.etType.text.toString().trim { it <= ' ' }
                    val category = binding.etCategory.text.toString().trim { it <= ' ' }
                    val ingredients = binding.etIngredients.text.toString().trim { it <= ' ' }
                    val cookingTimeInMins = binding.etCookingTime.text.toString().trim { it <= ' ' }
                    val cookingDirections =
                        binding.etDirectionToCook.text.toString().trim { it <= ' ' }

                    when {
                        TextUtils.isEmpty(imagePath) -> {
                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "Image not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "Title cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "Type cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(category) -> {
                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "Category cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(ingredients) -> {
                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "Ingredients cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(cookingTimeInMins) -> {
                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "CookingTimeInMins cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(cookingDirections) -> {
                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "CookingDirections cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            Toast.makeText(
                                this@AddUpdateFavDishActivity,
                                "All entries are valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    return
                }

                R.id.iv_add_dish_image -> customImageSelectionDialog()
            }
        }
    }

    private fun customImageSelectionDialog() {
        mCustomImageSelectionDialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        mCustomImageSelectionDialog.setContentView(binding.root)

        // GALLERY
        binding.tvGallery.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
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
            mCustomImageSelectionDialog.dismiss()
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
            mCustomImageSelectionDialog.dismiss()


        }
        mCustomImageSelectionDialog.show()
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

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                data?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
//                    binding.ivDishImage.setImageBitmap(thumbnail)
                    binding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.baseline_edit_24
                        )
                    )
                    Glide.with(this@AddUpdateFavDishActivity)
                        .load(thumbnail)
                        .centerCrop()
                        .into(binding.ivDishImage)

                    imagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath", imagePath)
                }
            } else if (requestCode == GALLERY) {
                data?.let {
                    val selectedPhototUri = data.data
//                    binding.ivDishImage.setImageURI(selectedPhototUri)

                    Glide.with(this@AddUpdateFavDishActivity)
                        .load(selectedPhototUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("Error Image", "ERROR loading IMAGE")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                val bitmap: Bitmap = resource.toBitmap()
                                imagePath = saveImageToInternalStorage(bitmap)
                                Log.i("ImagePath", imagePath)
                                return false
                            }
                        })
                        .into(binding.ivDishImage)

                    // Replace the add icon with edit icon once the image is selected.
                    binding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.baseline_edit_24
                        )
                    )
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this@AddUpdateFavDishActivity, "Cancelled", Toast.LENGTH_LONG).show()
        }
    }

    private fun customItemsDialog(title: String, itemList: List<String>, selection: String) {
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = ListItemAdapter(this, itemList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: String, selection: String) {
        when (selection) {
            Constants.DISH_TYPE -> {
                mCustomListDialog.dismiss()
                binding.etType.setText(item)
                return
            }

            Constants.DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                binding.etCategory.setText(item)
                return
            }

            Constants.DISH_COOKING -> {
                mCustomListDialog.dismiss()
                binding.etCookingTime.setText(item)
                return
            }
        }

    }

    companion object {
        const val CAMERA = 1
        const val GALLERY = 2

        private const val IMAGE_DIRECTORY = "FavDishImages"
    }
}


