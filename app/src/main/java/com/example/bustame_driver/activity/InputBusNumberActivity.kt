package com.example.bustame_driver.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.bustame_driver.R
import com.example.bustame_driver.databinding.ActivityInputBusNumberBinding

class InputBusNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputBusNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBusNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버스 번호 저장할 변수
        var busNumber: String = ""

        // 버튼 비활성화
        binding.InputButton.isEnabled = false
        binding.InputButton.setBackgroundResource(R.color.green300)

        // 버스 번호가 입력될 때 버튼 활성화
        binding.editBusNumber.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            // 값이 변경되면 실행되는 함수
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력값 저장하기
                busNumber = binding.editBusNumber.text.toString()

                // 값의 유무에 따른 버튼 활성화 여부
                if(busNumber.isNotEmpty()){
                    binding.InputButton.isEnabled = true
                    binding.InputButton.isClickable = true
                    binding.InputButton.setBackgroundResource(R.color.white)
                } else {
                    binding.InputButton.isEnabled = false
                    binding.InputButton.setBackgroundResource(R.color.green300)
                }

            }
        })

        binding.InputButton.setOnClickListener {
            // 입력된 버스 번호
            val inputBusNumber = busNumber

            // 새 액티비티를 생성하는 인텐트 생성
            val intent = Intent(this, RideBellViewActivity::class.java).apply{
                // 입력된 버스 번호를 inputBusNumber라는 키로 넘긴다.
                putExtra("inputBusNumber", inputBusNumber)
            }

            // 액티비티 시작
            startActivity(intent)
        }

    }
}