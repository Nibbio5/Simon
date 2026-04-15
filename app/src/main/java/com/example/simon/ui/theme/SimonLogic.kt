package com.example.simon.ui.theme

class Simon (
    private var history: MutableList<String> = mutableListOf(),
    private var scoreHistory: MutableList<Int> = mutableListOf(),
    private var round: Int = 0,
    private var currentPressed: String = "",
)
{
    fun press (){
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

}
