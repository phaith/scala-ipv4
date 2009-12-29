package be.jvb.datatypes

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
@RunWith(classOf[JUnitRunner])
class MacAddressTest extends FunSuite with Checkers {
  test("create from string 0.0.0.0.0.0") {
    val address = new MacAddress("0.0.0.0.0.0")
    assert(0 === address.value)
  }

  test("create from null string") {
    intercept[IllegalArgumentException] {
      new MacAddress(null:String)
    }
  }

  test("create from invalid string") {
    intercept[IllegalArgumentException] {
      new MacAddress("something.invalid")
    }
  }

  test("create from invalid length string") {
    intercept[IllegalArgumentException] {
      new MacAddress("0.1.2.3")
    }
  }

  test("create from string FF.FF.FF.FF.FF.FF") {
    val address = new MacAddress("FF.FF.FF.FF.FF.FF")
    assert(Math.pow(2, 48).toLong - 1 === address.value)
  }

  test("individual bytes are padded with zeros when required") {
    assert("01.AB.0F.12.34.06" === new MacAddress("01.AB.0F.12.34.06").toString)
  }

  test("constructing from string without padding is ok") {
    assert(new MacAddress("01.AB.0F.12.34.06") === new MacAddress("1.AB.F.12.34.6"))
  }

  test("create from long too large") {
    intercept[IllegalArgumentException] {
      new MacAddress(Math.pow(2, 48).toLong)
    }
  }

  test("create from long too small") {
    intercept[IllegalArgumentException] {
      new MacAddress(-1)
    }
  }

  test("to and from string conversion 1F.AB.CD.99.34.23") {
    val address = new MacAddress("1F.AB.CD.99.34.23")
    assert("1F.AB.CD.99.34.23" === address.toString())
  }

  test("to and from string conversion FF.FF.FF.FF.FF.FF") {
    val address = new MacAddress("FF.FF.FF.FF.FF.FF")
    assert("FF.FF.FF.FF.FF.FF" === address.toString())
  }

  test("addition") {
    assert(new MacAddress("01.02.03.04.05.06") === new MacAddress("01.02.03.04.05.05") + 1)
  }

  test("substraction") {
    assert(new MacAddress("01.02.03.04.05.06") === new MacAddress("01.02.03.04.05.07") - 1)
  }

  test("greather then") {
    assert(new MacAddress("1.2.3.4.5.6") > new MacAddress("1.2.3.0.0.0"))
    assert(new MacAddress("FF.FF.FF.FF.FF.FF") > new MacAddress("1.2.3.0.0.0"))
    assert(new MacAddress("FF.FF.FF.FF.FF.FF") > new MacAddress("0.0.0.0.0.0"))
  }

  test("greather then or equals") {
    assert(new MacAddress("1.2.3.4.5.6") >= new MacAddress("1.2.3.0.0.0"))
    assert(new MacAddress("1.2.3.4.5.6") >= new MacAddress("1.2.3.4.5.6"))
    assert(new MacAddress("FF.FF.FF.FF.FF.FF") >= new MacAddress("FF.FF.FF.FF.FF.FF"))
    assert(new MacAddress("FF.FF.FF.FF.FF.FF") >= new MacAddress("0.0.0.0.0.0"))
  }

}