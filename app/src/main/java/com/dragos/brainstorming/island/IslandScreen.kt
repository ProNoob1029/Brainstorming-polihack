package com.dragos.brainstorming.island

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.Manipulator
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.gesture.orbitHomePosition
import io.github.sceneview.gesture.targetPosition
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.toPosition
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.PlaneNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit.MILLISECONDS

@Composable
fun IslandScreen(
    modifier: Modifier = Modifier,
    viewModel: IslandViewModel = viewModel()
) {
    val treePos by viewModel.treeLocation.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val environmentLoader = rememberEnvironmentLoader(engine)

        val centerNode = rememberNode(engine)

        val cameraNode = rememberCameraNode(engine) {
            position = Position(y = 0.07f, z = 0.12f)
            lookAt(centerNode)
            centerNode.addChildNode(this)

        }
        var move by remember {
            mutableStateOf(false)
        }

        val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
        val cameraRotation by cameraTransition.animateRotation(
            initialValue = Rotation(y = 0.0f),
            targetValue = Rotation(y = 360.0f),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 30.seconds.toInt(MILLISECONDS), easing = LinearEasing)
            )
        )

        val planeNode = rememberNode {
            PlaneNode(
                engine = engine,
            ).apply {
                rotation = Rotation(x = 90.0f)
            }
        }

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

        val treeParent = rememberNode(engine)

        val treeModel = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/tree3.glb"
                ),
                scaleToUnits = 0.04f,

            ).apply {
                isHittable =false
                treeParent.addChildNode(this)
                position = Position(x = 0.008f, z = -0.01f)
            }
        }


        val islandModel = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/newIsland.glb"
                ),
                scaleToUnits = 1f,
            ).apply {
                isHittable = false
                //centerNode.addChildNode(this)
            }
        }

        val childNodes = remember {
            listOf(centerNode, islandModel, treeModel, planeNode, treeParent)
        }

        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            mainLightNode = rememberMainLightNode(engine) {
                intensity = 1f
            },
            cameraNode = cameraNode,
            cameraManipulator = cameraManipulator,
            childNodes = childNodes,
            environment = environmentLoader.createHDREnvironment(
                assetFileLocation = "environments/sky_2k.hdr"
            )!!,
            onFrame = {
                centerNode.rotation = cameraRotation
                cameraNode.lookAt(centerNode)
                treeParent.position = treePos.toFloatArray().toPosition()
            },
            onTouchEvent = { _, hitResult ->
                hitResult?.let {
                    if (!move) return@let
                    viewModel.moveTree(
                        Float3(
                            x = hitResult.worldPosition.x,
                            y = hitResult.worldPosition.y,
                            z = hitResult.worldPosition.z
                        )
                    )
                    Log.d("A", hitResult.distance.toString())
                }
                false
            }
        )

        Button(
            onClick = {
                move = !move
                //treeModel.isVisible = !treeModel.isVisible
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("tree")
        }
    }
}