package com.vinyl.turntable

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private lateinit var textView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        textView = findViewById(R.id.textView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        
        val gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (gyro != null) {
            sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_FASTEST)
            textView.text = "🎵 Виниловый проигрыватель\n\nВращайте телефон как пластинку!\n\nСкорость: 0.0 RPM"
        } else {
            textView.text = "❌ Ошибка: телефон не поддерживает гироскоп"
        }
    }
    
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            // Угловая скорость в радианах/секунду
            val angularSpeed = event.values[2]
            // Конвертируем в RPM
            val rpm = abs(angularSpeed * 9.549f)
            // Коэффициент скорости (база 33.3 RPM)
            val speedFactor = rpm / 33.3f
            
            runOnUiThread {
                textView.text = String.format(
                    "🎵 Виниловый проигрыватель\n\nВращайте телефон как пластинку!\n\nСкорость: %.1f RPM\nКоэффициент: %.2fx",
                    rpm,
                    speedFactor.coerceIn(0f, 6f)
                )
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    
    override fun onResume() {
        super.onResume()
        val gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        gyro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }
    
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
