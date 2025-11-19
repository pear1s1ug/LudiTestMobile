package com.example.luditestmobilefinal.utils

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

object YouTubeUtils {

    /**
     * Extrae el ID de video de una URL de YouTube
     */
    fun extractYouTubeVideoId(url: String): String? {
        if (url.isBlank()) return null

        val patterns = listOf(
            "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*",
            "(?<=youtube.com/shorts/)[^#\\&\\?]*",
            "(?<=youtube.com/live/)[^#\\&\\?]*"
        )

        for (pattern in patterns) {
            val compiledPattern = java.util.regex.Pattern.compile(pattern)
            val matcher = compiledPattern.matcher(url)
            if (matcher.find()) {
                return matcher.group()
            }
        }

        // Fallback: try to extract from any YouTube URL
        val fallbackPattern = "youtube\\.com.*[?&]v=([^&]*)"
        val fallbackCompiled = java.util.regex.Pattern.compile(fallbackPattern)
        val fallbackMatcher = fallbackCompiled.matcher(url)
        if (fallbackMatcher.find()) {
            return fallbackMatcher.group(1)
        }

        return null
    }

    /**
     * Composable para mostrar un reproductor de YouTube
     */
    @Composable
    fun YouTubePlayerComposable(
        videoId: String,
        modifier: Modifier = Modifier,
        height: Int = 220
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current

        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(height.dp),
            factory = { context ->
                val youTubePlayerView = YouTubePlayerView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }

                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })

                // Importante: asociar el ciclo de vida para pausar/reanudar correctamente
                lifecycleOwner.lifecycle.addObserver(youTubePlayerView)

                youTubePlayerView
            }
        )
    }

    /**
     * Verifica si una URL es de YouTube válida
     */
    fun isValidYouTubeUrl(url: String): Boolean {
        return extractYouTubeVideoId(url) != null
    }
}