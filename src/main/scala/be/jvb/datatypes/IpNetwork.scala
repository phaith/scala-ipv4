package be.jvb.datatypes

import java.lang.String

/**
 * Represents an Ipv4 network (i.e. an address and a mask).
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
class IpNetwork(val address: IpAddress, val mask: IpNetworkMask)
        extends IpAddressRange(IpNetwork.first(address, mask), IpNetwork.last(address, mask)) {
  def this(first: IpAddress, last: IpAddress) = this (first, IpNetworkMask.longestPrefixNetwork(first, last))

  private def this(addressAndMask: (IpAddress, IpNetworkMask)) = this (addressAndMask._1, addressAndMask._2)

  def this(network: String) = this (IpNetwork.parseAddressAndMaskFromCidrNotation(network))

  override def toString: String = first.toString + "/" + mask.prefixLength
}

object IpNetwork {
  /**
   * get the first address from a network which contains the given address.
   */
  private[datatypes] def first(address: IpAddress, mask: IpNetworkMask): IpAddress = {
    new IpAddress(address.value & mask.value)
  }

  /**
   * get the last address from a network which contains the given address.
   */
  private[datatypes] def last(address: IpAddress, mask: IpNetworkMask): IpAddress = {
    first(address, mask) + (0xFFFFFFFFL >> mask.prefixLength)
  }

  private[datatypes] def parseAddressAndMaskFromCidrNotation(cidrString: String): (IpAddress, IpNetworkMask) = {
    if (!cidrString.contains("/"))
      throw new IllegalArgumentException("no CIDR format [" + cidrString + "]")

    val addressAndMask: (String, String) = splitInAddressAndMask(cidrString)

    val address = new IpAddress(addressAndMask._1)
    val mask = parseNetworkMask(addressAndMask._2)

    return (address, mask)
  }

  private def splitInAddressAndMask(cidrString: String): (String, String) = {
    val addressAndMask: Array[String] = cidrString.split("/")
    if (addressAndMask.length != 2)
      throw new IllegalArgumentException("no CIDR format [" + cidrString + "]")
    (addressAndMask(0), addressAndMask(1))
  }

  private def parseNetworkMask(mask: String) = {
    try
    {
      if (mask.contains("."))
        new IpNetworkMask(mask)
      else
        IpNetworkMask.fromPrefixLength(Integer.parseInt(mask))
    } catch {
      case e: Exception => throw new IllegalArgumentException("not a valid network mask [" + mask + "]", e)
    }
  }
}