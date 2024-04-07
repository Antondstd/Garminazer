package net.garminazer.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class SpotifyReceiver() : BroadcastReceiver() {

    private val TAG = "SpotifyReceiver"
    internal object BroadcastTypes {
        const val SPOTIFY_PACKAGE = "com.spotify.music"
        const val PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged"
        const val QUEUE_CHANGED = SPOTIFY_PACKAGE + ".queuechanged"
        const val METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged"
    }

    companion object {
        public fun getIntentFilter(): IntentFilter {
            var intentFilter: IntentFilter = IntentFilter()
            intentFilter.addAction("com.spotify.music.playbackstatechanged")
//        intentFilter.addAction(BroadcastTypes.QUEUE_CHANGED)
            intentFilter.addAction(BroadcastTypes.METADATA_CHANGED)
            return intentFilter
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This is sent with all broadcasts, regardless of type. The value is taken from
        // System.currentTimeMillis(), which you can compare to in order to determine how
        // old the event is.
        val timeSentInMs = intent.getLongExtra("timeSent", 0L)
        val action = intent.action
        Log.d(TAG,action.toString())
        if (action == BroadcastTypes.METADATA_CHANGED) {
//            spotifizerService.updateSong()
        } else if (action == BroadcastTypes.PLAYBACK_STATE_CHANGED) {
//            spotifizerService.updateSong()
            // Do something with extracted information
        } else if (action == BroadcastTypes.QUEUE_CHANGED) {
            // Sent only as a notification, your app may want to respond accordingly.
        }
    }
}