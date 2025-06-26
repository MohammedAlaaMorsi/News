package io.mohammedalaamorsi.nyt.data.remote

import io.mohammedalaamorsi.nyt.BuildConfig


class NewsUrlProvider : UrlsProvider {

    override fun getPopularNews(daysPeriod: Int): String {
        return "https://api.nytimes.com/svc/mostpopular/v2/viewed/${daysPeriod}.json?api-key=${BuildConfig.NYTIMES_API_KEY}"
    }

}
