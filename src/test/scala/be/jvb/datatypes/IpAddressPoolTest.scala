package be.jvb.datatypes

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.PropSuite

class IpAddressPoolTest extends PropSuite {

  test("allocate next"){
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    assert(Some(new IpAddress("1.2.3.4")) === pool.allocate)
    assert(Some(new IpAddress("1.2.3.5")) === pool.allocate)
    assert(Some(new IpAddress("1.2.3.6")) === pool.allocate)
  }

  test("allocate to exhaustion"){
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.4"))
    assert(Some(new IpAddress("1.2.3.4")) === pool.allocate)
    assert(None === pool.allocate)
  }

  test("allocate specific address"){
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    assert(Some(new IpAddress("1.2.3.5")) === pool.allocate(new IpAddress("1.2.3.5")))
  }

  test("allocate unfree address"){
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.6"))
    assert(Some(new IpAddress("1.2.3.5")) === pool.allocate(new IpAddress("1.2.3.5")))
    assert(None === pool.allocate(new IpAddress("1.2.3.5")))
  }

  test("allocate and free"){
    val pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.10"))
    assert(pool.isFree(new IpAddress("1.2.3.6")))
    pool.allocate(new IpAddress("1.2.3.6"))
    assert(!pool.isFree(new IpAddress("1.2.3.6")))
    pool.allocate(new IpAddress("1.2.3.8"))
    assert(pool.isFree(new IpAddress("1.2.3.9")))
    pool.allocate(new IpAddress("1.2.3.9"))
    assert(!pool.isFree(new IpAddress("1.2.3.9")))
  }

  test("deallocate and free"){
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

  // todo: make sure that corner cases are included in every run...
  def ipAddressGenerator(miminalAddress: Option[IpAddress]): Gen[IpAddress] = {
    miminalAddress match {
      case None => for{value <- Gen.choose(0L, 0xFFFFFFFFL)} yield new IpAddress(value)
      case Some(address) => for{value <- Gen.choose(address.value, 0xFFFFFFFFL)} yield new IpAddress(value)
    }
  }

  def ipAddressGenerator(pool: IpAddressPool): Gen[IpAddress] = {
    for {
      value <- Gen.choose(pool.first.value, pool.last.value)
    } yield new IpAddress(value)
  }

  def ipAddressPoolGenerator: Gen[IpAddressPool] = {
    for{
      first <- ipAddressGenerator(None)
      last <- ipAddressGenerator(Some(first))
    } yield new IpAddressPool(first, last)
  }

  def ipAddressAndContainingPoolGenerator: Gen[(IpAddress, IpAddressPool)] = {
    for {
      pool <- ipAddressPoolGenerator
      address <- ipAddressGenerator(pool)
    } yield (address, pool)
  }

  implicit def arbitraryIpAddress: Arbitrary[IpAddress] = Arbitrary[IpAddress](ipAddressGenerator(None))

  implicit def arbitraryIpAddressPool: Arbitrary[IpAddressPool] = Arbitrary[IpAddressPool](ipAddressPoolGenerator)

  // TODO: address should be in the pool...
//  specify("allocating an adress means it is not free afterwards",
//    forAll(ipAddressAndContainingPoolGenerator)
//              ((address,pool) => {pool.allocate(address) == Some(address) ==> !pool.isFree(address)}))



}