package com.example.coaching_app

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.coaching_app.databinding.ActivitySchedulerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Intent
import android.view.View
import java.util.*

class SchedulerActivity : AppCompatActivity() {

    private lateinit var tvDate : TextView
    private lateinit var btnShowDatePicker : Button
    private val calendar = Calendar.getInstance()
    private lateinit var binding: ActivitySchedulerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        tvDate = findViewById<TextView>(R.id.tvSelectDate)
//        btnShowDatePicker = findViewById<Button>(R.id.btnShowDatePicker)

//        btnShowDatePicker.setOnClickListener{
//            showDatePicker()
//        }

    }


    fun addCalendarEvent(view: View) {
        val calendarEvent: Calendar = Calendar.getInstance()
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra("beginTime", calendarEvent.timeInMillis)
        intent.putExtra("allDay", true)
        intent.putExtra("rule", "FREQ=YEARLY")
        intent.putExtra("endTime", calendarEvent.timeInMillis + 60 * 60 * 1000)
        intent.putExtra("title", "Calendar Event")
        startActivity(intent)
    }

//    private fun showDatePicker(){
//        val datePickerDialog = DatePickerDialog(this,{DatePicker, year : Int, monthOfYear : Int, dayOfMonth : Int ->
//            val selectedDate = Calendar.getInstance()
//            selectedDate.set(year, monthOfYear, dayOfMonth)
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//            val formattedDate = dateFormat.format(selectedDate.time)
//            tvDate.text = "Selected Date: " + formattedDate
//        },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//
//        datePickerDialog.show()
//    }
}