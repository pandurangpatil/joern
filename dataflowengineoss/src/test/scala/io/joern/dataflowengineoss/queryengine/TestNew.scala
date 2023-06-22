package io.joern.dataflowengineoss.queryengine

import io.joern.dataflowengineoss.language._
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.generated.nodes.{CfgNode, StoredNode}
import io.shiftleft.semanticcpg.language._
import overflowdb.traversal.Traversal

import java.math.BigInteger
import java.security.MessageDigest
import java.util.Calendar
import scala.collection.mutable.ListBuffer

object TestNew extends App {
  implicit val engineContext: EngineContext =
    EngineContext(config = EngineConfig(4))

  val cpg = Cpg.withStorage("/Users/pandurang/projects/code-scan/temp/privado-core/cpg.bin")
//  val cpg = Cpg.withStorage("/Users/pandurang/projects/joern-perf/privado-accounts-api-joern-1.1.1359.cpg.bin")
//  val cpg = Cpg.withStorage("/Users/pandurang/projects/joern-perf/privado-accounts-api-joern-1.1.1146-new.cpg.bin")
//  val cpg = Cpg.withStorage("/Users/pandurang/projects/joern-perf/Library-Assistant-joern-1.1.1359.bin")
//  val cpg = Cpg.withStorage("/Users/pandurang/projects/joern-perf/Library-Assistant-joern-1.1.1146.bin")
//  cpg.call("<operator>.alloc").groupBy(_.name).map { case (k, v) => k -> v.map(_.signature).l }.foreach(println)
//  println(cpg.call("<operator>.alloc").head.callee(NoResolve).size)
  val sinks   = getSinks
  val sources = getSources
  println(s" ${Calendar.getInstance().getTime} started finding flows ..")
  val paths         = sinks.reachableByFlows(sources).l
  val outPutPath    = ListBuffer[List[String]]()
  val outputPathIds = ListBuffer[String]()
  paths.foreach(path => {
    var pathIds     = ListBuffer[String]()
    var someNewList = ListBuffer[String]()
    path.elements.foreach(element => {
      pathIds += s"${element.id()}"
      someNewList.addOne(s"${element.id()} - ${element.code} - ${element.file.name.headOption
          .getOrElse("<empty>")} - ${element.lineNumber.getOrElse("NON")}")
    })
    outputPathIds.addOne(pathIds.toList.mkString("->"))
    outPutPath.addOne(someNewList.toList)
  })
  for (i <- 0 to outPutPath.size - 1) {
    println(outputPathIds(i))
    outPutPath(i).foreach(step => {
      println(step)
    })
    println("\n\n\n --- ")

  }
  //  outPutPath.foreach(path => {
  //    println(path)
  //  })

  println(s" ${Calendar.getInstance().getTime}its done. size - ${paths.size}")

  def getSources: List[CfgNode] = {
    def filterSources(traversal: Traversal[StoredNode]) = {
      traversal.tag
        .nameExact("catLevelOne")
        .or(_.valueExact("sources"), _.valueExact("DerivedSources"))
    }
    cpg.literal
      .where(filterSources)
      .l ++ cpg.identifier
      .where(filterSources)
      .l ++ cpg.call
      .where(filterSources)
      .l ++ cpg.argument.isFieldIdentifier.where(filterSources).l

  }

  def getSinks: List[CfgNode] = {
    cpg.call.where(_.tag.nameExact("catLevelOne").valueExact("sinks")).l
  }
}
