package com.vinyl.turntable

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscope: Sensor
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var textView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        textView = findViewById(R.id.textView)
        
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!
        
        mediaPlayer = MediaPlayer.create(this, R.raw.test_audio)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        
        textView.text = "Вращайте телефон!"
    }
    
    override fun onSensorChanged(event: SensorEvent) {
        val rpm = abs(event.values[2] * 9.55f)
        val rate = rpm / 33.3f
        
        textView.text = String.format("%.1f RPM (%.2fx)", rpm, rate)
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(
                rate.coerceIn(0.1f, 6.0f)
            )
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST)
    }
    
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        mediaPlayer.pause()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
