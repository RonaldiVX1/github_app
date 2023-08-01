package com.example.androidsubmissionintermediate.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.androidsubmissionintermediate.MainActivity
import com.example.androidsubmissionintermediate.R
import com.example.androidsubmissionintermediate.data.Result
import com.example.androidsubmissionintermediate.data.response.login.LoginResponse
import com.example.androidsubmissionintermediate.databinding.FragmentLoginBinding
import com.example.androidsubmissionintermediate.utils.SharedPreference


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playAnimation()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(LoginViewModel::class.java)

        showExistingPreference()
        onBackPressed()

        binding?.buttonRegister?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
        )

        binding?.buttonLogin?.setOnClickListener {
            val email = _binding?.edEmail?.text.toString()
            val password = _binding?.edPassword?.text.toString()

            if (password.length < 8) {
                Toast.makeText(requireContext(), "password must be 8 character", Toast.LENGTH_LONG)
                    .show()
            } else {
                showLoading(true)
                viewModel.postlogin(email, password, requireContext())
                    .observe(requireActivity()) { result ->
                        if (result != null) {

                            when (result) {
                                is Result.Success -> {
                                    showLoading(false)
                                    processLogin(result.data)
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        requireContext(),
                                        result.error,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                                Result.Loading -> showLoading(true)
                            }
                        }
                    }
            }
        }
    }

    private fun processLogin(data: LoginResponse) {
        if (data.error) {
            Toast.makeText(requireContext(), data.message, Toast.LENGTH_LONG).show()
        } else {
            SharedPreference.saveToken(data.loginResult.token, requireContext())
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun showExistingPreference() {
        val shared = SharedPreference.initPref(requireContext(), "onSignIn")
        val token = shared.getString("token", null)

        if (token != null){
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }else  {
            Log.e("token tidak ada", "")
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun playAnimation() {

        val tvEmail = ObjectAnimator.ofFloat(binding?.textEmail, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding?.edEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding?.textPassword, View.ALPHA, 1f).setDuration(500)
        val edPassword = ObjectAnimator.ofFloat(binding?.edPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding?.buttonRegister, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding?.buttonLogin, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether( edEmail, edPassword)
        }

        val btnAnimationStart = AnimatorSet().apply {
            playTogether( btnRegister, btnLogin)
        }


        AnimatorSet().apply {
            playSequentially(tvEmail, tvPassword, together, btnAnimationStart)
            start()
        }
    }

}