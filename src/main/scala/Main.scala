package com.github.haretaro.blogscraper

import java.io.File
import java.net.URL
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import scala.collection.JavaConversions._
import scala.sys.process._

object Main extends App{
  println("hello")
  val text = scrape("http://d.hatena.ne.jp/okachimachiorz/20160424/1461498575")
  println(text)

  def scrape(url: String): String = {
    val driver = new HtmlUnitDriver()
    driver.get(url)
    val content = driver.findElementByXPath("//div[@class='section']/p[2]")
    content.getText()
  }
}
