package com.maxdemarzi;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class WarmupTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withExtension("/v1", Service.class)
            .withFixture(TEST_DATA);

    @Test
    public void shouldRespondToWarmup() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/warmup").toString());
        HashMap actual = response.content();
        assertTrue(actual.equals(expected));
    }

    private static final HashMap expected = new HashMap<String, Object>() {{
        put("count", 2);
    }};

    public static final String TEST_DATA =
            new StringBuilder()
                    .append("")
                    .append("CREATE (o1:TXObject {name:'o1'})")
                    .append("CREATE (o2:TXObject {name:'o2'})")
                    .append("CREATE (o3:TXObject {name:'o3'})")
                    .append("MERGE (o1)-[:HAS_CHILD]->(o2)")
                    .append("MERGE (o2)-[:HAS_CHILD]->(o3)")
                    .toString();
}
