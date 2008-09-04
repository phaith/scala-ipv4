package be.jvb.datatypes

import org.scalacheck.{Arbitrary, Properties, Gen, Prop}
import org.scalacheck.Gen._
import org.scalacheck.Prop._

object IpAddressPoolSpecification extends Properties("IpAddressPool") {

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

//  // TODO: address should be in the pool...
//  specify("allocating an adress means it is not free afterwards",
//    forAll(ipAddressAndContainingPoolGenerator)
//              ((address,pool) => {pool.allocate(address) == Some(address) ==> !pool.isFree(address)}))

}