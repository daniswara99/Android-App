package com.kelompok4.androidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FirstScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        val inputName = findViewById<EditText>(R.id.inputName)
        val inputPalindrome = findViewById<EditText>(R.id.inputPalindrome)
        val btnCheck = findViewById<Button>(R.id.btnCheck)
        val btnNext = findViewById<Button>(R.id.btnNext)

        btnCheck.setOnClickListener {
            val sentence = inputPalindrome.text.toString().replace(" ", "").toLowerCase()
            val isPalindrome = sentence == sentence.reversed()
            val message = if (isPalindrome) "is Palindrome" else "Not Palindrome"
            AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }

        btnNext.setOnClickListener {
            val name = inputName.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SecondScreen::class.java).apply {
                    putExtra("name", name)
                }
                startActivity(intent)
            }
        }
    }
}
