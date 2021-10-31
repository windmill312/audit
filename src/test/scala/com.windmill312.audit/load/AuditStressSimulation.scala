package com.windmill312.audit.load

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class AuditStressSimulation extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://localhost:8101")
  val countryCodeCandidates: Vector[String] = Set("ru", "kg", "kz").toVector
  val random = new Random(System.currentTimeMillis())

  val scenarioBuilder: ScenarioBuilder = scenario("Scenario Name")
    .exec(
      http("incrementVisitNumber")
        .post("/api/v1/visit")
        .queryParam("countryCode", countryCodeCandidates(random.nextInt(countryCodeCandidates.length)))
    )

  setUp(scenarioBuilder.inject(atOnceUsers(1000)).protocols(httpProtocol))
}