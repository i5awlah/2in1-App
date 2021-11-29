package com.example.a2in1app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var numbersGameButton: Button
    lateinit var guessPhraseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numbersGameButton = findViewById(R.id.btnNumbersGame)
        guessPhraseButton = findViewById(R.id.btnGuessPhrase)

        numbersGameButton.setOnClickListener{ openNumbersGame() }
        guessPhraseButton.setOnClickListener{ openGuessPhrase() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.numbersGame -> {
                openNumbersGame()
                return true
            }
            R.id.guessPhrase -> {
                openGuessPhrase()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openNumbersGame() {
        val intent = Intent(this, NumbersGame::class.java)
        startActivity(intent)
    }
    private fun openGuessPhrase() {
        val intent = Intent(this, GuessPhrase::class.java)
        startActivity(intent)
    }
}