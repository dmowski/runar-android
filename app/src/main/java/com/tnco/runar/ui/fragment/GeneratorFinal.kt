package com.tnco.runar.ui.fragment

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.GeneratorFinalBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.viewmodel.MainViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GeneratorFinal : Fragment() {

    private var _binding: GeneratorFinalBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private companion object {
        const val QUALITY = 100
        const val AUTHORITY = "com.tnco.runar.provider"
        const val IMAGE_NAME = "image.jpeg"
        const val CHILD = "images"
        const val PAGE = "generator_final"
        const val TYPE = "image/jpeg"
    }
//    lateinit var twitter: ImageView
//    lateinit var facebook: ImageView
//    lateinit var instagram: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showCancelDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GeneratorFinalBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        twitter =view.findViewById(R.id.twitter)
//        facebook = view.findViewById(R.id.facebook)
//        instagram = view.findViewById(R.id.instagram)

        binding.share.setOnClickListener {
            AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SHARED)
            val bmp = (binding.imgFinal.drawable as BitmapDrawable).bitmap
            shareImage(bmp, requireContext())
        }

        binding.imgFinal.setImageBitmap(viewModel.backgroundInfo.first { it.isSelected }.img!!)

        binding.finalBack.setOnClickListener {
            showCancelDialog()
        }

        binding.save.setOnClickListener {
            AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SAVED)
            val fileName = generateFileName()
            val bmp = (binding.imgFinal.drawable as BitmapDrawable).bitmap
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, TYPE)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val resolver = requireActivity().contentResolver
                val uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    bmp.compress(
                        Bitmap.CompressFormat.JPEG,
                        QUALITY,
                        resolver.openOutputStream(uri)
                    )
                }
                val msg = resources.getString(R.string.image_saved)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            } else {
                checkPermissionAndSave()
            }
        }
    }

    private fun shareImage(bitmap: Bitmap, context: Context) {
        val uri = saveImageToCacheDir(bitmap, context)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = TYPE
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun saveImageToCacheDir(bitmap: Bitmap, context: Context): Uri {
        try {
            val cachePath = File(context.cacheDir, CHILD)
            cachePath.mkdirs()
            val stream =
                FileOutputStream("$cachePath/$IMAGE_NAME")
            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val imagePath = File(context.cacheDir, CHILD)
        val newFile = File(imagePath, IMAGE_NAME)
        return FileProvider.getUriForFile(context, AUTHORITY, newFile)
    }

    private fun checkPermissionAndSave() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else savePicture()
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                savePicture()
                Log.i("DEBUG", "permission granted")
            } else {
                val msg = resources.getString(R.string.permission_denied)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                Log.i("DEBUG", "permission denied")
            }
        }

    private fun savePicture() {
        val fileName = generateFileName()
        val bmp = (binding.imgFinal.drawable as BitmapDrawable).bitmap
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(path, fileName)
        val os = FileOutputStream(image)
        os.use {
            bmp.compress(Bitmap.CompressFormat.JPEG, QUALITY, os)
            val msg = resources.getString(R.string.image_saved)
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun generateFileName(): String {
        val time = System.currentTimeMillis()
        val formatter = SimpleDateFormat("dd:MM:yyyy_HH:mm:ss", Locale.getDefault())
        val date = formatter.format(time)
        return "rune_$date.jpg"
    }

    private fun showCancelDialog() {
        CancelDialog(
            requireContext(),
            viewModel.fontSize.value!!,
            PAGE,
            getString(R.string.description_generator_popup)
        ) {
            requireActivity().viewModelStore.clear()
            val direction = GeneratorFinalDirections.actionGlobalGeneratorFragment()
            findNavController().navigate(direction)
        }
            .showDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
