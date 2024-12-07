package com.dragos.brainstorming

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.dragos.brainstorming.ui.theme.BrainstormingTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.QUERY_ALL_PACKAGES
        ) == PackageManager.PERMISSION_GRANTED

        Log.d("MyApp", "granted: $granted")

        val pm = packageManager

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val packages = pm.queryIntentActivities(intent, 0).map {
            AppInfo(
                appName = it.loadLabel(packageManager).toString(),
                packageName = it.activityInfo.packageName,
                appIcon = it.loadIcon(packageManager)
            )
        }.sortedBy { it.appName }

        setContent {
            BrainstormingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        packages
                    )
                }
            }
        }

    }
}

data class AppInfo(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable?
)