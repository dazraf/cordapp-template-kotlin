# Issue with Driver DSL on my laptop (at least)

To reproduce/test, run the [NodeDriver](cordapp/src/test/kotlin/com/template/NodeDriver.kt) with `-ea -javaagent:lib/quasar.jar`.

The flow is a basic cash issuance, balance check, payment and subsequent balance check between two parties.

Uses Corda 3.2 OSS.

[Line 49](cordapp/src/test/kotlin/com/template/NodeDriver.kt#L49)

The output log from the line

```kotlin
partyAProxy.startFlow(::CashPaymentFlow, 10.POUNDS, partyBProxy.nodeInfo().legalIdentities.first()).returnValue.getOrThrow()
```

is as follows:

```
[INFO ] 09:33:24,827 [nioEventLoopGroup-12-1] (AMQPChannelHandler.kt:49) O=PartyA, L=London, C=GB.channelActive - New client connection e32a17e2 from localhost/127.0.0.1:10000 to /127.0.0.1:63779 {}
[WARN ] 09:33:24,839 [Node thread-3] (AppendOnlyPersistentMap.kt:79) utilities.AppendOnlyPersistentMapBase.set - Double insert in net.corda.node.utilities.AppendOnlyPersistentMap for entity class class net.corda.node.services.identity.PersistentIdentityService$PersistentIdentity key BC0AA7612C81D24A2DC5D0D7E69E0508C7C3549CBD3FF0498C1983CD163DD539, not inserting the second time {}
[INFO ] 09:33:24,863 [nioEventLoopGroup-12-1] (AMQPChannelHandler.kt:89) O=PartyA, L=London, C=GB.userEventTriggered - handshake completed subject: O=PartyA, L=London, C=GB {}
[INFO ] 09:33:24,870 [nioEventLoopGroup-12-1] (AMQPBridgeManager.kt:106) peers.DL6peD56B7zNNTgSnxyqC2PX78CbhfhKLLqj5aeBCRHFL5 -> localhost:10000:O=PartyA, L=London, C=GB.onSocketConnected - Bridge Connected {}
[INFO ] 09:33:24,886 [nioEventLoopGroup-12-1] (ConnectionStateMachine.kt:100) O=PartyB, L=New York, C=US.onConnectionLocalOpen - Connection local open org.apache.qpid.proton.engine.impl.ConnectionImpl@228cb46 {}
[WARN ] 09:33:24,941 [Node thread-3] (AppendOnlyPersistentMap.kt:79) utilities.AppendOnlyPersistentMapBase.set - Double insert in net.corda.node.utilities.AppendOnlyPersistentMap for entity class class net.corda.node.services.identity.PersistentIdentityService$PersistentIdentity key 567ECDD3423FA9BC7EAE9275D1BA8E227B60B88FE6F104B4BCF248E6D32EAA2A, not inserting the second time {}
[INFO ] 09:33:24,978 [Thread-0 (ActiveMQ-client-factory-threads-1679362123)] (P2PMessagingClient.kt:402) messaging.P2PMessagingClient.artemisToCordaMessage - Received message from: p2p.inbound.DL6peD56B7zNNTgSnxyqC2PX78CbhfhKLLqj5aeBCRHFL5 user: O=PartyB, L=New York, C=US topic: platform.session uuid: e0542ed7-06bc-4233-b28f-0c97a3e42195 {}
[INFO ] 09:33:25,034 [Thread-0 (ActiveMQ-client-factory-threads-1679362123)] (P2PMessagingClient.kt:402) messaging.P2PMessagingClient.artemisToCordaMessage - Received message from: p2p.inbound.DL6peD56B7zNNTgSnxyqC2PX78CbhfhKLLqj5aeBCRHFL5 user: O=PartyB, L=New York, C=US topic: platform.session uuid: ad09ad7e-c1df-438a-8200-85eb27fbfb97 {}
[INFO ] 09:33:25,103 [Thread-0 (ActiveMQ-client-factory-threads-1679362123)] (P2PMessagingClient.kt:402) messaging.P2PMessagingClient.artemisToCordaMessage - Received message from: p2p.inbound.DL6peD56B7zNNTgSnxyqC2PX78CbhfhKLLqj5aeBCRHFL5 user: O=PartyB, L=New York, C=US topic: platform.session uuid: bce8d7b2-21f8-42ca-bd07-961da67928a6 {}
[WARN ] 09:33:25,119 [Node thread-1] (AppendOnlyPersistentMap.kt:79) utilities.AppendOnlyPersistentMapBase.set - Double insert in net.corda.node.utilities.AppendOnlyPersistentMap for entity class class net.corda.node.services.identity.PersistentIdentityService$PersistentIdentity key BC0AA7612C81D24A2DC5D0D7E69E0508C7C3549CBD3FF0498C1983CD163DD539, not inserting the second time {}
[WARN ] 09:33:25,271 [Node thread-1] (AppendOnlyPersistentMap.kt:79) utilities.AppendOnlyPersistentMapBase.set - Double insert in net.corda.node.utilities.AppendOnlyPersistentMap for entity class class net.corda.node.services.identity.PersistentIdentityService$PersistentIdentity key 567ECDD3423FA9BC7EAE9275D1BA8E227B60B88FE6F104B4BCF248E6D32EAA2A, not inserting the second time {}
[INFO ] 09:33:25,567 [Node thread-1] (FlowStateMachineImpl.kt:432) flow.[ee8debc4-caea-43cf-a19a-1e812922ad1b].initiateSession - Initiating flow session with party O=Notary Service, L=Zurich, C=CH. Session id for tracing purposes is SessionId(toLong=-4884935399875269269). {}
[INFO ] 09:33:25,615 [Messaging DL6peD56B7zNNTgSnxyqC2PX78CbhfhKLLqj5aeBCRHFL5] (P2PMessagingClient.kt:623) messaging.P2PMessagingClient.createQueueIfAbsent - Create fresh queue internal.peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd bound on same address {}
[WARN ] 09:33:25,624 [Thread-2 (ActiveMQ-server-org.apache.activemq.artemis.core.server.impl.ActiveMQServerImpl$3@3e4fc89c)] (QueueImpl.java:3193) core.server.checkDeadLetterAddressAndExpiryAddress - AMQ222165: No Dead Letter Address configured for queue internal.peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd in AddressSettings {}
[WARN ] 09:33:25,625 [Thread-2 (ActiveMQ-server-org.apache.activemq.artemis.core.server.impl.ActiveMQServerImpl$3@3e4fc89c)] (QueueImpl.java:3196) core.server.checkDeadLetterAddressAndExpiryAddress - AMQ222166: No Expiry Address configured for queue internal.peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd in AddressSettings {}
[INFO ] 09:33:25,639 [Thread-0 (ActiveMQ-client-factory-threads-1244519356)] (BridgeControlListener.kt:79) bridging.BridgeControlListener.processControlMessage - Received bridge control message Create(nodeIdentity=DL6peD56B7zNNTgSnxyqC2PX78CbhfhKLLqj5aeBCRHFL5, bridgeInfo=BridgeEntry(queueName=internal.peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd, targets=[localhost:10001], legalNames=[O=Notary Service, L=Zurich, C=CH])) {}
[INFO ] 09:33:25,644 [Thread-0 (ActiveMQ-client-factory-threads-1244519356)] (AMQPBridgeManager.kt:82) peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd -> localhost:10001:O=Notary Service, L=Zurich, C=CH.start - Create new AMQP bridge {}
[INFO ] 09:33:25,644 [Thread-0 (ActiveMQ-client-factory-threads-1244519356)] (AMQPClient.kt:126) netty.AMQPClient.start - connect to: localhost:10001 {}
[INFO ] 09:33:25,649 [nioEventLoopGroup-3-2] (AMQPClient.kt:76) netty.AMQPClient.operationComplete - Connected to localhost:10001 {}
[INFO ] 09:33:25,651 [nioEventLoopGroup-3-2] (AMQPChannelHandler.kt:49) O=Notary Service, L=Zurich, C=CH.channelActive - New client connection 25c1f884 from localhost/127.0.0.1:10001 to /127.0.0.1:63780 {}
[INFO ] 09:33:25,651 [nioEventLoopGroup-3-2] (AMQPClient.kt:86) netty.AMQPClient.operationComplete - Disconnected from localhost:10001 {}
[ERROR] 09:33:25,655 [nioEventLoopGroup-3-2] (AMQPChannelHandler.kt:98) O=Notary Service, L=Zurich, C=CH.userEventTriggered - Handshake failure SslHandshakeCompletionEvent(java.nio.channels.ClosedChannelException) {}
[INFO ] 09:33:25,657 [nioEventLoopGroup-3-2] (AMQPChannelHandler.kt:74) O=Notary Service, L=Zurich, C=CH.channelInactive - Closed client connection 25c1f884 from localhost/127.0.0.1:10001 to /127.0.0.1:63780 {}
[INFO ] 09:33:25,658 [nioEventLoopGroup-3-2] (AMQPBridgeManager.kt:115) peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd -> localhost:10001:O=Notary Service, L=Zurich, C=CH.onSocketConnected - Bridge Disconnected {}
[INFO ] 09:33:26,653 [nioEventLoopGroup-3-3] (AMQPClient.kt:91) netty.AMQPClient.run - Retry connect {}
[INFO ] 09:33:26,657 [nioEventLoopGroup-3-4] (AMQPClient.kt:76) netty.AMQPClient.operationComplete - Connected to localhost:10001 {}
[INFO ] 09:33:26,658 [nioEventLoopGroup-3-4] (AMQPChannelHandler.kt:49) O=Notary Service, L=Zurich, C=CH.channelActive - New client connection db926eb8 from localhost/127.0.0.1:10001 to /127.0.0.1:63781 {}
[INFO ] 09:33:26,658 [nioEventLoopGroup-3-4] (AMQPClient.kt:86) netty.AMQPClient.operationComplete - Disconnected from localhost:10001 {}
[ERROR] 09:33:26,658 [nioEventLoopGroup-3-4] (AMQPChannelHandler.kt:98) O=Notary Service, L=Zurich, C=CH.userEventTriggered - Handshake failure SslHandshakeCompletionEvent(java.nio.channels.ClosedChannelException) {}
[INFO ] 09:33:26,659 [nioEventLoopGroup-3-4] (AMQPChannelHandler.kt:74) O=Notary Service, L=Zurich, C=CH.channelInactive - Closed client connection db926eb8 from localhost/127.0.0.1:10001 to /127.0.0.1:63781 {}
[INFO ] 09:33:26,659 [nioEventLoopGroup-3-4] (AMQPBridgeManager.kt:115) peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd -> localhost:10001:O=Notary Service, L=Zurich, C=CH.onSocketConnected - Bridge Disconnected {}
[INFO ] 09:33:27,662 [nioEventLoopGroup-3-5] (AMQPClient.kt:91) netty.AMQPClient.run - Retry connect {}
[INFO ] 09:33:27,667 [nioEventLoopGroup-3-6] (AMQPClient.kt:76) netty.AMQPClient.operationComplete - Connected to localhost:10001 {}
[INFO ] 09:33:27,668 [nioEventLoopGroup-3-6] (AMQPChannelHandler.kt:49) O=Notary Service, L=Zurich, C=CH.channelActive - New client connection 81808e51 from localhost/127.0.0.1:10001 to /127.0.0.1:63782 {}
[INFO ] 09:33:27,668 [nioEventLoopGroup-3-6] (AMQPClient.kt:86) netty.AMQPClient.operationComplete - Disconnected from localhost:10001 {}
[ERROR] 09:33:27,669 [nioEventLoopGroup-3-6] (AMQPChannelHandler.kt:98) O=Notary Service, L=Zurich, C=CH.userEventTriggered - Handshake failure SslHandshakeCompletionEvent(java.nio.channels.ClosedChannelException) {}
[INFO ] 09:33:27,669 [nioEventLoopGroup-3-6] (AMQPChannelHandler.kt:74) O=Notary Service, L=Zurich, C=CH.channelInactive - Closed client connection 81808e51 from localhost/127.0.0.1:10001 to /127.0.0.1:63782 {}
[INFO ] 09:33:27,669 [nioEventLoopGroup-3-6] (AMQPBridgeManager.kt:115) peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd -> localhost:10001:O=Notary Service, L=Zurich, C=CH.onSocketConnected - Bridge Disconnected {}
[INFO ] 09:33:28,671 [nioEventLoopGroup-3-7] (AMQPClient.kt:91) netty.AMQPClient.run - Retry connect {}
[INFO ] 09:33:28,677 [nioEventLoopGroup-3-8] (AMQPClient.kt:76) netty.AMQPClient.operationComplete - Connected to localhost:10001 {}
[INFO ] 09:33:28,680 [nioEventLoopGroup-3-8] (AMQPChannelHandler.kt:49) O=Notary Service, L=Zurich, C=CH.channelActive - New client connection a0e0cd1e from localhost/127.0.0.1:10001 to /127.0.0.1:63783 {}
[INFO ] 09:33:28,680 [nioEventLoopGroup-3-8] (AMQPClient.kt:86) netty.AMQPClient.operationComplete - Disconnected from localhost:10001 {}
[ERROR] 09:33:28,681 [nioEventLoopGroup-3-8] (AMQPChannelHandler.kt:98) O=Notary Service, L=Zurich, C=CH.userEventTriggered - Handshake failure SslHandshakeCompletionEvent(java.nio.channels.ClosedChannelException) {}
[INFO ] 09:33:28,682 [nioEventLoopGroup-3-8] (AMQPChannelHandler.kt:74) O=Notary Service, L=Zurich, C=CH.channelInactive - Closed client connection a0e0cd1e from localhost/127.0.0.1:10001 to /127.0.0.1:63783 {}
[INFO ] 09:33:28,682 [nioEventLoopGroup-3-8] (AMQPBridgeManager.kt:115) peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd -> localhost:10001:O=Notary Service, L=Zurich, C=CH.onSocketConnected - Bridge Disconnected {}
[INFO ] 09:33:29,682 [nioEventLoopGroup-3-9] (AMQPClient.kt:91) netty.AMQPClient.run - Retry connect {}
[INFO ] 09:33:29,686 [nioEventLoopGroup-3-10] (AMQPClient.kt:76) netty.AMQPClient.operationComplete - Connected to localhost:10001 {}
[INFO ] 09:33:29,687 [nioEventLoopGroup-3-10] (AMQPChannelHandler.kt:49) O=Notary Service, L=Zurich, C=CH.channelActive - New client connection 00ab0536 from localhost/127.0.0.1:10001 to /127.0.0.1:63784 {}
[INFO ] 09:33:29,688 [nioEventLoopGroup-3-10] (AMQPClient.kt:86) netty.AMQPClient.operationComplete - Disconnected from localhost:10001 {}
[ERROR] 09:33:29,688 [nioEventLoopGroup-3-10] (AMQPChannelHandler.kt:98) O=Notary Service, L=Zurich, C=CH.userEventTriggered - Handshake failure SslHandshakeCompletionEvent(java.nio.channels.ClosedChannelException) {}
[INFO ] 09:33:29,689 [nioEventLoopGroup-3-10] (AMQPChannelHandler.kt:74) O=Notary Service, L=Zurich, C=CH.channelInactive - Closed client connection 00ab0536 from localhost/127.0.0.1:10001 to /127.0.0.1:63784 {}
[INFO ] 09:33:29,689 [nioEventLoopGroup-3-10] (AMQPBridgeManager.kt:115) peers.DLF1ZmHt1DXc9HbxzDNm6VHduUABBbNsp7Mh4DhoBs6ifd -> localhost:10001:O=Notary Service, L=Zurich, C=CH.onSocketConnected - Bridge Disconnected {}
[INFO ] 09:33:30,693 [nioEventLoopGroup-3-11] (AMQPClient.kt:91) netty.AMQPClient.run - Retry connect {}
.... ad infinitum
```