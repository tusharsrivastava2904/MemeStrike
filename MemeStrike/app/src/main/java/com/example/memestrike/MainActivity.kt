package com.example.memestrike

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    var currentImageUrl : String? =null

    private fun loadMeme() {
        progressBar.visibility = View.VISIBLE                                                                                            // created newRequestQueue using documentation of volley on  developer.android.com
                                                                                                    // Instantiate the RequestQueue.

        val url = "https://meme-api.herokuapp.com/gimme"                                            //free get meme api
                                                                                                    //creating request using GET
                                                                                                    // Request a JSON object response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->                         //success
                currentImageUrl = response.getString("url")                                           //passing the variable of url as defined in API

                Glide.with(this).load(currentImageUrl).listener(object :RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeimageView)                                                              //Request listener passed under listener
            },
                                                                                                    //we use this url to insert image using **glide library**
            Response.ErrorListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            })                                                        //failure

                                                                                                    // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)                  //single instance of volley to access request during entire app cycle
                                                                                                    //as soon as request is added to the Queue, Volley
                                                                                                    // will trace it and respond to the Response.Listener
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Checkout this meme on Reddit: $currentImageUrl Sent via Meme Strike ;)")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}