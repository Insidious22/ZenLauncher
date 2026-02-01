package com.insidious22.zenlauncher

import com.insidious22.zenlauncher.presentation.AnimationConstants
import org.junit.Assert.*
import org.junit.Test

class AnimationConstantsTest {

    @Test
    fun `apps wave range is positive`() {
        assertTrue(AnimationConstants.APPS_WAVE_RANGE_PX > 0)
    }

    @Test
    fun `apps wave translation is positive`() {
        assertTrue(AnimationConstants.APPS_WAVE_TRANSLATION_DP > 0)
    }

    @Test
    fun `alphabet wave range is positive`() {
        assertTrue(AnimationConstants.ALPHABET_WAVE_RANGE_PX > 0)
    }

    @Test
    fun `alphabet wave power is greater than 1`() {
        assertTrue(AnimationConstants.ALPHABET_WAVE_POWER > 1f)
    }

    @Test
    fun `alphabet max scale is greater than 1`() {
        assertTrue(AnimationConstants.ALPHABET_MAX_SCALE > 1f)
    }

    @Test
    fun `alphabet alpha values are in valid range`() {
        assertTrue(AnimationConstants.ALPHABET_MIN_ALPHA >= 0f)
        assertTrue(AnimationConstants.ALPHABET_MIN_ALPHA <= 1f)
        assertTrue(AnimationConstants.ALPHABET_MAX_ALPHA >= 0f)
        assertTrue(AnimationConstants.ALPHABET_MAX_ALPHA <= 1f)
    }

    @Test
    fun `icon cache size is reasonable`() {
        assertTrue(AnimationConstants.ICON_CACHE_SIZE > 0)
        assertTrue(AnimationConstants.ICON_CACHE_SIZE <= 500)
    }
}
