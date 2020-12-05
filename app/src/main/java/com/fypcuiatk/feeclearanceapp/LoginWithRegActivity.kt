package com.fypcuiatk.feeclearanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class LoginWithRegActivity : AppCompatActivity() {

    private lateinit var signInWithEmailBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_reg)

        signInWithEmailBtn = findViewById(R.id.signInWithEmailBtn)

        signInWithEmailBtn.setOnClickListener(View.OnClickListener {

            startActivity(Intent( this, LoginActivity::class.java ))
            finish()
            
        })

    }
}