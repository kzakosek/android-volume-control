package com.example.volumecontrol

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSetVolume: Button = findViewById(R.id.btn_SetVolume)
        val btnSetLines: Button = findViewById(R.id.button_SetLines)
        val cbMethod: CheckBox = findViewById(R.id.checkBox_pickMethod)
        val textVolume: TextView = findViewById(R.id.text_Volume)
        val textLines: TextView = findViewById(R.id.text_Lines)
        val textCurrentVolume: TextView = findViewById(R.id.text_CurrentVolume)
        val volumeControl : VolumeControl = findViewById(R.id.bar1)

        //On start fill the bars and textfield based on current phone volume
        val currentVolumePercentage = volumeControl.getVolumePercentage()
        val currentVolume = volumeControl.getVolumeCurrent()
        val currentLines = volumeControl.getDivisionsNumber()
        cbMethod.isChecked=true
        textVolume.text = currentVolumePercentage.toString()
        textLines.text = currentLines.toString()
        textCurrentVolume.text = "Volume set at : 0%"
        volumeControl.setCurrentBarValue(currentVolumePercentage.toDouble()/100)

        //OTHER SET METHODS
        //Spacing
        //volumeControl.setBarSpacing(100)
        //Bar Background color
        //volumeControl.setBarColorBackground(Color.parseColor("#00ff00"))
        //Bar Foreground color
        //volumeControl.setBarColorForeground(Color.parseColor("#00ff00"))

        volumeControl.invalidate ()

        //set volume button
        btnSetVolume.setOnClickListener{
            if(!TextUtils.isEmpty(textVolume.text)){
                volumeControl.setVolume(textVolume.text.toString().toInt())
                volumeControl.setCurrentBarValue((textVolume.text.toString().toDouble()/100))
                volumeControl.invalidate ()

                val result : String = "Volume set at : " + textVolume.text.toString() + "%"
                textCurrentVolume.text = result
            }else{
                Toast.makeText (applicationContext, "Enter volume", Toast.LENGTH_LONG).show ()
            }
        }

        //set lines button
        btnSetLines.setOnClickListener{
           if(!TextUtils.isEmpty(textLines.text)){
                volumeControl.setDivisionsNumber(textLines.text.toString().toInt())
                volumeControl.setCurrentBarValue((textVolume.text.toString().toDouble()/100))
                volumeControl.invalidate ()
            }else{
                Toast.makeText (applicationContext, "Enter lines", Toast.LENGTH_LONG).show ()
            }
        }

        //on start add listener
        if(cbMethod.isChecked){
            volumeControl.setOnTouchListener(object : VolumeControl.SwipeListener(applicationContext) {
                override fun onSwipeTop(increase : Int) {
                    val currentPercentage = volumeControl.getVolumePercentage()
                    var nextVolume : Int = currentPercentage + increase

                    if(nextVolume > 100){
                        nextVolume = 100
                    }

                    volumeControl.setVolumePercentage(nextVolume)
                    volumeControl.setVolume(nextVolume)

                    volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                    volumeControl.invalidate ()

                    textVolume.text = nextVolume.toString()
                }
                override fun onSwipeBottom(increase : Int) {
                    val currentPercentage = volumeControl.getVolumePercentage()
                    var nextVolume : Int = currentPercentage - increase

                    if(nextVolume < 0){
                        nextVolume = 0
                    }

                    volumeControl.setVolumePercentage(nextVolume)
                    volumeControl.setVolume(nextVolume)

                    volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                    volumeControl.invalidate ()

                    textVolume.text = nextVolume.toString()
                }
            })
        }else{
            volumeControl.setOnTouchListener(object : VolumeControl.DragListener(applicationContext){
                override fun onDragStart(){
                    //var c = volumeControl.getVolumePercentage()
                    //Log.d("CURRENT", c.toString())
                }
                override fun onDragTop(increase : Int){
                    val currentPercentage = volumeControl.getVolumePercentage()
                    var nextVolume : Int = currentPercentage + increase

                    if(nextVolume > 100){
                        nextVolume = 100
                    }

                    volumeControl.setVolumePercentage(nextVolume)
                    volumeControl.setVolume(nextVolume)

                    volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                    volumeControl.invalidate ()

                    textVolume.text = nextVolume.toString()
                }
                override fun onDragBottom(increase : Int){
                    val currentPercentage = volumeControl.getVolumePercentage()
                    var nextVolume : Int = currentPercentage - increase

                    if(nextVolume < 0){
                        nextVolume = 0
                    }

                    volumeControl.setVolumePercentage(nextVolume)
                    volumeControl.setVolume(nextVolume)

                    volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                    volumeControl.invalidate ()

                    textVolume.text = nextVolume.toString()
                }
            })
        }

        //pick swipe or drag
        cbMethod.setOnClickListener{
            if(cbMethod.isChecked){
                volumeControl.setOnTouchListener(object : VolumeControl.SwipeListener(applicationContext) {
                    override fun onSwipeTop(increase : Int) {
                        val currentPercentage = volumeControl.getVolumePercentage()
                        var nextVolume : Int = currentPercentage + increase

                        if(nextVolume > 100){
                            nextVolume = 100
                        }

                        volumeControl.setVolumePercentage(nextVolume)
                        volumeControl.setVolume(nextVolume)

                        volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                        volumeControl.invalidate ()

                        textVolume.text = nextVolume.toString()
                    }
                    override fun onSwipeBottom(increase : Int) {
                        val currentPercentage = volumeControl.getVolumePercentage()
                        var nextVolume : Int = currentPercentage - increase

                        if(nextVolume < 0){
                            nextVolume = 0
                        }

                        volumeControl.setVolumePercentage(nextVolume)
                        volumeControl.setVolume(nextVolume)

                        volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                        volumeControl.invalidate ()

                        textVolume.text = nextVolume.toString()
                    }
                })
            }else{
                volumeControl.setOnTouchListener(object : VolumeControl.DragListener(applicationContext){
                    override fun onDragStart(){
                        //var c = volumeControl.getVolumePercentage()
                        //Log.d("CURRENT", c.toString())
                    }
                    override fun onDragTop(increase : Int){
                        val currentPercentage = volumeControl.getVolumePercentage()
                        var nextVolume : Int = currentPercentage + increase

                        if(nextVolume > 100){
                            nextVolume = 100
                        }

                        volumeControl.setVolumePercentage(nextVolume)
                        volumeControl.setVolume(nextVolume)

                        volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                        volumeControl.invalidate ()

                        textVolume.text = nextVolume.toString()
                    }
                    override fun onDragBottom(increase : Int){
                        val currentPercentage = volumeControl.getVolumePercentage()
                        var nextVolume : Int = currentPercentage - increase

                        if(nextVolume < 0){
                            nextVolume = 0
                        }

                        volumeControl.setVolumePercentage(nextVolume)
                        volumeControl.setVolume(nextVolume)

                        volumeControl.setCurrentBarValue(nextVolume.toDouble()/100)
                        volumeControl.invalidate ()

                        textVolume.text = nextVolume.toString()
                    }
                })
            }
        }
    }
}