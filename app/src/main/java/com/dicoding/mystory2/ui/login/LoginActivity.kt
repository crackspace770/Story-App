package com.dicoding.mystory2.ui.login


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystory2.R
import com.dicoding.mystory2.databinding.ActivityLoginBinding
import com.dicoding.mystory2.model.UserPreference
import com.dicoding.mystory2.ui.main.StoryActivity
import com.dicoding.mystory2.ui.signup.SignupActivity
import com.dicoding.mystory2.viewModel.ViewModelFactory

class LoginActivity : AppCompatActivity(){
    private var emailValid = false
    private lateinit var binding: ActivityLoginBinding
    private var sharedPreference: UserPreference? = null
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this, ViewModelFactory(this))[LoginViewModel::class.java]
        sharedPreference = UserPreference(this)


        loginViewModel.noConnections.observe(this) {
            hasLogin(it)
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.toast.observe(this) {
            it.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }


        val strLoginStatus = sharedPreference?.getPreferenceString("isLogin")
        if (strLoginStatus != null) {
            val intent = Intent(this@LoginActivity, StoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.setOnClickListener {
            val strEmail = binding.emailEditText.text.toString()
            val strPassword = binding.passwordEditText.text.toString()

            if (strEmail == "" || strPassword == "") {
                Toast.makeText(this@LoginActivity, "Please Fill in your detail.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                sharedPreference?.saveString("isLogin", "1")
                loginViewModel.userLogin(strEmail, strPassword)
            }
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //email corrector
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateEmail()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        playAnimation()
        setupView()

    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                passwordTextView,
                login)
            startDelay = 500
        }.start()
    }



    private fun showToast(isToast: Boolean) {
        val caution = "Login failed, try again"
        if (isToast) {
            Toast.makeText(this@LoginActivity, caution, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun hasLogin(value: Boolean) {
        if (value) {
            val intent = Intent(this@LoginActivity, StoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

            Toast.makeText(this, "Successfully Login.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this, "Email and Password not Correct or Data not Available", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showEmailError(isNotValid: Boolean) {
        binding.emailEditText.error = if (isNotValid) getString(R.string.email_error) else null
    }

    fun validateEmail() {
        val input = binding.emailEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            emailValid = false
            showEmailError(true)
        } else {
            emailValid = true
            showEmailError(false)
        }

    }

    companion object {
        val SHARED_PREFERENCES = "shared_preferences"
        val NAME = "name"
        val USER_ID = "user_id"
        val TOKEN = "token"
        val IS_LOGIN = "is_login"
    }
}