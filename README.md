
# Gemfire Starbucks

This is an example project exploring features of Gemfire

## Overview

A starbucks client application simulates customers placing CoffeeRequest orders in the system. Barista(Cache)listeners on the servers simulate baristas concurrently picking up CoffeeRequest entries, marking them as in-progress. When they are done with a coffee, they put back PreparedCoffee entries to signal the client that the coffee is ready.
The server-side BaristasListeners expose their work-queue statistics which can be queried using a server function. The monitoring client uses this to sample load metrics.


## Prerequisites

To build and run this project you need to have

* maven 3 installed and on your path
* Gemfire on your path


## Running the example

1. Start the locator and 3 cache instances using:
    $>./start_caches
	
2. Start a new terminal for the adminclient to watch for administrative cache events in the cluster and run
	$>./adminclient

3. Start a new terminal for the monitorclient to sample the barista queues in 2s intervals and run
	$>./monitorclient
	
4. Start a new terminal the starbucksclient to simulate customers placing coffee orders and run
	$>./starbucksclient
	
Hitting "Enter" ends the applications


## Features

* Uses partitioned regions

    <region name="PreparedCoffees">
        <region-attributes statistics-enabled="true" data-policy="partition">
            <partition-attributes redundant-copies="1"/>
        </region-attributes>
    </region>

	
* The starbucks client that
  - puts CoffeeRequests
  - continuously queries for PreparedCoffees
	
* CacheListener that listens for CoffeeRequest create events

* Captures statistics in ./target/server<nr>/stats.gfsh

* Uses a function to poll for barista work queue metrics

* Uses a custom BaristaStatisticsCollector to aggregate barista metrics from all servers

* Uses a continuous query to listen for PreparedCoffee entries that indicate that the coffee is ready

* Shows how to listen to administrative events like create/destroy regions etc.
 

## Display partition/bucket info and data of PreparedRequests using gfsh

<pre>
$ gfsh -l localhost:40404

gfsh:/>ls
Subregions:
   CoffeeRequests
   PreparedCoffees

gfsh:/>cd PreparedCoffees

gfsh:/PreparedCoffees>pr -b

....

gfsh:/PreparedCoffees>size -m

     Region: /PreparedCoffees
Region Type: Partitioned
Member Id                         Member Name  Region Size
---------                         -----------  -----------
localhost(5476)<v1>:54487/51669                       157
localhost(7256)<v1>:44286/51670                       175
localhost(7956)<v1>:23170/51671                       139

                                        Total:         471
</pre>
