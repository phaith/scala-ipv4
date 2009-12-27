package be.jvb.datatypes

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class IpAddressExample extends WordSpec with ShouldMatchers {
  "An IpAddress" when {
    "constructed from a String" should {
      val address = new IpAddress("192.168.0.1")

      "be convertable back into the same string" in {
        address.toString should be("192.168.0.1")
      }
    }
  }

  "An Ipaddress" should {
    val address = new IpAddress("192.168.0.1")

    "have an operator to find the next ip address" in {
      address + 1 should be(new IpAddress("192.168.0.2"))
      address + 10 should be(new IpAddress("192.168.0.11"))
    }

    "have an operator to find the previous ip address" in {
      address - 1 should be(new IpAddress("192.168.0.0"))
      address - 10 should be(new IpAddress("192.167.255.247"))
    }

    val max = new IpAddress("255.255.255.255")
    val min = new IpAddress("0.0.0.0")
    "have + and - operators that don't overflow but wrap around" in {
      max + 1 should be(min)
      min - 1 should be(max)
    }

    "be convertable to and constructable from a java.net.InetAddress" in {
      address.toInetAddress should be(java.net.InetAddress.getByName("192.168.0.1"))
      new IpAddress(java.net.InetAddress.getByName("192.168.0.1")) should be(address)
    }

  }
}