package be.jvb.datatypes

/**
 * Represents a byte array which is small enough to fit in a long (max 8 bytes).
 */
trait SmallByteArray extends Ordered[SmallByteArray] {
  def value: Long

  def nBytes: Int

  val maxValue = Math.pow(2, nBytes * 8).toLong - 1

  if (nBytes < 0 || nBytes > 8) {
    throw new IllegalArgumentException("SmallByteArray can be used for arrays of length 0 to 8 only")
  }

  if (value > maxValue || value < 0) {
    throw new IllegalArgumentException("out of range [0x" + java.lang.Long.toHexString(value) + "] with [" + nBytes + "] bytes")
  }

  protected def toIntArray(size: Int): Array[Int] = {
    val ints = new Array[Int](size)

    for (i <- 0 until size) {
      ints(i) = (((value << i * 8) >>> 24) & 0xFF).asInstanceOf[Int]
    }

    return ints
  }

  override def compare(that: SmallByteArray): Int = this.value.compare(that.value)

}

object SmallByteArray {
  def parse(string: String): Long = {
    if (string eq null)
      throw new IllegalArgumentException("can not parse [null]")

    // TODO: generates NPE -> wrap parseLong in a function that first checks and throws illegalargumentexception
    val longArray: Array[Long] = string.split("\\.").map(java.lang.Long.parseLong(_))

    val length = longArray.length

    if (length > 8)
      throw new IllegalArgumentException("can not parse [" + string + "] into a SmallByteArray, can not be more than 8 bytes")

    var result: Long = 0
    for (i <- 0 until length) {
      result |= (longArray(i) << ((length - i - 1) * 8))
    }
    return result
  }

}