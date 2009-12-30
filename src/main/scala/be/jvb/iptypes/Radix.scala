package be.jvb.iptypes

/**
 * Enumeration of possible radix values (hexadecimal and decimal).
 *
 * @author Jan Van Besien
 */
private[iptypes] sealed trait Radix {
  val radix:Int
}
private[iptypes] final case class HEX() extends Radix {
  override val radix = 16
}
private[iptypes] final case class DEC() extends Radix {
  override val radix = 10
}