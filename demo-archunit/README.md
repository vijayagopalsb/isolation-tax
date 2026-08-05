# isolation-tax demo (ArchUnit)

A second companion to the [isolation-tax article](../README.md), alongside
[`demo-app/`](../demo-app/). Where `demo-app` shows the difference at
runtime — call it and see the trace — this one enforces it at build time:
an ArchUnit test that fails the moment Service A, Service B, or Service C
reach into each other directly.

## What's here

- `ServiceA`, `ServiceB`, `ServiceC` — three peer services, matching the
  article's original Service A/B/C example, each isolated in its own
  package.
- `Gateway` — the shared door. Routes by service id, owns no business
  logic.
- `ServiceIsolationTest` — the actual enforcement: an ArchUnit rule that
  fails the build if any two services depend on each other directly.
- `GatewayTest` — a plain functional test, proving routing itself works.

## Run it

```bash
mvn test