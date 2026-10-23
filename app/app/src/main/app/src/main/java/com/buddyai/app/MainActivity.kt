package com.buddyai.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.Toast
import android.view.inputmethod.InputMethodManager
import android.content.Context
import kotlin.concurrent.thread

class MainActivity : Activity() {

    private lateinit var questionInput: EditText
    private lateinit var answerText: TextView
    private lateinit var askButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(20, 20, 35)
        window.navigationBarColor = Color.rgb(20, 20, 35)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 16)
            setBackgroundColor(Color.rgb(20, 20, 35))
        }

        val title = TextView(this).apply {
            text = "🤖 Buddy AI"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 16, 0, 8)
        }
        root.addView(title)

        val subtitle = TextView(this).apply {
            text = "Your Personal AI Friend"
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }
        root.addView(subtitle)

        questionInput = EditText(this).apply {
            hint = "Ask Buddy anything..."
            textSize = 16f
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            setPadding(16, 12, 16, 12)
            setBackgroundColor(Color.rgb(45, 45, 65))
        }

        root.addView(
            questionInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        askButton = Button(this).apply {
            text = "Ask Buddy"
            textSize = 16f
            setOnClickListener { askBuddy() }
        }

        root.addView(
            askButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        progressBar = ProgressBar(this).apply {
            visibility = android.view.View.GONE
        }
        root.addView(
            progressBar,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                topMargin = 12
                bottomMargin = 12
            }
        )

        val scrollView = ScrollView(this)
        answerText = TextView(this).apply {
            text = "Hi! I'm Buddy, your AI friend. Ask me anything!"
            textSize = 17f
            setTextColor(Color.WHITE)
            setPadding(12, 20, 12, 20)
        }

        scrollView.addView(answerText)
        root.addView(
            scrollView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }

    private fun askBuddy() {
        val question = questionInput.text.toString().trim()

        if (question.isEmpty()) {
            Toast.makeText(this, "Pehle apna sawal likho!", Toast.LENGTH_SHORT).show()
            return
        }

        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        keyboard.hideSoftInputFromWindow(questionInput.windowToken, 0)

        askButton.isEnabled = false
        progressBar.visibility = android.view.View.VISIBLE
        answerText.text = "Buddy soch raha hai..."

        thread {
            val answer = GeminiClient.askBuddy(question)

            runOnUiThread {
                answerText.text = answer
                askButton.isEnabled = true
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
}
