package com.template

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.contracts.FungibleAsset
import net.corda.core.identity.CordaX500Name
import net.corda.core.messaging.startFlow
import net.corda.core.node.services.vault.PageSpecification
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.node.services.vault.Sort
import net.corda.core.node.services.vault.builder
import net.corda.core.utilities.OpaqueBytes
import net.corda.core.utilities.getOrThrow
import net.corda.core.utilities.loggerFor
import net.corda.finance.GBP
import net.corda.finance.POUNDS
import net.corda.finance.flows.CashIssueFlow
import net.corda.finance.flows.CashPaymentFlow
import net.corda.finance.schemas.CashSchemaV1
import net.corda.testing.driver.DriverParameters
import net.corda.testing.driver.driver
import net.corda.testing.node.User
import java.util.*

class NodeDriver {
    companion object {
        private val log = loggerFor<NodeDriver>()
        @JvmStatic
        fun main(args: Array<String>) {
            val user = User("user1", "test", permissions = setOf("ALL"))
            log.info("Starting network")
            driver(DriverParameters(isDebug = true, waitForAllNodesToFinish = true, startNodesInProcess = true, extraCordappPackagesToScan = listOf("net.corda.finance"))) {
                val partyA = startNode(providedName = CordaX500Name("PartyA", "London", "GB"), rpcUsers = listOf(user)).getOrThrow()
                val partyB = startNode(providedName = CordaX500Name("PartyB", "New York", "US"), rpcUsers = listOf(user)).getOrThrow()
                log.info("nodes are ready, connecting to via RPC")
                val partyAProxy = CordaRPCClient(partyA.rpcAddress).start(user.username, user.password).proxy
                val partyBProxy = CordaRPCClient(partyB.rpcAddress).start(user.username, user.password).proxy
                log.info("caching the notary")
                val notary = partyAProxy.notaryIdentities().first()
                log.info("issuing cash to partyA")
                partyAProxy.startFlow(::CashIssueFlow, 1000.POUNDS, OpaqueBytes.of(0), notary).returnValue.getOrThrow()
                log.info("cash issued to party A")
                val result1 = partyAProxy.vaultQueryBy(generateCashSumCriteria(GBP), PageSpecification(), Sort(emptySet()), FungibleAsset::class.java)
                log.info("balance ${result1.otherResults[0]}")
                log.info("paying from party A to party B")
                partyAProxy.startFlow(::CashPaymentFlow, 10.POUNDS, partyBProxy.nodeInfo().legalIdentities.first()).returnValue.getOrThrow()

                // NOTE: on my laptop the program never reaches the next line!!

                log.info("paid from party A to party B")
                val result2 = partyBProxy.vaultQueryBy(generateCashSumCriteria(GBP), PageSpecification(), Sort(emptySet()), FungibleAsset::class.java)
                log.info("balance ${result2.otherResults[0]}")
            }
        }
    }
}

private fun generateCashSumCriteria(currency: Currency): QueryCriteria {
    val sum = builder { CashSchemaV1.PersistentCashState::pennies.sum(groupByColumns = listOf(CashSchemaV1.PersistentCashState::currency)) }
    val sumCriteria = QueryCriteria.VaultCustomQueryCriteria(sum)

    val ccyIndex = builder { CashSchemaV1.PersistentCashState::currency.equal(currency.currencyCode) }
    val ccyCriteria = QueryCriteria.VaultCustomQueryCriteria(ccyIndex)
    return sumCriteria.and(ccyCriteria)
}
