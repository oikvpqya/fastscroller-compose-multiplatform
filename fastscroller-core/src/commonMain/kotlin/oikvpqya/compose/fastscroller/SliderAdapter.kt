/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oikvpqya.compose.fastscroller

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.roundToInt

class SliderAdapter(
    val adapter: ScrollbarAdapter,
    private val trackSize: Int,
    private val minHeight: Float,
    private val reverseLayout: Boolean,
    private val isVertical: Boolean,
    private val coroutineScope: CoroutineScope
) {

    private val contentSize get() = adapter.contentSize
    private val visiblePart: Double
        get() {
            val contentSize = contentSize
            return if (contentSize == 0.0)
                1.0
            else
                (adapter.viewportSize / contentSize).coerceAtMost(1.0)
        }

    val thumbSize: Double
        get() = (trackSize * visiblePart).coerceAtLeast(minHeight.toDouble())


    private val scrollScale: Double
        get() {
            val extraScrollbarSpace = trackSize - thumbSize
            val extraContentSpace = adapter.maxScrollOffset  // == contentSize - viewportSize
            return if (extraContentSpace == 0.0) 1.0 else extraScrollbarSpace / extraContentSpace
        }

    private val rawPosition: Double
        get() = scrollScale * adapter.scrollOffset

    val position: Double
        get() = if (reverseLayout) trackSize - thumbSize - rawPosition else rawPosition

    val thumbCenter: Double
        get() = position + (thumbSize / 2.0)

    val bounds get() = position..position + thumbSize

    val thumbPixelRange: IntRange
        get() {
            val start = position.roundToInt()
            val endExclusive = start + thumbSize.roundToInt()

            return (start until endExclusive)
        }


            // How much of the current drag was ignored because we've reached the end of the scrollbar area
    private var unscrolledDragDistance = 0.0

    /** Called when the thumb dragging starts */
    fun onDragStarted() {
        unscrolledDragDistance = 0.0
    }

    private suspend fun setPosition(value: Double) {
        val rawPosition = if (reverseLayout) {
            trackSize - thumbSize - value
        } else {
            value
        }
        adapter.scrollTo(rawPosition / scrollScale)
    }

    private val dragMutex = Mutex()

    /** Called on every movement while dragging the thumb */
    fun onDragDelta(offset: Offset) {
        coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
            // Mutex is used to ensure that all earlier drag deltas were applied
            // before calculating a new raw position
            dragMutex.withLock {
                val dragDelta = if (isVertical) offset.y else offset.x
                val maxScrollPosition = adapter.maxScrollOffset * scrollScale
                val currentPosition = position
                val targetPosition =
                    (currentPosition + dragDelta + unscrolledDragDistance).coerceIn(
                        0.0,
                        maxScrollPosition
                    )
                val sliderDelta = targetPosition - currentPosition

                // Have to add to position for smooth content scroll if the items are of different size
                val newPos = position + sliderDelta
                setPosition(newPos)
                unscrolledDragDistance += dragDelta - sliderDelta
            }
        }
    }

    /** Called on every movement while dragging the thumb */
    fun onDragRaw(raw: Offset) {
        coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
            // Mutex is used to ensure that all earlier drag deltas were applied
            // before calculating a new raw position
            dragMutex.withLock {
                val maxScrollPosition = adapter.maxScrollOffset * scrollScale
                val currentPosition = position
                val targetPosition =
                    ((if (isVertical) raw.y else raw.x).toDouble() - thumbSize / 2.0).coerceIn(
                        0.0,
                        maxScrollPosition
                    )
                val sliderDelta = targetPosition - currentPosition

                // Have to add to position for smooth content scroll if the items are of different size
                val newPos = position + sliderDelta
                setPosition(newPos)
            }
        }
    }
}

val ScrollbarAdapter.maxScrollOffset: Double
    get() = (contentSize - viewportSize).coerceAtLeast(0.0)
