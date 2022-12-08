package com.muiedhossain.bmicalculator

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val weightInput = findViewById<EditText>(R.id.weight_ET)
        val heightInput = findViewById<EditText>(R.id.height_ET)
        val calculate = findViewById<Button>(R.id.calculate_btn)

        calculate.setOnClickListener {
            val weight = weightInput.text.toString()
            val height = heightInput.text.toString()

            if(validateInputs(weight,height)){
                val bmi = weight.toFloat()/((height.toFloat()/100)*((height.toFloat()/100)))
                val bmi_in_2Digits = String.format("%.2f",bmi).toFloat()
                displayTheBMIValue(bmi_in_2Digits)
            }
            weightInput.text.clear()
            heightInput.text.clear()

        }
        weightInput.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        heightInput.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

    }

    private fun validateInputs(weight:String? , height: String?):Boolean{
        return when{
            weight.isNullOrEmpty() ->{
                Toast.makeText(this,"Weight is empty",Toast.LENGTH_LONG).show()
                return false
            }
            height.isNullOrEmpty() -> {
                Toast.makeText(this,"Height is empty",Toast.LENGTH_LONG).show()
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun displayTheBMIValue(bmi:Float) {
        val bmiValue = findViewById<TextView>(R.id.bmi_value)
        val bmiCondition = findViewById<TextView>(R.id.bmi_condition)
        val info = findViewById<TextView>(R.id.tv_info)

        bmiValue.text = bmi.toString()
        info.text = "( Normal Value = 18.5 - 24.9 )"

        var health_condition = ""
        var color = 0

        when{
            bmi < 18.50 -> {
                health_condition = "Underweight"
                color = R.color.under_weight
            }
            bmi in 18.50..24.99 -> {
                health_condition = "Healthy"
                color = R.color.normal
            }
            bmi in 25.00..29.99 -> {
                health_condition = "Overweight"
                color = R.color.over_weight
            }
            bmi >= 30.00 -> {
                health_condition = "Obese"
                color = R.color.obese
            }
        }
        bmiCondition.setTextColor(ContextCompat.getColor(this,color))
        bmiCondition.text = health_condition
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view != null && view is EditText) {
                val r = Rect()
                view.getGlobalVisibleRect(r)
                val rawX = ev.rawX.toInt()
                val rawY = ev.rawY.toInt()
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}