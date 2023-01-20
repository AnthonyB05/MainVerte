package com.example.mainverte.parameterViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mainverte.room.models.BaliseFav
import com.example.mainverte.room.models.Parameter
import com.example.mainverte.room.repository.BaliseFavRepository
import com.example.mainverte.room.repository.ParameterRepository
import kotlinx.coroutines.launch

class ParameterViewModel(private val repository: ParameterRepository) : ViewModel(){

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allParameters: LiveData<List<Parameter>> = repository.allParameter

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(parameter: Parameter) = viewModelScope.launch {
        repository.insert(parameter)
    }

    fun getParameterByNameBalise(baliseName: String) : LiveData<List<Parameter>>{
        return repository.getParameterByNameBalise(baliseName)
    }

    fun getParameterByIdBalise(idBalise: Long) : LiveData<List<Parameter>>{
        return repository.getParameterByIdBalise(idBalise)
    }

}
class ParameterViewModelFactory(private val repository: ParameterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParameterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParameterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}