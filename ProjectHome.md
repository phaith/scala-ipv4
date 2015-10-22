scala-ipv4 is a Scala library for IPv4 related concepts such as IP addresses, network masks, address pools, etc.

# Key Concepts #

## IpAddress ##

IpAddress represents an IPv4 address.

```
val address = new IpAddress("192.168.0.1")
```

IpAddress can be used to make calculations on IP addresses, such as finding the next address.

```
val address = new IpAddress("192.168.0.1")
val next = address + 1
println(next == new IpAddress("192.168.0.2")) // prints true
```

## IpAddressRange ##

IpAddressRange represents a continuous range of consecutive addresses.

```
val range = new IpAddressRange(new IpAddress("192.168.0.1"), new IpAddress("192.168.0.5"))
println(range.contains(new IpAddress("192.168.0.3")) // prints true
```

## IpNetworkMask ##

IpNetworkMask represents a network mask, to be used in an IpNetwork.

```
val mask1 = new IpNetworkMask("255.255.255.128")
val mask2 = IpNetworkMask.fromPrefixLength(25)
println(mask1 == mask2) // prints true

val invalid = new IpNetworkMask("255.255.255.100") // throws exception

```

## IpNetwork ##

An IpNetwork is a range (extends IpAddressRange) that can be expressed as a network address and a network mask.

```
val network1 = new IpNetwork(new IpAddress("192.168.0.0"), new IpNetworkMask("255.255.255.0"))
val network2 = new IpNetwork("192.168.0.0/24")
println(network1 == network2) // prints true
```

## IpAddressPool ##

An IpAddressPool is like a range (extends IpAddressRange) of which certain addresses are "allocated" and other are "free".

```
var pool = new IpAddressPool(new IpAddress("1.2.3.4"), new IpAddress("1.2.3.10"))
println(pool.isFree(new IpAddress("1.2.3.6"))) // prints true
pool.allocate(new IpAddress("1.2.3.6")) match {
  case (newPool, allocated) => {
    println(newPool.isFree(new IpAddress("1.2.3.6"))) // prints false
  }
}
```

## And Much More ##

Much more can be done with these types. Have a look at the [scaladoc](http://scala-ipv4.googlecode.com/svn/artifacts/0.1/doc/scaladocs/index.html) or the test sources (especially the ones ending in "Example") to get an idea of the possibilities.