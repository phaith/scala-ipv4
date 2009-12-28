package be.jvb.datatypes

/**
 * Represents an IPv4 network mask.
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
case class IpNetworkMask(override val value: Long) extends IpAddress(value) {
  def this(address: String) = this (SmallByteArray.parseAsLong(address, IpAddress.N_BYTES, IpAddress.RADIX))

  // TODO: there are only 32 valid masks... validate that

  def prefix() = {
    var result: Int = 0;
    var bit: Long = 1L << 31;

    while (((value & bit) != 0) && (result < 32)) {
      bit = bit >> 1;
      result += 1;
    }
    result
  }

}

object IpNetworkMask {

  /**
   * Convert a prefix (e.g. 24) into a network mask (e.g. 255.255.255.0). IpNetworkMask hasn't got a public constructor for this, because
   * it would be confusing with the constructor that takes a long.
   */
  def fromPrefixNotation(prefix: Int) = {
    new IpNetworkMask((((1L << 32) - 1) << (32 - prefix)) & 0xFFFFFFFFL)
  }

}