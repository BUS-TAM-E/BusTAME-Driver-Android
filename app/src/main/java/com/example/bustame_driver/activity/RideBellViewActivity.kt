package com.example.bustame_driver.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bustame_driver.R
import com.example.bustame_driver.databinding.ActivityRideBellViewBinding
import com.example.bustame_driver.model.RideBellBody
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RideBellViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRideBellViewBinding
    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideBellViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트에서 버스 번호 가져오기
        val bus = intent.getStringExtra("inputBusNumber")

        Log.d("BusNumber", "Bus Number: $bus")

        binding.busNumber.text = bus

        // 액션바 만들기
        binding.backButton.setOnClickListener {
            finish()
        }

        // 정류장명 설정하기
        binding.textBusStopName.setText("동양미래대학.구로성심병원(중)")

        // 실시간 갱신을 통한 탑승 현황 파악하기
        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(5000)
                withContext(Dispatchers.Main) {
                    updateRideBellData(bus.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun updateRideBellData(busNumber: String) {

        // 인텐트에서 버스 번호 가져오기
        val bus = intent.getStringExtra("inputBusNumber")

        // 17007 동양미래대학.구로성심병원(중) 정류장에서 승차벨을 울린 탑승인원 파악
        // 버스 번호를 입력한 number를 통해 승차벨 api 통신
        val getRideBellView = com.example.bustame_driver.api.Retrofit.service.findRideBell(bus)

        getRideBellView.enqueue(object : Callback<ArrayList<RideBellBody>> {
            // 응답
            override fun onResponse(
                call: Call<ArrayList<RideBellBody>>,
                response: Response<ArrayList<RideBellBody>>
            ) {
                if(response.isSuccessful) {
                    val rideBellBodyList: ArrayList<RideBellBody>? = response.body()
                    processRideBellData(busNumber, rideBellBodyList)
                }

                // 응답이 실패했을 때
                else {
                    Log.d("getRideBell", "Response code: " + response.code().toString())
                    Log.d("getRideBell", "Response message: " + response.message().toString())
                }
            }

            // 실패
            override fun onFailure(call: Call<ArrayList<RideBellBody>>, t: Throwable) {
                Log.d("getRideBell", t.message.toString())
                Log.d("getRideBell", "fail")
            }
        })
    }

    private fun processRideBellData(busNumber: String, rideBellBodyList: ArrayList<RideBellBody>?) {
        // 일반 승객
        var PassengerCount: Int = 0
        // 교통약자 승객
        var disabilityPassengerCount: Int = 0
        // 요청사항 : 휠체어
        var wheelCount: Int = 0
        // 요청사항 : 기다려주세요
        var slowCount: Int = 0
        // 요청사항 : 유모차
        var babyCount: Int = 0

        if(rideBellBodyList != null && rideBellBodyList.isNotEmpty()) {

            // 응답 받은 값들을 변수에 저장하기
            for(rideBellBody in rideBellBodyList){
                val busNum: String? = rideBellBody.busNumber
                val busStopNum: String? = rideBellBody.busStopNumber
                val passengerType: String? = rideBellBody.passengerType
                val message: String? = rideBellBody.message

                // 해당 버스의 해당 정류장에서 승차벨을 요청했을 때 17001 17228
                if(busNum == busNumber && busStopNum == "17001") {
                    if (passengerType == "일반")
                        PassengerCount += 1
                    else if (passengerType == "교통약자")
                        disabilityPassengerCount += 1

                    if (message != null) {
                        if (message.contains("휠체어"))
                            wheelCount += 1
                        if (message.contains("유모차"))
                            babyCount += 1
                        if (message.contains("기다려주세요"))
                            slowCount += 1
                    }

                    // 탑승 인원 현황 출력하기
                    binding.textCountHuman.text = "${PassengerCount + disabilityPassengerCount}"
                    binding.textDisadvantaged.visibility = if (disabilityPassengerCount > 0) View.VISIBLE else View.GONE
                    binding.imageWheel.setImageResource(if (wheelCount > 0) R.drawable.wheel else R.drawable.none_wheel)
                    binding.imageBaby.setImageResource(if (babyCount > 0) R.drawable.baby else R.drawable.none_baby)
                    binding.imageSlow.setImageResource(if (slowCount > 0) R.drawable.slow else R.drawable.none_slow)
                }
            }
        }
        // 값이 null일 때
        else {
            binding.textCountHuman.text = "0"
            binding.textDisadvantaged.visibility = View.GONE
            binding.imageWheel.setImageResource(R.drawable.none_wheel)
            binding.imageBaby.setImageResource(R.drawable.none_baby)
            binding.imageSlow.setImageResource(R.drawable.none_slow)
        }
    }
}
