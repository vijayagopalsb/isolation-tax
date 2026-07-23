# Isolation Tax — Spring Boot Demo: Facade vs. Enforced Lateral Isolation

A companion project to [The Isolation Tax](https://github.com/vijayagopalsb/isolation-tax),
demonstrating in real, runnable code what that article and the Facade
pattern discussion only covered in prose: **Facade and Lateral Isolation
solve related but distinct problems, and only one of them can be proven
to hold structurally.**

## The three variants

| Variant | Endpoint | What it shows |
|---|---|---|
| **v1 — Direct** | `GET /v1/order` | No Facade. `OrderServiceDirect` depends on all four subsystems directly — the "one line of code, quick hop" temptation the original article opens with. |
| **v2 — Facade, unenforced** | `GET /v2/order` and `GET /v2/order/bypass` | A Facade (`OrderFacade`) exists and simplifies the call — but `OrderServiceBypass` proves nothing stops another class from reaching around it and calling the subsystems directly anyway. |
| **v3 — Facade enforced as Lateral Isolation** | `GET /v3/order` | Same Facade pattern, but the four subsystem classes are **package-private**. No class outside `v3facadeenforced` can even import them, let alone call them. The compiler enforces the boundary — not a code review comment. |

## Running it

```bash
mvn spring-boot:run
```

Then:

```bash
curl http://localhost:8080/v1/order
curl http://localhost:8080/v2/order
curl http://localhost:8080/v2/order/bypass
curl http://localhost:8080/v3/order
```

Compare the `took_ms` field across v1, v2, and v3 — v3 should read a few
milliseconds higher, because `OrderGateway` charges a simulated
`simulateGatewayOverhead()` cost on top of the four subsystem hops. That
extra cost is the literal "isolation tax" the original article is about,
made measurable instead of abstract.

## Proving the isolation, not just claiming it

```bash
mvn test
```

`ArchitectureRulesTest` uses [ArchUnit](https://www.archunit.org/) to
assert that the v3 subsystem classes are only ever accessed from within
their own package. Try writing the equivalent rule against
`v1direct` or `v2facadeunenforced` — it will fail, because
`OrderServiceDirect` and `OrderServiceBypass` both reach the shared
`subsystems` package with nothing to stop them. That contrast — one
rule holds, the other can't even be written truthfully — is the entire
argument of this repo in executable form.

## Why this matters

The Facade pattern gives you a clean interface. It does not, by itself,
forbid anyone from ignoring that interface and calling the subsystem
directly — v2 proves this literally, with working code. Lateral
Isolation is the additional, stricter guarantee: not just *a* clean way
in, but the *only* way in. v3 shows what it actually takes to get that
guarantee in Java: package-private visibility, not a naming convention
or a comment saying "please use the Facade."

## Note on verification

This project was written and reviewed for correctness by hand, but has
not been compiled or run in the environment it was authored in (Maven
Central was not reachable there). Please run `mvn compile` and
`mvn test` yourself before relying on it, and report back if anything
fails to build — most likely candidate for a fix would be the ArchUnit
rule syntax in `ArchitectureRulesTest`.
