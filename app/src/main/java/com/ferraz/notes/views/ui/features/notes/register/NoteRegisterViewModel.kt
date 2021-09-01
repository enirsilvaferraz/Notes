package com.ferraz.notes.views.ui.features.notes.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.repositories.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteRegisterViewModel @Inject constructor(private val repository: NotesRepository) : ViewModel() {

    fun onConfirm(text: String) {
        viewModelScope.launch {
            try {
                repository.save(NotesEntity(description = text))
            }catch(e:Exception){
                e.toString()
            }
        }
    }
}