package be.jvb.datatypes

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec


/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class IpNetworkExample extends WordSpec with ShouldMatchers {
//  "An ip network" should {
//    val network = new IpNetwork(new IpAddress("192.168.0.0"), new IpNetworkMask("255.255.255.0"))
//
//    "be constructable from two ip addresses" in {
//      new IpNetwork(new IpAddress("192.168.0.0"), new IpAddress("192.168.0.255")) should be(network)
//    }
//
//    "be constructable from its CIDR notation" in {
//      new IpNetwork("192.168.0.0/24") should be(network)
//      new IpNetwork("192.168.0.0/255.255.255.0") should be(network)
//    }
//
//    "align with the closest matching network" in {
//      new IpNetwork(new IpAddress("192.168.0.3"), new IpAddress("192.168.0.250")) should be(network)
//    }
//
//    "have a network mask" in {
//      network.mask should be(new IpNetworkMask("255.255.255.0"))
//    }
//
//    "be an ip address range" in {
//      val range: IpAddressRange = network
//      range.contains(new IpAddress("192.168.0.150")) should be(true)
//    }
//
//
//  }
}