package io.mohammedalaamorsi.nyt.data.remote



interface UrlsProvider {
    //("https://api.nytimes.com/svc/mostpopular/v2/viewed/7.json?api-key=sample-key")
    fun getPopularNews(daysPeriod: Int): String

}
