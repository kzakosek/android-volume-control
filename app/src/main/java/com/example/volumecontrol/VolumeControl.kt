package com.example.volumecontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioManager
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.roundToInt


class VolumeControl : View{

    private val defaultBarPixelHeight = 8
    private val defaultBarPixelSpacing = 2
    private val defaultNumDivisions = 10
    private val defaultInitialValue = 0
    private var mPaint = Paint()
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    constructor(context: Context):super(context)

    constructor(context: Context, attrs: AttributeSet):super(context,attrs){
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int):super(context, attrs, defStyle){
        init(context, attrs)
    }

    // Properties
    private var barColorForeground = ContextCompat.getColor(context,R.color.bar_on)
    private var barColorBackground = ContextCompat.getColor(context,R.color.bar_off)
    private var divisionsNumber = 0
    private var barHeight = 0
    private var barSpacing = defaultBarPixelSpacing
    private var currentBarValue = defaultInitialValue
    private var currentVolume = 0
    private var currentVolumePercentage = 0

    //Get the value of the barColorForeground property.
    fun getBarColorForeground(): Int {
        return barColorForeground
    }
    //Set the value of the barColorForeground property.
    fun setBarColorForeground(barColorForeground: Int) {
        this.barColorForeground = barColorForeground
    }

    //Get the value of the barColorBackground property.
    fun getBarColorBackground(): Int {
        return barColorBackground
    }
    //Set the value of the barColorBackground property.
    fun setBarColorBackground(barColorBackground: Int) {
        this.barColorBackground = barColorBackground
    }

    //Get the value of the barHeight property.
    fun getBarHeight(): Int {
        return barHeight
    }
    //Set the value of the barHeight property.
    fun setBarHeight(barHeight: Int) {
        this.barHeight = barHeight
    }

    //Get the value of the barSpacing property.
    fun getBarSpacing(): Int {
        return barSpacing
    }
    //Set the value of the barSpacing property.
    fun setBarSpacing(barSpacing: Int) {
        this.barSpacing = barSpacing
    }

    //Get the value of the divisionsNumber property.
    fun getDivisionsNumber(): Int {
        return divisionsNumber
    }
    //Set the value of the divisionsNumber property.
    fun setDivisionsNumber(divisionsNumber: Int) {
        this.divisionsNumber = divisionsNumber
    }

    //Get the value of the currentBarValue property.
    fun getCurrentBarValue(): Int {
        return currentBarValue
    }
    //Set the value of the currentBarValue property.
    fun setCurrentBarValue(currentValue: Double) {
        val divisionsMax : Int = getDivisionsNumber() //10
        var barValue = (divisionsMax*currentValue).roundToInt()
        if(barValue > divisionsMax){
            barValue = 0
        }
        this.currentBarValue = barValue
    }

    //Get the value of the currentVolume property. (android volume)
    fun getVolumeCurrent() : Int{
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return currentVolume
    }
    //Set the value of the currentVolume property. (android volume)
    fun setVolumeCurrent(currentVolume: Int){
        this.currentVolume = currentVolume
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume,AudioManager.FLAG_SHOW_UI)
    }

    //Get the value of the currentVolumePercentage property
    fun getVolumePercentage() : Int{
        val maxVol : Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        currentVolumePercentage = 100 * getVolumeCurrent()/maxVol
        return currentVolumePercentage
    }
    //Set the value of the currentVolumePercentage property
    fun setVolumePercentage(currentVolumePercentage: Int){
        this.currentVolumePercentage = currentVolumePercentage
    }


    // Methods

    fun setVolume(enteredVolume : Int){
        val volumeMax : Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volumePercentage : Double = enteredVolume.toDouble()/100

        val volume : Double = volumeMax*volumePercentage
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume.roundToInt(),AudioManager.FLAG_SHOW_UI)

        //val currentVolume : Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        //val currentPercentage : Int = currentVolume*100/volumeMax

        setVolumeCurrent(volume.roundToInt())
        setVolumePercentage(enteredVolume)
    }

    private fun drawOnCanvasV(canvas: Canvas) {
        val maxValue = getDivisionsNumber()
        var numValue = getCurrentBarValue()
        val spacing = getBarSpacing()
        val customBarHeight = getBarHeight()
        var numDivs = getDivisionsNumber()
        val color1 = getBarColorForeground()
        val color2 = getBarColorBackground()

        if (numValue > maxValue)
            numValue = maxValue

        if (numValue < 0)
            numValue = 0

        if (numDivs <= 0)
            numDivs = 1

        val vw = width
        val vh = height
        val vwf = vw.toFloat()
        val vhf = vh.toFloat()

        // Draw bars (height of the view)
        // Add spacing ( Number of spaces is 1 less than the numDivs)
        var x = 0.0f + paddingLeft
        var y = 0.0f + paddingTop

        var barHeight = ((vhf - ((numDivs - 1) * spacing).toFloat() - paddingTop - paddingBottom) / numDivs.toFloat())
        val deltaX = 0.0f
        val deltaY = barHeight + spacing

        if (customBarHeight > 0)
            barHeight = customBarHeight.toFloat()

        val barWidth = vwf - paddingLeft - paddingRight
        var rcolor: Int

        // Draw N rectangles. Choose the color based on whether the current value
        for (i in 0 until numDivs) {
            rcolor = if (i < numValue) {
                color1
            } else {
                color2
            }
            mPaint.color = rcolor
            canvas.save()
            canvas.translate(canvas.width.toFloat(), canvas.height.toFloat())
            canvas.rotate(180f)
            canvas.drawRect(x, y, x + barWidth, y + barHeight, mPaint)
            x += deltaX
            y += deltaY
            canvas.restore()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawOnCanvasV(canvas)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.VolumeControl)
        val n = a.indexCount
        for (i in 0 until n) {
            when (val attr = a.getIndex(i)) {
                R.styleable.VolumeControl_bar_color_foreground -> {
                    val c = a.getColor(attr, Color.WHITE)
                    setBarColorForeground(c)
                }
                R.styleable.VolumeControl_bar_color_background -> {
                    val c2 = a.getColor(attr, Color.GRAY)
                    setBarColorBackground(c2)
                }
                R.styleable.VolumeControl_bar_initial_value -> {
                    val barValue = a.getInt(attr, 0)
                    setCurrentBarValue(barValue.toDouble())
                }
                R.styleable.VolumeControl_bar_num_divisions -> {
                    val divisions = a.getInt(attr, defaultNumDivisions)
                    setDivisionsNumber(divisions)
                }
                R.styleable.VolumeControl_bar_height -> {
                    val barHeight = a.getDimensionPixelSize(attr, defaultBarPixelHeight)
                    setBarHeight(barHeight)
                }
                R.styleable.VolumeControl_bar_spacing -> {
                    val barSpace = a.getDimensionPixelSize(attr, defaultBarPixelSpacing)
                    setBarSpacing(barSpace)
                }
                R.styleable.VolumeControl_current_volume -> {
                    val volume = a.getInt(attr, 0)
                    setVolumeCurrent(volume)
                }
                R.styleable.VolumeControl_current_volume_percentage -> {
                    val volumePercentage = a.getInt(attr, 0)
                    setVolumePercentage(volumePercentage)
                }
            }
        }
        a.recycle()
    }

    open class SwipeListener(context: Context) : OnTouchListener {

        private val gestureDetector: GestureDetector

        companion object {
            private const val SWIPE_THRESHOLD = 100
            private const val SWIPE_VELOCITY_THRESHOLD = 100
            private const val SWIPE_VERTICAL_INCREMENT = 30
        }

        init {
            gestureDetector = GestureDetector(context, GestureListener())
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                var result = false
                try {
                    val diffY = e2.y - e1.y
                    if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        val swipeVerticalWeight : Int = (abs(diffY) / SWIPE_VERTICAL_INCREMENT).roundToInt() // how fast you swipe

                        if (diffY > 0) {
                            onSwipeBottom(swipeVerticalWeight)
                        } else {
                            onSwipeTop(swipeVerticalWeight)
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return result
            }
        }

        open fun onSwipeTop(increase: Int) {}
        open fun onSwipeBottom(increase: Int) {}
    }

    open class DragListener(context: Context): OnTouchListener {

        companion object {
            private const val DRAG_VERTICAL_INCREMENT = 4
        }

        private var y : Float = 0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            try{
                val newY: Float

                when(event.action){
                    MotionEvent.ACTION_DOWN -> {
                        y = event.y
                        onDragStart()

                        return true
                    }
                    MotionEvent.ACTION_MOVE ->{
                        newY = event.y
                        if(y < newY){
                            onDragBottom(DRAG_VERTICAL_INCREMENT)
                        }else{
                            onDragTop(DRAG_VERTICAL_INCREMENT)
                        }
                    }
                    MotionEvent.ACTION_UP ->{
                        onDragEnd()
                    }
                }
            }catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }
        open fun onDragStart() {}
        open fun onDragTop(increase: Int) {}
        open fun onDragBottom(increase: Int) {}
        open fun onDragEnd() {}
    }
}


