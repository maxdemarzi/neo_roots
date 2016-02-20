package com.maxdemarzi;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class ServiceTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withExtension("/v1", Service.class)
            .withFixture(TEST_DATA);

    @Test
    public void shouldRespondToPaths() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/paths/user1").toString());
        ArrayList actual = response.content();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void shouldRespondToStreamingPaths() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/paths_streaming/user1").toString());
        ArrayList actual = response.content();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void shouldRespondToCachedStreamingPaths() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/paths_streaming_cached/user1").toString());
        ArrayList actual = response.content();
        assertTrue(actual.equals(expected));
    }

    private static final ArrayList<HashMap<String,Object>> expected = new ArrayList<HashMap<String, Object>>() {{
        add(new HashMap<String,Object>(){{
            put("paths", new ArrayList<String>(){{
                add("user1");
            }});
            put("length", 0);
        }});
        add(new HashMap<String,Object>(){{
            put("paths", new ArrayList<String>(){{
                add("user1");
                add("user2");
            }});
            put("length", 1);
        }});
        add(new HashMap<String,Object>(){{
            put("paths", new ArrayList<String>(){{
                add("user1");
                add("user2");
                add("user3");
            }});
            put("length", 2);
        }});

    }};

    public static final String TEST_DATA =
            new StringBuilder()
                    .append("CREATE (user1:Person {name:'user1'})")
                    .append("CREATE (user2:Person {name:'user2'})")
                    .append("CREATE (user3:Person {name:'user3'})")
                    .append("MERGE (user1)-[:HAS_CHILD]->(user2)")
                    .append("MERGE (user2)-[:HAS_CHILD]->(user3)")
                    .toString();
}

