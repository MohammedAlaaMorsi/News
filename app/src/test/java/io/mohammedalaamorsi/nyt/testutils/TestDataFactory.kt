package io.mohammedalaamorsi.nyt.testutils

import io.mohammedalaamorsi.nyt.data.models.Media
import io.mohammedalaamorsi.nyt.data.models.MediaMetadata
import io.mohammedalaamorsi.nyt.data.models.MostPopularApiResponse
import io.mohammedalaamorsi.nyt.data.models.Result

/**
 * Test data factory for creating mock objects used in unit tests
 */
object TestDataFactory {

    fun createMockMediaMetadata(
        format: String = "mediumThreeByTwo210",
        height: Int = 140,
        url: String = "https://static01.nyt.com/images/test.jpg",
        width: Int = 210
    ): MediaMetadata {
        return MediaMetadata(
            format = format,
            height = height,
            url = url,
            width = width
        )
    }

    fun createMockMedia(
        approvedForSyndication: Int = 1,
        caption: String = "Test caption",
        copyright: String = "The New York Times",
        mediaMetadata: List<MediaMetadata> = listOf(createMockMediaMetadata()),
        subtype: String = "photo",
        type: String = "image"
    ): Media {
        return Media(
            approvedForSyndication = approvedForSyndication,
            caption = caption,
            copyright = copyright,
            mediaMetadata = mediaMetadata,
            subtype = subtype,
            type = type
        )
    }

    fun createMockResult(
        id: Long = 1L,
        title: String = "Test Article Title",
        abstract: String = "Test article abstract",
        byline: String = "By Test Author",
        section: String = "Technology",
        publishedDate: String = "2025-01-01",
        url: String = "https://www.nytimes.com/test",
        media: List<Media> = listOf(createMockMedia()),
        assetId: Long = id,
        adxKeywords: String = "test, news",
        column: String? = null,
        desFacet: List<String> = listOf("test"),
        etaId: Int = 1,
        geoFacet: List<String> = listOf("US"),
        nytdsection: String = "Test Section",
        orgFacet: List<String> = listOf("Test Org"),
        perFacet: List<String> = listOf("Test Person"),
        source: String = "The New York Times",
        subsection: String = "",
        type: String = "Article",
        updated: String = "2025-01-01 12:00:00",
        uri: String = "nyt://article/test$id"
    ): Result {
        return Result(
            abstract = abstract,
            adxKeywords = adxKeywords,
            assetId = assetId,
            byline = byline,
            column = column,
            desFacet = desFacet,
            etaId = etaId,
            geoFacet = geoFacet,
            id = id,
            media = media,
            nytdsection = nytdsection,
            orgFacet = orgFacet,
            perFacet = perFacet,
            publishedDate = publishedDate,
            section = section,
            source = source,
            subsection = subsection,
            title = title,
            type = type,
            updated = updated,
            uri = uri,
            url = url
        )
    }

    fun createMockApiResponse(
        results: List<Result> = listOf(createMockResult()),
        numResults: Int = results.size,
        copyright: String = "Copyright (c) 2025 The New York Times Company.",
        status: String = "OK"
    ): MostPopularApiResponse {
        return MostPopularApiResponse(
            copyright = copyright,
            numResults = numResults,
            results = results,
            status = status
        )
    }

    fun createMockResultList(count: Int): List<Result> {
        return (1..count).map { index ->
            createMockResult(
                id = index.toLong(),
                title = "Test Article $index",
                abstract = "Test abstract for article $index",
                byline = "By Test Author $index",
                section = if (index % 2 == 0) "Technology" else "Sports",
                publishedDate = "2025-01-${String.format("%02d", index)}"
            )
        }
    }

}
