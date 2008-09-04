package be.jvb.datatypes

import org.scalatest.FunSuite

class IpNetworkMaskTest extends FunSuite {

  test("mask or prefix construction") {
    assert (new IpNetworkMask("255.255.255.0") === IpNetworkMask.fromPrefixNotation(24))
  }

}