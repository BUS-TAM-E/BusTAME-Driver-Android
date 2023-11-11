package com.example.bustame_driver.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bustame_driver.R
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

        // 실시간 갱신을 통한 탑승 현황 파악하기
        Thread {
            while (!Thread.interrupted()) try {
                // 10초 간격으로 데이터 갱신
                Thread.sleep(10000)
                runOnUiThread{

                    // 17007 동양미래대학.구로성심병원(중) 정류장에서 승차벨을 울린 탑승인원 파악
                    // 버스 번호를 입력한 number를 통해 승차벨 api 통신
                    val getRideBellView = com.example.bustame_driver.api.Retrofit.service.findRideBell(busNumber.toString())
                    // 일반 승객
                    var PassengerCount = 0
                    // 교통약자 승객
                    var disabilityPassengerCount = 0
                    // 요청사항 : 휠체어
                    var wheelCount = 0
                    // 요청사항 : 기다려주세요
                    var slowCount = 0
                    // 요청사항 : 유모차
                    var babyCount = 0

                    getRideBellView.enqueue(object : Callback<ArrayList<RideBellBody>> {
                        // 응답
                        override fun onResponse(
                            call: Call<ArrayList<RideBellBody>>,
                            response: Response<ArrayList<RideBellBody>>
                        ) {
                            // 응답이 성공했을 때
                            if(response.isSuccessful) {
                                val rideBellBodyList: ArrayList<RideBellBody>? = response.body()

                                // 값이 비어 있지 않을 때
                                if(rideBellBodyList != null && rideBellBodyList.isNotEmpty()) {

                                    // 응답 받은 값들을 변수에 저장하기
                                    for(rideBellBody in rideBellBodyList){
                                        val busNum: String? = rideBellBody.busNumber
                                        val busStopNum: String? = rideBellBody.busStopNumber
                                        val passengerType: String? = rideBellBody.passengerType
                                        val message: String? = rideBellBody.message

                                        // 해당 버스의 해당 정류장에서 승차벨을 요청했을 때
                                        if(busNum == busNumber && busStopNum == "17007") {
                                            //----------------
                                            // 탑승 인원 파악하기
                                            // 일반 승객 수
                                            if (passengerType == "일반")
                                                PassengerCount += 1
                                            // 교통약자 승객 수
                                            else if (passengerType == "교통약자")
                                                disabilityPassengerCount += 1

                                            //----------------
                                            // 요청사항 파악하기
                                            if (message != null) {
                                                // 휠체어
                                                if (message.contains("휠체어"))
                                                    wheelCount += 1

                                                // 유모차
                                                if (message.contains("유모차"))
                                                    babyCount += 1

                                                // 기다려주세요
                                                if (message.contains("기다려주세요"))
                                                    slowCount += 1

                                            }

                                            // 탑승 인원 현황 출력하기
                                            binding.textCountHuman.setText("${PassengerCount + disabilityPassengerCount}")

                                            // 교통약자가 한 명이라도 있을 때 교통약자가 있다는 표시하기
                                            if (disabilityPassengerCount > 0) {
                                                binding.textDisadvantaged.visibility = View.VISIBLE
                                            } else {
                                                binding.textDisadvantaged.visibility = View.GONE
                                            }

                                            // 휠체어 유무
                                            if (wheelCount > 0) {
                                                binding.imageWheel.setImageResource(R.drawable.wheel)
                                            } else {
                                                binding.imageWheel.setImageResource(R.drawable.none_wheel)
                                            }

                                            // 유모차 유무
                                            if (babyCount > 0) {
                                                binding.imageBaby.setImageResource(R.drawable.baby)
                                            } else {
                                                binding.imageBaby.setImageResource(R.drawable.none_baby)
                                            }

                                            // 기다려주세요 유무
                                            if (slowCount > 0) {
                                                binding.imageSlow.setImageResource(R.drawable.slow)
                                            } else {
                                                binding.imageSlow.setImageResource(R.drawable.none_slow)
                                            }
                                        }
                                    }
                                }
                                // 값이 null일 때
                                else {
                                    binding.textCountHuman.setText("0")
                                    binding.textDisadvantaged.visibility = View.GONE
                                    binding.imageWheel.setImageResource(R.drawable.none_wheel)
                                    binding.imageBaby.setImageResource(R.drawable.none_baby)
                                    binding.imageSlow.setImageResource(R.drawable.none_slow)
                                }
                            }
                            // 응답이 실패했을 때
                            else {
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
            } catch (e: InterruptedException) {

            }
        }.start()
    }

}