# isolation-tax
A discipline for deciding when to enforce lateral isolation between peer services — with runnable code and an ArchUnit test proving it.

![Header illustration](images/illustration.png)

> **Want to see the shape of this, not just read about it?** [`demo-app/`](demo-app/) is a small
> Spring Boot project that runs the "Dependency Sprawl" example below both ways — `OrderService`
> calling every peer directly vs. routed through a single `ServiceGateway` — and returns the
> resulting call graph as JSON so the "N vs. one door" claim is something you can hit with curl.
> **Want the rule enforced, not just demonstrated?** [`demo-archunit/`](demo-archunit/) is a second,
> smaller companion: Service A, Service B, and Service C, isolated the way the article describes,
> with an ArchUnit test that fails the build the moment any two of them depend on each other directly.

## Temptation

Imagine three services — call them Service A, Service B, and Service C. (This is a deliberately simplified 
illustration, not a real incident — the pattern is common enough that a stylized example serves better than 
a specific war story.) Service A needs one small thing from Service C: a lookup, a status check, a single field. 
The direct call is one line of code and a network hop away. Going through a shared entry point instead means 
an extra round trip, another log line, another thing to configure.

That's the moment this article is about. Not the architecture diagram — the moment right before someone decides 
whether to take the shortcut.

## Lateral Isolation

In this article, I'll call this discipline Lateral Isolation: preventing direct communication between peer components by requiring all interactions to pass through a controlled boundary.

![Direct peer-to-peer communication without lateral isolation](images/direct-peer-to-peer.png)

**Figure 1. Direct Peer-to-Peer Communication (Without Lateral Isolation)**

![Controlled communication through a shared boundary with lateral isolation](images/shared-boundary.png)

**Figure 2. Controlled Communication Through a Shared Boundary (With Lateral Isolation)**


## Naming the rule

This article does not introduce a new pattern or principle. It applies two existing ones — Information Hiding and the Acyclic Dependencies Principle — more strictly than usual, using patterns that already have names. The discipline is simple to state: every path runs through one door. No service (or module) calls another laterally; everything is routed through a shared entry point that owns cross-cutting concerns — authentication, logging, routing — and nothing else talks around it.

This isn't a new idea, and it's worth naming its ancestors up front rather than hoping nobody notices the resemblance:
- **Facade** (Gamma, Helm, Johnson, Vlissides — *Design Patterns*, 1994) gives a subsystem one simplified interface instead of many entry points.
- **Mediator** (same book) goes further: it explicitly forbids colleagues from referencing each other directly, forcing all interaction through a central object. The "no Service A ↔ Service C" rule is Mediator's core idea, applied at service/module granularity instead of object granularity.
- **API Gateway** (widely documented by Chris Richardson[^2] and others in the microservices literature) is the same shape applied to client-facing traffic — one door, centralized auth/logging/routing, many services behind it.
- **Enterprise Service Bus** is what this idea became at enterprise scale in 2000s SOA — a central bus that every integration flowed through.

None of that is a criticism of the rule. It's context. A reader who knows these patterns will recognize the shape immediately, and pretending otherwise is the fastest way to lose their trust.

There's also a direct object-level ancestor — see [Law of Demeter → Lateral Isolation](docs/law-of-demeter.md) for the full comparison.


## What you buy

The payoff is real, and it's the reason this discipline keeps getting reinvented under different names:

- **One contract to audit.** Every access path funnels through the same gate, so there's one place to check auth, one place to check logging, one place to reason about who can reach what.
- **No hidden coupling.** Without the rule, "just this once" direct calls accumulate. Eighteen months later nobody can draw an accurate dependency diagram, because half the real dependencies never went through the documented interfaces.
- **N instead of N².** A system with a shared door needs one contract per service. A system without it risks a contract for every pair that ever decided to talk directly.

## Real-World Example: Dependency Sprawl

> Consider an e-commerce platform where the OrderService initially communicates only with the PaymentService and InventoryService. As new business capabilities are introduced, developers begin adding direct integrations with ShippingService, NotificationService, TaxService, DiscountService, LoyaltyService, FraudDetectionService, AuditService, and AnalyticsService.
>
> Each new requirement seems harmless in isolation — "just one more dependency." Over time, however, the OrderService evolves into a central hub with numerous direct connections. Understanding its behavior becomes difficult, testing requires mocking many collaborators, and even a small change in one dependency can have unexpected side effects. The architecture gradually suffers from **dependency sprawl**, where hidden and ever-growing dependencies make the system harder to maintain and evolve.
>
> By enforcing a single, well-defined access path — for example, through a shared façade, mediator, or event-driven interaction — these direct dependencies are replaced with a controlled interaction model. The result is a cleaner dependency graph, improved maintainability, and an architecture whose relationships remain easy to understand and audit even as the system grows.

## What it costs

This is the part that's usually left out, and it's the part that actually matters for deciding whether to adopt the rule.

**Latency.** A → Gateway/Mediator → C is strictly slower than A → C. If Service A and Service C are on a hot path — something latency-sensitive, called at high frequency — that extra hop isn't free. In many cloud-based microservice deployments, an additional network hop may add roughly 5–10 ms of latency, depending on the infrastructure, network conditions, and deployment topology. For most business applications this overhead is negligible, but on high-throughput or latency-sensitive paths it can accumulate into measurable P99 latency. Pretending the cost does not exist turns "architectural discipline" into "unexplained P99 regression" six months later.

**Centralization risk.** The shared boundary now sits on every path in the system. If it goes down, misconfigures, or gets slow, everything downstream feels it. This is exactly the criticism that ended the ESB's dominance: Martin Fowler and James Lewis's microservices writing argued for "smart endpoints, dumb pipes"[^1] — pushing logic and direct communication out to the edges rather than concentrating it in one bus, precisely because a central hub becomes both a bottleneck and a single point of failure as a system grows.

**A rule applied past its usefulness.** If the boundary accumulates business logic beyond auth/logging/routing, it stops being a thin door and starts being a monolith with extra steps — the exact coupling problem the rule was meant to prevent, just relocated.

## The decision rule

Not "always," not "never." A concrete way to decide:

**Enforce lateral isolation when:**

- the boundary is also a trust or compliance boundary (different auth requirements, different data sensitivity, an audit trail is a real requirement, not a nice-to-have)
- the calls are low-frequency or not latency-sensitive
- the number of potential lateral relationships is large enough that an explicit contract is genuinely cheaper than tracking implicit ones

**Skip it, or relax it, when:**

- the calling pattern is a hot path where the added hop shows up in your actual latency budget
- the two sides are deployed and owned together, so the "isolation" buys governance you don't need
- the boundary would need business-specific logic to do the routing, not just auth/logging — that's a sign the boundary is artificial

![Lateral Isolation Benefits VS Tax](images/lateral-isolation-benefits-vs-tax.png)

### 🟢 Enforce lateral isolation (high benefit, low tax)

> **Enforce it when:**
> - The boundary acts as a strict trust or compliance boundary where audit trails are mandatory.
> - The calls are low-frequency and not sensitive to latency.
> - The number of potential relationships makes an explicit contract cheaper than tracking implicit ones.

| Diagram label | Article bullet |
|---|---|
| Compliance / audit boundary | "The boundary acts as a strict trust or compliance boundary where audit trails are mandatory." |
| Low-frequency, non-latency calls | "The calls are low-frequency and not sensitive to latency." |

### 🟠 Skip it (low benefit, high tax)

> **Skip it when:**
> - The interaction sits on a latency-sensitive hot path.
> - The communicating services are deployed and owned together.
> - The shared boundary would require business-specific logic to route the request, signaling that the boundary might be artificial.

| Diagram label | Article bullet |
|---|---|
| Hot-path, latency-sensitive call | "The interaction sits on a latency-sensitive hot path." |
| Same-team, tightly coupled services | "The communicating services are deployed and owned together." |

### Note
The article lists a third bullet in each group — "number of potential relationships" (enforce) and "business-specific routing logic" (skip) — which aren't plotted as separate dots, since they describe *conditions for judging* a boundary rather than standalone scenarios like the other four. Happy to add them as two more points if you'd like all six bullets represented.

## Verdict

Lateral isolation isn't free, and it isn't a default. It's a tax you choose to pay for auditability and decoupling, and like any tax, it's worth it exactly when what you're protecting is worth more than the hop it costs you. Name the pattern you're actually using, be honest about the latency and centralization price, and make the call boundary-by-boundary rather than as a blanket rule. That's the whole discipline — everything else is implementation detail.

[^1]: James Lewis and Martin Fowler, "Microservices" (2014), https://martinfowler.com/articles/microservices.html

[^2]: Chris Richardson, "Pattern: API Gateway / Backends for Frontends," microservices.io, https://microservices.io/patterns/apigateway.html

