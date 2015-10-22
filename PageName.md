#Version 0.3 notes

# Introduction #

The goal of version 0.3 of scala-ipv4 was to update it to Scala 2.8.1 and convert it from a maven project to an SBT project.  No API-level changes were made.


# Details #

To use the project, download the source, and invoke sbt-0.10.1 from the top-level directory.  Example:

```
jjstanford-mbp:scala-ipv4 jstanford$ java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -Xss2M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256m -Xmx768M -jar $SBT_HOME/sbt-launch-0.10.1.jar "$@"
Listening for transport dt_socket at address: 5005
[info] Set current project to default-54a3d6 (in build file:/Users/jstanford/.sbt/plugins/)
[info] Set current project to default-46f26b (in build file:/Users/jstanford/Development/google_code/scala-ipv4/)
> ;compile ;test ;package
[info] Updating {file:/Users/jstanford/Development/google_code/scala-ipv4/}default-46f26b...
[info] Done updating.
[success] Total time: 1 s, completed Oct 1, 2011 8:08:28 AM
[info] IpNetworkMaskTest:
[info] - mask or prefix construction
[info] - prefix used by construction is same as calculated prefix
[info] IpAddressPoolTest:
[info] - allocate next
[info] - allocate to exhaustion
[info] - allocate specific address
[info] - allocate address outside pool range
[info] - deallocate address outside pool range
[info] - allocate unfree address
[info] - allocate and free
[info] - deallocate and free
[info] - deallocate defragmentation
[info] - performance test shouldn't crash with stack overflow error !!! IGNORED !!!
[info] - list free addresses
[info] - list allocated addresses
[info] IpAddressTest:
[info] - create from string 0.0.0.0
[info] - create from null string
[info] - create from null InetAddress
[info] - create from invalid string
[info] - create from invalid length string
[info] - create from string 255.255.255.255
[info] - create from invalid string 0.0.0.500
[info] - create from long too large
[info] - create from long too small
[info] - to and from string conversion 192.168.254.115
[info] - to and from string conversion 255.255.255.255
[info] - addition
[info] - substraction
[info] - addition and substraction inverse each other
[info] - addition overflow
[info] - substraction underflow
[info] - greather then
[info] - greather then or equals
[info] - construct from InetAddress
[info] - pattern match
[info] - plus and minus inverse each other
[info] - toString yields the same string it was constructed with
[info] IpNetworkTest:
[info] - to string
[info] IpNetworkMaskExample:
[info] An IpNetworkMask 
[info] - should be constructable from a String
[info] - should be constructable from a prefix length
[info] The construction of an invalid IpNetworkMask 
[info] - should fail
[info] IpAddressPoolCheck:
[info] - allocated addresses are not free
[info] IpAddressExample:
[info] An IpAddress 
[info] - should be constructable from a String
[info] - should be constructable from a long
[info] - should be constructable from a java.net.InetAddress
[info] An IpAddress 
[info]   when constructed from a String 
[info]   - should be convertable back into the same string
[info]   when constructed from a java.net.InetAddress 
[info]   - should be convertable back into the same InetAddress
[info] An IpAddress 
[info] - should have an operator to find the next ip address
[info] - should have an operator to find the previous ip address
[info] - should have + and - operators that don't overflow but wrap around
[info] - should be comparable
[info] IpAddressRangeExample:
[info] An ip address range 
[info] - should be constructable from two ip addresses
[info] - should have a length equal to the number of addresses in the range
[info] - should have a method to check if it contains an address
[info] - should have a method to check if it contains another range
[info] - should have a method to check if it overlaps with another range
[info] - should be comparable
[info] - should have a method to subtract something from the range, resulting in two different ranges
[info] - should have a method to iterate through all addresses
[info] IpNetworkExample:
[info] An ip network 
[info] - should be constructable from two ip addresses
[info] - should be constructable from its CIDR notation
[info] - should align with the longest matching prefix
[info] - should have a network mask
[info] - should be an ip address range
[info] MacAddressTest:
[info] - create from string 0.0.0.0.0.0
[info] - create from null string
[info] - create from invalid string
[info] - create from invalid length string
[info] - create from string FF.FF.FF.FF.FF.FF
[info] - individual bytes are padded with zeros when required
[info] - constructing from string without padding is ok
[info] - create from long too large
[info] - create from long too small
[info] - to and from string conversion 1F.AB.CD.99.34.23
[info] - to and from string conversion FF.FF.FF.FF.FF.FF
[info] - addition
[info] - substraction
[info] - greather then
[info] - greather then or equals
[info] IpAddressRangeTest:
[info] - string representation
[info] - contains
[info] - contains lower bound
[info] - contains upper bound
[info] - doesn't contain because too large
[info] - doesn't contain because too small
[info] - addresses list
[info] - very large address list shouldn't go out of memory
[info] - compare
[info] Passed: : Total 87, Failed 0, Errors 0, Passed 86, Skipped 1
[success] Total time: 21 s, completed Oct 1, 2011 8:08:50 AM
[info] Packaging /Users/jstanford/Development/google_code/scala-ipv4/target/scala-2.8.1.final/scala-ipv4_2.8.1-0.3.jar ...
[info] Done packaging.
[success] Total time: 1 s, completed Oct 1, 2011 8:08:50 AM
> 

```

If all goes well, the jar file will be placed in the target/scala-2.8.1.final folder and you can move it to your classpath or upload it to your repository.