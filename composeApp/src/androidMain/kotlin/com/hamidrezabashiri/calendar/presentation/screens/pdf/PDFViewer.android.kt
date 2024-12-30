package com.hamidrezabashiri.calendar.presentation.screens.pdf

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.rajat.pdfviewer.PdfRendererView
import com.rajat.pdfviewer.compose.PdfRendererViewCompose




actual class PdfViewerWrapper {
    actual fun getPdfFile(): Any {
        TODO("Not yet implemented")
    }
}

@Composable
actual fun PdfViewer(filePath: String, modifier: Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    PdfRendererViewCompose(
        modifier = modifier,
        url = filePath,
        lifecycleOwner = lifecycleOwner,
        statusCallBack = object : PdfRendererView.StatusCallBack {
            override fun onPdfLoadStart() {
                Log.i("statusCallBack", "onPdfLoadStart")
            }

            override fun onPdfLoadProgress(
                progress: Int,
                downloadedBytes: Long,
                totalBytes: Long?
            ) {
                //Download is in progress
            }

            override fun onPdfLoadSuccess(absolutePath: String) {
                Log.i("statusCallBack", "onPdfLoadSuccess")
            }

            override fun onError(error: Throwable) {
                Log.i("statusCallBack", "onError")
            }

            override fun onPageChanged(currentPage: Int, totalPage: Int) {
                //Page change. Not require
            }
        }

    )
}