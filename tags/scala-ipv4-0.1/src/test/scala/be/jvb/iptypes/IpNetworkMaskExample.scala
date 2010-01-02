package be.jvb.iptypes

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class IpNetworkMaskExample extends WordSpec with ShouldMatchers {
  "An IpNetworkMask" should {
    val mask = new IpNetworkMask("255.255.255.128")

    "be constructable from a String" in {
      new IpNetworkMask("255.255.255.128") should be(mask)
    }

    "be constructable from a prefix length" in {
       IpNetworkMask.fromPrefixLength(25) should be(mask)
    }

  }

  "The construction of an invalid IpNetworkMask" should {
    "fail" in {
      evaluating {new IpNetworkMask("255.255.255.100")} should produce[IllegalArgumentException]
    }
  }
}