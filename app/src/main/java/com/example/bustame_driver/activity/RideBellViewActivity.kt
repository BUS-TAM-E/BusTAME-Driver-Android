package com.example.bustame_driver.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bustame_driver.databinding.ActivityRideBellViewBinding
import com.example.bustame_driver.model.RideBellBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RideBellViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRideBellViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideBellViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트에서 버스 번호 가져오기
        val busNumber = intent.getStringExtra("inputBusNumber")

        binding.busNumber.text = busNumber

        // 액션바 만들기
        binding.backButton.setOnClickListener {
            finish()
        }

        // 테스트용 코드
        binding.imageView2.setOnClickListener {
            val getRideBellView = com.example.bustame_driver.api.Retrofit.service.findRideBell(busNumber.toString())
            var PassengerCount = 0
            var disabilityPassengerCount = 0
            var wheelCount = 0
            var slowCount = 0
            var babyCount = 0

            getRideBellView.enqueue(object : Callback<ArrayList<RideBellBody>> {
                // 응답
                override fun onResponse(
                    call: Call<ArrayList<RideBellBody>>,
                    response: Response<ArrayList<RideBellBody>>
                ) {
                    // 응답이 성공했을 때
                    if(response.isSuccessful) {
                        Log.d("getRideBell", response.code().toString())
                    } else {
                        Log.d("getRideBell", response.code().toString())
                    }
                }

                // 실패
                override fun onFailure(call: Call<ArrayList<RideBellBody>>, t: Throwable) {
                    Log.d("getRideBell", t.message.toString())
                    Log.d("getRideBell", "fail")
                }
            })
        }
    }

}