package com.monmaru.service

import com.monmaru.model.Repository
import org.jsoup.Jsoup

class GitHubService {

    fun fetchRepositories(lang: String): Iterable<Repository> {
        val doc = Jsoup.connect("https://github.com/trending/$lang").get()
        return doc.select(".repo-list li").map {
            val subPath = it.select("h3 a").attr("href")
            val refs = subPath.split("/")
            Repository(
                    owner = refs[1],
                    title = refs[2],
                    url = "https://github.com$subPath",
                    description = it.select("""div[class="py-1"] p""").text().trim(),
                    language = it.select("""span[itemprop="programmingLanguage"]""").text().trim(),
                    star = it.selectFirst("""a[class="muted-link d-inline-block mr-3"]""").text().trim())
        }
    }

}