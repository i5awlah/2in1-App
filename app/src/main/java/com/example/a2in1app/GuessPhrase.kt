package com.example.a2in1app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class GuessPhrase : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var myLayout: ConstraintLayout
    private lateinit var myRV: RecyclerView
    private lateinit var scoreText: TextView
    private lateinit var phraseText: TextView
    private lateinit var guessedLettersText: TextView
    private lateinit var guessField: EditText
    private lateinit var submitButton: Button

    private var messages = arrayListOf<String>()
    private val guessLetters = arrayListOf<Char>()

    private lateinit var phrases: ArrayList<String>
    private var phrase = ""

    private val myAnswerDictionary = mutableMapOf<Int, Char>()
    private var myAnswer = ""
    private var guessedLetters = ""
    private var count = 0

    var guessPhrase = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_phrase)

        getSupportActionBar()?.setTitle("Guess the Phrase")

        myLayout = findViewById(R.id.clMain)
        myRV = findViewById(R.id.rvMain)
        myRV.adapter = MessageAdaptor(messages)
        myRV.layoutManager = LinearLayoutManager(this)

        scoreText = findViewById(R.id.tvScore)
        phraseText = findViewById(R.id.tvPhrase)
        guessedLettersText = findViewById(R.id.tvGuessedLetters)

        guessField = findViewById(R.id.etGuessField)
        submitButton = findViewById(R.id.btnSubmitButton)
        submitButton.setOnClickListener { newGuess() }

        scoreText.setText("Height Score: ${getHeightScore()}")

        playGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item: MenuItem = menu!!.getItem(1)
        item.title = "Numbers Game"
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newGame -> {
                playGame()
                return true
            }
            R.id.anotherGame -> {
                val intent = Intent(this, NumbersGame::class.java)
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

    // Preserve the state to allow users to rotate the device
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("guessPhrase",guessPhrase)
        outState.putInt("myCount", count)
        outState.putString("myAnswer", myAnswer)
        outState.putString("myGuessedLetters", guessedLetters)
        outState.putString("myPhrase", phrase)
        outState.putStringArrayList("myMessage", messages)
        //outState.putCharArrayList("myGuessLetters", guessLetters)
        // myAnswerDictionary
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)


        guessPhrase = savedInstanceState.getBoolean("guessPhrase", true)
        count = savedInstanceState.getInt("myCount", 0)

        myAnswer = savedInstanceState.getString("myAnswer", "")
        guessedLetters = savedInstanceState.getString("myGuessedLetters", "")
        phrase = savedInstanceState.getString("myPhrase", "")

        //messages = savedInstanceState.getStringArrayList("myMessage", [])
    }

    fun playGame() {
        myAnswer = ""
        guessedLetters = ""
        count = 0
        guessPhrase = true

        messages.clear()
        guessLetters.clear()

        enableEntry()

        phrases = arrayListOf("My son lives in London",
            "She plays basketball",
            "He goes to football every day",
            "He loves to play basketball",
            "Does he go to school?",
            "It usually rains every day here",
            "It smells very delicious in the kitchen",
            "George brushes her teeth twice a day",
            "He gets up early every day",
            "They speak English in USA")

        // Expand the game to randomly select a phrase from a list
        phrase = phrases[Random.nextInt(phrases.size)].lowercase(Locale.getDefault())

        convertToStar(phrase)
        updateText()
    }

    private fun newGuess() {
        val userGuess = guessField.text.toString()
        guessField.text.clear()
        guessField.clearFocus()

        if(userGuess.isNotEmpty()) {
            // phrase guess
            if (guessPhrase) {
                guessPhrase = false // to switch between phrase and letter guess
                if (userGuess.uppercase(Locale.getDefault()) == phrase.uppercase(Locale.getDefault())) {
                    messages.add("You got it!")
                    disableEntry()
                    saveSharedPreferences(10-count)
                    showAlert("You win!\n\nPlay again?")
                }
                else {
                    messages.add("Wrong guess: $userGuess")
                    updateText()
                }
                // Scroll the Recycler View to the bottom each time a new message is added
                myRV.scrollToPosition(messages.size - 1)
            } else { // letter guess
                if (userGuess.length == 1) {
                    // Don't allow the user to guess the same letter twice
                    if (isTwice(userGuess[0])) {
                        Snackbar.make(myLayout,"You shouldn't guess the same letter twice!",
                            Snackbar.LENGTH_LONG).show()
                    }
                    else {
                        guessPhrase = true // to switch between phrase and letter guess
                        myAnswer = ""
                        checkLetters(userGuess[0])
                    }
                }
                else {
                    Snackbar.make(myLayout,"You should enter one letter!", Snackbar.LENGTH_LONG).show()
                }

            }
        }
        else {
            Snackbar.make(myLayout,"Empty guess!", Snackbar.LENGTH_LONG).show()
        }


    }

    private fun updateText() {
        phraseText.text = "Phrase:  " + myAnswer.uppercase(Locale.getDefault())
        guessedLettersText.text = "Guessed Letters:  " + guessedLetters
        if (guessPhrase) {
            guessField.hint = "Guess the full phrase"
        }
        else {
            guessField.hint = "Guess a letter"
        }
    }

    private fun isTwice(c: Char) : Boolean {
        for (letter in guessLetters) {
            if (c == letter) return true
        }
        guessLetters.add(c)
        return false
    }

    private fun convertToStar(phrase: String) {
        for(i in phrase.indices){
            if(phrase[i] == ' '){
                myAnswerDictionary[i] = ' '
                myAnswer += ' '
            }else{
                myAnswerDictionary[i] = '*'
                myAnswer += '*'
            }
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in phrase.indices){
            if(phrase[i] == guessedLetter){
                myAnswerDictionary[i] = guessedLetter
                found++
            }
        }
        for(i in myAnswerDictionary){myAnswer += myAnswerDictionary[i.key]}
        if(myAnswer==phrase){
            disableEntry()
            saveSharedPreferences(10-count)
            showAlert("You win!\n\nPlay again?")
        }
        if(guessedLetters.isEmpty()){guessedLetters+=guessedLetter}else{guessedLetters+=", "+guessedLetter}
        if(found>0){
            messages.add("Found $found ${guessedLetter.uppercaseChar()}(s)")
        }else{
            messages.add("No ${guessedLetter.uppercaseChar()}s found")
        }
        count++
        val guessesLeft = 10 - count
        if(count<10){
            messages.add("$guessesLeft guesses remaining")
        }
        else {
            disableEntry()
            showAlert("You loss!\n\nPlay again?")
        }
        updateText()
        // Scroll the Recycler View to the bottom each time a new message is added
        myRV.scrollToPosition(messages.size - 1)
    }

    private fun enableEntry(){
        submitButton.isEnabled = true
        submitButton.isClickable = true
        guessField.isEnabled = true
        guessField.isClickable = true
    }

    private fun disableEntry(){
        submitButton.isEnabled = false
        submitButton.isClickable = false
        guessField.isEnabled = false
        guessField.isClickable = false
    }

    private fun showAlert(title: String){
        // first we create a variable to hold an AlertDialog builder
        val dialogBuilder = AlertDialog.Builder(this)

        // here we set the message of our alert dialog
        dialogBuilder.setMessage(title)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> playGame()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game over")
        // show alert dialog
        alert.show()
    }

    private fun getHeightScore() : Int {
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        return sharedPreferences.getInt("heightScore", 0)  // --> retrieves data from Shared Preferences
    }
    private fun saveSharedPreferences(score: Int) {

        var heightScore = getHeightScore()

        if (heightScore < score) {
            // save data with the following code
            with(sharedPreferences.edit()) {
                putInt("heightScore", score)
                apply()
            }
            heightScore = score
        }
        scoreText.setText("Height Score: $heightScore")
    }
}