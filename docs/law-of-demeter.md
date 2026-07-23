# Law of Demeter → Lateral Isolation

[Back to README](../README.md)

The patterns named in the main README — Facade, Mediator, API Gateway, ESB (Enterprise Service Bus) — are
the service-level ancestors of Lateral Isolation. There's an older, smaller-scale
one worth naming too, because it's the same principle at a different granularity —
not a distant cousin, but a direct one.

## The object-level version

The **Law of Demeter** (Ian Holland, Northeastern University, 1987) is a guideline
for object-oriented code: a method on object `A` should only call methods on `A`
itself, on objects passed to that method, on objects `A` creates, or on `A`'s own
fields. It should not call a method on an object obtained *through* another object
— the canonical violation is a chain like `a.getB().getC().doSomething()`.
Informally: talk to your immediate friends, not to strangers you found by
rummaging through a friend's belongings.

## The same violation, one layer up

Service A reaching directly into Service C for a single field is the exact same
violation, one layer up. The object graph became a service graph; the "stranger"
became a network peer; the fragility became a dependency nobody can draw an
accurate diagram of six months later. This is precisely the dependency sprawl
scenario described in the main README — the OrderService that starts with two
integrations and quietly accumulates eight more, one "just this once" direct call
at a time.

Lateral Isolation is the Law of Demeter enforced at service granularity instead
of object granularity — arguably its most direct architectural descendant, more
so than Facade or Mediator, which solve a related but distinct problem of
*interface simplification* rather than *topology control*. Facade makes the
subsystem easier to use without forbidding direct access. Mediator and Lateral
Isolation actually forbid the shortcut. Law of Demeter is a guideline you're
expected to follow but nothing stops you from breaking; Lateral Isolation, when
enforced structurally (no network route exists from A to C at all), can't be
broken by accident the way a method chain can.

## Where they diverge — and why this repo needed a decision rule

The two ideas share a diagnosis but not a price tag, and that difference is the
whole reason this repo exists.

Violating the Law of Demeter costs you fragile code and harder refactors — real,
but cheap, and paid entirely inside one process, inside one deploy. Enforcing
Lateral Isolation costs real, measurable latency and creates an actual single
point of failure, because the "extra indirection" now crosses a network boundary
instead of a method call. In many cloud deployments that's roughly 5–10 ms per
hop — negligible for most calls, but not for a hot path, and not free at scale.

Nobody writes "Law of Demeter: when to skip it" articles, because the math
rarely justifies skipping it — the cost of following the rule is close to zero,
so there's nothing to weigh. Lateral Isolation needed a real decision rule (see
the main README's *"The Decision Rule"* section) precisely because at network
scale, the move that Law of Demeter treats as free isn't free anymore. You have
to actually decide, boundary by boundary, whether what you're protecting is
worth the hop it costs you.

## The compressed version

> Lateral Isolation is the Law of Demeter applied to services, except at
> network scale, the free move isn't free anymore.

---

[Back to README](../README.md)