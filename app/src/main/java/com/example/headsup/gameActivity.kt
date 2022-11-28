package com.example.headsup

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.headsup.databinding.ActivityGameBinding
import com.example.headsup.databinding.LandscapeActivityGameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random



class gameActivity : AppCompatActivity()  {
    lateinit var binding:ActivityGameBinding
    lateinit var bindingLandscape : LandscapeActivityGameBinding
    var celebrity= arrayListOf<CelebrityItem>()

    lateinit var sensorManager: SensorManager
    var time= 0L
    var highScore=-1
    var startGame=false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        bindingLandscape = LandscapeActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCeleibreites()
        Log.d("response", "onCreate: $celebrity")
        binding.score.text = "Score: $highScore"





    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLandscape.root)
            var index=Random.nextInt(celebrity.size-1)
            getCelebrity(celebrity[index].pk)
            if(!startGame)
            {   Timer()
                startGame=true
            }

            val display: Display = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
            val rotation: Int = display.getRotation()
            when (rotation) {
                Surface.ROTATION_270 -> highScore++
            }
                binding.score.text = "Score: $highScore"


        }
    }
    //Save Time
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("time",time)
        outState.putBoolean("startGame",startGame)
        outState.putInt("Score",highScore)
    }
    //Restore Time
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        time=savedInstanceState.getLong("time",0L)
        startGame=savedInstanceState.getBoolean("startGame",startGame)
        highScore=savedInstanceState.getInt("Score",0)

        binding.score.text="Score: ${highScore.toString()}"
        getSupportActionBar()?.setTitle("Time: ${time}").toString()
    }

    fun Timer(){
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time = millisUntilFinished / 1000
                getSupportActionBar()?.setTitle("Time: ${time}").toString()
            }
            override fun onFinish() {
                startGame=false

                finish()
            }
        }.start()
    }


    fun getCelebrity(primaryKey:Int){
        bindingLandscape.apply {
            var apiClient=APIClient().getClient()
            if(apiClient != null){
                val apiInterface = apiClient.create(APIinterface::class.java)
                //get Celebrity Info
                apiInterface.getCelebrity(primaryKey).enqueue(object : Callback<CelebrityItem> {
                    override fun onResponse(
                        call: Call<CelebrityItem>,
                        response: Response<CelebrityItem>
                    ) {
                        binding.apply {
                            var celebrities = response.body()!!

                            //Get Celebrity Info
                            CelebritynameTv.setText(celebrities.name)
                            taboo1tv.setText(celebrities.taboo1)
                            taboo2tv.setText(celebrities.taboo2)
                            taboo3tv.setText(celebrities.taboo3)

                        }
                    }//End response

                    override fun onFailure(call: Call<CelebrityItem>, t: Throwable) {
                        Toast.makeText(applicationContext,"Something Wrong!!!", Toast.LENGTH_LONG).show()
                        Log.d("response", "onFailure: ${t.message}")
                    }

                })//Get API interface , Responses

            }else{
                Toast.makeText(applicationContext,"Something Wrong!!!!", Toast.LENGTH_LONG).show()
                Log.d("response", "onResponse: ${apiClient}")
            }
        }
    }

    fun getCeleibreites(){
        val apiInterface= APIClient().getClient()?.create(APIinterface::class.java)

        //Get Items from API
        apiInterface!!.getCelebrites()?.enqueue(object :Callback<Celebrity>{
            override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                var  celebrities= response.body()!!
                celebrity.addAll(celebrities)
                Log.d("response", "onResponse: ${celebrities} ")

            }

            override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                Log.d("response", "OnFailure: ${t.message}")
            }

        })


    }

//    private fun setUpSensor(){
//        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
//            sensorManager.registerListener(
//                this,
//                it,
//                SensorManager.SENSOR_DELAY_FASTEST,
//                SensorManager.SENSOR_DELAY_FASTEST
//            )
//
//        }
//    }
//
//
//
//    override fun onSensorChanged(event: SensorEvent?) {
//        if(event?.sensor?.type==Sensor.TYPE_ACCELEROMETER){
//            val sides=event.values[0]
//           if(sides==0f){
//               Toast.makeText(this, "Congratulation " + ("\ud83d\ude0D"),Toast.LENGTH_LONG).show()
//           }else{
//               Toast.makeText(this, "Sorry, try again " + ("\ud83d\ude22"),Toast.LENGTH_LONG).show()
//           }
//
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        return
//    }


}