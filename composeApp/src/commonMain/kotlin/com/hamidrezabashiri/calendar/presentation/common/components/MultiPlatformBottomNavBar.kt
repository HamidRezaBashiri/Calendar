package com.hamidrezabashiri.calendar.presentation.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import kotlinx.coroutines.launch

@Composable
fun MultiPlatformBottomNavBar(
    modifier: Modifier = Modifier,
    tabs: List<Tab>
) {
    val tabNavigator = LocalTabNavigator.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    var selectedTabIndex by remember { mutableStateOf(1) }
    val currentTab by remember(selectedTabIndex) {
        derivedStateOf { tabs.getOrNull(selectedTabIndex) ?: tabs[1] }
    }

    val navBarState = remember { NavBarState() }
    val animatedOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        tabNavigator.current = tabs[1]
        selectedTabIndex = 1
    }

    LaunchedEffect(tabNavigator.current) {
        val index = tabs.indexOf(tabNavigator.current)
        if (index != -1) {
            selectedTabIndex = index
        }
    }

    LaunchedEffect(selectedTabIndex) {
        if (navBarState.isReady) {
            val targetOffset = navBarState.calculateTargetOffset(
                selectedTabIndex,
                tabs.size
            )
            scope.launch {
                animatedOffset.animateTo(
                    targetValue = targetOffset,
                    animationSpec = SpringSpec(
                        dampingRatio = 0.7f,
                        stiffness = 300f
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        FloatingNavButton(
            currentTab = currentTab,
            onWidthChanged = { navBarState.floatingButtonWidth = it },
            offset = animatedOffset.value,
            density = density
        )

        BottomNavBar(
            tabs = tabs,
            currentTab = currentTab,
            onNavBarWidthChanged = { navBarState.navBarWidth = it },
            onTabPositionChanged = { index, position ->
                navBarState.updateTabPosition(index, position)
            },
            onTabSelected = { tab ->
                selectedTabIndex = tabs.indexOf(tab)
                tabNavigator.current = tab
            }
        )
    }
}

@Composable
private fun FloatingNavButton(
    currentTab: Tab,
    onWidthChanged: (Float) -> Unit,
    offset: Float,
    density: androidx.compose.ui.unit.Density
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colors.primary,
        elevation = 8.dp,
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                onWidthChanged(coordinates.size.width.toFloat())
            }
            .offset(x = with(density) { offset.toDp() }, y = (-40).dp)
            .size(64.dp)
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = currentTab.options.icon ?: rememberVectorPainter(Icons.Default.Home),
                contentDescription = currentTab.options.title,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentTab.options.title,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun BottomNavBar(
    tabs: List<Tab>,
    currentTab: Tab,
    onNavBarWidthChanged: (Float) -> Unit,
    onTabPositionChanged: (Int, Float) -> Unit,
    onTabSelected: (Tab) -> Unit
) {
    Surface(
        color = MaterialTheme.colors.surface,
//        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                onNavBarWidthChanged(coordinates.size.width.toFloat())
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .onGloballyPositioned { coordinates ->
                            onTabPositionChanged(index, coordinates.positionInParent().x)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    NavBarItem(
                        tab = tab,
                        isSelected = currentTab.options.index == tab.options.index,
                        onSelected = { onTabSelected(tab) }
                    )
                }
            }
        }
    }
}

private class NavBarState {
    var navBarWidth by mutableStateOf(0f)
    var floatingButtonWidth by mutableStateOf(0f)
    private val tabPositions = mutableStateListOf<Float>()
    val horizontalPadding = 32f
    val isReady: Boolean
        get() = navBarWidth > 0 && floatingButtonWidth > 0 && tabPositions.isNotEmpty()

    fun updateTabPosition(index: Int, position: Float) {
        if (tabPositions.size > index) {
            tabPositions[index] = position
        } else {
            tabPositions.add(position)
        }
    }

    fun calculateTargetOffset(selectedIndex: Int, totalTabs: Int): Float {
        // Usable width of the nav bar after accounting for horizontal padding
        val usableWidth = navBarWidth - (horizontalPadding * 2)

        // Width of each tab
        val itemWidth = usableWidth / totalTabs

        // Calculate the absolute position of the selected tab
        // Adjust the absolute position to consider the starting position
        val absolutePosition = (itemWidth * selectedIndex) + horizontalPadding

        println("Absolute Position (Tab Start Position): $absolutePosition")

        // Calculate the center of the selected tab
        val tabCenter = absolutePosition + (itemWidth / 2f)

        // Calculate the center of the navbar
        val navCenter = navBarWidth / 2f

        // Center-align the floating button with the selected tab
        // This calculation effectively offsets the floating button
        val targetOffset = tabCenter - navCenter

        return targetOffset
    }




}

@Composable
private fun NavBarItem(
    tab: Tab,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor by animateColorAsState(
        if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0f)
        else MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    )

    Column(
        modifier = modifier.fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onSelected
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = tab.options.icon ?: rememberVectorPainter(Icons.Default.Home),
            contentDescription = tab.options.title,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = tab.options.title,
            style = MaterialTheme.typography.caption,
            color = contentColor
        )
    }
}