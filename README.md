# HTTP log monitoring console program

## Requirements

The program has been written in Java 8, so you need at least a JRE 1.8+ installed. You also need maven to build
the project.

## Getting Started

You can build the project by launching the command `mvn clean install` in the root directory. You can then find the 
resulting jar file in the target directory. 

```

Usages: java -jar PATH_TO_JAR_FILE [options]. e.g java -jar http-log-monitoring-1.0-SNAPSHOT.jar -AW 5 -AT 5
 
             -P: specifies the path of the log file (/tmp/access.log by default)
             -R: specifies the stats display rate in seconds (10s by default)
             -AT: specifies the average section hits above which an alert should be emitted (10 by default)
             -AW: specifies the alert window duration in seconds on which the average section hits is
             calculated upon (120s by default)

```

## Overview

The program consumes an actively written-to w3c-formatted HTTP access log file and should follow these principles:

* Display stats every 10s about the traffic during those 10s: the sections of the web site with the most hits, 
as well as interesting summary statistics on the traffic as a whole. A section is defined as being what's before 
the second '/' in the resource section of the log line. For example, the section for "/pages/create" is "/pages"

* A user can keep the app running and monitor the log file continuously
* Whenever total traffic for the past 2 minutes exceeds a certain number on average, a message saying that
 “High traffic generated an alert - hits = {value}, triggered at {time}” is printed. 
 The default threshold should be 10 requests per second, and is overridable.
* Whenever the total traffic drops again below that value on average for the past 2 minutes, a message detailing when 
the alert recovered is printed.
* All messages showing when alerting thresholds are crossed remain visible on the page for historical reasons.

## Design improvements

* The console printed messages are not that great. The statistics could have more details for example include some stats on
IPs.

* It is not ready for tremendous amount of data. The locations where HashMap are used can cause the JVM to crash due to an
OutOfMemoryError. HashMap could be replaced by a noSQL DB distributed over a chosen number of clusters. Every section name could 
be hashed with SHA2 for e.g, converted back to base 10 and then use modulo over the chosen number of clusters to have an
even repartition of data across the servers. To go further we can even use a probabilist data structure for example a bloom
filter in order to reduce the calls to the database.

* Nothing is persisted, we lose statistics we have calculated and if the program shuts down, we also lose all in-memory data.
