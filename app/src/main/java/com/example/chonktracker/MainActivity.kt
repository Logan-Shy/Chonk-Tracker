package com.example.chonktracker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
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



    fun getTimeSinceFed(view: View) {
        // get content view and initialize networking
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(getApplicationContext())
        var timeSinceFed = "Default Time Text"
        var dayFed = "Default Day Text"

        // Get Current time and day since fed from backend service
        AndroidNetworking.get("http://157.245.250.195/")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    timeSinceFed = response.get("time").toString()
                    dayFed = response.get("day").toString()

                    val timeView : TextView = findViewById(R.id.textView2)
                    timeView.text = "$dayFed, $timeSinceFed"
                }

                override fun onError(error: ANError) {
                    val timeView : TextView = findViewById(R.id.textView2)
                    timeView.text = error.message.toString()
                }
            })

    }



    fun updateTime(view: View){
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(getApplicationContext())

        // Use java util calendar method to get current hour and minute
        // Format differently depending on AM or PM and fill with 0's if less than 10
        val c = Calendar.getInstance()
        var day : String
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

        when(c.get(Calendar.DAY_OF_WEEK)){
            Calendar.SUNDAY -> day = "Sunday"
            Calendar.MONDAY -> day = "Monday"
            Calendar.TUESDAY -> day = "Tuesday"
            Calendar.WEDNESDAY -> day = "Wednesday"
            Calendar.THURSDAY -> day = "Thursday"
            Calendar.FRIDAY -> day = "Friday"
            Calendar.SATURDAY -> day = "Saturday"
            Calendar.SUNDAY -> day = "Sunday"
            else -> day ="day N/A"
        }


        val time = "$hour:$minutes $pmOrAm"

        // Set time since fed to time and day var
        val timeView: TextView = findViewById(R.id.textView2)
        timeView.text = "$day, $time"

        // Update the backend with the time and day in JSON format
        val timeJSON = JSONObject();
        timeJSON.put("time", time)
        timeJSON.put("day", day)
        AndroidNetworking.post("http://157.245.250.195/")
            .addJSONObjectBody(timeJSON)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    // No error, so post was successful
                }

                override fun onError(error: ANError) {
//                    val timeView: TextView = findViewById(R.id.textView2)
//                    timeView.text = error.message
                }
            })

    }

    //TODO: schedule worker to calculate hunger since fed
    fun updateHunger(view: View) {
        // get content view and initialize networking
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(getApplicationContext())
        var timeSinceFed: String

        // Get Current time since fed from backend service
        AndroidNetworking.get("http://157.245.250.195/")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    //get time and strip to first character
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

}

class DinnerBellWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        //Do the work here
        val timeSinceFed =
            inputData.getString("TIME_SINCE_FED") ?: return Result.failure()



        //Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}
