scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.seleniumhq.webdriver" % "webdriver-htmlunit" % "0.9.7376"
  )

initialCommands in console := """
import com.github.haretaro.blogscraper._
"""
