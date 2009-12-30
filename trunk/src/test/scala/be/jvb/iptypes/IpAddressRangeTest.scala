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

  test("compare") {
    val range1 = new IpAddressRange(new IpAddress("1.0.0.1"), new IpAddress("1.0.0.1"))
    val range2 = new IpAddressRange(new IpAddress("1.0.0.3"), new IpAddress("1.0.0.3"))
    val range3 = new IpAddressRange(new IpAddress("1.0.0.5"), new IpAddress("1.0.0.5"))
    val range4 = new IpAddressRange(new IpAddress("1.0.0.7"), new IpAddress("1.2.255.255"))

    assert(range1 < range2)
    assert(range1 < range3)
    assert(range1 < range4)

    assert(range2 > range1)
    assert(range2 < range3)
    assert(range2 < range4)

    assert(range3 > range1)
    assert(range3 > range2)
    assert(range3 < range4)

    assert(range4 > range1)
    assert(range4 > range2)
    assert(range4 > range3)


  }

}