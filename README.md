
# Event  Sourced Process Managers

## Actors

- Waiter
    - takes order from customer
    - writes items to document
- Cook
    - makes the food
    - writes time taken and ingredients to document 
- Assistant manager 
    - calculates tax and totals, writes them to document
- Cashier
    - writes "paid" to document
    - puts document on spike
- Manager
    - takes and stores documents at the end of the day

## Passing a document through microservices

- Each microservice should pass on unchanged all data it doesn't know 
    - Wrap JSON in an object which provides accessors which are backed by the JSON instead of fields
    - Do not change or remove unknown data from the JSON

## Queues

- By monitoring the queues you can see the performance bottlenecks
    - Watch the length of queue and it's relation to the items per second processed
- Head of the line problem (e.g. muslim in airport security check) vs. How fair can you be
    - Ideal throughput on many lines, but latency and starvation issues
- There is no such thing as uniform load (GC, network, disk, context switches etc.), must assume non-uniform workload
    - https://genius.com/James-somers-herokus-ugly-secret-annotated
    - Round robin vs American passport lines
