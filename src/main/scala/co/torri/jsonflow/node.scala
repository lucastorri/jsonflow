package co.torri.jsonflow

import com.fasterxml.jackson.databind.JsonNode
import scala.language.dynamics


//TODO make it a Seq, able to use foreach/map/etc and use in for loops
trait Node extends Dynamic {

    def isDeclared : Boolean

    def selectDynamic[T](field: String) : Node

    def applyDynamic(field: String)(args: Any*) : Node

    def as[T](implicit convert: NodeConverter[T]) : Option[T]
}
object Node {
    def apply(src: String) = WrappedNode(mapper.readValue(src, classOf[JsonNode]))
}


object NullNode extends Node {

    def isDeclared = false

    def selectDynamic[T](field: String) : Node = this

    def applyDynamic(field: String)(args: Any*) : Node = this

    def as[T](implicit convert: NodeConverter[T]) : Option[T] = None
}

case class WrappedNode(node: JsonNode) extends Node {

    def isDeclared = true

    def selectDynamic[T](field: String) : Node = option(node.get(field))

    def applyDynamic(field: String)(args: Any*) : Node = args.toList match {
        case Nil => selectDynamic(field)
        case (i: Int) :: Nil => option(node.get(field).path(i))
        case _ => NullNode
    }

    @inline
    private def option(jn: JsonNode) = jn match {
        case null => NullNode
        case n => WrappedNode(n)
    }

    def as[T](implicit convert: NodeConverter[T]) = convert(node)

}