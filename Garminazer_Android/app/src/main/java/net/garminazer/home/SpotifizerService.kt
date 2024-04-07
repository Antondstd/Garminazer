package net.garminazer.home

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.garminazer.imageState
import net.garminazer.playerState
import net.garminazer.spotifyAppRemote
import net.garminazer.trackState


class SpotifizerService(
) : Service() {
    private val TAG = "SpotifizerService"
    var isShouldWork: Boolean = true
    var watchDevice: IQDevice? = null
    var watchApp: IQApp? = null
    var connectIQ: ConnectIQ = ConnectIQ.getInstance()
    private val redirectUri = "https://net.garminazer/callback"
    private var spotifyReceiver: SpotifyReceiver? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createConnectIQ()
        initSpotify()
        spotifyReceiver = SpotifyReceiver()
        registerReceiver(spotifyReceiver, SpotifyReceiver.getIntentFilter(),RECEIVER_EXPORTED)
        CoroutineScope(Dispatchers.IO).launch {
            checkAndInitConnection()
        }
    }

    fun initSpotify() {
        val clientId = "4dee21fa9af84e7e8bf733e596a75e0f"
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(false)
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

    fun createConnectIQ() {
        connectIQ = ConnectIQ.getInstance(applicationContext, ConnectIQ.IQConnectType.TETHERED)
        connectIQ.initialize(applicationContext, true, object : ConnectIQ.ConnectIQListener {
            // Called when the SDK has been successfully initialized
            override fun onSdkReady() {
                Log.i(TAG, "SDK READY")

            }

            // Called when the SDK has been shut down
            override fun onSdkShutDown() {

                // Take care of any post shutdown requirements
            }

            // Called when initialization fails.
            override fun onInitializeError(status: ConnectIQ.IQSdkErrorStatus?) {
                Log.i(TAG, "Error with onInitializeError:" + status)
                // A failure has occurred during initialization. Inspect
                // the IQSdkErrorStatus value for more information regarding
                // the failure.
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY;
    }

    fun checkAndInitConnection() {
        while (isShouldWork) {
            try {
                if (watchDevice == null || watchApp == null) {
                    if (watchDevice == null) {
                        registerWatchApp(connectIQ, CoroutineScope(Dispatchers.IO))
                    }
                    if (watchApp == null) {
                        connectToWatch(
                            connectIQ,
                            applicationContext,
                            CoroutineScope(Dispatchers.IO)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Error: " + e)
            }
            Thread.sleep(5000)
        }
    }

    fun connected() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            playerState.value = it
            val track: Track = it.track
            trackState.value = track
            Log.i("MainActivity", "Track: " + track.name + " by " + track.artist.name)
            updateSong()
        }

    }

    fun connectToWatch(
        connectIQ: ConnectIQ,
        localContext: Context,
        coroutineScope: CoroutineScope
    ) {
        // Do any post initialization setup.
        if (watchDevice == null) {
            val paired = connectIQ.knownDevices


            if (paired != null && paired.size > 0) {
                // get the status of the devices
                for (device in paired) {
                    val status: IQDevice.IQDeviceStatus = connectIQ.getDeviceStatus(device)
                    if (status == IQDevice.IQDeviceStatus.CONNECTED && device != null) {
                        watchDevice = device
                    }
                }
            }
        }
        registerWatchApp(connectIQ, coroutineScope)

    }

    fun registerWatchApp(connectIQ: ConnectIQ, coroutineScope: CoroutineScope) {
        Log.i(TAG, "WatchDevice" + watchDevice + " and watchApp " + watchApp)
        if (watchDevice != null) {
            connectIQ.registerForDeviceEvents(watchDevice) { device, status ->

            }
            connectIQ.getApplicationInfo(
//            "62d05e34-d9cf-41ee-9326-06fb139f5ab1",
                "", //Empty Id for simulator
                watchDevice,
                object :
                    ConnectIQ.IQApplicationInfoListener {
                    override fun onApplicationInfoReceived(app: IQApp?) {
                        Log.i(
                            TAG,
                            "Watch onApplicationInfoReceived status" + app!!.displayName
                        )
                        if (app != null) {
                            watchApp = app
                            updateSong()
                            connectIQ.registerForAppEvents(
                                watchDevice,
                                watchApp,
                                object : ConnectIQ.IQApplicationEventListener {
                                    override fun onMessageReceived(
                                        device: IQDevice?,
                                        app: IQApp?,
                                        message: MutableList<Any>?,
                                        status: ConnectIQ.IQMessageStatus?
                                    ) {
                                        Log.i(
                                            TAG,
                                            "Watch onMessageReceived status" + message!!.get(0)
                                        )
                                        val data =
                                            TransmitData(message!!.get(0) as Map<String, Any?>)
                                        performAction(data)
                                    }

                                })
                        }
                    }

                    override fun onApplicationNotInstalled(p0: String?) {

                    }

                })
        }
    }

    fun performAction(data: TransmitData) {
        Log.i(
            TAG,
            "Perform action on Data: " + data
        )
        if (data.action == SpotifyAction.LIKE) {
            if (data.songId == null) {
                likeSong()
            } else {
                likeSong(data.songId)
            }
        }
        if (data.action == SpotifyAction.DISLIKE) {
            dislikeSong(data.songId!!)
        }
        if (data.action == SpotifyAction.PAUSE) {
            pausePlayer()
        }
        if (data.action == SpotifyAction.PLAY) {
            playPlayer()
        }
        if (data.action == SpotifyAction.NEXT) {
            nextSong()
        }
        if (data.action == SpotifyAction.PREVIOUS) {
            previousSong()
        }
        if (data.action == SpotifyAction.SHUFFLE) {
            spotifyAppRemote?.playerApi?.toggleShuffle()
        }
    }

    fun nextSong() {
        spotifyAppRemote?.playerApi?.skipNext()
            ?.setResultCallback { updateSong() }
    }


    fun previousSong() {
        spotifyAppRemote?.playerApi?.skipPrevious()
            ?.setResultCallback { updateSong() }
    }

    fun likeSong() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            val track: Track = it.track
            spotifyAppRemote!!.userApi.addToLibrary(track.uri)
        }
    }

    fun likeSong(trackURI: String) {
        spotifyAppRemote!!.userApi.addToLibrary(trackURI)
    }

    fun dislikeSong(trackURI: String) {
        spotifyAppRemote!!.userApi.removeFromLibrary(trackURI)
    }

    fun playPlayer() {
        spotifyAppRemote?.playerApi?.resume()
    }

    fun pausePlayer() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun updateSong() {
        spotifyAppRemote!!.playerApi.playerState.setResultCallback { playerState ->
            spotifyAppRemote!!.userApi.getLibraryState(playerState.track.uri)
                .setResultCallback { libraryState ->
                    var currentSongTransmitData: TransmitData? =
                        TransmitData(playerState, libraryState.isAdded)
                    if (currentSongTransmitData != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.IO) {
                                Log.i(TAG, "Sending update song to watch")
                                connectIQ.sendMessage(
                                    watchDevice,
                                    watchApp,
                                    currentSongTransmitData.toMap(),
                                    object :
                                        ConnectIQ.IQSendMessageListener {
                                        override fun onMessageStatus(
                                            device: IQDevice?,
                                            app: IQApp?,
                                            status: ConnectIQ.IQMessageStatus?
                                        ) {
                                            if (status != ConnectIQ.IQMessageStatus.SUCCESS) {
                                                // Evalute status for cause of the failure
                                            }
                                        }
                                    })
                            }
                        }
                    }

                }
        }
    }

}