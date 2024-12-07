package com.dragos.brainstorming

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.dragos.brainstorming.monitor.getDailyStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    val appList = MutableStateFlow(getAppList())

    val goodAppList = MutableStateFlow(listOf<AppInfo>())

    val badAppList = MutableStateFlow(listOf<AppInfo>())

    fun goodAppClick(app: AppInfo) {
        goodAppList.update { list ->
            if (list.contains(app).not())
                (list + app).sortedBy { it.appName }
            else
                list
        }
        appList.update { list ->
            list - app
        }
    }
    fun badAppClick(app: AppInfo) {
        badAppList.update { list ->
            if (list.contains(app).not())
                (list + app).sortedBy { it.appName }
            else
                list
        }
        appList.update { list ->
            list - app
        }
    }
    fun removeAppClick(app: AppInfo) {
        badAppList.update { list ->
            list - app
        }
        goodAppList.update { list ->
            list - app
        }
        appList.update { list ->
            if (list.contains(app).not())
                (list + app).sortedBy { it.appName }
            else
                list
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