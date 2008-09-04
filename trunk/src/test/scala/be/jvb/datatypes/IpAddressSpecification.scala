package be.jvb.datatypes

import org.scalacheck.{Arbitrary, Properties, Gen, Prop}
import org.scalacheck.Gen._
import org.scalacheck.Prop._

object IpAddressSpecification extends Properties("IpAddress") {

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

  specify("plus and minus inverse each other", (address: IpAddress) => collect(address){address == address + 1 - 1})

  specify("toString yields the same string it was constructed with", forAll(ipAddressAsStringWithCornerCases)(address => collect(address){address == new IpAddress(address).toString}))
}