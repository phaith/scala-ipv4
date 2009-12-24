package be.jvb.datatypes

import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class IpNetworkMaskTest extends FunSuite with Checkers {
  test("mask or prefix construction") {
    assert(new IpNetworkMask("255.255.255.0") === IpNetworkMask.fromPrefixNotation(24))
  }

  def prefixGenerator: Gen[Int] = Gen.choose(0, 24)

  test("prefix used by construction is same as calculated prefix") {
    check(Prop.forAll(prefixGenerator)(prefix => {prefix == IpNetworkMask.fromPrefixNotation(prefix).prefix}))
  }

}