package com.kelompok4.androidapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.kelompok4.androidapp.model.ApiService
import com.kelompok4.androidapp.model.User
import com.kelompok4.androidapp.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ThirdScreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var emptyView: TextView
    private val users = mutableListOf<User>()
    private val apiService: ApiService
    private var currentPage = 1
    private var totalPages = 1

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_screen)

        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        emptyView = findViewById(R.id.emptyView)

        adapter = UserAdapter(users) { user ->
            val intent = Intent()
            intent.putExtra("selectedUserName", "${user.first_name} ${user.last_name}")
            intent.putExtra("selectedUser", user)
            setResult(RESULT_OK, intent)
            finish()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            fetchUsers()
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        swipeRefreshLayout.isRefreshing = true
        apiService.getUsers(currentPage, 10).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let {
                        if (currentPage == 1) {
                            users.clear()
                        }
                        users.addAll(it.data)
                        adapter.notifyDataSetChanged()
                        totalPages = it.total_pages
                        emptyView.visibility = if (users.isEmpty()) TextView.VISIBLE else TextView.GONE
                    }
                } else {
                    Log.e("ThirdScreen", "Response not successful: ${response.code()}")
                    emptyView.text = "Failed to load users. Response not successful: ${response.code()}"
                    emptyView.visibility = TextView.VISIBLE
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Log.e("ThirdScreen", "Error fetching users", t)
                emptyView.text = "Failed to load users. Error: ${t.message}"
                emptyView.visibility = TextView.VISIBLE
            }
        })
    }
}
