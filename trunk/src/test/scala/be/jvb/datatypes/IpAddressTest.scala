package be.jvb.datatypes

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.FunSuite
import org.scalatest.prop.{Checkers, PropSuite}
import org.scalacheck.Gen._
import org.scalacheck.Prop._

class IpAddressTest extends PropSuite {

  test("create from string 0.0.0.0"){
    val address = new IpAddress("0.0.0.0")
    assert(0 === address.value)
  }

  test("create from null string"){
    intercept(classOf[IllegalArgumentException]) {
      new IpAddress(null)
    }
  }

  test("create from invalid string"){
    intercept(classOf[IllegalArgumentException]) {
      new IpAddress("something.invalid")
    }
  }

  test("create from string 255.255.255.255"){
    val address = new IpAddress("255.255.255.255")
    assert(Math.pow(2, 32).toLong - 1 === address.value)
  }

  test("create from long too large"){
    intercept(classOf[IllegalArgumentException]) {
      IpAddress(Math.pow(2, 32).toLong)
    }
  }

  test("create from long too small"){
    intercept(classOf[IllegalArgumentException]) {
      IpAddress(-1)
    }
  }

  test("to and from string conversion 192.168.254.115"){
    val address = new IpAddress("192.168.254.115")
    assert("192.168.254.115" === address.toString())
  }

  test("to and from string conversion 255.255.255.255"){
    val address = new IpAddress("255.255.255.255")
    assert("255.255.255.255" === address.toString())
  }

  test("addition") {
    assert(new IpAddress("1.2.3.4") === new IpAddress("1.2.3.3") + 1)
  }

  test("substraction") {
    assert(new IpAddress("1.2.3.4") === new IpAddress("1.2.3.5") - 1)
  }

  test("addition and substraction inverse each other") {
    assert(new IpAddress("1.2.3.4") - 1 + 1 === new IpAddress("1.2.3.4"))
    assert(new IpAddress("1.2.3.4") + 6 - 6 === new IpAddress("1.2.3.4"))
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

  def ipAddressGenerator: Gen[IpAddress] = {
    for {
      value <- Gen.choose(0L, 0xFFFFFFFFL)
    } yield new IpAddress(value)
  }

  implicit def arbitraryIpAddress: Arbitrary[IpAddress] = Arbitrary[IpAddress](ipAddressGenerator)

  def ipAddressAsString: Gen[String] = {
    for{
      octets <- vectorOf(4, choose(0, 255))
    } yield octets.mkString(".")
  }

  def minIpAddressAsString: Gen[String] = {
    for {
      string <- vectorOf(4, elements(0))
    } yield string.mkString(".")
  }

  def maxIpAddressAsString: Gen[String] = {
    for {
      string <- vectorOf(4, elements(255))
    } yield string.mkString(".")
  }

  def ipAddressAsStringWithCornerCases: Gen[String] = {
    oneOf(ipAddressAsString, minIpAddressAsString, maxIpAddressAsString)
  }

  test("plus and minus inverse each other", (address: IpAddress) => address == address + 1 - 1)

  test("toString yields the same string it was constructed with", forAll(ipAddressAsStringWithCornerCases)(address => address == new IpAddress(address).toString))

}