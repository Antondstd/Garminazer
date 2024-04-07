package net.garminazer

import android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.garmin.android.connectiq.ConnectIQ
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import io.ktor.server.netty.*
import net.garminazer.home.SpotifizerService


var spotifyAppRemote: SpotifyAppRemote? = null
var trackState: MutableState<Track?> = mutableStateOf(null)
var playerState: MutableState<PlayerState?> = mutableStateOf(null)
var imageState: MutableState<Bitmap?> = mutableStateOf(null)

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startForegroundService(
            Intent(applicationContext, SpotifizerService::class.java).setAction(
                Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            ).setAction(FOREGROUND_SERVICE_MEDIA_PLAYBACK)
        )

        setContent {
            GarminazerNavHost()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
    }


    override fun onStop() {
        super.onStop()

    }
}