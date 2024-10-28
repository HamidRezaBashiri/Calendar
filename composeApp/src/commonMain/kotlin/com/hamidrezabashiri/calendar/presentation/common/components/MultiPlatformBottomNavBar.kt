package com.hamidrezabashiri.calendar.presentation.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab

@Composable
fun MultiPlatformBottomNavBar(
    modifier: Modifier = Modifier,
    tabs: List<Tab>
) {
    val tabNavigator = LocalTabNavigator.current
    var currentTab by remember { mutableStateOf(tabNavigator.current) }

    LaunchedEffect(tabNavigator.current) {
        currentTab = tabNavigator.current
    }
    LaunchedEffect(tabNavigator.current) {
        currentTab = if (tabs.contains(tabNavigator.current)) tabNavigator.current else tabs.first()
    }

    val springSpec = SpringSpec<Float>(
        stiffness = 800f,
        dampingRatio = 0.8f
    )

    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        BottomNavLayout(
            selectedIndex = tabs.indexOf(currentTab),
            itemCount = tabs.size,
            animSpec = springSpec,
            indicator = { BottomNavIndicator() }
        ) {
            tabs.forEach { tab ->
                val isSelected = currentTab.options.index == tab.options.index
                val iconColor by animateColorAsState(
                    if (isSelected) MaterialTheme.colors.onSurface
                    else MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )

                NavBarItem(
                    tab = tab,
                    isSelected = isSelected,
                    iconColor = iconColor,
                    onSelected = {
                        currentTab = tab
                        tabNavigator.current = tab
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    indicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val selectionFractions = remember(itemCount) {
        List(itemCount) { i ->
            Animatable(if (i == selectedIndex) 1f else 0f)
        }
    }

    selectionFractions.forEachIndexed { index, selectionFraction ->
        val target = if (index == selectedIndex) 1f else 0f
        LaunchedEffect(target, animSpec) {
            selectionFraction.animateTo(target, animSpec)
        }
    }

    val indicatorIndex = remember { Animatable(selectedIndex.toFloat()) }

    LaunchedEffect(selectedIndex) {
        indicatorIndex.animateTo(selectedIndex.toFloat(), animSpec)
    }

    Layout(
        modifier = modifier.height(60.dp),
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = indicator)
        }
    ) { measurables, constraints ->
        val itemCount = measurables.size - 1 // Account for indicator
        val totalWidth = constraints.maxWidth
        val itemWidth = totalWidth / itemCount

        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }
        val itemPlaceable = measurables
            .filterNot { it == indicatorMeasurable }
            .map { measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = itemWidth,
                        maxWidth = itemWidth
                    )
                )
            }

        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = itemWidth,
                maxWidth = itemWidth
            )
        )

        layout(constraints.maxWidth, itemPlaceable.maxOf { it.height }) {
            val indicatorLeft = indicatorIndex.value * itemWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)

            var x = 0
            itemPlaceable.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
private fun NavBarItem(
    tab: Tab,
    isSelected: Boolean,
    iconColor: Color,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onSelected
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.animateContentSize()
        ) {
            // Icon
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = tab.options.title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            // Show text only when selected
            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tab.options.title,
                    color = iconColor,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Composable
private fun BottomNavIndicator(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background indicator
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(
                    MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
        )

        // Bottom indicator line
        Spacer(
            modifier = Modifier
                .padding(bottom = 2.dp)
                .align(Alignment.BottomCenter)
                .height(4.dp)
                .fillMaxWidth(0.3f)
                .background(
                    color = MaterialTheme.colors.onSurface,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}