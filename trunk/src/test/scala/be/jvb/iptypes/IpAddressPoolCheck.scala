package be.jvb.iptypes

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import org.scalacheck.{Prop, Gen, Arbitrary}
import org.scalacheck.Prop._

/**
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class IpAddressPoolCheck extends FunSuite with Checkers {
  /**
   * @return generator for a random ip address
   */
  def ipAddressGenerator: Gen[IpAddress] = {
    for{
      value <- Gen.choose(0L, 0xFFFFFFFFL)
    } yield new IpAddress(value)
  }

  /**
   * @return generator for a random ip address within the given range
   */
  def ipAddressWithinRangeGenerator(range: IpAddressRange): Gen[IpAddress] = {
    for{
      value <- Gen.choose(range.first.value, range.last.value)
    } yield new IpAddress(value)
  }

  /**
   * @return generator for a list of random ip addresses, all within the given range
   */
  def ipAddressListWithinRangeGenerator(range: IpAddressRange): Gen[List[IpAddress]] = {
    Gen.sized {
      size =>
        Gen.vectorOf(size, ipAddressWithinRangeGenerator(range) suchThat (address => range.contains(address)))
    }
  }

  /**
   * @return generator for a random ip address pool
   */
  def ipAddressPoolGenerator: Gen[IpAddressPool] = {
    for{
      first <- ipAddressGenerator
      last <- ipAddressGenerator suchThat (address => address > first)
    } yield new IpAddressPool(first, last)
  }

  /**
   * @return generator for a rondom ip address pool and a list of addresses contained in the pool
   */
  def ipAddressPoolAndListOfContainedAddressesGenerator: Gen[(IpAddressPool, List[IpAddress])] = {
    for{
      pool <- ipAddressPoolGenerator
      addresses <- ipAddressListWithinRangeGenerator(pool)
    } yield (pool, addresses)
  }

  test("allocated addresses are not free") {
    check(
      Prop.forAll(ipAddressPoolGenerator) {
        pool =>
          Prop.forAll(ipAddressListWithinRangeGenerator(pool)) {
            addresses =>
              Prop.forAll(Gen.pick(1, addresses)) {
                pickedAddresses =>
                  val address = pickedAddresses.first
                  val (newPool, allocated) = pool.allocate(pickedAddresses.first)
                  !newPool.isFree(address)
              }
          }
      })
  }
}