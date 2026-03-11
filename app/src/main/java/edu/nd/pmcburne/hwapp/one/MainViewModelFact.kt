package edu.nd.pmcburne.hwapp.one

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.SavedStateHandle
import edu.nd.pmcburne.hwapp.one.data.ScoreRepo

class MainViewModelFact(private val repo: ScoreRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo, SavedStateHandle()) as T
    }
}