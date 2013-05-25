import com.netflix.astyanax.AstyanaxContext
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl
import com.netflix.astyanax.connectionpool.NodeDiscoveryType
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor
import com.netflix.astyanax.thrift.ThriftFamilyFactory
import com.netflix.astyanax.model.ColumnFamily
import com.netflix.astyanax.serializers.StringSerializer

//object HelloAstyanax extends App {
//  val context = new AstyanaxContext.Builder()
//    .forCluster("social_app")
//    .forKeyspace("social_engine_astyanax")
//    .withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE))
//    .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
//      .setPort(9160)
//      .setMaxConnsPerHost(10)
//      .setSeeds("127.0.0.1:9160"))
//    .withConnectionPoolMonitor(new CountingConnectionPoolMonitor)
//    .buildKeyspace(ThriftFamilyFactory.getInstance)
//  
//  context.start()
//  val keyspace = context.getEntity
//  val users = new ColumnFamily[String, String] ("users", StringSerializer.get, StringSerializer.get)
//  val m = keyspace.prepareMutationBatch()
//  m.withRow(users, "dosht").putColumn("name", "mostafa").putColumn("email", "dosht@gmail.com")
//  m.execute()
//  val result = keyspace.prepareQuery(users).getKey("dosht").execute()
//  val columns = result.getResult
//  println(s"name: ${columns.getColumnByName("name").getStringValue}, email: ${columns.getColumnByName("email").getStringValue}")
//}