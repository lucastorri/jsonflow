package co.torri.jsonflow

import com.fasterxml.jackson.annotation._
import org.specs2.mutable._


class JSONFlowSpec extends Specification {

	"jsonflow".title

	val node = Node("""
		{
			"anInt": 1, 
			"anObj": {
				"anotherInt": 2,
				"anotherObj": {
					"string": "hello world"
				}
			}, 
			"anArray": [1,2,3,4],
			"aBoolean": true,
			"aDouble": 3.14,
            "simpleObj": {
                "key": "lucky",
                "value": 7
            },
            "intObj": {
                "a": 1,
                "b": 2,
                "c": 3
            }
		}
	""")

	"reaches fields inside an outer object" in {
		node.anInt.as[Int] must beSome(1)
	}

	"reaches fields inside an inner object" in {
		node.anObj.anotherInt.as[Int] must beSome(2)
	}

	"doesn't complain about reaching invalid paths" in {
		node.invalidField.as[Int] must beNone
		
		node.invalidField.anotherField.as[Int] must beNone

		node.anObj.invalidField.as[Int] must beNone
	}

    "reaches strings" in {
        node.anObj.anotherObj.string.as[String] must beSome("hello world")
    }

	"reaches ints" in {
		node.anInt.as[Int].get must beEqualTo(1)
	}

	"reaches longs" in {
		node.anInt.as[Long].get must beEqualTo(1L)	
	}

	"reaches big ints" in {
		node.anInt.as[BigInt].get must beEqualTo(BigInt(1))
	}

	"reaches doubles" in {
		node.aDouble.as[Double].get must beEqualTo(3.14)
	}

	"reaches booleans" in {
		node.aBoolean.as[Boolean].get must beEqualTo(true)
	}

	"reaches arrays" in {
		node.anArray.as[Seq[Int]] must beSome(Seq(1, 2, 3, 4))
	}

	"reaches array elements" in {
		node.anArray(0).as[Int] must beSome(1)
		node.anArray(1).as[Int] must beSome(2)
		node.anArray(2).as[Int] must beSome(3)
		node.anArray(3).as[Int] must beSome(4)
	}

    "converts to POJOs" in {
        node.simpleObj.as[SimpleObj] must beSome(SimpleObj("lucky", 7))
    }

    "converts to maps of basic types" in {
        node.intObj.as[Map[String, Int]] must beSome(Map("a" -> 1, "b" -> 2, "c" -> 3))
    }

    "converts to maps of Node" in {
        val m = node.anObj.as[Map[String, Node]].get

        m("anotherInt").as[Int] must beSome(2)
        m("anotherObj").string.as[String] must beSome("hello world")
    }

	"converts ints automatically" in {
		val i: Int = node.anInt
		i must beEqualTo(1)
	}

}

case class SimpleObj(val key: String, val value: Int)
