package com.example.compound_interest

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val currency = NumberFormat.getCurrencyInstance(Locale.US)
    private val frequencyLabels = listOf("Annually", "Semi-Annually", "Quarterly", "Monthly", "Weekly", "Daily")
    private val frequencyValues = listOf(1, 2, 4, 12, 52, 365)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerFrequency = findViewById<Spinner>(R.id.spinnerFrequency)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val btnReset = findViewById<Button>(R.id.btnReset)

        spinnerFrequency.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, frequencyLabels)
        spinnerFrequency.setSelection(3)

        btnCalculate.setOnClickListener { calculate() }
        btnReset.setOnClickListener { reset() }
    }

    private fun calculate() {
        val etPrincipal = findViewById<EditText>(R.id.etPrincipal)
        val etRate = findViewById<EditText>(R.id.etRate)
        val etYears = findViewById<EditText>(R.id.etYears)
        val etMonthly = findViewById<EditText>(R.id.etMonthly)
        val spinnerFrequency = findViewById<Spinner>(R.id.spinnerFrequency)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        val principal = etPrincipal.text.toString().toDoubleOrNull() ?: return
        val rate = etRate.text.toString().toDoubleOrNull() ?: return
        val years = etYears.text.toString().toIntOrNull() ?: return
        val monthly = etMonthly.text.toString().toDoubleOrNull() ?: 0.0

        val n = frequencyValues[spinnerFrequency.selectedItemPosition]
        val r = rate / 100.0
        var balance = principal
        var totalContributions = principal

        repeat (years) {
            repeat (n) {
                balance *=  (1 + r / n)
                val contrib = monthly * (12.0 / n)
                balance += contrib
                totalContributions += contrib
            }
        }

        val totalInterest = balance - totalContributions

        tvResult.visibility = View.VISIBLE
        tvResult.text = buildString {
            appendLine("Future Value:    ${currency.format(balance)}")
            appendLine("Total Invested:  ${currency.format(totalContributions)}")
            append("Interest Earned: ${currency.format(totalInterest)}")
        }
    }

    private fun reset() {
        findViewById<EditText>(R.id.etPrincipal).text.clear()
        findViewById<EditText>(R.id.etRate).text.clear()
        findViewById<EditText>(R.id.etYears).text.clear()
        findViewById<EditText>(R.id.etMonthly).text.clear()
        findViewById<Spinner>(R.id.spinnerFrequency).setSelection(3)
        findViewById<TextView>(R.id.tvResult).visibility = View.GONE
    }
}