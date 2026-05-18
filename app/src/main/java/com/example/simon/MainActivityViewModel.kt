package com.example.simon

import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simon.ui.theme.Blue
import com.example.simon.ui.theme.Cyan
import com.example.simon.ui.theme.Green
import com.example.simon.ui.theme.Magenta
import com.example.simon.ui.theme.Red
import com.example.simon.ui.theme.Simon
import com.example.simon.ui.theme.Yellow
import com.example.simon.ui.theme.gg
import com.example.simon.ui.theme.simonColors
import com.example.simon.ui.theme.simonLetters
import database.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivityViewModel (private val repository: GamesRepository): ViewModel() {

    private val _pressed = MutableLiveData<String>("")
    var colors = mutableStateListOf(Cyan, Magenta, Blue, Yellow, Red, Green)
        private set
    private val _score = MutableLiveData<Int>()

    private var currentTurn = 0
    private var _isRobotPlaying = true
    //private val _order: MutableMap<Char, Color> = mutableMapOf()
    private val _order: MutableList<Char> = mutableListOf()
    private val _history: MutableList<Char> = mutableListOf()
    //private val _simon = MutableLiveData<Simon>()

     fun startGame () {
         viewModelScope.launch {
             delay(1000)
             _score.value = 0
             _order.clear()
             newTurn()
         }

        //_order.entries.add(gg.entries.elementAt(Random.nextInt(0,7)) as MutableMap.MutableEntry<Char, Color>)


    }

    fun getPressed () : LiveData<String> {
        return _pressed
    }

     fun newTurn () {
         viewModelScope.launch {
             _isRobotPlaying = true

             _order.add(simonLetters[Random.nextInt(0, 6)])
             _order.forEach { entry ->
                 delay(500)

                 val originalColor = colors[_order.indexOf(entry)]
                 colors[_order.indexOf(entry)] = Color.White

                 delay(500)

                 colors[_order.indexOf(entry)] = originalColor
             }
             _isRobotPlaying = false
         }
    }

    fun press(value: Char) {
        if (!_isRobotPlaying) {
            if (currentTurn == _order.size-1  && value != _order.last())
                endGame()
            if (_pressed.value == "") {
                _pressed.value = "$value"
            } else {
                _pressed.value += " ,$value"
            }
            currentTurn++
            if(currentTurn == _order.size) newTurn()
        }
    }

    fun check (value: Char) : Boolean{
        return value == _order.last()
    }

    fun reset (){
        _pressed.value = ""
    }

    fun pressed () : LiveData<String> {
        return _pressed
    }

    fun endGame () {
        _pressed.value=""
    }

    fun insert (game: Game) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.insert(game)
            }
        }


}