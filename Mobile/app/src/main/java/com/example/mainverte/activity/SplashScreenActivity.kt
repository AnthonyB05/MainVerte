package com.example.mainverte.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mainverte.R
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Timer(false).schedule(2000){
            val intentMain: Intent = Intent(this@SplashScreenActivity, MainActivity::class.java);
            startActivity(intentMain);
            finish()
        }
    }
}