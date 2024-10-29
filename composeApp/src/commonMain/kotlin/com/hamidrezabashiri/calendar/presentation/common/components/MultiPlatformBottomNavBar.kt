package com.hamidrezabashiri.calendar.presentation.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val animationProgress = remember { Animatable(0f) }
    val selectionAnimationProgress = remember { Animatable(0f) }

    // Store initial and target X positions
    var initialX by remember { mutableStateOf(0f) }
    var targetX by remember { mutableStateOf(0f) }

    val curveCenterX = remember { Animatable(0f) }
    LaunchedEffect(selectedTabIndex) {
        if (navBarState.isReady) {
            val targetCurveX = navBarState.calculateTargetOffset(selectedTabIndex, tabs.size)
            launch {
                curveCenterX.animateTo(
                    targetValue = targetCurveX,
                    animationSpec = SpringSpec(
                        dampingRatio = 1.0f,
                        stiffness = 600f
                    )
                )
            }
        }
    }
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
            // Store current X position as initial
            initialX = lerp(initialX, targetX, animationProgress.value)

            // Calculate target X position
            targetX = navBarState.calculateTargetOffset(
                selectedTabIndex,
                tabs.size
            )

            // Reset and start the animations
            launch {
                // Horizontal movement animation
                animationProgress.snapTo(0f)
                animationProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = SpringSpec(
                        dampingRatio = 0.7f,
                        stiffness = 300f
                    )
                )
            }

            launch {
                // Selection bounce animation
                selectionAnimationProgress.snapTo(0f)
                selectionAnimationProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = SpringSpec(
                        dampingRatio = 0.5f,
                        stiffness = 600f
                    )
                )
            }
        }
    }

    // Calculate current X position based on animation progress
    val currentX = remember(initialX, targetX, animationProgress.value) {
        lerp(initialX, targetX, animationProgress.value)
    }

    // Calculate Y offset with selection bounce effect
    val currentY = remember(selectionAnimationProgress.value, animationProgress.value) {
        val baseOffset = -120f  // Base floating height
        val selectionBounce = 20f // How much it bounces on selection

        // Selection bounce effect (down-then-up)
        val bounceOffset = when {
            selectionAnimationProgress.value < 0.5f -> {
                // Going down (0 to 0.5)
                val bounceProgress = selectionAnimationProgress.value * 2
                selectionBounce * kotlin.math.sin(bounceProgress * kotlin.math.PI.toFloat())
            }
            else -> {
                // Going up (0.5 to 1.0)
                val bounceProgress = (selectionAnimationProgress.value - 0.5f) * 2
                selectionBounce * (1 - bounceProgress)
            }
        }

        // Combine base offset with bounce effect
        baseOffset + bounceOffset
    }

    Box(
        modifier = modifier.fillMaxWidth()
            .height(120.dp)
            .offset()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.09f),
                        Color.Black.copy(alpha = 0.1f)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.BottomCenter
    ) {

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
            },
            curveCenterX=curveCenterX
        )
        FloatingNavButton(
            currentTab = currentTab,
            onWidthChanged = { navBarState.floatingButtonWidth = it },
            offsetX = currentX,
            offsetY = currentY,
            density = density
        )
    }
}

@Composable
private fun BottomNavBar(
    tabs: List<Tab>,
    currentTab: Tab,
    onNavBarWidthChanged: (Float) -> Unit,
    onTabPositionChanged: (Int, Float) -> Unit,
    onTabSelected: (Tab) -> Unit,
    curveCenterX: Animatable<Float, AnimationVector1D>,
) {

    Surface(
        color = MaterialTheme.colors.surface,
        shape = InwardCurveShape(curveWidth = 80.dp, curveDepth = 90.dp,curveCenterX.value),
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

@Composable
private fun FloatingNavButton(
    currentTab: Tab,
    onWidthChanged: (Float) -> Unit,
    offsetX: Float,
    offsetY: Float,
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
            .offset(
                x = with(density) { offsetX.toDp() },
                y = with(density) { offsetY.toDp() }  // Convert Y offset to Dp
            )
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
            .clickable(
                onClick = onSelected,
                indication = null,  // Removes the ripple effect
                interactionSource = remember { MutableInteractionSource() }
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
        val usableWidth = navBarWidth - (horizontalPadding * 2)
        val itemWidth = usableWidth / totalTabs
        val absolutePosition = (itemWidth * selectedIndex) + horizontalPadding
        val tabCenter = absolutePosition + (itemWidth / 2f)
        val navCenter = navBarWidth / 2f
        return tabCenter - navCenter
    }
}

// Helper function for linear interpolation
private fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}
