package edu.nd.pmcburne.hwapp.one

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
data class ScoreUIState(
    val games: List<Game> = emptyList(),
    val dateSelected: String=LocalDate.now().toString(),
    val genderSelected: String="women",
    val loading:Boolean =false,
    val errorMsg:String? =null
)

class MainViewModel(private val repo:ScoreRepo) :ViewModel() {
    private val ui = MutableStateFlow(ScoreUIState())
    val uiState: StateFlow<ScoreUIState> =ui.asStateFlow()

    private var job: Job?=null
    init {
        watchGames()
        refresh()
    }
    fun setDate(date: String) {
        ui.value=ui.value.copy(dateSelected=date)
        watchGames()
        refresh()
    }
    fun setGender(gender: String) {
        ui.value=ui.value.copy(genderSelected =gender)
        watchGames()
        refresh()
    }
    fun refresh() {
        viewModelScope.launch {
            ui.value = ui.value.copy(
                loading = true,
                errorMsg = null
            )
            try {
                repo.refresh(
                    ui.value.genderSelected,
                    ui.value.dateSelected
                )
            } catch (e: Exception) {
                ui.value = ui.value.copy(
                    errorMsg = "Error."
                )
            }
            ui.value = ui.value.copy(loading=false)
        }
    }
    private fun watchGames() {
        job?.cancel()
        job = viewModelScope.launch {
            repo.getGames(
                ui.value.genderSelected,
                ui.value.dateSelected
            ).collect {games ->
                ui.value=ui.value.copy(games=games)
            }
        }
    }
}

