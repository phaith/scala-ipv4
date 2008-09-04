package be.jvb.datatypes

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