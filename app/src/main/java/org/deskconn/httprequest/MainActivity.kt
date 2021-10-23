package org.deskconn.httprequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import pk.codebase.requests.HttpRequest
import pk.codebase.requests.HttpResponse
import org.json.JSONException




class MainActivity : AppCompatActivity() {

    private val URL_BASE = "http://192.168.100.149:5000/api/"

    private val request = HttpRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val urlButton = findViewById<TextView>(R.id.urlButton)
        val postButton = findViewById<TextView>(R.id.postButton)
        val firstName = findViewById<EditText>(R.id.firstNameText)
        val secondName = findViewById<EditText>(R.id.secondNameText)
        val nameTextView = findViewById<TextView>(R.id.getName)
        val friendTextView = findViewById<TextView>(R.id.getFriend)
        urlButton.setOnClickListener {
            request.setOnResponseListener { response ->
                if (response.code == HttpResponse.HTTP_OK) {
                    val jsonResponse: JSONObject = response.toJSONObject()
                    println("here ${jsonResponse.get("name")}")
                    nameTextView.text = jsonResponse.get("name").toString()
                    friendTextView.text = jsonResponse.getString("friend")

                    Toast.makeText(this, response.text, Toast.LENGTH_LONG).show()
                }
                else if (response.code != HttpResponse.HTTP_OK){
                    println(response.text)
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            request.setOnErrorListener {
                // There was an error, deal with it
                println("Response error")
            }
            request[URL_BASE]
        }

        postButton.setOnClickListener setOnResponseListener@{
            when {
                TextUtils.isEmpty(firstName.text.toString()) -> {
                    firstName.error = "Please enter the First Name"
                }
                TextUtils.isEmpty(secondName.text.toString()) -> {
                    secondName.error = "Please enter the Last Name"
                }
                else -> {
                    request.setOnResponseListener{ response ->
                        if (response.code == HttpResponse.HTTP_OK) {
                            println(response.toJSONObject())
                            Toast.makeText(this, response.text, Toast.LENGTH_LONG).show()
                        }
                    }
                    request.setOnErrorListener{
                        // There was an error, deal with it
                        println("Post Not Sent")
                    }

                    val json: JSONObject
                    try {
                        json = JSONObject()
                        json.put("first_name", firstName.text.toString())
                        json.put("last_name", secondName.text.toString())
                    } catch (ignore: JSONException) {
                        return@setOnResponseListener
                    }
                    request.post(URL_BASE, json)
                }
            }

        }

    }
}
