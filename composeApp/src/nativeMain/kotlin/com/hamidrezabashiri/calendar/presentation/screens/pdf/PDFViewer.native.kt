package com.hamidrezabashiri.calendar.presentation.screens.pdf

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi


actual class PdfViewerWrapper {
    actual fun getPdfFile(): Any {
        TODO("Not yet implemented")
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PdfViewer(filePath: String, modifier: Modifier) {
//    UIKitView(
//        factory = {
//            // Create native PDFView
//            PDFView().apply {
//                // Load PDF document
//                val url = NSURL.fileURLWithPath(filePath)
//                val document = PDFDocument(url)
//                setDocument(document)
//
//                // Configure view properties
//                setAutoScales(true)
//            }
//        },
//        modifier = modifier,
//        update = { view ->
//            // Update view if needed
//        }
//    )

}