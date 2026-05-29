package com.example.simon

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sin
import kotlin.random.Random

class MainActivityViewModel(
    private val repository: GamesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val allGames: StateFlow<List<Game>> = repository.allGames
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var colors = mutableStateListOf(Cyan, Magenta, Blue, Yellow, Red, Green)
        private set

    private val soundFrequencies = mapOf(
        'C' to 261.63, // Do (Cyan)
        'M' to 293.66, // Re (Magenta)
        'B' to 329.63, // Mi (Blue)
        'Y' to 392.00, // Sol (Yellow)
        'R' to 440.00, // La (Red)
        'G' to 523.25  // Do alto (Green)
    )

    var score by mutableIntStateOf(savedStateHandle.get<Int>("score") ?: 0)
        private set

    var currentTurn by mutableIntStateOf(savedStateHandle.get<Int>("currentTurn") ?: 0)
        private set

    var isRobotPlaying by mutableStateOf(savedStateHandle.get<Boolean>("isRobotPlaying") ?: true)
        private set

    var isGameStart by mutableStateOf(savedStateHandle.get<Boolean>("isGameStart") ?: false)
        private set

    var isPaused by mutableStateOf(savedStateHandle.get<Boolean>("isPaused") ?: false)
        private set

    var isGameOver by mutableStateOf(savedStateHandle.get<Boolean>("isGameOver") ?: false)
        private set

    var pressedText by mutableStateOf(savedStateHandle.get<String>("pressedText") ?: "")
        private set

    val order = mutableStateListOf<Char>()

    init {
        savedStateHandle.get<String>("orderString")?.forEach { char ->
            order.add(char)
        }
        viewModelScope.launch {
            snapshotFlow { score }.collect { savedStateHandle["score"] = it }
        }
        viewModelScope.launch {
            snapshotFlow { currentTurn }.collect { savedStateHandle["currentTurn"] = it }
        }
        viewModelScope.launch {
            snapshotFlow { isRobotPlaying }.collect { savedStateHandle["isRobotPlaying"] = it }
        }
        viewModelScope.launch {
            snapshotFlow { isGameStart }.collect { savedStateHandle["isGameStart"] = it }
        }
        viewModelScope.launch {
            snapshotFlow { isPaused }.collect { savedStateHandle["isPaused"] = it }
        }
        viewModelScope.launch {
            snapshotFlow { isGameOver }.collect { savedStateHandle["isGameOver"] = it }
        }
        viewModelScope.launch {
            snapshotFlow { pressedText }.collect { savedStateHandle["pressedText"] = it }
        }
        viewModelScope.launch {
            snapshotFlow { order.joinToString("") }.collect { savedStateHandle["orderString"] = it }
        }
        if (isGameStart && isRobotPlaying && !isGameOver) {
            replaySequence()
        }
    }

    fun startGame() {
        if (!isGameStart) {
            isGameStart = true
            viewModelScope.launch {
                isPaused = false
                pausableDelay(1000)
                score = 0
                order.clear()
                isGameOver = false
                newTurn()
            }
        }
    }

    fun newTurn() {
        viewModelScope.launch {
            isRobotPlaying = true
            currentTurn = 0
            score++

            pausableDelay(500)

            val newLetter = simonLetters[Random.nextInt(0, 6)]
            order.add(newLetter)

            playOrderSequence()
        }
    }

    private suspend fun playOrderSequence() {
        order.forEach { entry ->
            pausableDelay(500)

            val index = simonLetters.indexOf(entry)
            val originalColor = colors[index]

            colors[index] = Color.White
            playSoundAndDelay(soundFrequencies[entry] ?: 440.0, 500)
            colors[index] = originalColor
        }
        pressedText = ""
        isRobotPlaying = false
    }

    private fun replaySequence() {
        viewModelScope.launch {
            isRobotPlaying = true
            pausableDelay(500)
            playOrderSequence()
        }
    }

    fun pause() {
        if (isRobotPlaying) {
            isPaused = true
        }
    }

    fun resume() {
        if (isRobotPlaying) {
            isPaused = false
        }
    }

    fun getGameById(id: Int): Flow<Game> {
        return repository.getGameById(id)
    }

    /**
     * the press function is used to add the letter to the string of the pressed buttons
     * and to launch the sound of each press, it also check that the corresponding letter
     * of the button is correct in that sequence, if not it call the game over.
     *
     * @param value is the letter corresponding to the color of the button pressed
     */
    fun press(value: Char) {
        if (!isRobotPlaying) {

            viewModelScope.launch {
                playSoundAndDelay(soundFrequencies[value] ?: 440.0, 300)
            }

            if (order[currentTurn] != value) {
                endGame()
                return
            }

            if (pressedText.isEmpty()) {
                pressedText = "$value"
            } else {
                pressedText += " ,$value"
            }

            currentTurn++

            if (pressedText.split(",").size == order.size) {
                newTurn()
            }
        }
    }

    /**
     * This function is used to end the game, save the game data in the database and
     * reset the game variables, when a lose happen the GAME OVER text is written, and the
     * game over sound is displayed
     */
    fun endGame() {
        isGameStart = false
        isRobotPlaying = true
        isPaused = false
        pressedText = "GAME OVER"
        playGameOverSound()

        val newGame = Game(
            score = score - 1,
            sequence = order.joinToString(""),
            errorIndex = currentTurn
        )
        insert(newGame)
        currentTurn = 0
        isGameOver = true
    }

    fun endGameWithButton() {
        if (isGameStart) {
            if (score <= 1) {
                isGameStart = false
                isRobotPlaying = true
                isPaused = false
                isGameOver = true
                currentTurn = 0
            } else {
                endGame()
            }
        }
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
            if (isPaused) {
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

                val fundamental = sin(2 * Math.PI * frequency * time)
                val harmonic = sin(2 * Math.PI * (frequency * 2) * time)
                var wave = (0.8 * fundamental) + (0.2 * harmonic)

                if (i < attackSamples) {
                    wave *= (i.toDouble() / attackSamples)
                } else if (i > numSamples - releaseSamples) {
                    wave *= ((numSamples - i).toDouble() / releaseSamples)
                }

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

        while (elapsed < durationMs) {
            if (isPaused) {
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
            val durationMs = 1200
            val numSamples = durationMs * sampleRate / 1000
            val generatedSnd = ByteArray(2 * numSamples)
            var idx = 0

            val startFreq = 300.0
            val endFreq = 100.0

            for (i in 0 until numSamples) {
                val time = i.toDouble() / sampleRate
                val currentFreq = startFreq - ((startFreq - endFreq) * (i.toDouble() / numSamples))
                val fundamental = sin(2 * Math.PI * currentFreq * time)
                val harmonic = sin(2 * Math.PI * (currentFreq * 2) * time)
                var wave = (0.8 * fundamental) + (0.2 * harmonic)

                val fadeOutThreshold = numSamples * 0.8
                if (i > fadeOutThreshold) {
                    wave *= ((numSamples - i).toDouble() / (numSamples - fadeOutThreshold))
                }

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
            track.play()

            delay(durationMs.toLong())

            track.stop()
            track.release()
        }
    }
}