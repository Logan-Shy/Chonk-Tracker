package com.example.chonktracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun feedQuant(view: View){
        setContentView(R.layout.activity_main)

        val c = Calendar.getInstance()
        var minutes : String = c.get(Calendar.MINUTE).toString()
        if (minutes.toInt() < 10){
            minutes = "0$minutes"
        }
        val hour = c.get(Calendar.HOUR)
        var pmOrAm : String = c.get(Calendar.AM_PM).toString()
        if(pmOrAm == "1"){
            pmOrAm = "P.M."
        } else {
            pmOrAm = "A.M."
        }
        val time = "$hour:$minutes $pmOrAm"

        val timeView: TextView = findViewById(R.id.textView2)
        timeView.text = time
    }
}
