package com.example.bustame_driver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bustame_driver.databinding.ActivityRideBellViewBinding

class RideBellViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRideBellViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideBellViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트에서 버스 번호 가져오기
        val busNumber = intent.getStringExtra("inputBusNumber")

        binding.textView3.text = busNumber
    }
}