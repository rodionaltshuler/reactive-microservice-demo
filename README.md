## Reactive API demo

#### It's a demo project to show (and get hands-on experience) with the following features Spring5 and Redis provides:

1. Redis Streams (available since Redis 5.0) to store time series data and get realtime updates 
2. Kotlin support: Route functional DSL
3. Use reactive web client WebClient instead of Feign
4. Provide SSE (server-sent events) based API
5. Testing: evaluate what changes from testing endpoint when we use those fancy features 

#### Usage

Run from terminal and observe ticks from Bittrex crypto exchange delivered with 3s interval via SSE:

`curl -X GET http://localhost:6002/tick?market=usd-btc`

Push data item to stream:
`curl -X POST http://localhost:6002/streams/btc-usd \ -d someplaintextdata`

Observe events stream and get new items in real time as SSE (use in browser, Postman doesn't support SSE yet)
`http://localhost:6002/streams/btc-usd`