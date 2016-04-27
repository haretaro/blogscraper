/**
 * はてなダイアリーから文章をたくさん引っ張ってくるプログラム
 */
package com.github.haretaro.blogscraper

import java.io.File
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import scala.collection.JavaConversions._
import scala.sys.process._

object Main extends App{
  val output = "output.txt"

  hotEntries
    .map(wait(authorOf(_)))
    .map(wait(allPagesOf(_)))
    .flatten
    .map(page => {
      println(page)
      wait(scrapeBlog(page))
    })
    .foreach("echo %s".format(_) #>> new File(output)!)

  /**
   * ページのURLから本文を抜き出す
   */
  def scrapeBlog(url: String): String = {
    val driver = new HtmlUnitDriver()
    driver.get(url)
    val content = driver.findElementByXPath("//div[@class='section']")
    content.getText().replaceAll("Permalink.+\\Z","")
  }

  /**
   * ホットエントリーのURLを返す
   */
  def hotEntries: Seq[String] = {
    val url = "http://b.hatena.ne.jp/hotentry/diary"
    val driver = new HtmlUnitDriver()
    driver.get(url)
    val contents = driver.findElementsByXPath("//a[@class='entry-link']")
    val urls = contents.map(_.getAttribute("href"))
    urls.distinct
  }

  /**
   * {username} の書いたブログ記事のURLのリストを返す
   */
  def allPagesOf(username: String): Seq[String] = {
    val url = "http://d.hatena.ne.jp/"+username+"/archive"
    val driver = new HtmlUnitDriver()
    driver.get(url)
    val pages = driver.findElementsByXPath("//li[@class='archive archive-section']/a")
    pages.map(_.getAttribute("href"))
      .filter(_.matches("http://d.hatena.ne.jp/.+"))
  }

  /**
   * ページURLからユーザー名を抜き出す
   */
  def authorOf(url: String): String = {
    val pattern = ".+d.hatena.ne.jp/(.+)/2.+\\Z".r
    val pattern(name) = url
    name
  }

  /**
   * 100ms 待つ恒等写像
   */
  def wait[K](in:K): K = {
    Thread.sleep(100)
    in
  }
}
