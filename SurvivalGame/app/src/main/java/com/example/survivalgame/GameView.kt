package com.example.survivalgame
import android.content.Context
import android.graphics.Paint
import android.view.View
import  android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent

// Define the GameView class, extending View and taking a Context and a GameTask object as parameters
class GameView(var c :Context,var gameTask: GameTask) :View(c) {
    private var myPaint: Paint? = null  // Paint object for drawing
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myManPosition = 0
    private val otherBooms = ArrayList<HashMap<String, Any>>()  // List to store Boombs droped by air

    // Width and height of the view
    var viewWidth = 0
    var viewHeight = 0

    // Initialization block
    init {
        myPaint = Paint()

    }

    // Override the onDraw method to draw the game elements on the canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Measure the width and height of the view
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        //checks whether it's time to create a new boomb
        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()  //assigns a random lane to the obstacle
            map["startTime"] = time  //assigns the current time as the start time of the boomb
            otherBooms.add(map)
        }

        time = time + 10 + speed
        val manWidth = viewWidth / 5 //calculate the width and height of the surviving man character based on the width of the view
        val manHeight = manWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.man_run, null)

        d.setBounds(
            myManPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - manHeight,
            myManPosition * viewWidth / 3 + viewWidth / 15 + manWidth - 25,
            viewHeight - 2

        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for (i in otherBooms.indices) {
            try {
                val boombX = otherBooms[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var boombY = time - otherBooms[i]["startTime"] as Int
                var d2 = resources.getDrawable(R.drawable.enemy, null)

                d2.setBounds(
                    boombX + 25, boombY - manHeight, boombX + manWidth - 25, boombY
                )

                d2.draw(canvas)
                if (otherBooms[i]["lane"] as Int == myManPosition) {
                    if (boombY > viewHeight - 2 - manHeight && boombY < viewHeight - 2) {
                        gameTask.closeGame(score)
                    }

                    if (boombY > viewHeight + manHeight) {
                        otherBooms.removeAt(i)
                        score++
                        speed = 1 + Math.abs(score / 8)
                        if (score > highScore) {
                            highScore = score
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        myPaint!!.color = Color.RED
        myPaint!!.textSize = 60f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()  //triggers a redraw of the view,



    }

    fun resetGame(){
        time = 0
        score = 0
        speed = 1
        myManPosition = 0
        otherBooms.clear()
        invalidate()
    }



    override  fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth/2){
                    if(myManPosition> 0){
                        myManPosition--
                    }
                }
                if (x1 > viewWidth / 2){
                    if (myManPosition<2){
                        myManPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP ->{}

        }
        return true
    }

}