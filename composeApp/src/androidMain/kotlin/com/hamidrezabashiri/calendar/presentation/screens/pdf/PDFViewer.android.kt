package com.hamidrezabashiri.calendar.presentation.screens.pdf

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rajat.pdfviewer.PdfRendererView
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


actual class PdfViewerWrapper {
    actual fun getPdfFile(): Any {
        TODO("Not yet implemented")
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun PdfViewer(
    filePath: String,
    title: String,
    modifier: Modifier,
    onBackPressed: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    // States
    var isLoading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableStateOf(0f) }
    var error by remember { mutableStateOf<String?>(null) }
    var currentPageNumber by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(1) }
    var showControls by remember { mutableStateOf(true) }

    // Auto-hide controls after 3 seconds of inactivity
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(3000)
            showControls = false
        }
    }

    // Handle system back button
    BackHandler {
        onBackPressed()
    }

    Box(modifier = modifier) {
        // PDF Viewer
        if (!isLoading && error == null) {
            PdfRendererViewCompose(
                modifier = Modifier.fillMaxSize(),
                url = filePath,
                lifecycleOwner = lifecycleOwner,
                statusCallBack = object : PdfRendererView.StatusCallBack {
                    override fun onPdfLoadStart() {
                        isLoading = true
                        error = null
                        Log.i("statusCallBack", "onPdfLoadStart")
                    }

                    override fun onPdfLoadProgress(
                        progress: Int,
                        downloadedBytes: Long,
                        totalBytes: Long?
                    ) {
                        isLoading = true
                        downloadProgress = when {
                            totalBytes != null && totalBytes > 0 -> {
                                downloadedBytes.toFloat() / totalBytes.toFloat()
                            }
                            else -> progress / 100f
                        }
                    }

                    override fun onPdfLoadSuccess(absolutePath: String) {
                        isLoading = false
                        error = null
                    }

                    override fun onError(throwable: Throwable) {
                        isLoading = false
                        error = throwable.message ?: "Error loading PDF"
                        Log.e("statusCallBack", "onError", throwable)
                    }

                    override fun onPageChanged(currentPage: Int, totalPage: Int) {
                        currentPageNumber = currentPage +1
                        totalPages = totalPage
                    }
                }
            )

            // Transparent overlay for click handling
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter {
                        // Return false to let the event pass through
                        false
                    }
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            var downPosition: Offset? = null
                            val tapTimeout = 100L // Maximum time for a tap (in milliseconds)
                            val tapSlop = 10.dp.toPx() // Maximum movement allowed for a tap

                            while (scope.isActive) {
                                val event = awaitPointerEvent()
                                when (event.type) {
                                    PointerEventType.Press -> {
                                        // Record the position of the press
                                        downPosition = event.changes.first().position
                                    }

                                    PointerEventType.Release -> {
                                        val upPosition = event.changes.first().position
                                        if (downPosition != null) {
                                            // Check if the pointer moved significantly
                                            val distance = (upPosition - downPosition).getDistance()
                                            if (distance <= tapSlop) {
                                                // It's a tap, toggle the controls
                                                showControls = !showControls
                                            }
                                        }
                                        downPosition = null
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
            )
        }

        // Loading placeholder
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = downloadProgress,
                    modifier = Modifier
                        .width(200.dp)
                        .height(4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${(downloadProgress * 100).toInt()}%")
            }
        }

        // Error message
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Animated Header
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                color = MaterialTheme.colors.surface.copy(alpha = 0.9f),
                elevation = 4.dp
            ) {
                TopAppBar(
                    title = { Text(text = title) },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        }

        // Animated Footer
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                color = MaterialTheme.colors.surface.copy(alpha = 0.9f),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Page $currentPageNumber of $totalPages",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = currentPageNumber.toFloat() / totalPages.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )
                }
            }
        }
    }
}