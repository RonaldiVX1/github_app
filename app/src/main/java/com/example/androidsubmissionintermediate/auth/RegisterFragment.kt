package com.example.androidsubmissionintermediate.auth


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.androidsubmissionintermediate.R
import com.example.androidsubmissionintermediate.data.Result
import com.example.androidsubmissionintermediate.data.response.register.RegisterResponse
import com.example.androidsubmissionintermediate.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playAnimation()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(RegisterViewModel::class.java)



        binding?.buttonRegister?.setOnClickListener {
            val name = _binding?.edName?.text.toString()
            val email = _binding?.edEmail?.text.toString()
            val password = _binding?.edPassword?.text.toString()

            if (password.length < 8) {
                Toast.makeText(requireContext(), "password must be 8 character", Toast.LENGTH_LONG)
                    .show()
            } else {
                showLoading(true)
                viewModel.postRegister(name, email, password, requireContext()).observe(requireActivity()) { result ->
                    if (result != null) {

                        when (result) {
                            is Result.Success -> {
                                showLoading(false)
                                processRegister(result.data)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG)
                                    .show()
                            }
                            Result.Loading -> showLoading(true)
                        }
                    }
                }
            }
        }
    }

    private fun processRegister(data: RegisterResponse) {
        if (data.error) {
            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_LONG).show()
        } else {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            Toast.makeText(requireContext(), "Success, Silahkan Login", Toast.LENGTH_LONG).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBarRegister?.visibility = View.VISIBLE
        } else {
            binding?.progressBarRegister?.visibility = View.GONE
        }
    }

    private fun playAnimation() {

        val tvName = ObjectAnimator.ofFloat(binding?.textName, View.ALPHA, 1f).setDuration(500)
        val edName = ObjectAnimator.ofFloat(binding?.edName, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding?.textEmail, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding?.edEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding?.textPassword, View.ALPHA, 1f).setDuration(500)
        val edPassword = ObjectAnimator.ofFloat(binding?.edPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding?.buttonRegister, View.ALPHA, 1f).setDuration(500)


        val together = AnimatorSet().apply {
            playTogether(edName, edEmail, edPassword)
        }


        AnimatorSet().apply {
            playSequentially(tvName, tvEmail, tvPassword, together, btnRegister)
            start()
        }
    }

}