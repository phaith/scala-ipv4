package be.jvb.iptypes

/**
 * Enumeration of possible radix values (hexadecimal and decimal).
 *
 * @author Jan Van Besien
 */
private[iptypes] sealed trait Radix {
  def radix:Int
}
private[iptypes] final case class HEX extends Radix {
  override def radix = 16
}
private[iptypes] final case class DEC extends Radix {
  override def radix = 10
}