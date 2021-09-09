package com.ferraz.notes.views

import com.ferraz.notes.database.NotesEntity

object MockHelper {

    val items = listOf(
        NotesEntity(uid = 1, title = "Card só com titulo"),
        NotesEntity(uid = 2, description = "Card só com descrição"),
        NotesEntity(uid = 3, title = "Card com titulo e descrição", description = "Card com titulo e descrição"),
        NotesEntity(uid = 4, description = "Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa."),
        NotesEntity(uid = 5, title= "Card com título e descrição longa", description = "Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa. Card com descrição longa."),
    )
}