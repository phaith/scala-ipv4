package be.jvb.iptypes

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
@RunWith(classOf[JUnitRunner])
class IpNetworkTest extends FunSuite {

  test("to string") {
    assert ("192.168.1.0/24" === new IpNetwork(new IpAddress("192.168.1.2"), new IpNetworkMask("255.255.255.0")).toString)
  }

}