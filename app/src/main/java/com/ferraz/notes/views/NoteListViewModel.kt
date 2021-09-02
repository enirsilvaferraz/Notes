package com.ferraz.notes.views

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferraz.notes.R
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.repositories.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel(), LifecycleObserver {

    val notes = MediatorLiveData<NotesState>().apply {
        viewModelScope.launch {
            try {
                value = NotesState.Loading
                delay(1500)
                addSource(repository.getNotes()) {
                    value = if (it.isEmpty()) NotesState.Empty
                    else NotesState.Success(data = it)
                }
            } catch (e: Exception) {
                value = NotesState.Failure(message = R.string.hello_first_fragment)
            }
        }
    }

    val actions = MutableLiveData<Actions>(Actions.Idle)

    fun onCardLongClick(note: NotesEntity) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun onCardClick(note: NotesEntity? = null) {
        actions.postValue(Actions.OpenDialog(note))
    }

    fun onDismissDialog() {
        actions.postValue(Actions.Idle)
    }

    sealed interface NotesState {
        object Loading : NotesState
        object Empty : NotesState
        class Success(val data: List<NotesEntity>) : NotesState
        class Failure(@StringRes val message: Int) : NotesState
    }

    sealed interface Actions {
        object Idle : Actions
        data class OpenDialog(val note: NotesEntity?) : Actions
    }
}