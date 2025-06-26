package io.mohammedalaamorsi.nyt.testutils

import io.mohammedalaamorsi.nyt.data.models.Media
import io.mohammedalaamorsi.nyt.data.models.MediaMetadata
import io.mohammedalaamorsi.nyt.data.models.Result

/**
 * Test data factory for creating mock objects used in UI tests
 */
object TestDataFactory {
    fun createMockMediaMetadata(
        format: String = "mediumThreeByTwo210",
        height: Int = 140,
        url: String = "https://static01.nyt.com/images/test.jpg",
        width: Int = 210,
    ): MediaMetadata {
        return MediaMetadata(
            format = format,
            height = height,
            url = url,
            width = width,
        )
    }

    fun createMockMedia(
        approvedForSyndication: Int = 1,
        caption: String = "Test caption",
        copyright: String = "The New York Times",
        mediaMetadata: List<MediaMetadata> = listOf(createMockMediaMetadata()),
        subtype: String = "photo",
        type: String = "image",
    ): Media {
        return Media(
            approvedForSyndication = approvedForSyndication,
            caption = caption,
            copyright = copyright,
            mediaMetadata = mediaMetadata,
            subtype = subtype,
            type = type,
        )
    }

    fun createMockResult(
        id: Long = 1L,
        title: String = "Test Article Title",
        abstract: String = "Test article abstract",
        byline: String = "By Test Author",
        section: String = "Technology",
        publishedDate: String = "2023-10-15",
        updated: String = "2023-10-15",
        uri: String = "nyt://article/test-id",
        url: String = "https://www.nytimes.com/test-article",
        adxKeywords: String = "Technology;Test",
        assetId: Long = 100000000000000L,
        etaId: Int = 1,
        nytdsection: String = "technology",
        column: String? = null,
        desFacet: List<String> = emptyList(),
        geoFacet: List<String> = emptyList(),
        media: List<Media> = listOf(createMockMedia()),
        orgFacet: List<String> = emptyList(),
        perFacet: List<String> = emptyList(),
        source: String = "New York Times",
        subsection: String = "",
        type: String = "Article",
    ): Result {
        return Result(
            id = id,
            title = title,
            abstract = abstract,
            byline = byline,
            section = section,
            publishedDate = publishedDate,
            updated = updated,
            uri = uri,
            url = url,
            adxKeywords = adxKeywords,
            assetId = assetId,
            etaId = etaId,
            nytdsection = nytdsection,
            column = column,
            desFacet = desFacet,
            geoFacet = geoFacet,
            media = media,
            orgFacet = orgFacet,
            perFacet = perFacet,
            source = source,
            subsection = subsection,
            type = type,
        )
    }

    fun createMockResultList(size: Int = 3): List<Result> {
        return (1..size).map { index ->
            createMockResult(
                id = index.toLong(),
                title = "Test Article Title $index",
                abstract = "Test article abstract for article number $index",
                byline = "By Test Author $index",
                section = if (index % 2 == 0) "Technology" else "Science",
                publishedDate = "2023-10-${15 + index}",
                assetId = 100000000000000L + index,
                etaId = index,
            )
        }
    }
}
