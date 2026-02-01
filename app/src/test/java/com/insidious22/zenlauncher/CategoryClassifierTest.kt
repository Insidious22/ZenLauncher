package com.insidious22.zenlauncher

import com.insidious22.zenlauncher.data.CategoryClassifier
import com.insidious22.zenlauncher.domain.AppModel
import com.insidious22.zenlauncher.domain.Category
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CategoryClassifierTest {

    private lateinit var classifier: CategoryClassifier

    @Before
    fun setUp() {
        classifier = CategoryClassifier()
    }

    @Test
    fun `Gmail is classified as WORK`() {
        val app = AppModel(
            packageName = "com.google.android.gm",
            label = "Gmail",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.WORK))
    }

    @Test
    fun `Google Docs is classified as WORK`() {
        val app = AppModel(
            packageName = "com.google.android.apps.docs",
            label = "Docs",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.WORK))
    }

    @Test
    fun `Slack is classified as WORK`() {
        val app = AppModel(
            packageName = "com.slack",
            label = "Slack",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.WORK))
    }

    @Test
    fun `YouTube is classified as MEDIA`() {
        val app = AppModel(
            packageName = "com.google.android.youtube",
            label = "YouTube",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.MEDIA))
    }

    @Test
    fun `Spotify is classified as MEDIA`() {
        val app = AppModel(
            packageName = "com.spotify.music",
            label = "Spotify",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.MEDIA))
    }

    @Test
    fun `Netflix is classified as MEDIA`() {
        val app = AppModel(
            packageName = "com.netflix.mediaclient",
            label = "Netflix",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.MEDIA))
    }

    @Test
    fun `unknown app returns empty categories`() {
        val app = AppModel(
            packageName = "com.example.unknown",
            label = "Unknown App",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.isEmpty())
    }

    @Test
    fun `app with music in name is classified as MEDIA`() {
        val app = AppModel(
            packageName = "com.example.myapp",
            label = "My Music Player",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.MEDIA))
    }

    @Test
    fun `Microsoft Teams is classified as WORK`() {
        val app = AppModel(
            packageName = "com.microsoft.teams",
            label = "Microsoft Teams",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.WORK))
    }

    @Test
    fun `Zoom is classified as WORK`() {
        val app = AppModel(
            packageName = "us.zoom.videomeetings",
            label = "Zoom",
            icon = null
        )
        val categories = classifier.categoryFor(app)
        assertTrue(categories.contains(Category.WORK))
    }
}
