package co.torri

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import co.torri.jsonflow.conversions

package object jsonflow extends conversions {
	private[jsonflow] val mapper = new ObjectMapper
	mapper.registerModule(DefaultScalaModule)

	type NodeConverter[T] = JsonNode => Option[T]
}