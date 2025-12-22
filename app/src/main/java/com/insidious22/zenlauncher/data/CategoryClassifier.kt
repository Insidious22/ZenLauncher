package com.insidious22.zenlauncher.data

import com.insidious22.zenlauncher.domain.AppModel
import com.insidious22.zenlauncher.domain.Category

class CategoryClassifier {

    fun categoryFor(app: AppModel): Set<Category> {
        val pkg = app.packageName.lowercase()
        val name = app.label.lowercase()

        val result = mutableSetOf<Category>()

        if (isWork(pkg, name)) result += Category.WORK
        if (isMedia(pkg, name)) result += Category.MEDIA

        return result
    }

    private fun isWork(pkg: String, name: String): Boolean {
        val workPkgs = listOf(
            "com.google.android.apps.docs",
            "com.google.android.apps.docs.editors",
            "com.google.android.apps.docs.editors.docs",
            "com.google.android.apps.docs.editors.sheets",
            "com.google.android.apps.docs.editors.slides",
            "com.google.android.gm",
            "com.google.android.apps.meetings",
            "com.google.android.keep",
            "com.google.android.calendar",
            "com.google.android.apps.drive",
            "com.microsoft.office",
            "com.microsoft.office.word",
            "com.microsoft.office.excel",
            "com.microsoft.office.powerpoint",
            "com.microsoft.teams",
            "us.zoom.videomeetings",
            "com.slack",
            "com.discord",
            "com.notion",
            "com.github.android",
            "com.atlassian"
        )

        if (workPkgs.any { pkg.startsWith(it) }) return true

        val keywords = listOf(
            "docs", "sheets", "slides", "drive", "meet", "calendar", "keep",
            "word", "excel", "powerpoint", "teams", "zoom", "slack", "notion",
            "github", "jira", "trello"
        )
        return keywords.any { name.contains(it) }
    }

    private fun isMedia(pkg: String, name: String): Boolean {
        val mediaPkgs = listOf(
            "com.google.android.youtube",
            "com.google.android.youtube.music",
            "com.spotify.music",
            "com.netflix.mediaclient",
            "com.amazon.avod.thirdpartyclient",
            "com.disney.disneyplus",
            "com.hbo.hbonow",
            "tv.twitch.android.app",
            "com.soundcloud.android",
            "com.mxtech.videoplayer",
            "org.videolan.vlc"
        )

        if (mediaPkgs.any { pkg.startsWith(it) }) return true

        val keywords = listOf(
            "music", "música", "spotify", "video", "vídeo", "youtube", "netflix",
            "prime", "disney", "hbo", "twitch", "vlc", "galería", "gallery",
            "fotos", "photos", "player", "reproductor", "podcast"
        )
        return keywords.any { name.contains(it) }
    }
}
