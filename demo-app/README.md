# isolation-tax demo

A runnable companion to the [isolation-tax article](../README.md). It implements the article's
"Real-World Example: Dependency Sprawl" scenario twice — once without Lateral Isolation, once
with it — so the difference between the two is something you can hit with curl instead of just
read about.

## What's here

- `OrderServiceDirect` — calls all 10 peer services (`PaymentService`, `InventoryService`, ...)
  directly. This is the "temptation" from the article.
- `OrderServiceIsolated` — calls only `ServiceGateway`, which is the single place that owns
  routing, logging, and auth, and the only thing that talks to the peer services.
- `DemoController` — exposes both flows as JSON, including a full call trace and the set of
  `OrderService`'s *direct* dependencies, so the "N vs. one door" claim in the article is visible
  in the response, not asserted.

## Run it

```bash
mvn spring-boot:run
```

Then, in another terminal:

```bash
curl http://localhost:8080/demo/without-isolation
curl http://localhost:8080/demo/with-isolation
```

`without-isolation` reports 10 direct dependencies for `OrderService`. `with-isolation` reports
exactly 1: `Gateway`.

## Test it

```bash
mvn test
```

Tests assert the shape of each call graph directly:

- `OrderServiceDirectTest` — `OrderService` has 10 direct edges, one per peer.
- `OrderServiceIsolatedTest` — `OrderService` has exactly one direct edge (`Gateway`), and the
  gateway is the one that reaches all 10 peers on its behalf.
- `DemoControllerTest` — the two endpoints return the expected dependency counts.

> **Note (Windows/Maven):** if `mvn test` fails at the very end with
> `SurefireBooterForkException: Error occurred in starting fork` *after* all test classes have
> already logged as passing, that's a known flaky fork-startup issue in this environment, not a
> test failure — check `target/surefire-reports/*.txt` to confirm all tests passed, or run with
> `mvn test -DforkCount=0` to execute tests in-process and avoid it entirely.