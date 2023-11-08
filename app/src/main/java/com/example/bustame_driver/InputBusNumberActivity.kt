package com.example.bustame_driver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bustame_driver.databinding.ActivityInputBusNumberBinding
class InputBusNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputBusNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBusNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}