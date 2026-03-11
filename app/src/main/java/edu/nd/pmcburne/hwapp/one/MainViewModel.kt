package edu.nd.pmcburne.hwapp.one

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import edu.nd.pmcburne.hwapp.one.data.ScoreRepo
import edu.nd.pmcburne.hwapp.one.data.local.Game
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class ScoreUIState(
    val games: List<Game> = emptyList(),
    val dateSelected:String= LocalDate.now().toString(),
    val genderSelected: String = "women",
    val loading: Boolean = false,
    val errorMsg: String? = null
)
class MainViewModel (private val repo: ScoreRepo, private val stateHandle: SavedStateHandle): ViewModel(){
    companion object {
        private const val DATE_KEY="selected_date"
        private const val GENDER_KEY = "selected_gender"
    }
    private val dateSelected=MutableStateFlow(stateHandle[DATE_KEY]?: LocalDate.now().toString())
    private val genderSelected = MutableStateFlow(stateHandle[GENDER_KEY]?:"women")
    private val loading= MutableStateFlow(false)
    private val errorMsg= MutableStateFlow<String?>(null)
    val uiState: StateFlow<ScoreUIState> =
        combine(
            dateSelected,
            genderSelected,
            loading,
            errorMsg,
            genderSelected.flatMapLatest {gender ->
                dateSelected.flatMapLatest {date ->
                    repo.getGamesSaved(gender,date)
                }
            }
        ){date,gender,loadingValue,error,games ->
            ScoreUIState(
                games=games,
                dateSelected=date,
                genderSelected=gender,
                loading=loadingValue,
                errorMsg=error
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ScoreUIState()
        )
    init{
        refresh()
    }
    fun setDate(date:String){
        stateHandle[DATE_KEY]=date
        dateSelected.value=date
        refresh()
    }
    fun setGender(gender: String) {
        stateHandle[GENDER_KEY] = gender
        genderSelected.value = gender
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            loading.value = true
            errorMsg.value = null
            try {
                repo.refreshScore(
                    gender = genderSelected.value,
                    date = dateSelected.value
                )
            }
            catch(e:Exception){
                errorMsg.value = "Error."
            }
            finally {
                loading.value = false
            }
        }
    }

}