package be.jvb.datatypes;

/**
 * Represents an IPv4 address.
 *
 * @author Jan Van Besien
 */
case class IpAddress(val value: Long) extends QuadDottedDecimal {

  def this(address: String) = this (QuadDottedDecimal.parse(address))

  /**
   * Addition. Will never overflow, but wraps around when the highest ip address has been reached.
   */
  def + (value: Long) = new IpAddress((this.value + value) & 0x00000000FFFFFFFFL)

  /**
   * Substraction. Will never underflow, but wraps around when the lowest ip address has been reached.
   */
  def - (value: Long) = new IpAddress((this.value - value) & 0x00000000FFFFFFFFL)

}

object IpAddress {
  def ipAddress(address: String) = new IpAddress(address)
}

