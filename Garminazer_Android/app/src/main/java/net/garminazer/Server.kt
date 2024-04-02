package net.garminazer


import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.spotify.protocol.types.ImageUri
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit


fun Application.module() {
    install(ContentNegotiation) {
        gson()
    }

    routing {
        get("/spotifyImage") {
//            val imageUri = ImageUri(call.parameters["imageId"])

//            val imageUri = trackState.value!!.imageUri
//            Log.i("Server","Spotify:" + imageUri)
//            val res = spotifyAppRemote?.imagesApi?.getImage(imageUri)!!.setResultCallback { bitmap ->
//                run {
//                    Log.i("Server","Spotify Image result:" + bitmap)
//                }
//            }
//            if (res != null) {
//            }
            if (imageState.value != null) {
                val stream = ByteArrayOutputStream()
                imageState.value!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()
                call.respondBytes(byteArray, ContentType("image", "jpeg", emptyList()))
            }
            else{
                call.respondText("No image")
            }
        }
        get("/textCheck") {
                call.respondText("Bruh")
        }
    }
}