package de.torbilicious.energyflasher

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import github.nisrulz.lantern.Lantern


class Flasher(private val activity: Activity) {
    private var flashPremissionGranted = false
    private val context = activity.applicationContext
    private val lantern: Lantern
    private var flashingNumbers = false

    private val timeBetweenNumbers: Long = 3100
    private val timeBetweenNumberParts: Long = 250
    private val timeToFlash: Long = 500

    init {
        lantern = initCamera()
    }

    private fun initCamera(): Lantern {
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.CAMERA, "android.permission.FLASHLIGHT"),
                1)

        val permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)

        when (permissionCheck) {
            PackageManager.PERMISSION_GRANTED -> {
                flashPremissionGranted = true
                Lantern.getInstance().init(context)
            }

            else -> {
                flashPremissionGranted = false

                Toast.makeText(context,
                        "Permission was not granted!",
                        Toast.LENGTH_LONG).show()
            }
        }

        return Lantern.getInstance()
    }

    private fun flash() {
        lantern.turnOnFlashlight(context)

        Thread.sleep(timeToFlash)

        lantern.turnOffFlashlight(context)
    }

    fun startFlash() {
        lantern.turnOnFlashlight(context)
    }

    fun stopFlash() {
        lantern.turnOffFlashlight(context)
    }

    fun flashNumbers(numbers: List<Int>) {
        if (numbers.size != 4) {
            Log.w(this::class.java.simpleName, "The size of the list was ${numbers.size} and not 4")
        }

        flashingNumbers = true

        numbers.forEach {
            flashnumber(it)

            Thread.sleep(timeBetweenNumbers)
        }

        flashingNumbers = false
    }

    private fun flashnumber(number: Int) {
        val timesToFlash = when {
            number < 1 -> 1
            number > 9 -> 9
            else -> {
                number
            }
        }

        Log.d(this::class.java.simpleName, "flashing number $timesToFlash")

        for (i in 1..timesToFlash) {
            flash()

            Thread.sleep(timeBetweenNumberParts)
        }
    }
}