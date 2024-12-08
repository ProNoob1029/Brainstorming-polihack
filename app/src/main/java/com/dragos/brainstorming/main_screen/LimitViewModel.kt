package com.dragos.brainstorming.main_screen

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dragos.brainstorming.MainApplication
import com.dragos.brainstorming.database.SetLimit
import com.dragos.brainstorming.monitor.getDailyStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LimitViewModel : ViewModel() {
    private val fullAppList = getAppList()

    val limitApps = MutableStateFlow(listOf<AppInfo>())

    val remainingApps = limitApps.map { list ->
        (fullAppList - fullAppList.filter { fullApp -> list.map { it.packageName }.contains(fullApp.packageName) }.toSet()).sortedByDescending { it.appTime }
    }.stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = listOf())


    init {
        viewModelScope.launch(Dispatchers.IO) {
            limitApps.update {
                val list = MainApplication.database.appDao().getSetLimit().map { limitApp ->
                    AppInfo(
                        appName = limitApp.appName,
                        packageName = limitApp.packageName,
                        appTime = fullAppList.firstOrNull { it.packageName == limitApp.packageName }?.appTime ?: 0,
                        timeLimit = limitApp.minuteLimit
                    )
                }.sortedBy { it.appName }
                list.forEach { newApp ->
                    fullAppList.replaceAll {
                        if (it.packageName == newApp.packageName) {
                            newApp
                        } else {
                            it
                        }
                    }
                }
                list
            }

        }
    }

    fun addLimitApp(app: AppInfo) {
        limitApps.update { list ->
            if (!list.contains(app))
                (list + app).sortedBy { it.appName }
            else
                list
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().insertSetLimit(
                SetLimit(
                    appName = app.appName,
                    packageName = app.packageName,
                    minuteLimit = app.timeLimit
                )
            )
        }
    }

    fun addMinutes(app: AppInfo) {
        val newApp = app.copy(timeLimit = app.timeLimit + 5)
        fullAppList.replaceAll {
            if (it.packageName == newApp.packageName) {
                newApp
            } else {
                it
            }
        }
        limitApps.update { list ->
            (list - app + newApp).sortedBy { it.appName }
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().insertSetLimit(
                SetLimit(
                    appName = newApp.appName,
                    packageName = newApp.packageName,
                    minuteLimit = newApp.timeLimit
                )
            )
        }
    }

    fun removeMinutes(app: AppInfo) {
        val newApp = app.copy(timeLimit = (app.timeLimit - 5).coerceAtLeast(0))
        fullAppList.replaceAll {
            if (it.packageName == newApp.packageName) {
                newApp
            } else {
                it
            }
        }
        limitApps.update { list ->
            (list - app + newApp).sortedBy { it.appName }
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().insertSetLimit(
                SetLimit(
                    appName = newApp.appName,
                    packageName = newApp.packageName,
                    minuteLimit = newApp.timeLimit
                )
            )
        }
    }

    fun removeLimitApp(app: AppInfo) {
        limitApps.update { list ->
            list - app
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().deleteSetLimit(
                SetLimit(
                    appName = app.appName,
                    packageName = app.packageName,
                    minuteLimit = app.timeLimit
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
        }.sortedBy { it.appName }.toMutableList()
}