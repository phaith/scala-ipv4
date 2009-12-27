package be.jvb.datatypes

/**
 * Represents a byte array which is small enough to fit in a long (max 8 bytes).
 *
 * @author <a href="http://janvanbesien.blogspot.com">Jan Van Besien</a>
 */
private[datatypes] trait SmallByteArray extends Ordered[SmallByteArray] {
  val value: Long

  def nBytes: Int

  def radix: Int // TODO: only accept 10 or 16 -> enum?

  def zeroPaddingUpTo: Int = 0

  val maxValue = Math.pow(2, nBytes * 8).toLong - 1

  if (nBytes < 0 || nBytes > 8) {
    throw new IllegalArgumentException("SmallByteArray can be used for arrays of length 0 to 8 only")
  }

  if (value > maxValue || value < 0) {
    throw new IllegalArgumentException("out of range [0x" + java.lang.Long.toHexString(value) + "] with [" + nBytes + "] bytes")
  }

  def toIntArray(): Array[Int] = {
    val ints = new Array[Int](nBytes)

    for (i <- 0 until nBytes) {
      ints(i) = (((value << i * 8) >>> 8 * (nBytes - 1)) & 0xFF).asInstanceOf[Int]
    }

    return ints
  }

  def toByteArray(): Array[Byte] = {
    val bytes = new Array[Byte](nBytes)

    for (i <- 0 until nBytes) {
      bytes(i) = (((value << i * 8) >>> 8 * (nBytes - 1)) & 0xFF).asInstanceOf[Byte]
    }

    return bytes
  }

  override def compare(that: SmallByteArray): Int = this.value.compare(that.value)

  override def toString: String = {
    val ints = toIntArray
    val strings = for (i <- 0 until nBytes) yield String.format(formatString,ints(i).asInstanceOf[Object])

    return strings.mkString(".")
  }

  lazy val formatString = {
    if (radix == 16 && zeroPaddingUpTo != 0)
      "%0" + zeroPaddingUpTo + "X"
    else if (zeroPaddingUpTo != 0)
      "%0" + zeroPaddingUpTo + "d"
    else
      "%d"
  }

}

object SmallByteArray {

  // TODO: private in this package!
  def parseAsLong(string:String, length:Int, radix:Int):Long = {
    if (string eq null)
      throw new IllegalArgumentException("can not parse [null]")

    val longArray = parseAsLongArray(string, radix)

    if (longArray.length != length)
      throw new IllegalArgumentException("can not parse [" + string + "] into a SmallByteArray of [" + length + "] bytes")

    mergeBytesOfArrayIntoLong(longArray)
  }

  private def parseAsLongArray(string: String, radix:Int): Array[Long] = {
    // TODO: generates NPE -> wrap parseLong in a function that first checks and throws illegalargumentexception
    string.split("\\.").map(java.lang.Long.parseLong(_, radix))
  }

  private def mergeBytesOfArrayIntoLong(array: Array[Long]): Long = {
    var result = 0L
    for (i <- 0 until array.length) {
      result |= (array(i) << ((array.length - i - 1) * 8))
    }
    return result
  }
}