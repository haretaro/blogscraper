/**
 * はてなダイヤリーから文章をたくさん引っ張ってくるプログラム
 */
package com.github.haretaro.blogscraper

import java.io.File
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import scala.collection.JavaConversions._
import scala.sys.process._

object Main extends App{
  val output = "output.txt"

  hotEntries.map(scrapeBlog)
    .foreach("echo %s".format(_) #>> new File(output)!)

  def scrapeBlog(url: String): String = {
    println(url)
    val driver = new HtmlUnitDriver()
    driver.get(url)
    val content = driver.findElementByXPath("//div[@class='section']")
    Thread.sleep(100)
    content.getText().replaceAll("Permalink.+\\Z","")
  }

  /**
   * ホットエントリーのURLを返す
   */
  def hotEntries = {
    val url = "http://b.hatena.ne.jp/hotentry/diary"
    val driver = new HtmlUnitDriver()
    driver.get(url)
    val contents = driver.findElementsByXPath("//a[@class='entry-link']")
    val urls = contents.map(_.getAttribute("href"))
    urls.distinct
  }
}
