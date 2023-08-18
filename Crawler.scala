import $file.FetchLinks
import FetchLinks._

import java.util.concurrent.Executors
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.{Await, ExecutionContext, Future}

implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
def fetchAllLinksParallel(startTitle: String, depth: Int): Set[String] = {

  var seen    = Set(startTitle)
  var current = Set(startTitle)

  for (i <- Range(0, depth)) {
    val futures       = for (title <- current) yield Future{ fetchLinks(title)}
    val nextTitleList = futures.map(Await.result(_, Inf))
    current = nextTitleList.flatten.filter(!seen.contains(_))
    seen = seen ++ current
  }
  seen
}

def fetchAllLinks(startTitle: String, depth: Int): Set[String] = {

  var seen    = Set(startTitle)
  var current = Set(startTitle)

  for (i <- Range(0, depth)) {
    val nextTitleList = for (title <- current) yield fetchLinks(title)
    current = nextTitleList.flatten.filter(!seen.contains(_))
    seen = seen ++ current
  }
  seen
}

def fetchAllLinksRec(startTitle: String, depth: Int): Set[String] = {

  def rec(current: Set[String], seen: Set[String], recDepth: Int): Set[String] = {
    if (recDepth >= depth) seen
    else {
      val futures = for (title <- current) yield Future{ fetchLinks(title) }
      val nextTitles = futures.map(Await.result(_, Inf)).flatten
      rec(nextTitles.filter(!seen.contains(_)), seen ++ nextTitles, recDepth + 1)
    }
  }
  rec(Set(startTitle), Set(startTitle), 0)
}