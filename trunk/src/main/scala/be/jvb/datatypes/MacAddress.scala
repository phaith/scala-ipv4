package be.jvb.datatypes

/**
 * Represents a MAC address.
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class MacAddress(val value: Long) extends SmallByteArray {

  def this(address: String) = this(SmallByteArray.parseAsLong(address, MacAddress.N_BYTES, MacAddress.RADIX))

  def nBytes = MacAddress.N_BYTES

  def radix = MacAddress.RADIX

  override def zeroPaddingUpTo = 2

  /**
   * Addition. Will never overflow, but wraps around when the highest MAC address has been reached.
   */
  def +(value: Long) = new MacAddress((this.value + value) & maxValue)

  /**
   * Substraction. Will never underflow, but wraps around when the lowest MAC address has been reached.
   */
  def -(value: Long) = new MacAddress((this.value - value) & maxValue)

}

object MacAddress {
  val N_BYTES = 6

  val RADIX = 16
  
//  def apply(string: String): MacAddress = new MacAddress(SmallByteArray.parseAsLong(string, N_BYTES))

//  def unapply(macAddress: MacAddress): Some[String] = macAddress.toString

}
