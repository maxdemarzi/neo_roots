# neo_roots
Unmanaged Extension to find Roots

Original Cypher Query:

    MATCH pt=(p:Person { name: '1-Max' })-[r:HAS_CHILD*]->(c)
    WITH extract(x IN nodes(pt) | x.name) as paths, length(pt) as lengths
    RETURN paths, lengths

# Instructions

1. Build it:

        mvn clean package

2. Copy target/roots-1.0.jar to the plugins/ directory of your Neo4j server.

3. Configure Neo4j by adding a line to conf/neo4j-server.properties:

        org.neo4j.server.thirdparty_jaxrs_classes=com.maxdemarzi=/v1

4. Download and copy additional jar to the plugins/ directory of your Neo4j server.

        wget http://repo1.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar

5. Start Neo4j server.

6. Check that it is installed correctly over HTTP:

        :GET /v1/service/helloworld

7. Warm up the database (optional)

        :GET /v1/service/warmup

8. Create test data:

Generate Dummy Nodes:

        CREATE (u:Person {name:"1-Max"})

Add an Index:

        CREATE INDEX ON :Person(name);

Create the family tree:

        WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
        MATCH (users:Person) WHERE users.name STARTS WITH "1-"
        WITH range(1,2) as children, users, names
        FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:2- + names[id% size(names)]+id}) );

Change the "1-" and "2-" until we get to level 20:

        WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
        MATCH (users:Person) WHERE users.name STARTS WITH "19-"
        WITH range(1,2) as children, users, names
        FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"20-" + names[id% size(names)]+id}) );

9. Perform Query:

        :GET /v1/service/paths/{name}
        :GET /v1/service/paths/1-Max  // Where {name} is 1-Max for example

10. Try Streaming Query:

        :GET /v1/service/paths_streaming/1-Max

We can also use curl:

        curl -u neo4j:swordfish http://localhost:7474/v1/service/paths_streaming/1-Max

11. Try Streaming Query with Cached Names:

        :GET /v1/service/paths_streaming_cached/1-Max
        curl -u neo4j:swordfish http://localhost:7474/v1/service/paths_streaming_cached/1-Max

To test times:

12. Create a curl-format.txt file and paste in:

           time_namelookup:  %{time_namelookup}\n
              time_connect:  %{time_connect}\n
           time_appconnect:  %{time_appconnect}\n
          time_pretransfer:  %{time_pretransfer}\n
             time_redirect:  %{time_redirect}\n
        time_starttransfer:  %{time_starttransfer}\n
                           ----------\n
                time_total:  %{time_total}\n

Run this command:

        curl -u neo4j:swordfish -w "@curl-format.txt" -s http://localhost:7474/v1/service/paths_streaming/1-Max
        curl -u neo4j:swordfish -w "@curl-format.txt" -s http://localhost:7474/v1/service/paths_streaming_cached/1-Max

To skip displaying the output use:

        curl -u neo4j:swordfish -w "@curl-format.txt" -o /dev/null -s http://localhost:7474/v1/service/paths_streaming/1-Max
        curl -u neo4j:swordfish -w "@curl-format.txt" -o /dev/null -s http://localhost:7474/v1/service/paths_streaming_cached/1-Max

On Windows operating systems:

        curl -u neo4j:swordfish -w "@curl-format.txt" -o NUL -s http://localhost:7474/v1/service/paths_streaming/1-Max
        curl -u neo4j:swordfish -w "@curl-format.txt" -o NUL -s http://localhost:7474/v1/service/paths_streaming_cached/1-Max

Compare to:

        curl -u neo4j:swordfish -w "@curl-format.txt" -o /dev/null -s http://localhost:7474/v1/service/paths/1-Max

neo4j:swordfish above are the username:password basic auth credentials


13. Compare to Original Query:

        MATCH pt=(p:Person { name: '1-Max' })-[r:HAS_CHILD*]->(c)
        WITH extract(x IN nodes(pt) | x.name) as paths, length(pt) as length
        RETURN paths, length

With curl:

        curl -u neo4j:swordfish -w "@curl-format.txt" -o /dev/null -s -H "Content-Type: application/json" -X POST -d '{ "statements" : [ { "statement" : "MATCH pt=(p:Person { name: \"1-Max\" })-[r:HAS_CHILD*]->(c) WITH extract(x IN nodes(pt) | x.name) as paths, length(pt) as lengths RETURN paths, lengths" } ]}' http://localhost:7474/db/data/transaction/commit

14. Performance Testing:

        ab -A neo4j:swordfish -n 10 -c 2 http://127.0.0.1:7474/v1/service/paths/1-Max
        ab -A neo4j:swordfish -n 10 -c 2 http://127.0.0.1:7474/v1/service/paths_streaming/1-Max
        ab -A neo4j:swordfish -n 10 -c 2 http://127.0.0.1:7474/v1/service/paths_streaming_cached/1-Max
        ab -A neo4j:swordfish -n 10 -c 2 http://127.0.0.1:7474/v1/service/paths_streaming_pre_cached/1-Max
        ab -A neo4j:swordfish -n 10 -c 2 -p cypher.statement -T 'application/json' http://127.0.0.1:7474/db/data/transaction/commit

The file cypher.statement contains:

        { "statements" : [ { "statement" : "MATCH pt=(p:Person { name: \"1-Max\" })-[r:HAS_CHILD*]->(c) WITH extract(x IN nodes(pt) | x.name) as paths, length(pt) as lengths RETURN paths, lengths " } ]}

15. To return only the longest paths:

        curl -u neo4j:swordfish -w "@curl-format.txt" -o /dev/null -s http://localhost:7474/v1/service/longest_paths_streaming_recursively/1-Max


