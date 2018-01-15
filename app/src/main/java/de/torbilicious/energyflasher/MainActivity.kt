package de.torbilicious.energyflasher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private var flasher: Flasher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flasher = Flasher(this)

        val numberInput: EditText = findViewById(R.id.number_input)

        //Show keyboard automatically
        numberInput.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)


        numberInput.setOnEditorActionListener { textView, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Log.d(this::class.java.simpleName, "Finished typing '${textView.text}'")

                    val numbers = textView.text.map { it.toInt() - 48 }

                    flasher?.flashNumbers(numbers)
                }
            }

            return@setOnEditorActionListener true
        }

        val flashButton: Button = findViewById(R.id.button_flash)

        with(flasher) {
            flashButton.setOnTouchListener(OnTouchListener { _, motionEvent ->

                when (motionEvent.action) {
                    MotionEvent.ACTION_UP -> {
                        Log.d(MainActivity::class.java.simpleName, "flash up")
                        this?.stopFlash()
                    }

                    MotionEvent.ACTION_DOWN -> {
                        Log.d(MainActivity::class.java.simpleName, "flash down")
                        this?.startFlash()
                    }

                    else -> {
                    }
                }

                return@OnTouchListener true
            })
        }
    }


}
