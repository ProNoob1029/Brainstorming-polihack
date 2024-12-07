package com.dragos.brainstorming.main_screen

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dragos.brainstorming.MainApplication
import com.dragos.brainstorming.database.BadApp
import com.dragos.brainstorming.database.GoodApp
import com.dragos.brainstorming.monitor.getDailyStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val fullAppList = getAppList()

    val goodAppList = MutableStateFlow(listOf<AppInfo>())

    val badAppList = MutableStateFlow(listOf<AppInfo>())

    val appList = combine(goodAppList, badAppList) { goodList, badList ->
        fullAppList - (goodList.toSet() + badList.toSet())
    }.stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = listOf())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            goodAppList.update {
                MainApplication.database.appDao().getGoodApps().map { goodApp ->
                    fullAppList.first { it.packageName == goodApp.packageName }
                }
            }
            badAppList.update {
                MainApplication.database.appDao().getBadApps().map { badApp ->
                    fullAppList.first { it.packageName == badApp.packageName }
                }
            }
        }
    }

    fun goodAppClick(app: AppInfo) {
        goodAppList.update { list ->
            if (list.contains(app).not())
                (list + app).sortedBy { it.appName }
            else
                list
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().insertGoodApp(
                GoodApp(
                    appName = app.appName,
                    packageName = app.packageName
                )
            )
        }
    }
    fun badAppClick(app: AppInfo) {
        badAppList.update { list ->
            if (list.contains(app).not())
                (list + app).sortedBy { it.appName }
            else
                list
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().insertBadApp(
                BadApp(
                    appName = app.appName,
                    packageName = app.packageName
                )
            )
        }
    }
    fun removeAppClick(app: AppInfo) {
        badAppList.update { list ->
            list - app
        }
        goodAppList.update { list ->
            list - app
        }
        viewModelScope.launch(Dispatchers.IO) {
            MainApplication.database.appDao().deleteGoodApp(
                GoodApp(
                    appName = app.appName,
                    packageName = app.packageName
                )
            )
            MainApplication.database.appDao().deleteBadApp(
                BadApp(
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
                    .firstOrNull { it.packageName == rezolveInfo.activityInfo.packageName }?.totalTime ?: 0,
                appName = rezolveInfo.loadLabel(MainApplication.packageManager).toString(),
                packageName = rezolveInfo.activityInfo.packageName
            )
        }.sortedBy { it.appName }
}