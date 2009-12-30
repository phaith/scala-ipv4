package be.jvb.datatypes

import scala.collection.immutable._

/**
 * Represents a pool of IPv4 addresses. A pool is a range of addresses, from which some can be "in use" and some can be
 * "free".
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class IpAddressPool(override val first: IpAddress, override val last: IpAddress, val freeRanges: SortedSet[IpAddressRange])
        extends IpAddressRange(first, last) {

  {
    // TODO: validate that the free ranges set is a valid set (no overlapping, not outside boundaries, ...)
  }

  def this(first: IpAddress, last: IpAddress) = {
    // the whole ranges is free initially
    this (first, last, TreeSet[IpAddressRange](new IpAddressRange(first, last)))
  }

  def this(range: IpAddressRange) = {
    this (range.first, range.last)
  }

  def allocate(): (IpAddressPool, Option[IpAddress]) = {
    if (!isExhausted) {
      // get the first range of free addresses, and take the first address of that range
      val range: IpAddressRange = freeRanges.firstKey
      val toAllocate: IpAddress = range.first
      return doAllocate(toAllocate, range)
    } else {
      return (this, None)
    }
  }

  def allocate(toAllocate: IpAddress): (IpAddressPool, Option[IpAddress]) = {
    // go find the range that contains the requested address
    findFreeRangeContaining(toAllocate) match {
      case Some(range) => doAllocate(toAllocate, range) // allocate in the range we found
      case None => {
        (this, None) // no free range found for the requested address
      }
    }
  }

  private def findFreeRangeContaining(toAllocate: IpAddress): Option[IpAddressRange] = {
    // split around the address to allocate
    val head = freeRanges.until(new IpAddressRange(toAllocate, toAllocate))
    val tail = freeRanges.from(new IpAddressRange(toAllocate, toAllocate))

    // the range we need is either the first of the tail, or the last of the head, or it doesn't exist
    if (!head.isEmpty && head.lastKey.contains(toAllocate)) {
      return Some(head.lastKey)
    }
    else if (!tail.isEmpty && tail.firstKey.contains(toAllocate)) {
      return Some(tail.firstKey)
    }
    else {
      return None
    }
  }

  /**
   * Allocate the given address in the given range. It is assumed at this point that the range actually contains
   * the address, and that the range is one of the free ranges of the pool.
   */
  private def doAllocate(toAllocate: IpAddress, range: IpAddressRange): (IpAddressPool, Option[IpAddress]) = {
    // remove the range and replace with ranges without the allocated address
    // note: the cast to SortedSet is a workaround until scala 2.8 (http://stackoverflow.com/questions/1271426/scala-immutable-sortedset-are-not-stable-on-deletion)
    val remainingRanges: SortedSet[IpAddressRange] = (freeRanges - range).asInstanceOf[SortedSet[IpAddressRange]] ++ (range - toAllocate)
    return (new IpAddressPool(this.first, this.last, remainingRanges), Some(toAllocate))
  }

  def deAllocate(address: IpAddress): IpAddressPool = {
    return new IpAddressPool(first, last, freeRangesWithAdditionalFreeAddress(address))
  }

  /**
   * Add the given address as a free address in the set of free ranges. The implementation tries to merge existing ranges
   * as much as possible to prevent fragmentation.
   */
  private def freeRangesWithAdditionalFreeAddress(address: IpAddress): SortedSet[IpAddressRange] = {
    val freeRangeBeforeAddress: Iterable[IpAddressRange] = freeRanges.filter(element => element.last + 1 == address)
    val freeRangeAfterAddress: Iterable[IpAddressRange] = freeRanges.filter(element => element.first - 1 == address)

    if (freeRangeBeforeAddress.isEmpty && freeRangeAfterAddress.isEmpty) {
      // no match -> nothing to "defragment"
      return freeRanges + new IpAddressRange(address, address)
    } else {
      if (!freeRangeBeforeAddress.isEmpty && !freeRangeAfterAddress.isEmpty) {
        // first and last match -> merge the 2 existing ranges
        return (freeRanges -
                freeRangeBeforeAddress.toSeq(0) -
                freeRangeAfterAddress.toSeq(0) +
                new IpAddressRange(freeRangeBeforeAddress.toSeq(0).first, freeRangeAfterAddress.toSeq(0).last)).
                asInstanceOf[SortedSet[IpAddressRange]] // workaround, see above
      } else if (!freeRangeBeforeAddress.isEmpty) {
        // append
        return (freeRanges -
                freeRangeBeforeAddress.toSeq(0) +
                (freeRangeBeforeAddress.toSeq(0) + address)).
                asInstanceOf[SortedSet[IpAddressRange]] // workaround, see above
      } else {
        // prepend
        return (freeRanges -
                freeRangeAfterAddress.toSeq(0) +
                (freeRangeAfterAddress.toSeq(0) + address)).
                asInstanceOf[SortedSet[IpAddressRange]] // workaround, see above
      }
    }
  }

  def isExhausted(): Boolean = {
    freeRanges.isEmpty
  }

  def isFree(address: IpAddress): Boolean = {
    // lookup the address in all free ranges, if true in one, the result is true
    freeRanges.map(_ contains address).reduceLeft((x, y) => x || y)
  }

  def fragments(): Int = {
    freeRanges.size
  }

  override def toString(): String = {
    freeRanges.toString
  }

  // TODO: toStringRepresentation which can also be parsed back into an IpAddressPool
}