package com.example.simon.ui.theme


/**
 * Used for the logic, it saves the score and the button letters in 2 lists
 * MutableList<T>
 * @param history all the letters od the previous sessions
 * @param scoreHistory all the scores of the previous sessions
 */
class Simon (
    private var history: MutableList<String> = mutableListOf(),
    private var scoreHistory: MutableList<Int> = mutableListOf(),
    private var round: Int = 0,
)
{
    // + 1 in the current score
    fun press (){
        ++round
    }

    // ad the game is finished
    fun endGame (str: String) {
        history.add(str)
        scoreHistory.add(round)
        round = 0
    }

    fun getHistory (): MutableList<String> {
        return history
    }

    fun getScoreHistory (): MutableList<Int> {
        return scoreHistory
    }

    fun resetPressed () {
        round = 0
    }

}
