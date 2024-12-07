package com.dragos.brainstorming.main_screen

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dragos.brainstorming.MainApplication
import com.dragos.brainstorming.database.BadApp
import com.dragos.brainstorming.database.GoodApp
import com.dragos.brainstorming.database.SetLimit
import com.dragos.brainstorming.monitor.getDailyStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LimitViewModel : ViewModel() {
    val fullAppList = getAppList()

    val setLimit = MutableStateFlow(listOf<AppInfo>())

    val appList = setLimit.map { list ->
        fullAppList - list.toSet()
    }.stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = listOf())


    init {
        viewModelScope.launch(Dispatchers.IO) {
            setLimit.update {
                MainApplication.database.appDao().getSetLimit().map { goodApp ->
                    fullAppList.first { it.packageName == goodApp.packageName }
                }
            }
        }
    }

    fun setLim(app: AppInfo) {
        setLimit.update { list ->
            if (!list.contains(app))
                (list + app).sortedBy { it.appName }
            else
                list
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().insertSetLimit(
                SetLimit(
                    appName = app.appName,
                    packageName = app.packageName
                )
            )
        }
    }

    fun delLim(app: AppInfo) {
        setLimit.update { list ->
            list - app
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().deleteSetLimit(
                SetLimit(
                    appName = app.appName,
                    packageName = app.packageName
                )
            )
        }
    }

    private fun getAppList() = MainApplication.packageManager
        .queryIntentActivities(
            Intent(Intent.ACTION_MAIN, null)
                .apply { addCategory(Intent.CATEGORY_LAUNCHER) },
            0
        ).map { rezolveInfo ->
            AppInfo(
                appTime = getDailyStats(MainApplication.usageStatsManager)
                    .firstOrNull { it.packageName == rezolveInfo.activityInfo.packageName }?.totalTime
                    ?: 0,
                appName = rezolveInfo.loadLabel(MainApplication.packageManager).toString(),
                packageName = rezolveInfo.activityInfo.packageName
            )
        }.sortedBy { it.appName }
}