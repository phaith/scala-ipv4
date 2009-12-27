package be.jvb.datatypes

/**
 * Represents an IPv4 network mask.
 *
 * @author JanVanBesien
 */
case class IpNetworkMask(override val value: Long) extends IpAddress(value) {

  def this(address: String) = this (SmallByteArray.parseAsLong(address, IpAddress.N_BYTES, IpAddress.RADIX))

  // TODO: can this not be more elegantly written
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

// TODO: design a strategy for which things to have as constructor, and which as factory method, and apply for all...
object IpNetworkMask {
  def fromPrefixNotation(prefix: Int) = {
    new IpNetworkMask((((1L << 32) - 1) << (32 - prefix)) & 0xFFFFFFFFL)
  }
}

