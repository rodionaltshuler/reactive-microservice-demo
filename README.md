## Reactive API demo

#### It's a demo project to show (and get hands-on experience) with the following features Spring5 provides:

1. Kotlin support: Route functional DSL
2. Use reactive web client WebClient instead of Feign
3. Provide SSE (server-sent events) based API
4. Integrate this features with components microservice usually use (Hystrix for example)
5. Testing: evaluate what changes from testing endpoint when we use those fancy features 

#### Usage

Run from terminal and observe ticks from Bittrex crypto exchange delivered with 3s interval via SSE:

`curl -X GET http://localhost:6002/tick?market=usd-btc`