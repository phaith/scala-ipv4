package be.jvb.datatypes

import java.net.InetAddress;

/**
 * Represents an IPv4 address.
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
case class IpAddress(val value: Long) extends SmallByteArray {
  def this(address: String) = this (SmallByteArray.parseAsLong(address, IpAddress.N_BYTES, IpAddress.RADIX))

  def this(inetAddress: InetAddress) = {
    this (if (inetAddress == null) throw new IllegalArgumentException("can not create from [null]") else inetAddress.getHostAddress())
  }

  def nBytes = IpAddress.N_BYTES

  def radix = IpAddress.RADIX

  /**
   * Addition. Will never overflow, but wraps around when the highest ip address has been reached.
   */
  def +(value: Long) = new IpAddress((this.value + value) & maxValue)

  /**
   * Substraction. Will never underflow, but wraps around when the lowest ip address has been reached.
   */
  def -(value: Long) = new IpAddress((this.value - value) & maxValue)

  def toInetAddress: InetAddress = InetAddress.getByName(toString)

}

object IpAddress {
  val N_BYTES = 4

  val RADIX = 10;

  def from(first: IpAddress, last: IpAddress): Stream[IpAddress] = {
    if (first < last)
      {
        Stream.cons(first, from(first + 1, last))
      } else {
      Stream.cons(first, Stream.empty)
    }
    // TODO: corner cases (first > last etc)
  }

//  def apply(string: String): IpAddress = new IpAddress(SmallByteArray.parseAsLong(string, N_BYTES, radix))

//  def unapply(ipAddress: IpAddress): Some[String] = ipAddress.toString

}

