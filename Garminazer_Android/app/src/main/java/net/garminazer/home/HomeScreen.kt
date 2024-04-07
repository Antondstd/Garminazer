package net.garminazer.home

import android.R.id.message
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.provider.AlarmClock
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.ConnectIQ.IQApplicationEventListener
import com.garmin.android.connectiq.ConnectIQ.IQMessageStatus
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import com.garmin.android.connectiq.IQDevice.IQDeviceStatus
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.garminazer.spotifyAppRemote
import net.garminazer.trackState
import java.util.Calendar


var watchDevice: IQDevice? = null
val watchUUID: String = "03f74f1d-73b5-42eb-bbcf-ee90253cef45"
var watchApp: IQApp? = null
//var connectIQ: ConnectIQ? = null


@Composable
fun HomeScreen(
) {

    Log.i("MainActivity", "test123")
//    var trackState:MutableState<Track?> = remember { mutableStateOf(null) }
    var test: MutableState<String> = remember { mutableStateOf("Test123") }

    var showBranding by rememberSaveable { mutableStateOf(true) }
    Scaffold(modifier = Modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(
                showBranding, Modifier.fillMaxWidth()
            ) {
                InterfaceScreen(trackState, test)
            }
        }
    }
}

//private fun connected(trackState: MutableState<Track?>) {
//    // Subscribe to PlayerState
//    var spot = spotifyAppRemote
//    spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
//        val track: Track = it.track
//        trackState.value = track
//        Log.i("MainActivity", "Track: " + track.name + " by " + track.artist.name)
//    }
//}

@Composable
private fun InterfaceScreen(
    trackState: MutableState<Track?>,
    test: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var message: List<String> = listOf("sending message", "228")
//    if (connectIQ == null) {
//        connectIQ = ConnectIQ.getInstance(localContext, ConnectIQ.IQConnectType.TETHERED)
////        connectIQ = ConnectIQ.getInstance(localContext, ConnectIQ.IQConnectType.WIRELESS)
//    }
    Column(
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        trackState.value?.let { Text(it.name, fontSize = 30.sp) }
        trackState.value?.let { it.imageUri.raw?.let { it1 -> Text(it1, fontSize = 30.sp) } }
        Text(test.value)
        Button(onClick = { getSongUri(localContext) }) {
            Text("Copy spotify Image Uri", fontSize = 30.sp)
        }
        Button(onClick = { startAlarm(localContext) }) {
            Text("Start Alarm", fontSize = 30.sp)
        }

        Button(onClick = { startAlarm(localContext) }) {
            Text("Start Alarm", fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.size(40.dp))

        Button(onClick = { stopAlarm(localContext) }) {
            Text("Stop Alarm", fontSize = 30.sp)
        }
//        Button(onClick = { nextSong() }) {
//            Text("Next song", fontSize = 30.sp)
//        }
//        Button(onClick = { previousSong() }) {
//            Text("Previous song", fontSize = 30.sp)
//        }
//        Button(onClick = { likeSong() }) {
//            Text("Like song", fontSize = 30.sp)
//        }
//        Button(onClick = { connectToWatch(connectIQ!!, localContext, coroutineScope) }) {
//            Text("Connect to watch", fontSize = 30.sp)
//        }
//        Button(onClick = { sendMessage(message, connectIQ!!, coroutineScope) }) {
//            Text("Send Message", fontSize = 30.sp)
//        }
//        Button(onClick = { registerWatchApp(connectIQ!!, coroutineScope) }) {
//            Text("Register", fontSize = 30.sp)
//        }
//        Button(onClick = { updateSong(connectIQ!!, coroutineScope) }) {
//            Text("Update song", fontSize = 30.sp)
//        }
//        trackState.value.let { Text(trackState.value!!.imageUri.toString()) }
    }
}

fun getSongUri(context: Context) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("spotifyImageUri", trackState.value?.imageUri?.raw)
    clipboardManager.setPrimaryClip(clip)
}
//
//fun connectToWatch(connectIQ: ConnectIQ, localContext: Context, coroutineScope: CoroutineScope) {
////    var connectIQ = ConnectIQ.getInstance(localContext, ConnectIQ.IQConnectType.TETHERED)
////    val pIntent = PendingIntent.getBroadcast(localContext, 1001, intent, PendingIntent.FLAG_IMMUTABLE);
//
////    val intentFilter = IntentFilter()
////    localContext.registerReceiver(connectIQ.getMessageReceiver(), intentFilter, RECEIVER_EXPORTED);
//
//
////    ConnectIQ connectIQ = ConnectIQ.getInstance(ConnectIQ.IQConnectType.<protocol>);
//    connectIQ.initialize(localContext, true, object : ConnectIQ.ConnectIQListener {
//        // Called when the SDK has been successfully initialized
//        override fun onSdkReady() {
//            Log.i("MainActivity", "SDK READY")
//            // Do any post initialization setup.
//            val paired = connectIQ.knownDevices
//
//            if (paired != null && paired.size > 0) {
//                // get the status of the devices
//                for (device in paired) {
//                    val status: IQDeviceStatus = connectIQ.getDeviceStatus(device)
//                    if (status == IQDeviceStatus.CONNECTED && device != null) {
//                        watchDevice = device
//                    }
//                }
//            }
//            registerWatchApp(connectIQ, coroutineScope)
//        }
//
//        // Called when the SDK has been shut down
//        override fun onSdkShutDown() {
//
//            // Take care of any post shutdown requirements
//        }
//
//        // Called when initialization fails.
//        override fun onInitializeError(status: ConnectIQ.IQSdkErrorStatus?) {
//            Log.i("MainActivity", "Error with onInitializeError:" + status)
//            // A failure has occurred during initialization. Inspect
//            // the IQSdkErrorStatus value for more information regarding
//            // the failure.
//        }
//    })
//
//}
//
//fun registerWatchApp(connectIQ: ConnectIQ, coroutineScope: CoroutineScope) {
//    Log.i("MainActivity", "WatchDevice" + watchDevice + " and watchApp " + watchApp)
//    if (watchDevice != null) {
//        connectIQ.registerForDeviceEvents(watchDevice) { device, status ->
//
//
//        }
//        connectIQ.getApplicationInfo(
////            "62d05e34-d9cf-41ee-9326-06fb139f5ab1",
//            "",
//            watchDevice,
//            object : //Empty Id for simulator
//                ConnectIQ.IQApplicationInfoListener {
//                override fun onApplicationInfoReceived(app: IQApp?) {
//                    Log.i(
//                        "MainActivity",
//                        "Watch onApplicationInfoReceived status" + app!!.displayName
//                    )
//                    if (app != null) {
////                    app.
//                        watchApp = app
//                        connectIQ.registerForAppEvents(
//                            watchDevice,
//                            watchApp,
//                            object : IQApplicationEventListener {
//                                override fun onMessageReceived(
//                                    device: IQDevice?,
//                                    app: IQApp?,
//                                    message: MutableList<Any>?,
//                                    status: IQMessageStatus?
//                                ) {
//                                    Log.i(
//                                        "MainActivity",
//                                        "Watch onMessageReceived status" + message!!.get(0)
//                                    )
//                                    val data = TransmitData(message!!.get(0) as Map<String, Any?>)
//                                    performAction(data, connectIQ, coroutineScope)
//                                }
//
//                            })
//                    }
//                }
//
//                override fun onApplicationNotInstalled(p0: String?) {
//
//                }
//
//            })
//    }
//}
//
//fun performAction(data: TransmitData, connectIQ: ConnectIQ, coroutineScope: CoroutineScope) {
//    Log.i(
//        "MainActivity",
//        "Perform action on Data: " + data
//    )
//    if (data.action == SpotifyAction.LIKE) {
//        if (data.songId == null) {
//            likeSong()
//        } else {
//            likeSong(data.songId!!)
//        }
//    }
//    if (data.action == SpotifyAction.DISLIKE) {
//        dislikeSong(data.songId!!)
//    }
//    if (data.action == SpotifyAction.PAUSE) {
//        spotifyAppRemote?.playerApi?.pause()
//    }
//    if (data.action == SpotifyAction.PLAY) {
//        spotifyAppRemote?.playerApi?.resume()
//    }
//    if (data.action == SpotifyAction.NEXT) {
//        spotifyAppRemote?.playerApi?.skipNext()
//            ?.setResultCallback { updateSong(connectIQ, coroutineScope) }
//
//    }
//    if (data.action == SpotifyAction.PREVIOUS) {
//        spotifyAppRemote?.playerApi?.skipPrevious()
//            ?.setResultCallback { updateSong(connectIQ, coroutineScope) }
//    }
//    if (data.action == SpotifyAction.SHUFFLE) {
//        spotifyAppRemote?.playerApi?.toggleShuffle()
//    }
//}
//
//fun sendMessage(message: List<String>, connectIQ: ConnectIQ, coroutineScope: CoroutineScope) {
//    coroutineScope.launch {
//        withContext(Dispatchers.IO) {
//            Log.i("MainActivity", "WatchDevice" + watchDevice + " and watchApp " + watchApp)
//            connectIQ.sendMessage(watchDevice, watchApp, message, object :
//                ConnectIQ.IQSendMessageListener {
//                @Override
//                override fun onMessageStatus(
//                    device: IQDevice?,
//                    app: IQApp?,
//                    status: IQMessageStatus?
//                ) {
//                    if (status != IQMessageStatus.SUCCESS) {
//                        // Evalute status for cause of the failure
//                    }
//                }
//            })
//        }
//    }
//}
//
//fun updateSong(connectIQ: ConnectIQ, coroutineScope: CoroutineScope) {
//    spotifyAppRemote!!.playerApi.playerState.setResultCallback { playerState ->
//        spotifyAppRemote!!.userApi.getLibraryState(playerState.track.uri)
//            .setResultCallback { libraryState ->
//                var currentSongTransmitData: TransmitData? =
//                    TransmitData(playerState, libraryState.isAdded)
//                if (currentSongTransmitData != null) {
//                    coroutineScope.launch {
//                        withContext(Dispatchers.IO) {
//                            Log.i("Spotifyzer", "Sending update song to watch")
//                            connectIQ.sendMessage(
//                                watchDevice,
//                                watchApp,
//                                currentSongTransmitData.toMap(),
//                                object :
//                                    ConnectIQ.IQSendMessageListener {
//                                    override fun onMessageStatus(
//                                        device: IQDevice?,
//                                        app: IQApp?,
//                                        status: IQMessageStatus?
//                                    ) {
//                                        if (status != IQMessageStatus.SUCCESS) {
//                                            // Evalute status for cause of the failure
//                                        }
//                                    }
//                                })
//                        }
//                    }
//                }
//
//            }
//    }
//}
//
//fun nextSong() {
//    spotifyAppRemote?.playerApi?.skipNext()
//}
//
//
//fun previousSong() {
//    spotifyAppRemote?.playerApi?.skipPrevious()
//}
//
//fun likeSong() {
//    spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
//        val track: Track = it.track
//        spotifyAppRemote!!.userApi.addToLibrary(track.uri)
//    }
//}
//
//fun likeSong(trackURI: String) {
//    spotifyAppRemote!!.userApi.addToLibrary(trackURI)
//}
//
//fun dislikeSong(trackURI: String) {
//    spotifyAppRemote!!.userApi.removeFromLibrary(trackURI)
//}
//
//fun playPlayer() {
//
//}
//
//fun stopPlayer() {
//
//}

@SuppressLint("ScheduleExactAlarm")
fun startAlarm(context: Context) {
    val alarmManager = context.getSystemService(AlarmManager::class.java)
//    alarmManager?.set(
//        AlarmManager.ELAPSED_REALTIME_WAKEUP,
//        SystemClock.elapsedRealtime() + 5 * 1000,
//        PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
//    )
//    val time = SystemClock.elapsedRealtime() + 5 * 1000
//    val pendingIntent = PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
//    alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(time,pendingIntent),pendingIntent)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MINUTE, 1)

    val intent: Intent = Intent(AlarmClock.ACTION_SET_ALARM)
        .putExtra(AlarmClock.EXTRA_MESSAGE, message) // show on it
        .putExtra(AlarmClock.EXTRA_HOUR, calendar.get(Calendar.HOUR_OF_DAY)) //24 hours
        .putExtra(AlarmClock.EXTRA_MINUTES, calendar.get(Calendar.MINUTE)) //not more than 60 :)
        .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
    startActivity(context, intent, null)
}

fun stopAlarm(context: Context) {
    val alarmManager = context.getSystemService(AlarmManager::class.java)
    val next = alarmManager.nextAlarmClock
    if (next != null) {
//        alarmManager.cancel(next.showIntent)
        next.showIntent.cancel()
    }
    val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM)
        .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        .putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_NEXT)
//    val pIntent = PendingIntent.getBroadcast(context, 1001, intent, PendingIntent.FLAG_IMMUTABLE);
//    manager.cancel(pIntent)
    startActivity(context, intent, null)
}


@Preview(name = "Welcome light theme", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Welcome dark theme", uiMode = UI_MODE_NIGHT_NO)
@Composable
fun HomecreenPreview() {
    HomeScreen(
    )
}