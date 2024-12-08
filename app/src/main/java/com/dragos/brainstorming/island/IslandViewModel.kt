package com.dragos.brainstorming.island

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dragos.brainstorming.MainApplication
import com.dragos.brainstorming.database.Tree
import com.google.android.filament.utils.Float3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class IslandViewModel: ViewModel() {
    val treeLocation = MutableStateFlow(Float3())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            treeLocation.update {
                val tree = MainApplication.database.appDao().getTree("tree")
                Float3(
                    x = tree?.x ?: 0.0f,
                    y = tree?.y ?: 0.0f,
                    z = tree?.z ?: 0.0f
                )
            }
            treeLocation.debounce(1000).collect { position ->
                MainApplication.database.appDao().insertTree(
                    Tree(
                        name = "tree",
                        x = position.x,
                        y = position.y,
                        z = position.z
                    )
                )
            }
        }
    }

    fun moveTree(position: Float3) {
        treeLocation.update {
            position
        }
    }
}