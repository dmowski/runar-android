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
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.GeneratorFinalBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.viewmodel.MainViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class GeneratorFinal : Fragment() {

    private var _binding: GeneratorFinalBinding? = null
    private val binding get() = _binding!!
    val REQUEST_PERMISSION_CODE = 111
    private lateinit var viewModel: MainViewModel
    lateinit var twitter: ImageView
    lateinit var facebook: ImageView
    lateinit var instagram: ImageView

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
    ): View? {
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
            val bytes = ByteArrayOutputStream()
            val bmp = (binding.imgFinal.drawable as BitmapDrawable).bitmap
            val title = resources.getString(R.string.share_title)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )
            } else {
                val path =
                    MediaStore.Images.Media.insertImage(requireContext().contentResolver, bmp, title, null)
                val uri = Uri.parse(path.toString())
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)

                    type = "image/jpeg"
                }
                startActivity(Intent.createChooser(shareIntent, null))
            }
        }

        binding.imgFinal.setImageBitmap(viewModel.backgroundInfo.value!!.first { it.isSelected }.img!!)

        binding.finalBack.setOnClickListener {
            showCancelDialog()
        }

        binding.save.setOnClickListener {
            AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_SAVED)
            val fileName = generateFileName()
            val bmp = (binding.imgFinal.drawable as BitmapDrawable).bitmap
            binding.save.visibility = View.INVISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val resolver = requireActivity().contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, resolver.openOutputStream(uri))
                }
                binding.save.visibility = View.VISIBLE
                val msg = resources.getString(R.string.image_saved)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
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
                    binding.save.visibility = View.VISIBLE
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
            if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                val fileName = generateFileName()
                val bmp = (binding.imgFinal.drawable as BitmapDrawable).bitmap
                val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val filePath = File(path, fileName)
                val os = FileOutputStream(filePath)
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()
                binding.save.visibility = View.VISIBLE
                val msg = resources.getString(R.string.image_saved)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun generateFileName(): String {
        val time = System.currentTimeMillis()
        val formatter = SimpleDateFormat("dd:MM:yyyy_HH:mm:ss", Locale.getDefault())
        val date = formatter.format(time)
        return "rune_$date.jpg"
    }

    private fun showCancelDialog() {
        CancelDialog(
            requireContext(),
            viewModel.fontSize.value!!,
            "generator_final",
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