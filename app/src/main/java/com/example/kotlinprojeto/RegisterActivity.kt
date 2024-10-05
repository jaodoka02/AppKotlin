// RegisterActivity.kt
package com.example.kotlinprojeto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnRegister: Button
    private lateinit var btnVoltar: Button
    private lateinit var confirmPasswordInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        nameInput = findViewById(R.id.name)
        emailInput = findViewById(R.id.email)
        passwordInput = findViewById(R.id.password)
        confirmPasswordInput = findViewById(R.id.confirm_password)
        btnRegister = findViewById(R.id.btn_register)
        btnVoltar = findViewById(R.id.btn_voltar)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener { handleRegister() }
        btnVoltar.setOnClickListener { navigateToLogin() }
    }

    private fun handleRegister() {
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()

        if (areFieldsValid(name, email, password, confirmPassword)) {
            saveUserData(email, password)
            Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun areFieldsValid(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                false
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Senhas não coincidem", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun saveUserData(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("USER_EMAIL", email)
            putString("USER_PASSWORD", password)
            apply()
        }
    }

    private fun navigateToLogin() {
        Toast.makeText(this, "Voltando para a tela de login", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
