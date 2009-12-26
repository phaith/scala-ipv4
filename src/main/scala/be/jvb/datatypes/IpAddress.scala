package be.jvb.datatypes

import java.net.InetAddress;

/**
 * Represents an IPv4 address.
 *
 * @author Jan Van Besien
 */
case class IpAddress(val value: Long) extends SmallByteArray {
  def this(address: String) = this (SmallByteArray.parse(address))

  def this(inetAddress: InetAddress) = {
    this (if (inetAddress == null) throw new IllegalArgumentException("can not create from [null]") else inetAddress.getHostAddress())
  }

  def nBytes = 4

  override def toString: String = {
    val ints = toIntArray(nBytes)
    ints(0) + "." + ints(1) + "." + ints(2) + "." + ints(3)
  }

  /**
   * Addition. Will never overflow, but wraps around when the highest ip address has been reached.
   */
  def +(value: Long) = new IpAddress((this.value + value) & 0x00000000FFFFFFFFL)

  /**
   * Substraction. Will never underflow, but wraps around when the lowest ip address has been reached.
   */
  def -(value: Long) = new IpAddress((this.value - value) & 0x00000000FFFFFFFFL)

  def toInetAddress: InetAddress = InetAddress.getByName(toString)
}

object IpAddress {
  def ipAddress(address: String) = new IpAddress(address)

  def from(first: IpAddress, last: IpAddress): Stream[IpAddress] = {
    if (first < last)
      {
        Stream.cons(first, from(first + 1, last))
      } else {
      Stream.cons(first, Stream.empty)
    }
    // TODO: corner cases (first > last etc)
  }


}

