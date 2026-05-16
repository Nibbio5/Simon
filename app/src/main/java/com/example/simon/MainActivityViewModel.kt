package com.example.simon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import database.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel (private val repository: GamesRepository): ViewModel() {

    private val _pressed = MutableLiveData<String>()
    private val _score = MutableLiveData<Int>()

    fun startGame () {
        _pressed.value = ""
        _score.value = 0
    }

    fun press(value: String) {
        if (_pressed.value == ""){
            _pressed.value = value
        }
        else {
            _pressed.value += "$value, "
        }
    }

    fun pressed () : LiveData<String> {
        return _pressed
    }



    fun insert (game: Game) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.insert(game)
            }
        }


}