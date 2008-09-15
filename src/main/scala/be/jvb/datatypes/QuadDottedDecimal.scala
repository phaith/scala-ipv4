package be.jvb.datatypes

/**
 * Represents everything that can be written as xx.xx.xx.xx in the context of IPv4 (addresses, networks, networkmasks, ...).
 *
 * TODO: maybe come up with a better name
 */
trait QuadDottedDecimal extends Ordered[QuadDottedDecimal] {

  def value:Long

  if (value > 0x00000000FFFFFFFFL || value < 0)
    throw new IllegalArgumentException("address out of range [0x" + java.lang.Long.toHexString(value) + "]")


  override def toString: String = {
    val ints = toInts()
    ints(0) + "." + ints(1) + "." + ints(2) + "." + ints(3)
  }

  private def toInts() : Array[Int] = {
    val ints = new Array[Int](4)

    ints(0) = ((value >>> 24) & 0xFF).asInstanceOf[Int]
    ints(1) = (((value << 8) >>> 24) & 0xFF).asInstanceOf[Int]
    ints(2) = (((value << 16) >>> 24) & 0xFF).asInstanceOf[Int]
    ints(3) = (((value << 24) >>> 24) & 0xFF).asInstanceOf[Int]
    return ints
  }

  override def compare(that: QuadDottedDecimal) : Int = this.value.compare(that.value)

}

object QuadDottedDecimal {
  def parse(address: String): Long = {
    if (address eq null)
      throw new IllegalArgumentException("invalid address [null]")

    // TODO: generates NPE -> wrap parseLong in a function that first checks and throws illegalargumentexception
    val ints: Array[Long] = address.split("\\.").map(java.lang.Long.parseLong(_))

    if (ints.length != 4)
      throw new IllegalArgumentException("invalid address [" + address + "]")

    (ints(3) & 0xFF | (ints(2) << 8) & 0xFF00 | (ints(1) << 16) & 0xFF0000 | (ints(0) << 24) & 0xFF000000)
  }
  
}