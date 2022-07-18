package com.tnco.runar.ui.fragment

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.viewmodel.MainViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class GeneratorFinal : Fragment() {
    private val REQUEST_PERMISSION_CODE = 111
    private lateinit var viewModel: MainViewModel
    private lateinit var saveImg: ImageView
    private lateinit var shareImg: ImageView
    private lateinit var imgFinal: ImageView
    private lateinit var finalBack: ImageView
//    lateinit var twitter: ImageView
//    lateinit var facebook: ImageView
//    lateinit var instagram: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.generator_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideBottomBar()

        saveImg = view.findViewById(R.id.save)
        shareImg = view.findViewById(R.id.share)
        imgFinal = view.findViewById(R.id.img_final)
        finalBack = view.findViewById(R.id.final_back)
//        twitter =view.findViewById(R.id.twitter)
//        facebook = view.findViewById(R.id.facebook)
//        instagram = view.findViewById(R.id.instagram)

        shareImg.setOnClickListener {
            AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SHARED)
            val bytes = ByteArrayOutputStream()
            val bmp = (imgFinal.drawable as BitmapDrawable).bitmap
            val title = resources.getString(R.string.share_title)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path =
                MediaStore.Images.Media.insertImage(context?.contentResolver, bmp, title, null)
            val uri = Uri.parse(path.toString())
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)

                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        imgFinal.setImageBitmap(viewModel.backgroundInfo.value!!.first { it.isSelected }.img!!)

        finalBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, GeneratorFragment())
                ?.commit()
        }


        saveImg.setOnClickListener {
            AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SAVED)
            val fileName = generateFileName()
            val bmp = (imgFinal.drawable as BitmapDrawable).bitmap
            saveImg.visibility = View.INVISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val resolver = activity!!.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, resolver.openOutputStream(uri))
                }
                saveImg.visibility = View.VISIBLE
                val msg = resources.getString(R.string.image_saved)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            } else {
                val hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(Array(1) {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_PERMISSION_CODE)
                } else {
                    val path =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val filePath = File(path, fileName)
                    val os = FileOutputStream(filePath)
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.close()
                    saveImg.visibility = View.VISIBLE
                    val msg = resources.getString(R.string.image_saved)
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val fileName = generateFileName()
                val bmp = (imgFinal.drawable as BitmapDrawable).bitmap
                val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val filePath = File(path, fileName)
                val os = FileOutputStream(filePath)
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()
                saveImg.visibility = View.VISIBLE
                val msg = resources.getString(R.string.image_saved)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun generateFileName(): String {
        val time = System.currentTimeMillis()
        val formatter = SimpleDateFormat("dd:MM:yyyy_HH:mm:ss", Locale.getDefault())
        val date = formatter.format(time)
        return "rune_$date.jpg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomBar()
    }
}