package be.jvb.datatypes

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class IpAddressPoolTest extends FunSuite {
  test("allocate next") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    assert(Some(new IpAddress("1.2.3.4")) === pool.allocate)
    assert(Some(new IpAddress("1.2.3.5")) === pool.allocate)
    assert(Some(new IpAddress("1.2.3.6")) === pool.allocate)
  }

  test("allocate to exhaustion") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.4"))
    assert(Some(new IpAddress("1.2.3.4")) === pool.allocate)
    assert(None === pool.allocate)
  }

  test("allocate specific address") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    assert(Some(new IpAddress("1.2.3.5")) === pool.allocate(new IpAddress("1.2.3.5")))
  }

  test("allocate unfree address") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    assert(Some(new IpAddress("1.2.3.5")) === pool.allocate(new IpAddress("1.2.3.5")))
    assert(None === pool.allocate(new IpAddress("1.2.3.5")))
  }

  test("allocate and free") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.10"))
    assert(pool.isFree(new IpAddress("1.2.3.6")))
    pool.allocate(new IpAddress("1.2.3.6"))
    assert(!pool.isFree(new IpAddress("1.2.3.6")))
    pool.allocate(new IpAddress("1.2.3.8"))
    assert(pool.isFree(new IpAddress("1.2.3.9")))
    pool.allocate(new IpAddress("1.2.3.9"))
    assert(!pool.isFree(new IpAddress("1.2.3.9")))
  }

  test("deallocate and free") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.10"))
    pool.allocate(new IpAddress("1.2.3.6"))
    pool.allocate(new IpAddress("1.2.3.8"))

    assert(!pool.isFree(new IpAddress("1.2.3.8")))
    pool.deAllocate(new IpAddress("1.2.3.8"))
    assert(pool.isFree(new IpAddress("1.2.3.8")))
  }

  test("deallocate defragmentation") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.11"))
    pool.allocate(new IpAddress("1.2.3.5"))
    pool.allocate(new IpAddress("1.2.3.7"))
    pool.allocate(new IpAddress("1.2.3.8"))
    pool.allocate(new IpAddress("1.2.3.9"))
    pool.allocate(new IpAddress("1.2.3.10"))
    expect(3) {pool.fragments}

    pool.deAllocate(new IpAddress("1.2.3.5")) // needs to merge two fragments
    assert(pool.isFree(new IpAddress("1.2.3.5")))
    expect(2) {pool.fragments}
    pool.deAllocate(new IpAddress("1.2.3.8")) // creates new fragment
    expect(3) {pool.fragments}
    assert(pool.isFree(new IpAddress("1.2.3.8")))
    pool.deAllocate(new IpAddress("1.2.3.9")) // needs to merge one fragment
    expect(3) {pool.fragments}
    assert(pool.isFree(new IpAddress("1.2.3.9")))
  }

  
  ignore("performance") {
    // create very large pool
    val pool = new IpAddressPool(new IpAddress("1.0.0.0"), new IpAddress("1.2.255.255"))

    // allocate one address every two addresses (lots of fragmentation)
    val start = System.nanoTime();
    var toAllocate = pool.first
    while (pool.contains(toAllocate)) {
      pool.allocate(toAllocate)
      toAllocate += 2
    }

    // should be finished in 5 seconds
    assert((System.nanoTime() - start) <= 5000000000L);
  }
}