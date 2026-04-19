package com.example.simon.ui.theme

class Simon (
    private var history: MutableList<String> = mutableListOf(),
    private var scoreHistory: MutableList<Int> = mutableListOf(),
    private var round: Int = 0,
    private var currentPressed: String = "",
)
{
    fun press (letter: Char){
       // currentPressed += letter
        if ( currentPressed == ""){
            currentPressed += letter
        }else{
            currentPressed += (", $letter")
        }
        ++round
    }

    fun endGame (str : String) {
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

    fun gerCurrentPressed (): String {
        return currentPressed
    }

    fun resetCurrentPressed () {
        currentPressed = ""
    }

}
