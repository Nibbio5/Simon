package com.example.simon.ui.theme

import kotlin.random.Random


/**
 * Used for the logic, it saves the score and the button letters in 2 lists
 * MutableList<T>
 * @param history all the letters od the previous sessions
 * @param scoreHistory all the scores of the previous sessions
 * @param round is the score of the current session
 */
class Simon (
    private var history: MutableList<Char> = mutableListOf(),
    //private var scoreHistory: MutableList<Int> = mutableListOf(),

    private var pressed: String = "",
    private var score: Int = 0,
    private var isRobotPlaying: Boolean = true,
)
{
    /**
     * add 1 every pressed of a colored button
     * @param color is a string containing the letter of the
     * colored button pressed
     * is called every user press
     */
    fun press (color: Char){
        if (isRobotPlaying) {
            if (pressed == "")
                pressed += color
            else
                pressed += ", $color"
            history.add(color)
            ++score
        }
    }

    /**
     * used when the game is finished by
     * the end button
     */

    fun endGame (str: String) {

    }

    /**
     * Used in score screen to get the history of the game
     */
    fun getPressed (): String{
        return pressed
    }

    /**
     * Used in score screen to get the history of the game
     */
    fun getScore (): Int {
        return score
    }

    /**
     * used in the delete button
     */
    fun resetPressed () {
        score = 0
    }



    fun blink (index: Int) {
        simonColors[index]
    }

    fun startGame (){
        history.add(simonLetters[Random.nextInt(0, 7)])

    }
}
