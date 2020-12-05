package com.fypcuiatk.feeclearanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var signInWithRegBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signInButton = findViewById(R.id.signInButton)
        signInWithRegBtn = findViewById(R.id.signInWithRegNumBtn)

        auth = FirebaseAuth.getInstance()

        signInButton.setOnClickListener(View.OnClickListener {

            signInButton.isEnabled = false
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val validInput = validateEmailAndPassword(email, password)
            if (validInput)
                trySignIn(email, password)
        })

        signInWithRegBtn.setOnClickListener(View.OnClickListener {

            startActivity(Intent(this, LoginWithRegActivity::class.java))
            finish()

        })

    }

    private fun trySignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java));
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    signInButton.isEnabled = true
                }
            })
    }

    private fun validateEmailAndPassword(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty())
            return false
        // TODO:: perform other validations HERE
        return true
    }
}