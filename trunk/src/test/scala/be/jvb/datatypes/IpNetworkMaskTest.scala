package be.jvb.datatypes

import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalatest.prop.PropSuite

class IpNetworkMaskTest extends PropSuite {

  test("mask or prefix construction") {
    assert (new IpNetworkMask("255.255.255.0") === IpNetworkMask.fromPrefixNotation(24))
  }

  def prefixGenerator:Gen[Int] = choose(0,24)

  test("prefix used by construction is same as calculated prefix",
    forAll(prefixGenerator)(prefix => {
      prefix == IpNetworkMask.fromPrefixNotation(prefix).prefix
    }))

}