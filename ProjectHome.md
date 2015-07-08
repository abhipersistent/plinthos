PlinthOS is a simple software deployment platform for services; in other words, a job tracker. Its goal is to accomplish two things: (a) **resource management**, and (b) **job scheduling**. Only the second aspect of PlinthOS is currently implemented.

The resource management is effectively a combination of manual work and configuration. You can deploy its computational nodes on multicore machines or in more than one machines, and even over a heterogeneous hardware infrastructure. The next version of PlinthOS will leverage [Cloudify](http://www.cloudifysource.org/) for its resource management.

PlinthOS defines a service interface that providers must implement, it implements a straightforward but yet powerful provider registration API, and it offers a service access API (strictly speaking, a methodology) for the access of these services by clients. You can think of PlinthOS as a generalization of Apache Hadoop YARN -- or rather think of Apache Hadoop YARN as a specialization of PlinthOS since the first implementation of the latter was written in 2003.

---
Plinthos is written 100% in Java. The default database for PlinthOS is HSQLDB, and MySQL is fully supported. It is trivial to use any other database for which you have a JDBC driver.
