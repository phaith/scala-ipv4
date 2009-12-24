package be.jvb.datatypes

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IpNetworkTest extends FunSuite {

  test("to string") {
    assert ("192.168.1.0/24" === new IpNetwork(new IpAddress("192.168.1.2"), new IpNetworkMask("255.255.255.0")).toString)
  }

}