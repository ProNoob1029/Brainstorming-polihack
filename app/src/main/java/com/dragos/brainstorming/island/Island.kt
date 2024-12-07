package com.dragos.brainstorming.island

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.filament.utils.Manipulator
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.gesture.orbitHomePosition
import io.github.sceneview.gesture.targetPosition
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit.MILLISECONDS

@Composable
fun Island(modifier: Modifier = Modifier) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)

    val centerNode = rememberNode(engine)

    val cameraNode = rememberCameraNode(engine) {
        position = Position(y = 0.07f, z = 0.12f)
        lookAt(centerNode)
        centerNode.addChildNode(this)
    }

    val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
    val cameraRotation by cameraTransition.animateRotation(
        initialValue = Rotation(y = 0.0f),
        targetValue = Rotation(y = 360.0f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30.seconds.toInt(MILLISECONDS), easing = LinearEasing)
        )
    )



    val cameraManipulator = remember {
        Manipulator.Builder()
            .apply {
                orbitHomePosition(cameraNode.worldPosition)
                targetPosition(centerNode.worldPosition)
            }
            .zoomSpeed(0.000000000001f)
            .orbitSpeed(0.005f, 0.0f)
            .build(Manipulator.Mode.ORBIT)
    }

    Scene(
        modifier = modifier.fillMaxSize(),
        engine = engine,
        modelLoader = modelLoader,
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 1f
        },
        cameraNode = cameraNode,
        cameraManipulator = cameraManipulator,
        childNodes = listOf(centerNode,
            rememberNode {
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(
                        assetFileLocation = "models/newIsland.glb"
                    ),
                    scaleToUnits = 1f
                )
            }),
        environment = environmentLoader.createHDREnvironment(
            assetFileLocation = "environments/sky_2k.hdr"
        )!!,
        onFrame = {
            centerNode.rotation = cameraRotation
            cameraNode.lookAt(centerNode)
        },
        onGestureListener = rememberOnGestureListener(
            onDoubleTap = { _, node ->
                node?.apply {
                    scale *= 2.0f
                }
            }
        )
    )
}