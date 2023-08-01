package com.example.androidsubmissionintermediate.ui.createStory

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.androidsubmissionintermediate.R
import com.example.androidsubmissionintermediate.data.Result
import com.example.androidsubmissionintermediate.databinding.FragmentCreateStoryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class CreateStoryFragment : Fragment() {

    private var _binding: FragmentCreateStoryBinding? = null
    private val binding get() = _binding
    private var getFile: File? = null

    private var location: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var viewModel: CreateStoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateStoryBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(CreateStoryViewModel::class.java)

        binding?.buttonCamera?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_createStoryFragment_to_cameraFragment))
        binding?.buttonUpload?.setOnClickListener { uploadImage() }
        binding?.buttonGallery?.setOnClickListener{ startGallery() }
        binding?.switchLocation?.setOnCheckedChangeListener{
                _, isChecked ->
            if (isChecked) {
                getMyLastLocation()
            } else {
                this.location = null
            }
        }


        val fileUri = arguments?.get("selected_image")
        if (fileUri != null) {
            val uri: Uri = fileUri as Uri
            val isBackCamera = arguments?.get("isBackCamera") as Boolean
            getFile = uri.toFile()
            rotateFile(getFile!!, isBackCamera)
            binding?.imagePreview?.setImageBitmap(BitmapFactory.decodeFile(getFile!!.path))
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding?.imagePreview?.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {

            val file = reduceFileImage(getFile as File)
            val descriptionText = binding?.edDescription?.text
            if (!descriptionText.isNullOrEmpty()) {
                showLoading(true)
                val description =
                    descriptionText.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                var lat: RequestBody? = null
                var lon: RequestBody? = null

                if (location != null) {
                    lat =
                        location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon =
                        location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }

                viewModel.postStory(imageMultipart, description, lat, lon, requireContext()).observe(viewLifecycleOwner){
                    if (it != null){
                        when(it){
                            is Result.Success ->{
                                showLoading(false)
                                Toast.makeText(context, it.data.message, Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_createStoryFragment_to_list_story)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                            }
                            Result.Loading -> {
                                showLoading(true)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }else {
            Toast.makeText(
                requireContext(),
                "Pilih gambar terlebih dahulu",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                    Log.d(ContentValues.TAG, "getLastLocation: ${location.latitude}, ${location.longitude}")
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.loadingUpload?.visibility = View.VISIBLE
        } else {
            binding?.loadingUpload?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

