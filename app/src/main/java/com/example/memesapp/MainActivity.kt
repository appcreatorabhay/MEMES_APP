package com.example.memesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private var current: String? = null
    private var previous: String? = null

    private lateinit var imageView1: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        imageView1 = findViewById(R.id.imageView1)
        progressBar = findViewById(R.id.progressBar)

        // Load memes on activity creation
        memes()
    }

    private fun memes() {
        // Show progress bar
        progressBar.visibility = View.VISIBLE

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url ="https://meme-api.com/gimme"

        // Request a JsonObject response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                // Display the image from the response using Glide
                previous=current
                current = response.getString("url")
                Glide.with(this).load(current).into(imageView1)

                // Hide progress bar
                progressBar.visibility = View.GONE
            },
            { error ->
                // Handle error
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()

                // Hide progress bar
                progressBar.visibility = View.GONE
            })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }

    fun share(view: View) {
        // Share the current meme
        if (current != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this meme ,It is really funny! $current")

            startActivity(Intent.createChooser(intent, "Share via"))
        }
    }

    fun next(view: View) {
        // Load next meme
        memes()
    }

    fun back(view: View) {
        if (previous != null) {
            Glide.with(this).load(previous).into(imageView1)
            current = previous
            previous = null
        }

    }
}
