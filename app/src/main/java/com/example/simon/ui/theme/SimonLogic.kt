package com.example.simon.ui.theme


/**
 * Used for the logic, it saves the score and the button letters in 2 lists
 * MutableList<T>
 * @param history all the letters od the previous sessions
 * @param scoreHistory all the scores of the previous sessions
 * @param round is the score of the current session
 */
class Simon (
    private var history: MutableList<String> = mutableListOf(),
    private var scoreHistory: MutableList<Int> = mutableListOf(),
    private var round: Int = 0,
)
{
    /**
     * add 1 every pressed of a colored button
     */
    fun press (){
        ++round
    }

    /**
     * used when the game is finished by
     * the end button
     */

    fun endGame (str: String) {
        history.add(str)
        scoreHistory.add(round)
        round = 0
    }

    /**
     * Used in score screen to get the history of the game
     */
    fun getHistory (): MutableList<String> {
        return history
    }

    /**
     * Used in score screen to get the history of the game
     */
    fun getScoreHistory (): MutableList<Int> {
        return scoreHistory
    }

    /**
     * used in the delete button
     */
    fun resetPressed () {
        round = 0
    }

}
