package com.hamidrezabashiri.calendar.presentation.screens.pdf

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect class PdfViewerWrapper {
    fun getPdfFile(): Any  // Will return File for Android, NSData for iOS
}


@Composable
expect fun PdfViewer(
    filePath: String,
    title: String,
    modifier: Modifier,
    onBackPressed: () -> Unit
)