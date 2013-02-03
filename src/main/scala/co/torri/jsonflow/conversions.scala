package co.torri.jsonflow

import com.fasterxml.jackson.databind.JsonNode
import scala.collection.JavaConversions._
import scala.language.implicitConversions


trait conversions {

    implicit val     intConverter : NodeConverter[Int] =     n => if (n.isNumber) Some(n.asInt) else None
    implicit val    longConverter : NodeConverter[Long] =    n => if (n.isNumber) Some(n.asLong) else None
    implicit val  bigIntConverter : NodeConverter[BigInt] =  n => if (n.isNumber) Some(n.bigIntegerValue) else None
    implicit val  doubleConverter : NodeConverter[Double] =  n => if (n.isFloatingPointNumber || n.isNumber) Some(n.asDouble) else None
    implicit val booleanConverter : NodeConverter[Boolean] = n => if (n.isBoolean) Some(n.asBoolean) else None
    implicit val  stringConverter : NodeConverter[String] =  n => if (n.isTextual) Some(n.asText) else None

    implicit def node2int(n: Node) : Int = n.as[Int].get

    implicit def seqConverter[T](implicit convert: NodeConverter[T]) : NodeConverter[Seq[T]] = n => if (n.isArray) Some(n.iterator.flatMap(convert(_)).toSeq) else None
    //implicit def listConverter[T](implicit convert: NodeConverter[T]) : NodeConverter[List[T]] = n => seqConverter(convert)(n).map(_.toList)
    //Use ClassTag: implicit def arrayConverter[T](implicit convert: NodeConverter[T]) : NodeConverter[Array[T]] = n => seqConverter(convert)(n).map(_.toArray)
    implicit def nodeMapConverter[T] : NodeConverter[Map[String, Node]] = n => if (n.isObject) Some(n.fieldNames.map(f => (f, WrappedNode(n.get(f)))).toMap) else None
    implicit def mapConverter[T](implicit convert: NodeConverter[T]) : NodeConverter[Map[String, T]] = n => if (n.isObject) Some(n.fieldNames.map(f => (f, convert(n.get(f)).get)).toMap) else None

    implicit def objectConverter[T](implicit m: Manifest[T]) : NodeConverter[T] = n => if (n.isObject) Some(mapper.readValue(n.toString, m.runtimeClass).asInstanceOf[T]) else None


    implicit def node2type[T : NodeConverter](n: Node) : T = n.as[T].get
}

object conversions extends conversions