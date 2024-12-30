package com.hamidrezabashiri.calendar.presentation.screens.pdf

import androidx.compose.runtime.Composable

expect class PdfViewerWrapper {
    fun getPdfFile(): Any  // Will return File for Android, NSData for iOS
}

@Composable
expect fun PdfViewer(
    filePath: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
)
