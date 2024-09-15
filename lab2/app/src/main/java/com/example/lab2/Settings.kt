package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton: Button = findViewById(R.id.settings_back_button)
        val confirmButton: Button = findViewById(R.id.score_confirm_button)
        val scoreEntry: EditText = findViewById(R.id.end_score_enter)
        val sharedPreferences = getSharedPreferences("GameSettings", MODE_PRIVATE)

        confirmButton.setOnClickListener() {
            val number = scoreEntry.text.toString().toIntOrNull() ?: 0
            val editor = sharedPreferences.edit()
            number.toInt()
            if (number == 0) {
                val toast = Toast.makeText(applicationContext, "Number can't be 0 or below!", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                editor.putInt("savedNumber", number)
                editor.apply()
            }
        }

        backButton.setOnClickListener() {
            this.finish()
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
    }
}