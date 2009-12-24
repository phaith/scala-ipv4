package be.jvb.datatypes

import scala.collection.{Set, SortedSet}
import scala.collection.jcl.TreeSet

/**
 * Represents a pool of IPv4 addresses. A pool is a range of addresses, from which some can be "in use" and some can be
 * "free".
 *
 * @author JanVanBesien
 */
case class IpAddressPool(override val first: IpAddress, override val last: IpAddress) extends IpAddressRange(first, last) {

  def this(range: IpAddressRange) = this (range.first, range.last)

  // initially the whole range is free (TODO: think of a way to do this with immutable data structures? -> returning a new ipaddresspool every time or so?)
  private val freeRanges: TreeSet[IpAddressRange] = new TreeSet[IpAddressRange]
  freeRanges.add(new IpAddressRange(first, last))

  def allocate(): Option[IpAddress] = {
    if (!isExhausted) {
      // get the first range of free addresses, and take the first address of that range
      val range: IpAddressRange = freeRanges.firstKey
      val allocated: IpAddress = range.first
      Some(doAllocate(allocated, range))
    } else {
      None
    }
  }

  def allocate(address: IpAddress): Option[IpAddress] = {
    var allocated: Option[IpAddress] = None
    // take the range that has the address, and allocate in that range
    // TODO: do this by using the sorted nature of the set
    freeRanges.filter(_.contains(address)).foreach(x => {
      doAllocate(address, x);
      allocated = Some(address)
    })
    return allocated
  }

  private def doAllocate(allocated: IpAddress, range: IpAddressRange): IpAddress = {
    // remove the range and replace with ranges without the allocated address
    freeRanges.remove(range)
    freeRanges.addAll(range.remove(allocated))
    return allocated
  }

  def deAllocate(address: IpAddress) = {
    // append or prepend existing range if possible, to avoid fragmentation

    val freeRangeBeforeAddress:Iterable[IpAddressRange] = freeRanges.filter(element => element.last + 1 == address)
    val freeRangeAfterAddress:Iterable[IpAddressRange] = freeRanges.filter(element => element.first - 1 == address)

//    println("address: " + address + ", pool: " + this + ",before: " + freeRangeBeforeAddress + ",after: " + freeRangeAfterAddress)

    if (freeRangeBeforeAddress.isEmpty && freeRangeAfterAddress.isEmpty) {
      // no match -> nothing to "defragment"
      freeRanges.add(new IpAddressRange(address, address))
    } else {
      if (!freeRangeBeforeAddress.isEmpty && !freeRangeAfterAddress.isEmpty) {
        // first and last match -> merge the 2 existing ranges
        freeRanges.remove(freeRangeBeforeAddress.toSeq(0))
        freeRanges.remove(freeRangeAfterAddress.toSeq(0))
        freeRanges.add(new IpAddressRange(freeRangeBeforeAddress.toSeq(0).first, freeRangeAfterAddress.toSeq(0).last))
      } else if (!freeRangeBeforeAddress.isEmpty) {
        // append
        freeRanges.remove(freeRangeBeforeAddress.toSeq(0))
        freeRanges.add(freeRangeBeforeAddress.toSeq(0) + address)
      } else if (!freeRangeAfterAddress.isEmpty) {
        // prepend
        freeRanges.remove(freeRangeAfterAddress.toSeq(0))
        freeRanges.add(freeRangeAfterAddress.toSeq(0) + address)
      }
    }
  }

  def isExhausted(): Boolean = freeRanges.isEmpty

  def isFree(address: IpAddress): Boolean = {
    // lookup the address in all free ranges, if true in one, the result is true
    freeRanges.map(_ contains address).reduceLeft((x, y) => x || y)
  }

  def fragments(): Int = freeRanges.size

  override def toString(): String = freeRanges.toString

}