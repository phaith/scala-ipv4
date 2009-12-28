package be.jvb.datatypes

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class IpAddressExample extends WordSpec with ShouldMatchers {
  "An IpAddress" should {
    val address = new IpAddress("192.168.0.1")

    "be constructable from a String" in {
      new IpAddress("192.168.0.1") should be(address)
    }

    "be constructable from a long" in {
      new IpAddress(3232235521L) should be(address)
    }

    "be constructable from a java.net.InetAddress" in {
      new IpAddress(java.net.InetAddress.getByName("192.168.0.1")) should be(address)
    }
  }

  "An IpAddress" when {
    "constructed from a String" should {
      val address = new IpAddress("192.168.0.1")

      "be convertable back into the same string" in {
        address.toString should be("192.168.0.1")
      }
    }

    "constructed from a java.net.InetAddress" should {
      val address = new IpAddress(java.net.InetAddress.getByName("192.168.0.1"))

      "be convertable back into the same InetAddress" in {
        address.toInetAddress should be(java.net.InetAddress.getByName("192.168.0.1"))
      }
    }

  }

  "An IpAddress" should {
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

    "be comparable" in {
      address < address + 1 should be(true)
      address >= address +1 should be(false)
    }

  }
}