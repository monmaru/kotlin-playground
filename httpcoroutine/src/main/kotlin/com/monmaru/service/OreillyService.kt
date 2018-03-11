package com.monmaru.service

import com.monmaru.model.BookCatalog
import com.monmaru.model.EBook
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class OreillyService {

    private val baseURL = "https://www.oreilly.co.jp/"

    private fun String.asDateString(): String = this.replace("""(.*?)-(.*?)-(.*?)T.*""".toRegex(), """$1/$2/$3""")

    fun fetchBookCatalog(): Iterable<BookCatalog> {
        val doc = Jsoup.connect("${baseURL}catalog/soon.xml").parser(Parser.xmlParser()).get()
        return doc.select("item").map {
            val content = it.selectFirst("content|encoded").text()
            val matcher = """img src="(.*)" """.toRegex().toPattern().matcher(content)
            val imageUrl = if (matcher.find()) matcher.group(1) else ""
            BookCatalog(
                    title = it.selectFirst("title").text(),
                    link = it.selectFirst("link").text(),
                    imageUrl = imageUrl,
                    creator = it.selectFirst("dc|creator").text().trim(),
                    date = it.selectFirst("dc|date").text().trim().asDateString())
        }
    }

    fun fetchNewEBooks(): Iterable<EBook> {
        val doc = Jsoup.connect("${baseURL}ebook/new_release.atom").parser(Parser.xmlParser()).get()
        return doc.select("feed > entry").map {
            val content = it.selectFirst("summary").text()
            val matcher = """img src="(.*)" class=""".toRegex().toPattern().matcher(content)
            val imageUrl = if (matcher.find()) matcher.group(1) else ""
            EBook(
                    title = it.selectFirst("title").text(),
                    link = it.selectFirst("link").attr("href"),
                    imageUrl = imageUrl,
                    updated = it.selectFirst("updated").text().trim().asDateString())
        }
    }

}
