package com.hamidrezabashiri.calendar.presentation.screens.pdf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamidrezabashiri.calendar.presentation.navigation.BaseTab
import compose.icons.Octicons
import compose.icons.octicons.Gear16

class BookReaderScreen (private val bookId: Int) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Box(modifier = Modifier.fillMaxSize()) {
            // Back button
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart),
                onClick = { navigator.pop() }
            ) {
                Icon(Icons.Default.ArrowBack, "Back")
            }

            // PDF Viewer
            PdfViewer("https://css4.pub/2015/textbook/somatosensory.pdf")
        }
    }
}