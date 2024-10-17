package com.zybooks.decodethemobilemystery

import android.app.Activity
import android.app.Dialog
import android.content.SharedPreferences
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader

/*
 TODO:
    - Fix Font Size in Boxes
    - Make a victory toast/screen for when you complete crossword
    - For hints, make an overflow/scroll so you can view each hint with a scroll bar
 */
import android.view.MenuInflater

/*
    The Decode the Mobile Mystery App
    @author: Hailey Wendt, Rachael Rennie, Maria Lyons, Key Mustago, Macy August
    @date: Created April 10th, 2024. Last modified May 15th, 2024
    @Desc: A crossword game to help foster knowledge within the Mobile App Development Course. Match descriptions with their corresponding word
    Currently contains 2 different crosswords
 */

class MainActivity : AppCompatActivity() {

    private lateinit var optionsMenu: Menu
    private lateinit var editText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private val crosswordGame = CrosswordGame()
    private var ROWS_MAX = 12
    private val COLUMNS_MAX = 12
    private val gameOver = false

    // 2D array that characters will be entered into
    var textViews = Array(ROWS_MAX) { arrayOfNulls<String?>(COLUMNS_MAX)}

    // Dummy variable that will hold the solution
    // Compared to actual 2D array to see if character is correct or not
    var textViewsDummy = Array(ROWS_MAX) { arrayOfNulls<EditText>(COLUMNS_MAX)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gifImage:ImageView = findViewById(R.id.gifImage)
        Glide.with(this).load(R.drawable.toddmad).into(gifImage)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        readCSVFile(R.id.wordsCSV, "words.csv")

        crosswordGame.newGame()
        setCrosswordGame1()
        val checkButton : Button = findViewById(R.id.check_answers_button)

        checkButton.setOnClickListener(){
            checkLetter()
        }

        println("Activity Created Successfully")
    }

    // Reads the CSV files to create definitions and words. Parameters are the TextView that needs to be changed and the different file name for the CSV (String)
    private fun readCSVFile(csvFile: Int, fileName: String) {
        val textView = findViewById<TextView>(csvFile)
        val bufferReader = BufferedReader(assets.open(fileName).reader())
        val csvParser = CSVParser.parse(
            bufferReader,
            CSVFormat.DEFAULT

        )
        val list = mutableListOf<Words>()
        list.clear() // Clear the list before populating it with new data
        csvParser.forEach {
            it?.let {
                val words = Words(
                    word = it.get(0),
                    definition = it.get(1),
                )
                list.add(words)
            }
        }
        textView.text = "" // Clear the TextView before appending new data
        list.forEach {
            textView.append(
                "${it.word} ${it.definition}\n\n"
            )
        }
    }


    // Sets up the activity fragment for the first crossword game
    private fun setCrosswordGame1() {
        val parentLinearLayout = findViewById<LinearLayout>(R.id.crosswordGrid1)
        parentLinearLayout.removeAllViews()
        parentLinearLayout.orientation = LinearLayout.VERTICAL

        val rows = 12
        val columns = 12

        // Initialize the 2D arrays for crossword 1
        val textViewsDummy = Array(rows) { arrayOfNulls<EditText>(columns) }
        val textViews = Array(rows) { arrayOfNulls<String?>(columns) }

        for (i in 0 until rows) {
            val rowLayout = LinearLayout(this)
            for (j in 0 until columns) {
                val textView = EditText(this)
                textView.setText(" ")
                textView.width = 100
                textView.height = 100
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                rowLayout.addView(textView)
                textViewsDummy[i][j] = textView
                textViews[i][j] = " "
            }
            parentLinearLayout.addView(rowLayout)
        }

        // Set text for specific EditTexts
        textViews[0][6] = "F"
        textViews[1][6] = "U"
        textViews[2][6] = "N"
        textViews[3][6] = "C"
        textViews[4][6] = "T"
        textViews[5][6] = "I"
        textViews[6][6] = "O"
        textViews[7][6] = "N"

        textViews[4][3] = "C"
        textViews[5][3] = "O"
        textViews[6][3] = "M"
        textViews[7][3] = "M"
        textViews[8][3] = "E"
        textViews[9][3] = "N"
        textViews[10][3] = "T"

        textViews[5][2] = "K"
        textViews[5][4] = "T"
        textViews[5][5] = "L"
        textViews[5][7] = "N"

        textViews[8][1] = "I"
        textViews[8][2] = "D"

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                if (textViews[i][j] != " " && textViews[i][j] != "") {
                    textViewsDummy[i][j]?.gravity = Gravity.CENTER
                    textViewsDummy[i][j]?.setBackgroundResource(R.drawable.border)
                } else {
                    textViewsDummy[i][j]?.background = null
                }
            }
        }

        this.textViewsDummy = textViewsDummy
        this.textViews = textViews
    }

    // Sets the activity fragment for the second crossword game
    private fun setCrosswordGame2() {
        val parentLinearLayout = findViewById<LinearLayout>(R.id.crosswordGrid1)
        parentLinearLayout.removeAllViews()
        parentLinearLayout.orientation = LinearLayout.VERTICAL

        readCSVFile(R.id.wordsCSV, "words_two.csv")

        val rows = 12
        val columns = 12

        // Initialize the 2D arrays for crossword 2
        val textViewsDummy = Array(rows) { arrayOfNulls<EditText>(columns) }
        val textViews = Array(rows) { arrayOfNulls<String?>(columns) }

        for (i in 0 until rows) {
            val rowLayout = LinearLayout(this)
            for (j in 0 until columns) {
                val textView = EditText(this)
                textView.setText(" ")
                textView.width = 100
                textView.height = 100
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                rowLayout.addView(textView)
                textViewsDummy[i][j] = textView
                textViews[i][j] = " "
            }
            parentLinearLayout.addView(rowLayout)
        }

        // Set specific letters for crossword 2
        textViews[8][3] = "V"
        textViews[9][3] = "A"
        textViews[10][3] = "L"

        textViews[0][6] = "D"
        textViews[1][6] = "E"
        textViews[2][6] = "C"
        textViews[3][6] = "L"
        textViews[4][6] = "A"
        textViews[5][6] = "R"
        textViews[6][6] = "A"
        textViews[7][6] = "T"
        textViews[8][6] = "I"
        textViews[9][6] = "O"
        textViews[10][6] = "N"

        textViews[0][10] = "I"
        textViews[1][10] = "D"
        textViews[2][10] = "E"
        textViews[3][10] = "N"
        textViews[4][10] = "T"
        textViews[5][10] = "I"
        textViews[6][10] = "F"
        textViews[7][10] = "I"
        textViews[8][10] = "E"
        textViews[9][10] = "R"

        textViews[3][0] = "I"
        textViews[3][1] = "N"
        textViews[3][2] = "I"
        textViews[3][3] = "T"
        textViews[3][4] = "I"
        textViews[3][5] = "A"
        textViews[3][6] = "L"
        textViews[3][7] = "I"
        textViews[3][8] = "Z"
        textViews[3][9] = "I"
        textViews[3][10] = "N"
        textViews[3][11] = "G"

        textViews[5][4] = "V"
        textViews[5][5] = "A"
        textViews[5][6] = "R"

        textViews[8][3] = "V"
        textViews[8][4] = "A"
        textViews[8][5] = "R"
        textViews[8][6] = "I"
        textViews[8][7] = "A"
        textViews[8][8] = "B"
        textViews[8][9] = "L"
        textViews[8][10] = "E"



        for (i in 0 until rows) {
            for (j in 0 until columns) {
                if (textViews[i][j] != " " && textViews[i][j] != "") {
                    textViewsDummy[i][j]?.gravity = Gravity.CENTER
                    textViewsDummy[i][j]?.setBackgroundResource(R.drawable.border)
                } else {
                    textViewsDummy[i][j]?.background = null
                }
            }
        }

    //Creates the menu bar
        this.textViewsDummy = textViewsDummy
        this.textViews = textViews
    }

    //Checks to see if the user wins.
    private fun checkWin(): Boolean {
        val rows = 12
        val columns = 12

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                if (textViews[i][j] != (textViewsDummy[i][j]).toString()) {
                    return false
                }
            }
        }
        Toast.makeText(this, "Congrats! You win :) ", Toast.LENGTH_LONG).show()
        return true
    }

    // Creates menu bar when activity launches
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        println("Menu run")
        return super.onCreateOptionsMenu(menu)
    }

    //Creates three different options in the menu bar options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.otherscrosswords -> {
                // Handle main menu item click if needed
                true
            }
            R.id.sub_option_1 -> {
                // Handle sub-option 1 click
                setCrosswordGame1()
                true
            }
            R.id.sub_option_2 -> {
                // Handle sub-option 2 click
                setCrosswordGame2()
                true
            }
            R.id.support -> {
                showFeedbackDialog()
                true
            }
            R.id.how_to_play -> {
                showCustomDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // How to Play Pop-up
    private fun showCustomDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialogue_fragment)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        val btnYepperoni : Button = dialog.findViewById(R.id.okButton)

        btnYepperoni.setOnClickListener{
            Toast.makeText(this, "You got this!", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        dialog.show()
        showGIF(dialog)
    }

    // Feedback Dialog popup and email functionality
    private fun showFeedbackDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.feedback_form)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        val feedbackText : EditText = dialog.findViewById(R.id.feedback_space)
        val btnConfirm : Button = dialog.findViewById(R.id.confirm_button)
        val btnCancel : Button = dialog.findViewById(R.id.cancel_button)

        btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        // Prepares an email to one of the developers ( Maria ;) )
        btnConfirm.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO)

            val emailAddress = "m-lyons.3@onu.edu"
            val subject = "Feedback"
            val body = feedbackText.text.toString()

            val uriText = "mailto:" + Uri.encode(emailAddress) +
                    "?subject=" + Uri.encode(subject) +
                    "&body=" + Uri.encode(body)
            val uri = Uri.parse(uriText)
            intent.data = uri

            startActivity(Intent.createChooser(intent, "Send email"))

            Toast.makeText(this, "Thank you for the Feedback!", Toast.LENGTH_LONG).show()
        }
        dialog.show()
    }

    //Allows gift to display on How to Play
    private fun showGIF(dialog: Dialog){
        val gifImage:ImageView = dialog.findViewById(R.id.imageView)
        Glide.with(this).load(R.drawable.toddmad).into(gifImage)
    }

    //Checks to see if the correct  letter is in the correct box
    private fun checkLetter() {

        for (i in 0 until ROWS_MAX) {
            for (j in 0 until COLUMNS_MAX) {
                val userInput = textViewsDummy[i][j]?.text.toString()


                val correctAnswer = textViews[i][j]
                if (userInput == " ") {
                    continue;
                } else {
                    // Check if the entered text matches the correct answer
                    if (userInput.uppercase() == correctAnswer) {
                        textViewsDummy[i][j]?.setBackgroundColor(resources.getColor(R.color.codinggreen))
                        textViewsDummy[i][j]?.setTextColor(Color.WHITE)
                    } else {
                        textViewsDummy[i][j]?.setBackgroundColor(resources.getColor(R.color.coolred))
                        textViewsDummy[i][j]?.setTextColor(Color.BLACK)
                    }
                }

            }
        }

        for (i in 0 until ROWS_MAX) {
            for (j in 0 until COLUMNS_MAX) {
                // Set an action listener to listen for the Enter key press
                textViewsDummy[i][j]?.setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                    ) {
                        // Save the text when Enter is pressed
                        val text = textViewsDummy[i][j]?.text.toString()
                        val editor = sharedPreferences.edit()
                        editor.putString("savedText", text)
                        editor.apply()

                        // Set the text again after saving
                        textViewsDummy[i][j]?.setText(text)

                        true // Consume the event
                    } else {
                        false // Let other listeners handle the event
                    }
                }
            }
        }
        checkWin()
    }

}