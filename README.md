
val json = """{
	a: 1
	b: {
		c: "ola"
		d: 3.14,
		e: [1,2,3,4]
		f: {}
		g: {text: "oi"}
	}
}""".json

case class Salute(text: String)

val a = json.a.as[Int] // Some[Int]

val x = json.b.f.x.as[String] // None

val b = json.b.as[Map[String, _]]

val g = json.b.g.as[Salute] // Some[Salute]

val e1 = json.b.e(1).as[Int]