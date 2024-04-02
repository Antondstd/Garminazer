package net.garminazer

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import io.ktor.application.Application
import io.ktor.server.engine.*
import io.ktor.server.netty.*

private val clientId = "4dee21fa9af84e7e8bf733e596a75e0f"
private val redirectUri = "http://localhost:3000"
var connectionParams:ConnectionParams? = null
var spotifyAppRemote: SpotifyAppRemote? = null
var trackState: MutableState<Track?> =  mutableStateOf(null)
var playerState:MutableState<PlayerState?> = mutableStateOf(null)
var imageState:MutableState<Bitmap?> = mutableStateOf(null)
//var trackState: MutableState<Track?> =  mutableStateOf(null)
class MainActivity : AppCompatActivity() {
    private lateinit var server: NettyApplicationEngine
    private val clientId = "4dee21fa9af84e7e8bf733e596a75e0f"
    private val redirectUri = "https://net.garminazer/callback"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startServer()

        setContent {
            GarminazerNavHost()
            }
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    private fun connected() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            playerState.value = it
            val track: Track = it.track
            trackState.value = track
            Log.i("MainActivity", "Track: " + track.name + " by " + track.artist.name)
            spotifyAppRemote?.imagesApi?.getImage(track.imageUri)!!.setResultCallback { bitmap ->
                run {
                    Log.i("MainActivity","Connected Spotify Image result:" + bitmap)
                    imageState.value = bitmap
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }

    private fun startServer() {
        server = embeddedServer(Netty, port = 53777, module = Application::module)
        server.start()
    }

}
fun getSongBitmap(): Bitmap? {
    val imageUri = trackState.value!!.imageUri
    Log.i("MainActivity","Spotify:" + imageUri)
    var img: Bitmap? = null
    val res = spotifyAppRemote?.imagesApi?.getImage(imageUri)!!.setResultCallback { bitmap ->
        run {
            Log.i("Server","Spotify Image result:" + bitmap)
            img = bitmap
        }
    }
    return img
}