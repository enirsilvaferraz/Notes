package com.ferraz.notes.views

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferraz.notes.R
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.repositories.NotesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class NoteListViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    private val _notes = MutableLiveData<NotesState>()
    val notes = _notes

    fun onStart() {

        _notes.postValue(NotesState.Loading)

        viewModelScope.launch {
            try {
                with(repository.getNotes()) {
                    if (isEmpty()) NotesState.Empty
                    else NotesState.Success(data = this)
                }.also {
                    _notes.postValue(it)
                }
            } catch (e: Exception) {
                _notes.postValue(NotesState.Failure(message = R.string.app_name))
            }
        }
    }

    sealed interface NotesState {
        object Loading : NotesState
        object Empty : NotesState
        class Success(val data: List<NotesEntity>) : NotesState
        class Failure(@StringRes val message: Int) : NotesState
    }
}