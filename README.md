
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

- Queueing strategies
    - Prefer first
    - Prefer round-robin
    - Pinned, e.g. all hamburgers to same cook
        - Hash the message, put to modulo buckets, each consumer takes from a module of buckets
- Queues should always be bound, because they are anyways bound by something (memory, disk space), but those bounds are not known and they vary for unknown reasons. Always bind by something explicit.
    - Bound by number
    - Bound by time (buffer at most x seconds, TTL)
        - If message expired, drop it (keep TTL a bit lower than timeout so that the client will know about it)
    - Block producer vs. drop messages


## Pub Sub

Starting the system should happen in the following non-overlapping stages. Avoids pains.
1. Create
2. Subscribe
3. Start
4. Stable

Subscribe may use locks, because it's called rarely. Publish needs to be fast; should be lock-free.

## Correlation and Causation ID

- "What was the external stimuli which caused this even (possibly many levels down)"
- Easy to build a graph visualizing the message flow

```
                Id  CorrId  CauseId      
OrderPlaced     7   19      -1
OrderCooked     8   19      7
OrderPriced     9   19      8
OrderPaid       10  19      9
```

## Process Managers

- Pay before eating: needs software changes to all 4 actors (4 microservices, 4 teams) because they all need to change to whom they are sending/receiving messages
- To form a view of the business process, will need to talk to everybody because they all know just a piece of the puzzle
- We add a new actor, "midget", who goes to the other actors. The other actors now only need to say when they are done. To pay before eating, only the midget needs a software change.
- We could have a Dutch midget (pay after eating) and a Zimbabwean midget (pay before eating), chosen for each order separately. For example pay first if the customer looks dodgy.
- Common design failures of implementing process managers: midget goes to kitchen to cook food, or the cook still talks to other actors. Need proper separation of concerns. Process managers should just be simple state machines which route messages.

- Every process manager gets started by *one* message (e.g. OrderPlaced)
- On order placed:
    1. Create midget
    2. Subscribe to correlation ID
        - Subscribing the MidgetHouse makes things simpler
            - wrapped by ThreadedHandler to ensure linearization/single-threadedness
            - a single place to do tracing etc.
        - Subscribing the Midget directly may produce concurrency issues (would need to handle linearization somehow)
- May add event from midget to house "I am done" so that midget can be removed
