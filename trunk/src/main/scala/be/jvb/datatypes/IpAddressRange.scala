package be.jvb.datatypes

/**
 * Represents a continuous range of IPv4 ip addresses (bounds included). Ip address ranges are ordered on the first
 * address, or on the second address if the first is equal.
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
case class IpAddressRange(first: IpAddress, last: IpAddress) extends Ordered[IpAddressRange] {
  if (last < first)
    throw new IllegalArgumentException("Cannot create ip address range with last address > first address")

  override def toString: String = {
    first.toString + " - " + last.toString
  }

  def contains(address: IpAddress): Boolean = {
    address >= first && address <= last
  }

  def contains(range: IpAddressRange): Boolean = {
    contains(range.first) && contains(range.last)
  }

  def overlaps(range: IpAddressRange): Boolean = {
    contains(range.first) || contains(range.last) || range.contains(first) || range.contains(last)
  }

  def length = {
    last.value - first.value + 1
  }

  def addresses(): Stream[IpAddress] = {
    if (first < last) {
      Stream.cons(first, new IpAddressRange(first + 1, last).addresses)
    } else {
      Stream.cons(first, Stream.empty)
    }
  }

  def compare(that: IpAddressRange): Int = {
    if (this.first != that.first)
      this.first.compare(that.first)
    else
      this.last.compare(that.last)
  }

  // remove an address from the range, resulting in one, none or two new ranges
  def remove(address: IpAddress): List[IpAddressRange] = {
    if (address eq null)
      throw new IllegalArgumentException("invalid address [null]")

    if (!contains(address))
      return List(this)
    else if (address == first && address == last)
      return List()
    else if (address == first)
      return List(new IpAddressRange(first + 1, last))
    else if (address == last)
      return List(new IpAddressRange(first, last - 1))
    else
      return List(new IpAddressRange(first, address - 1), new IpAddressRange(address + 1, last))
  }

  /**
   *  Extend the range such that the given address is included.
   */
  def +(address: IpAddress): IpAddressRange = {
    if (address < first)
      new IpAddressRange(address, last)
    else if (address > last)
      new IpAddressRange(first, address)
    else
      this
  }
}