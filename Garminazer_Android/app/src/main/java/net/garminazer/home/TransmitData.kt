package net.garminazer.home

import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track

class TransmitData(
    var action: SpotifyAction?,
    val songId: String? = null,
    val playerPlayBackPosition: Number = 0,
    val playerIsPaused: Boolean = true,
    val songName: String? = null,
    val songArtist: String? = null,
    val songLength: Number? = null,
    val imageURL: String? = null,
    val isSongLiked:Boolean = false
) {

    constructor(map: Map<String, Any?>) : this(
        action = SpotifyAction.values().get(map.get("action") as Int),
        songId = map.getOrDefault("songId",null) as String?,
        playerPlayBackPosition = map.getOrDefault("currentTime", 0) as Number,
        songName = map.getOrDefault("songName",null) as String?,
        songArtist = map.getOrDefault("songArtist",null) as String?,
        songLength = map.getOrDefault("songLength",null) as Number?
    ) {
    }

    constructor(playerState: PlayerState, isLiked: Boolean) : this(
        playerState.track,
        playerPlayBackPosition = playerState.playbackPosition,
        playerIsPaused = playerState.isPaused,
        isSongLiked = isLiked
    ){}

    constructor(track: Track, playerPlayBackPosition: Number = 0, playerIsPaused: Boolean = true, isSongLiked: Boolean = false) : this(
        action = SpotifyAction.UPDATE,
        songId = track.uri,
        playerPlayBackPosition = playerPlayBackPosition,
        playerIsPaused = playerIsPaused,
        songArtist = track.artist.name,
        songName = track.name,
        songLength = track.duration / 1000,
        imageURL = track.imageUri.raw!!.substring(14),
        isSongLiked = isSongLiked
    ) {

    }

    fun toMap(): Map<String, Any?> {
        val map = HashMap<String, Any?>()
        map.put("action", action!!.ordinal)
        map.put("songId", songId)
        map.put("songName", songName)
        map.put("songArtist", songArtist)
        map.put("songLength", songLength)
        map.put("currentTime", playerPlayBackPosition)
        map.put("imageURL", imageURL)
        map.put("isPlayerPaused", playerIsPaused)
        map.put("isSongLiked", isSongLiked)

        return map
    }
}