package be.jvb.datatypes

import org.scalacheck.{Properties, Gen}
import org.scalacheck.Gen._
import org.scalacheck.Prop._

object IpNetworkMaskSpecification extends Properties("IpNetworkMask") {

  def prefixGenerator:Gen[Int] = choose(0,24)
 
  specify("prefix used by construction is same as calculated prefix",
    forAll(prefixGenerator)(prefix => {
      prefix == IpNetworkMask.fromPrefixNotation(prefix).prefix
    }))
}