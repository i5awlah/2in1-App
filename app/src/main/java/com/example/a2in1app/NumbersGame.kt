package com.example.a2in1app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class NumbersGame : AppCompatActivity() {

    lateinit var myLayout: ConstraintLayout
    private lateinit var myRV: RecyclerView
    lateinit var guessButton: Button
    lateinit var guessField: EditText
    lateinit var messages: ArrayList<String>

    private var randomNumber = 0
    private var numberOfGuess = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numbers_game)

        getSupportActionBar()?.setTitle("Numbers Game")

        messages = ArrayList()

        myLayout = findViewById(R.id.clMain)

        myRV = findViewById(R.id.rvMain)
        myRV.adapter = MessageAdaptor(messages)
        myRV.layoutManager = LinearLayoutManager(this)

        guessField = findViewById(R.id.etGuessField)
        guessButton = findViewById(R.id.btnGuess)
        guessButton.setOnClickListener { addMessage() }


        startGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item: MenuItem = menu!!.getItem(1)
        item.title = "Guess the Phrase"
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newGame -> {
                startGame()
                return true
            }
            R.id.anotherGame -> {
                val intent = Intent(this, GuessPhrase::class.java)
                startActivity(intent)
                return true
            }
            R.id.mainMenu -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGame() {
        randomNumber = Random.nextInt(11)
        numberOfGuess = 3
        messages.clear()
        myRV.adapter?.notifyDataSetChanged()
    }

    private fun addMessage() {
        val userGuess = guessField.text.toString()
        guessField.text.clear()
        guessField.clearFocus()

        if (userGuess.isNotEmpty()) {
            if (checkNumber(userGuess)) {
                if(numberOfGuess > 0) {
                    numberOfGuess--
                    if (userGuess.toInt() == randomNumber) {
                        messages.add("You got it!")
                        CustomAlert(this, "You win!\n\nPlay again?")
                    }
                    else {
                        messages.add("You guessed $userGuess")
                        messages.add("You have $numberOfGuess guesses left")
                    }

                }
                if(numberOfGuess == 0) {
                    messages.add("The Answer is: $randomNumber")
                    messages.add("Game over!")

                    CustomAlert(this, "Do you want to play again?")
                }
            }
            else {
                Snackbar.make(myLayout, "Please enter numbers only", Snackbar.LENGTH_LONG).show()
            }
        }
        else {
            Snackbar.make(myLayout, "You didn't enter anything!", Snackbar.LENGTH_LONG).show()
        }
        myRV.adapter?.notifyDataSetChanged()
    }
    private fun checkNumber(userNumber: String): Boolean {
        return try {
            userNumber.toInt()
            true
        } catch (e: Exception) {
            false
        }
    }

}