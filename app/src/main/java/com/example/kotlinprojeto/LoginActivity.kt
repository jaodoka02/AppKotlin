// LoginActivity.kt
package com.example.kotlinprojeto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViews()
        configureListeners()
    }

    private fun findViews() {
        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        loginButton = findViewById(R.id.btn_login)
        registerButton = findViewById(R.id.btn_register)
    }

    private fun configureListeners() {
        loginButton.setOnClickListener { processLogin() }
        registerButton.setOnClickListener { goToRegisterScreen() }
    }

    private fun processLogin() {
        val userEmail = inputEmail.text.toString()
        val userPassword = inputPassword.text.toString()

        if (!checkValidEmail(userEmail)) {
            inputEmail.error = "Email inválido!"
            return
        }

        if (userPassword.isEmpty()) {
            inputPassword.error = "Campo obrigatório!"
            return
        }

        if (checkCredentials(userEmail, userPassword)) {
            Toast.makeText(this, "Login efetuado com sucesso...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        val preferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val savedEmail = preferences.getString("USER_EMAIL", "")
        val savedPassword = preferences.getString("USER_PASSWORD", "")
        return email == savedEmail && password == savedPassword
    }

    private fun goToRegisterScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
