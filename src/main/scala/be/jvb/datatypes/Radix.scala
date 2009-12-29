package be.jvb.datatypes

/**
 * Enumeration of possible radix values (hexadecimal and decimal).
 *
 * @author Jan Van Besien
 */
private[datatypes] sealed trait Radix {
  def radix:Int
}
private[datatypes] final case class HEX extends Radix {
  override def radix = 16
}
private[datatypes] final case class DEC extends Radix {
  override def radix = 10
}