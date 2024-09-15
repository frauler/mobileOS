package com.example.lab2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.BinderThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.system.exitProcess

class Game : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        val homeButton: Button = findViewById(R.id.home_button)
        val settingsGameButton: Button = findViewById(R.id.settings_game_button)
        val resetButton: Button = findViewById(R.id.reset_button)
        val rockButton: Button = findViewById(R.id.rock_button)
        val paperButton: Button = findViewById(R.id.paper_button)
        val scissorsButton: Button = findViewById(R.id.scissor_button)

        val playerMove: ImageView = findViewById(R.id.player_move_image)

        val sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE)
        val savedNumber = sharedPreferences.getInt("savedNumber", 3)
        val textNumber: TextView = findViewById(R.id.textView)
        textNumber.text = savedNumber.toString()

        val playerScore = sharedPreferences.getInt("playerScore", 0)
        val playerScoreText: TextView = findViewById(R.id.player_score_text)
        playerScoreText.text = playerScore.toString()

        val botScore = sharedPreferences.getInt("botScore", 0)
        val botScoreText: TextView = findViewById(R.id.bot_score_text)
        botScoreText.text = botScore.toString()

        // rock choice
        rockButton.setOnClickListener() {
            playerMove.setImageDrawable(getDrawable(R.drawable.gem))
            botChoiceFunc(0)
        }

        // rock choice
        paperButton.setOnClickListener() {
            playerMove.setImageDrawable(getDrawable(R.drawable.paper))
            botChoiceFunc(1)
        }

        // scissors choice
        scissorsButton.setOnClickListener() {
            playerMove.setImageDrawable(getDrawable(R.drawable.scissors))
            botChoiceFunc(2)
        }

        homeButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settingsGameButton.setOnClickListener() {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        resetButton.setOnClickListener() {
            resetScore()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun botChoiceFunc(player: Int) {
        val botMove: ImageView = findViewById(R.id.bot_move_image)
        val botChoice = (0..3).random()
        when (botChoice) {
            0 -> {
                botMove.setImageDrawable(getDrawable(R.drawable.gem));
                botMove.visibility = View.VISIBLE
            }

            1 -> {
                botMove.setImageDrawable(getDrawable(R.drawable.paper));
                botMove.visibility = View.VISIBLE
            }

            2 -> {
                botMove.setImageDrawable(getDrawable(R.drawable.scissors));
                botMove.visibility = View.VISIBLE
            }
        }
        compareResult(botChoice, player)
    }

    private fun compareResult(bot: Int, player: Int) {
        val sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var playerScore = sharedPreferences.getInt("playerScore", 0)
        var botScore = sharedPreferences.getInt("botScore", 0)
        val winner: TextView = findViewById(R.id.winner)
        winner.visibility = View.INVISIBLE

        when (bot) {
            0 -> {
                when (player) {
                    1 -> playerScore += 1 // Rock loses to paper
                    2 -> botScore += 1 // Rock wins against scissors
                }
            }
            1 -> {
                when (player) {
                    0 -> botScore += 1 // Paper wins against rock
                    2 -> playerScore += 1 // Paper loses to scissors
                }
            }
            2 -> {
                when (player) {
                    0 -> playerScore += 1 // Scissors lose to rock
                    1 -> botScore += 1 // Scissors win against paper
                }
            }
        }

        editor.putInt("playerScore", playerScore)
        editor.putInt("botScore", botScore)
        editor.apply()
        val playerScoreText: TextView = findViewById(R.id.player_score_text)
        playerScoreText.text = playerScore.toString()

        val botScoreText: TextView = findViewById(R.id.bot_score_text)
        botScoreText.text = botScore.toString()

        val savedNumber = sharedPreferences.getInt("savedNumber", 3)

        if (playerScore == savedNumber) {
            winner.text = "YOU WIN!"
            winner.setTextColor(Color.GREEN)
            winner.visibility = View.VISIBLE
            resetScore()
        } else if (botScore == savedNumber) {
            winner.text = "YOU LOSE!"
            winner.setTextColor(Color.RED)
            winner.visibility = View.VISIBLE
            resetScore()
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resetScore() {
        val sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val playerMove: ImageView = findViewById(R.id.player_move_image)
        val botMove: ImageView = findViewById(R.id.bot_move_image)

        val playerScoreText: TextView = findViewById(R.id.player_score_text)
        playerScoreText.text = 0.toString()

        val botScoreText: TextView = findViewById(R.id.bot_score_text)
        botScoreText.text = 0.toString()

        editor.putInt("playerScore", 0)
        editor.putInt("botScore", 0)
        editor.apply()

        playerMove.setImageDrawable(getDrawable(R.drawable.question))
        botMove.setImageDrawable(getDrawable(R.drawable.question))
    }
}