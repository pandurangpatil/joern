package io.joern.dataflowengineoss.queryengine

//import org.zeromq.{SocketType, ZContext}
//
//import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

case class User(fname: String, lname: String)

object JeromqTest extends App {
//
//  val context: ZContext = new ZContext(1)
//  val subThread = new Thread {
//    override def run {
//      val subscriber        = context.createSocket(SocketType.SUB)
//      subscriber.bind("inproc://sampletopic")
//      subscriber.subscribe("B".getBytes)
//
//      var done = false
//      while ( !done) {
//        val address = subscriber.recvStr()
//        val stream = subscriber.recv()
//        val us = deserialise(stream)
//        println("Subscriber1 " + us)
//        if ("Hello JeroMQ" == address)
//          done = true
//      }
//      subscriber.close()
//    }
//  }
//  subThread.start
//
//  val pubThread = new Thread {
//    override def run {
//      val publisher         = context.createSocket(SocketType.PUB)
//      publisher.connect("inproc://sampletopic")
//      var i =0
//      var topic :String = "B"
//      for ( i <- 0 until 10) {
//        publisher.sendMore(topic)
//        val us = new User(fname = "fname" + i, lname = "lname")
//        publisher.send(serialise(us))
//        println(s"sent ${us}" )
//        topic="A"
//      }
//      publisher.close()
//
//    }
//  }
//  pubThread.start
//  subThread.join()
//  context.close()
//
//  def serialise(value: Any): Array[Byte] = {
//    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
//    val oos                           = new ObjectOutputStream(stream)
//    oos.writeObject(value)
//    oos.close()
//    stream.toByteArray
//  }
//
//  def deserialise(bytes: Array[Byte]): Any = {
//    val ois   = new ObjectInputStream(new ByteArrayInputStream(bytes))
//    val value = ois.readObject
//    ois.close()
//    value
//  }

}
