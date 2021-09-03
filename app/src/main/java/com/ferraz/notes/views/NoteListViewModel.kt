package com.ferraz.notes.views

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.repositories.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel(), LifecycleObserver {

    val actions = MutableLiveData<Actions>(Actions.Idle)

    private val _loading = MutableLiveData(false)
    val loading = _loading

    val notes = MediatorLiveData<NotesState>().apply {
        viewModelScope.launch {
            try {
                loading.value = true
                delay(1000)

                addSource(repository.getNotes()) {
                    value = if (it.isEmpty()) NotesState.Empty()
                    else NotesState.Success(data = it)
                }
            } catch (e: Exception) {
                value = NotesState.Failure(message = e.message ?: "Erro genérico")
            }

            loading.value = false
        }
    }

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

    fun onConfirm(title: String, description: String) {
        viewModelScope.launch {
            try {
                repository.save(NotesEntity(title = title, description = description))
            } catch (e: Exception) {
                e.toString()
            }
        }
    }

    sealed interface NotesState {
        class Empty : NotesState
        class Success(val data: List<NotesEntity>) : NotesState
        class Failure(val message: String) : NotesState
    }

    sealed interface Actions {
        object Idle : Actions
        data class OpenDialog(val note: NotesEntity?) : Actions
    }
}