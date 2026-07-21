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
```

> **Note:** `pom.xml` sets `forkCount=0` for tests. This works around a known Windows issue
> where Maven's test process can report `BUILD FAILURE` right after all tests already passed,
> because of a broken communication channel — not a real test failure. Running tests in the
> same process instead of a separate one avoids it entirely.

## See it catch a real violation

1. Open `ServiceA.java`.
2. Add `import com.isolationtax.demo.services.c.ServiceC;` and reference
   it anywhere in the class body.
3. Run `mvn test` again — `ServiceIsolationTest` fails, naming both
   services.
4. Revert the change. It passes again.

## How this differs from demo-app

`demo-app` proves the *behavior* — fewer direct dependencies, a smaller
call graph, visible over HTTP. This proves the *rule itself stays
enforced* — nobody can accidentally reintroduce a direct dependency
without the build telling them immediately, without needing to remember
to call an endpoint and check.
