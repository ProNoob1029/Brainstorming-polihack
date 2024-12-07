package com.dragos.brainstorming

import android.app.KeyguardManager
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.dragos.brainstorming.ui.theme.BrainstormingTheme

class AlertActivity : ComponentActivity() {
    private val mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manageLockScreen()

        enableEdgeToEdge()

        setContent {
            BrainstormingTheme (
                darkTheme = false
            ) {
                Scaffold { scaffoldPadding ->
                    Column (
                        modifier = Modifier
                            .padding(scaffoldPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "JOS LABA!!!",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black
                            )
                        )
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            imageVector = Icons.Default.Lock,
                            contentDescription = "lock"
                        )
                    }
                }
            }
        }

        setupMediaPlayer()
    }

    override fun onStop() {
        super.onStop()
        println("STOPPED")
        mediaPlayer.stop()
        finish()
    }

    override fun onPause() {
        super.onPause()
        manageLockScreen()
    }

    private fun setupMediaPlayer() {
        val uri = Uri.parse("android.resource://com.dragos.brainstorming/${R.raw.alarm_sound}")

        mediaPlayer.setDataSource(applicationContext, uri)

        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        )

        mediaPlayer.prepare()

        mediaPlayer.start()

        mediaPlayer.isLooping = true
    }

    private fun manageLockScreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY or
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
        {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
            keyguardManager.inKeyguardRestrictedInputMode()
        }
    }
}