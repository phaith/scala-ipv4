package be.jvb.iptypes

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers
import org.scalacheck.{Arbitrary, Gen, Prop}
import java.net.InetAddress

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
@RunWith(classOf[JUnitRunner])
class IpAddressTest extends FunSuite with Checkers {
  test("create from string 0.0.0.0") {
    val address = new IpAddress("0.0.0.0")
    assert(0 === address.value)
  }

  test("create from null string") {
    intercept[IllegalArgumentException] {
      new IpAddress(null:String)
    }
  }

  test("create from null InetAddress") {
    intercept[IllegalArgumentException] {
      new IpAddress(null:InetAddress)
    }
  }

  test("create from invalid string") {
    intercept[IllegalArgumentException] {
      new IpAddress("something.invalid")
    }
  }

  test("create from invalid length string") {
    intercept[IllegalArgumentException] {
      new IpAddress("0.1.2.3.5.6")
    }
  }

  test("create from string 255.255.255.255") {
    val address = new IpAddress("255.255.255.255")
    assert(Math.pow(2, 32).toLong - 1 === address.value)
  }

  test("create from invalid string 0.0.0.500") {
    intercept[IllegalArgumentException] {
      new IpAddress("0.0.0.500")
    }
  }

  test("create from long too large") {
    intercept[IllegalArgumentException] {
      new IpAddress(Math.pow(2, 32).toLong)
    }
  }

  test("create from long too small") {
    intercept[IllegalArgumentException] {
      new IpAddress(-1)
    }
  }

  test("to and from string conversion 192.168.254.115") {
    val address = new IpAddress("192.168.254.115")
    assert("192.168.254.115" === address.toString())
  }

  test("to and from string conversion 255.255.255.255") {
    val address = new IpAddress("255.255.255.255")
    assert("255.255.255.255" === address.toString())
  }

  test("addition") {
    assert(new IpAddress("1.2.3.4") === new IpAddress("1.2.3.3") + 1)
    assert(new IpAddress("1.2.4.4") === new IpAddress("1.2.3.10") + 250)
  }

  test("substraction") {
    assert(new IpAddress("1.2.3.4") === new IpAddress("1.2.3.5") - 1)
  }

  test("addition and substraction inverse each other") {
    assert(new IpAddress("1.2.3.4") - 1 + 1 === new IpAddress("1.2.3.4"))
    assert(new IpAddress("1.2.3.4") + 345 - 345 === new IpAddress("1.2.3.4"))
  }

  test("addition overflow") {
    assert(new IpAddress("0.0.0.0") === new IpAddress("255.255.255.255") + 1)
  }

  test("substraction underflow") {
    assert(new IpAddress("255.255.255.255") === new IpAddress("0.0.0.0") - 1)
  }

  test("greather then") {
    assert(new IpAddress("1.2.3.4") > new IpAddress("1.2.3.0"))
    assert(new IpAddress("255.255.255.255") > new IpAddress("1.2.3.0"))
    assert(new IpAddress("255.255.255.255") > new IpAddress("0.0.0.0"))
  }

  test("greather then or equals") {
    assert(new IpAddress("1.2.3.4") >= new IpAddress("1.2.3.0"))
    assert(new IpAddress("1.2.3.4") >= new IpAddress("1.2.3.4"))
    assert(new IpAddress("255.255.255.255") >= new IpAddress("255.255.255.255"))
    assert(new IpAddress("255.255.255.255") >= new IpAddress("0.0.0.0"))
  }

  test("construct from InetAddress") {
    expect(new IpAddress(InetAddress.getByName("1.2.230.240"))) {new IpAddress("1.2.230.240")}
    expect(new IpAddress(InetAddress.getLocalHost)) {new IpAddress("127.0.0.1")}
  }

  def ipAddressGenerator: Gen[IpAddress] = {
    for{
      value <- Gen.choose(0L, 0xFFFFFFFFL)
    } yield new IpAddress(value)
  }

  implicit def arbitraryIpAddress: Arbitrary[IpAddress] = Arbitrary[IpAddress](ipAddressGenerator)

  def ipAddressAsString: Gen[String] = {
    for{
      octets <- Gen.vectorOf(4, Gen.choose(0, 255))
    } yield octets.mkString(".")
  }

  def minIpAddressAsString: Gen[String] = {
    for{
      string <- Gen.vectorOf(4, Gen.elements(0))
    } yield string.mkString(".")
  }

  def maxIpAddressAsString: Gen[String] = {
    for{
      string <- Gen.vectorOf(4, Gen.elements(255))
    } yield string.mkString(".")
  }

  def ipAddressAsStringWithCornerCases: Gen[String] = {
    Gen.oneOf(ipAddressAsString, minIpAddressAsString, maxIpAddressAsString)
  }

  test("plus and minus inverse each other") {
    check((address: IpAddress) => address == address + 1 - 1)
  }

  test("toString yields the same string it was constructed with") {
    check(Prop.forAll(ipAddressAsStringWithCornerCases)(address => address == new IpAddress(address).toString))
  }

}