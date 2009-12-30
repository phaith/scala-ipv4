package be.jvb.iptypes

import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
@RunWith(classOf[JUnitRunner])
class IpNetworkMaskTest extends FunSuite with Checkers {
  test("mask or prefix construction") {
    assert(new IpNetworkMask("255.255.255.0") === IpNetworkMask.fromPrefixLength(24))
  }

  def prefixGenerator: Gen[Int] = Gen.choose(0, 32)

  test("prefix used by construction is same as calculated prefix") {
    check(Prop.forAll(prefixGenerator)(prefix => {prefix == IpNetworkMask.fromPrefixLength(prefix).prefixLength}))
  }

}