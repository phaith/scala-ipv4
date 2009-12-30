package be.jvb.iptypes

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
@RunWith(classOf[JUnitRunner])
class IpAddressRangeTest extends FunSuite {

  test("string representation"){
    assert("1.2.3.4 - 5.6.7.8" === new IpAddressRange(new IpAddress("1.2.3.4"), new IpAddress("5.6.7.8")).toString)
  }

  test("contains"){
    assert(new IpAddressRange(new IpAddress("1.2.3.4"), new IpAddress("5.6.7.8")).contains(new IpAddress("2.2.2.2")))
  }

  test("contains lower bound"){
    assert(new IpAddressRange(new IpAddress("1.2.3.4"), new IpAddress("5.6.7.8")).contains(new IpAddress("1.2.3.4")))
  }

  test("contains upper bound"){
    assert(new IpAddressRange(new IpAddress("1.2.3.4"), new IpAddress("5.6.7.8")).contains(new IpAddress("5.6.7.8")))
  }

  test("doesn't contain because too large"){
    assert(!new IpAddressRange(new IpAddress("1.2.3.4"), new IpAddress("5.6.7.8")).contains(new IpAddress("8.8.8.8")))
  }

  test("doesn't contain because too small"){
    assert(!new IpAddressRange(new IpAddress("1.2.3.4"), new IpAddress("5.6.7.8")).contains(new IpAddress("1.1.1.1")))
  }

  test("addresses list"){
    assert(List(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.5")) === new IpAddressRange(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.5")).addresses().toList)
  }

  test("very large address list shouldn't go out of memory"){
    val range = new IpAddressRange(new IpAddress("0.0.0.0"), new IpAddress("0.255.255.255"))
    range.addresses().foreach(element => assert(range.contains(element)))
  }

}