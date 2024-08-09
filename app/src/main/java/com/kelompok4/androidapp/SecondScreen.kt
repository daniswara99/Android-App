package com.kelompok4.androidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok4.androidapp.model.User

class SecondScreen : AppCompatActivity() {

    private lateinit var selectedUserText: TextView
    private lateinit var nameText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private val selectedUserList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)

        selectedUserText = findViewById(R.id.selectedUserText)
        nameText = findViewById(R.id.nameText)
        recyclerView = findViewById(R.id.recyclerView)
        val btnChooseUser = findViewById<Button>(R.id.btnChooseUser)

        val name = intent.getStringExtra("name")
        nameText.text = name ?: "No name provided"

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(selectedUserList) { user ->
        }
        recyclerView.adapter = adapter

        btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdScreen::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val selectedUserName = data.getStringExtra("selectedUserName")
            selectedUserText.text = selectedUserName ?: "No user selected"

            val selectedUser = data.getParcelableExtra<User>("selectedUser")
            if (selectedUser != null) {
                // Clear the current list and add the selected user
                selectedUserList.clear()
                selectedUserList.add(selectedUser)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
