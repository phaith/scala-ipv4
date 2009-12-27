package be.jvb.datatypes

/**
 * Represents an Ipv4 network (i.e. an address and a mask).
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
case class IpNetwork(address: IpAddress, mask: IpNetworkMask) extends IpAddressRange(IpNetwork.first(address, mask), IpNetwork.last(address, mask)) {

  override def toString: String = first.toString + "/" + mask.prefix
}

object IpNetwork {
  def first(address: IpAddress, mask: IpNetworkMask): IpAddress = {
    new IpAddress(address.value & mask.value)
  }

  // TODO: test this...

  def last(address: IpAddress, mask: IpNetworkMask): IpAddress = {
    first(address, mask) + (0xFFFFFFFFL >> mask.prefix)
  }
}