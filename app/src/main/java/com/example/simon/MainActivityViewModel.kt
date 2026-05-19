package com.example.simon

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
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
import com.example.simon.ui.theme.Yellow
import com.example.simon.ui.theme.simonLetters
import database.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sin
import kotlin.random.Random

class MainActivityViewModel(private val repository: GamesRepository) : ViewModel() {

    val allGames: StateFlow<List<Game>> = repository.allGames
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _pressed = MutableLiveData("")
    var colors = mutableStateListOf(Cyan, Magenta, Blue, Yellow, Red, Green)
        private set

    private val _isPaused = MutableLiveData(false)
    private var _score = 0

    private var currentTurn = 0
    private var _isRobotPlaying = true

    private val _order: MutableList<Char> = mutableListOf()

    private val soundFrequencies = mapOf(
        'C' to 261.63, // Do (Cyan)
        'M' to 293.66, // Re (Magenta)
        'B' to 329.63, // Mi (Blue)
        'Y' to 392.00, // Sol (Yellow)
        'R' to 440.00, // La (Red)
        'G' to 523.25  // Do alto (Green)
    )

    fun startGame() {
        viewModelScope.launch {
            _isPaused.value = false
            pausableDelay(1000)
            _score = 0
            _order.clear()
            newTurn()
        }
    }

    fun getPressed(): LiveData<String> {
        return _pressed
    }

    fun getIsPaused(): LiveData<Boolean> {
        return _isPaused
    }

    fun newTurn() {
        viewModelScope.launch {
            _isRobotPlaying = true
            currentTurn = 0
            _score++

            pausableDelay(500)

            _order.add(simonLetters[Random.nextInt(0, 6)])
            _order.forEach { entry ->
                pausableDelay(500)

                val index = simonLetters.indexOf(entry)
                val originalColor = colors[index]

                colors[index] = Color.White

                playSoundAndDelay(soundFrequencies[entry] ?: 440.0, 500)

                colors[index] = originalColor
            }
            _pressed.value = ""
            _isRobotPlaying = false
        }
    }

    fun pause() {
        if (_isRobotPlaying) {
            _isPaused.value = true
        }
    }

    fun resume() {
        if (_isRobotPlaying) {
            _isPaused.value = false
        }
    }


    /**
     * the press function is used to add the letter to the string of the pressed buttons
     * and to launch the sound of each press, it also check that the corresponding letter
     * of the button is correct in that sequence, if not it call the game over
     */
    fun press(value: Char) {
        if (!_isRobotPlaying) {

            viewModelScope.launch {
                playSoundAndDelay(soundFrequencies[value] ?: 440.0, 300)
            }

            if (_order[currentTurn] != value) {
                endGame()
                return
            }
            if (_pressed.value == "") {
                _pressed.value = "$value"
            } else {
                _pressed.value += " ,$value"
            }
            currentTurn++
            if (_pressed.value?.split(",")?.size == _order.size)
                newTurn()
        }
    }


    fun reset() {
        _pressed.value = ""
    }

    /**
     * function that is necessary to use when working with LiveData and Mutable livedata
     * this return the the liveData of the string of the pressed buttons
     */
    fun pressed(): LiveData<String> {
        return _pressed
    }


    /**
     * This function is used to end the game, save the game data in the database and
     * reset the game variables, when a lose happen the GAME OVER text is written, and the
     * game over sound is displayed
     */
    fun endGame() {
        currentTurn = 0
        _isRobotPlaying = true
        _isPaused.value = false
        _pressed.value = "GAME OVER"
        playGameOverSound()
        val newGame = Game(
            score = _score,
            sequence = _order.toString()
        )

        insert(newGame)
    }

    /**
     * This function is used to insert a new game in the database
     * it use the gameDao and the repository to insert the game in
     * a Room database
     *
     * @param game is the game to insert in the database
     */
    fun insert(game: Game) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insert(game)
        }
    }

    /**
     * This function is created to avoid the use of delay, since it cannot be stop
     * if the pause is happening in the middle of the delay.
     * if the game is on pause is stay in a while true loop until the game is not paused.
     *
     * @param durationMs is the duration of the delay in milliseconds
     */
    private suspend fun pausableDelay(durationMs: Int) {
        var elapsed = 0
        while (elapsed < durationMs) {
            if (_isPaused.value == true) {
                delay(10)
            } else {
                delay(10)
                elapsed += 10
            }
        }
    }

    /**
     * Generate the audio sound of the corresponding note frequency and
     * it pause the thread and the sound when the game is on pause.
     *
     * @param frequency is the note frequency
     * @param durationMs is the duration of the sound in milliseconds
     */
    private suspend fun playSoundAndDelay(frequency: Double, durationMs: Int) {
        val audioTrack = withContext(Dispatchers.IO) {
            val sampleRate = 44100
            val numSamples = durationMs * sampleRate / 1000
            val generatedSnd = ByteArray(2 * numSamples)
            var idx = 0

            val attackSamples = (0.03 * sampleRate).toInt()
            val releaseSamples = (0.15 * sampleRate).toInt()

            for (i in 0 until numSamples) {
                val time = i.toDouble() / sampleRate

                // timber generation
                val fundamental = sin(2 * Math.PI * frequency * time)
                val harmonic = sin(2 * Math.PI * (frequency * 2) * time)
                var wave = (0.8 * fundamental) + (0.2 * harmonic)

                // Fade-In e Fade-Out
                if (i < attackSamples) {
                    wave *= (i.toDouble() / attackSamples)
                } else if (i > numSamples - releaseSamples) {
                    wave *= ((numSamples - i).toDouble() / releaseSamples)
                }

                // Max volume in order to avoid clipping
                val valShort = (wave * 24000).toInt().toShort()

                generatedSnd[idx++] = (valShort.toInt() and 0x00ff).toByte()
                generatedSnd[idx++] = (valShort.toInt() and 0xff00 ushr 8).toByte()
            }

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(generatedSnd.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(generatedSnd, 0, generatedSnd.size)
            track
        }

        var elapsed = 0
        var isAudioPlaying = false

        // this is needed in order to make sure that it pause when paused
        while (elapsed < durationMs) {
            if (_isPaused.value == true) {
                if (isAudioPlaying) {
                    audioTrack.pause()
                    isAudioPlaying = false
                }
                delay(10)
            } else {
                if (!isAudioPlaying) {
                    audioTrack.play()
                    isAudioPlaying = true
                }
                delay(10)
                elapsed += 10
            }
        }

        // release the resource after
        withContext(Dispatchers.IO) {
            audioTrack.stop()
            audioTrack.release()
        }
    }

    /**
     * Generate the game over sound sequence, it fade down gradually
     * to simulate a shutdown of the game.
     */
    private fun playGameOverSound() {
        viewModelScope.launch(Dispatchers.IO) {
            val sampleRate = 44100
            val durationMs = 1200 // 1.2 secondi di durata
            val numSamples = durationMs * sampleRate / 1000
            val generatedSnd = ByteArray(2 * numSamples)
            var idx = 0

            val startFreq = 300.0 // Frequenza di partenza (più acuta)
            val endFreq = 100.0   // Frequenza finale (bassa e cupa)

            for (i in 0 until numSamples) {
                val time = i.toDouble() / sampleRate

                // Il trucco: la frequenza cala linearmente col passare dei campioni
                val currentFreq = startFreq - ((startFreq - endFreq) * (i.toDouble() / numSamples))

                // Generazione timbro (stesso stile armonico del resto del gioco)
                val fundamental = sin(2 * Math.PI * currentFreq * time)
                val harmonic = sin(2 * Math.PI * (currentFreq * 2) * time)
                var wave = (0.8 * fundamental) + (0.2 * harmonic)

                // Fade-Out solo alla fine per non gracchiare quando si ferma
                val fadeOutThreshold = numSamples * 0.8
                if (i > fadeOutThreshold) {
                    wave *= ((numSamples - i).toDouble() / (numSamples - fadeOutThreshold))
                }

                // Volume
                val valShort = (wave * 24000).toInt().toShort()

                generatedSnd[idx++] = (valShort.toInt() and 0x00ff).toByte()
                generatedSnd[idx++] = (valShort.toInt() and 0xff00 ushr 8).toByte()
            }

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(generatedSnd.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            // Suona in modo indipendente senza bloccare altre coroutine
            track.write(generatedSnd, 0, generatedSnd.size)
            track.play()

            delay(durationMs.toLong())

            track.stop()
            track.release()
        }
    }
}