package com.example.chonktracker

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get Current time since fed
        getTimeSinceFed(findViewById(R.id.textView2))
    }



    public fun getTimeSinceFed(view: View) {
        // get content view and initialize networking
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(getApplicationContext())
        var timeSinceFed : String = "Default Text"

        // Get Current time since fed from backend service
        AndroidNetworking.get("http://157.245.250.195/")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    timeSinceFed = response.get("time").toString()
                    val timeView : TextView = findViewById(R.id.textView2)
                    timeView.text = timeSinceFed
                }

                override fun onError(error: ANError) {
                    timeSinceFed = error.message.toString()
                    val timeView : TextView = findViewById(R.id.textView2)
                    timeView.text = timeSinceFed
                }
            })

    }



    fun updateTime(view: View){
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(getApplicationContext())

        // Use java util calendar method to get current hour and minute
        // Format differently depending on AM or PM and fill with 0's if less than 10
        val c = Calendar.getInstance()
        var minutes : String = c.get(Calendar.MINUTE).toString()
        if (minutes.toInt() < 10){
            minutes = "0$minutes"
        }
        val hour = c.get(Calendar.HOUR)
        var pmOrAm : String = c.get(Calendar.AM_PM).toString()
        pmOrAm = if(pmOrAm == "1"){
            "P.M."
        } else {
            "A.M."
        }
        val time = "$hour:$minutes $pmOrAm"

        // Set time since fed to time var
        val timeView: TextView = findViewById(R.id.textView2)
        timeView.text = time

        // Update the backend with the time in JSON format
        val timeJSON = JSONObject();
        timeJSON.put("time", time)
        AndroidNetworking.post("http://157.245.250.195/")
            .addJSONObjectBody(timeJSON)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    // No error, so post was successful
                }

                override fun onError(error: ANError) {
//                    val timeSinceFed = error.message
//                    val timeView: TextView = findViewById(R.id.textView2)
//                    timeView.text = timeSinceFed
                }
            })

    }

    fun calculateHunger(hour : String, pmam : String){

    }
}
