package be.jvb.iptypes

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
@RunWith(classOf[JUnitRunner])
class IpAddressPoolTest extends FunSuite {
  test("allocate next") {
    var pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))

    pool.allocate() match {
      case (p, allocated) => {
        pool = p
        assert(Some(new IpAddress("1.2.3.4")) === allocated)
      }
    }

    pool.allocate() match {
      case (p, allocated) => {
        pool = p
        assert(Some(new IpAddress("1.2.3.5")) === allocated)
      }
    }

    pool.allocate() match {
      case (p, allocated) => {
        pool = p
        assert(Some(new IpAddress("1.2.3.6")) === allocated)
      }
    }
  }

  test("allocate to exhaustion") {
    var pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.4"))

    pool.allocate() match {
      case (p, allocated) => {
        pool = p
        assert(Some(new IpAddress("1.2.3.4")) === allocated)
      }
    }

    pool.allocate() match {
      case (p, allocated) => {
        pool = p
        assert(None === allocated)
      }
    }
  }

  test("allocate specific address") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    assert(Some(new IpAddress("1.2.3.5")) === pool.allocate(new IpAddress("1.2.3.5"))._2)
  }

  test("allocate address outside pool range") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    intercept[IllegalArgumentException] {pool.allocate(new IpAddress("4.4.4.4"))}
  }

  test("deallocate address outside pool range") {
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    intercept[IllegalArgumentException] {pool.deAllocate(new IpAddress("4.4.4.4"))}
  }

  test("allocate unfree address") {
    var pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))

    pool.allocate(new IpAddress("1.2.3.5")) match {
      case (p, allocated) => {
        pool = p
        assert(Some(new IpAddress("1.2.3.5")) === allocated)
      }
    }

    pool.allocate(new IpAddress("1.2.3.5")) match {
      case (p, allocated) => {
        pool = p
        assert(None === allocated)
      }
    }
  }

  test("allocate and free") {
    var pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.10"))
    assert(pool.isFree(new IpAddress("1.2.3.6")))

    pool.allocate(new IpAddress("1.2.3.6")) match {
      case (p, allocated) => {
        pool = p
        assert(!pool.isFree(new IpAddress("1.2.3.6")))
      }
    }

    pool.allocate(new IpAddress("1.2.3.8")) match {
      case (p, allocated) => {
        pool = p
        assert(pool.isFree(new IpAddress("1.2.3.9")))
      }
    }

    pool.allocate(new IpAddress("1.2.3.9")) match {
      case (p, allocated) => {
        pool = p
        assert(!pool.isFree(new IpAddress("1.2.3.9")))
      }
    }
  }

  test("deallocate and free") {
    var pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.10"))
    pool = pool.allocate(new IpAddress("1.2.3.6"))._1
    pool = pool.allocate(new IpAddress("1.2.3.8"))._1
    assert(!pool.isFree(new IpAddress("1.2.3.8")))
    pool = pool.deAllocate(new IpAddress("1.2.3.8"))
    assert(pool.isFree(new IpAddress("1.2.3.8")))
  }

  test("deallocate defragmentation") {
    var pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.11"))
    pool = pool.allocate(new IpAddress("1.2.3.5"))._1
    pool = pool.allocate(new IpAddress("1.2.3.7"))._1
    pool = pool.allocate(new IpAddress("1.2.3.8"))._1
    pool = pool.allocate(new IpAddress("1.2.3.9"))._1
    pool = pool.allocate(new IpAddress("1.2.3.10"))._1
    expect(3) {pool.fragments}

    pool = pool.deAllocate(new IpAddress("1.2.3.5")) // needs to merge two fragments
    assert(pool.isFree(new IpAddress("1.2.3.5")))
    expect(2) {pool.fragments}

    pool = pool.deAllocate(new IpAddress("1.2.3.8")) // creates new fragment
    expect(3) {pool.fragments}
    assert(pool.isFree(new IpAddress("1.2.3.8")))

    pool = pool.deAllocate(new IpAddress("1.2.3.9")) // needs to merge one fragment
    expect(3) {pool.fragments}
    assert(pool.isFree(new IpAddress("1.2.3.9")))
  }


  ignore("performance test shouldn't crash with stack overflow error") {
    // create very large pool
    var pool = new IpAddressPool(new IpAddress("1.0.0.0"), new IpAddress("1.2.255.255"))

    // allocate one address every two addresses (lots of fragmentation)
    var toAllocate = pool.first
    var count = 0
    while (pool.contains(toAllocate)) {
      pool.allocate(toAllocate) match {
        case (p, allocated) => {
          pool = p
          assert(allocated === Some(toAllocate))
          count += 1
        }
      }
      toAllocate += 2
    }
  }
}