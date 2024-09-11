package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startButton: Button = findViewById(R.id.start_button)
        val settingsButton: Button = findViewById(R.id.settings_button)
        val exitButton: Button = findViewById(R.id.exit_button)

        startButton.setOnClickListener() {
            val intent = Intent(this, Game::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener() {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        exitButton.setOnClickListener() {
            finish()
            exitProcess(0)
        }
    }
}