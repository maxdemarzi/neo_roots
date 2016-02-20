package com.maxdemarzi;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.store.NeoStores;
import org.neo4j.tooling.GlobalGraphOperations;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@javax.ws.rs.Path("/service")
public class Service {

    private static GraphDatabaseService db;

    public Service(@Context GraphDatabaseService graphDatabaseService) {
        db = graphDatabaseService;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String[] namesArray;

    private static final LoadingCache<Long, String> names = CacheBuilder.newBuilder()
            .maximumSize(2_000_000)
            .build(
                    new CacheLoader<Long, String>() {
                        public String load(Long nodeId) {
                            return getNameForNodeId(nodeId);
                        }
                    });

    private static String getNameForNodeId(Long nodeId) {
        final Node node = db.getNodeById(nodeId);
        return (String) node.getProperty("name", "unknown");
    }

    @GET
    @javax.ws.rs.Path("/helloworld")
    public Response helloWorld() throws IOException {
        Map<String, String> results = new HashMap<String, String>() {{
            put("hello", "world");
        }};
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

    @GET
    @javax.ws.rs.Path("/warmup")
    public Response warmUp(@Context GraphDatabaseService db) throws IOException {
        int counter = 0;
        try (Transaction tx = db.beginTx()) {
            for (Node n : GlobalGraphOperations.at(db).getAllNodes()) {
                n.getPropertyKeys();
                for (Relationship relationship : n.getRelationships()) {
                    relationship.getPropertyKeys();
                    relationship.getStartNode();
                }
            }

            for (Relationship relationship : GlobalGraphOperations.at(db).getAllRelationships()) {
                counter++;
                relationship.getPropertyKeys();
                relationship.getNodes();
            }
        }

        Map<String, Integer> results = new HashMap<>();
        results.put("count", counter);

        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

    @GET
    @javax.ws.rs.Path("/paths/{name}/")
    @Produces({"application/json"})
    public Response paths(
            @PathParam("name") final String name,
            @Context final GraphDatabaseService db) throws IOException {
        ArrayList<HashMap> results = new ArrayList<>();

        try (Transaction tx = db.beginTx()) {
            final Node user = db.findNode(Labels.Person, "name", name);

            if (user != null) {

                TraversalDescription td = db.traversalDescription()
                        .depthFirst()
                        .expand(PathExpanders.forTypeAndDirection(RelationshipTypes.HAS_CHILD, Direction.OUTGOING))
                        .uniqueness(Uniqueness.RELATIONSHIP_PATH);

                for (org.neo4j.graphdb.Path position : td.traverse(user)) {
                    HashMap<String, Object> result = new HashMap<>();
                    ArrayList<String> names = new ArrayList<>();
                    for (Node node : position.nodes()) {
                        names.add((String) node.getProperty("name"));
                    }
                    result.put("paths", names);
                    result.put("length", position.length());
                    results.add(result);
                }
            }
            tx.success();
        }

        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

    @GET
    @javax.ws.rs.Path("/paths_streaming/{name}/")
    @Produces({"application/json"})
    public Response pathsStreaming(
            @PathParam("name") final String name,
            @Context final GraphDatabaseService db) throws IOException {

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);

                try (Transaction tx = db.beginTx()) {
                    final Node user = db.findNode(Labels.Person, "name", name);

                    if (user != null) {

                        TraversalDescription td = db.traversalDescription()
                                .depthFirst()
                                .expand(PathExpanders.forTypeAndDirection(RelationshipTypes.HAS_CHILD, Direction.OUTGOING))
                                .uniqueness(Uniqueness.RELATIONSHIP_PATH);
                        jg.writeStartArray();
                        for (org.neo4j.graphdb.Path position : td.traverse(user)) {
                            jg.writeStartObject();
                            jg.writeArrayFieldStart("paths");
                            for (Node node : position.nodes()) {
                                jg.writeString((String) node.getProperty("name"));
                            }
                            jg.writeEndArray();
                            jg.writeNumberField("length", position.length());
                            jg.writeEndObject();
                        }
                        jg.writeEndArray();
                    }

                    tx.success();
                }
                jg.flush();
                jg.close();
            }
        };

        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @javax.ws.rs.Path("/paths_streaming_cached/{name}/")
    @Produces({"application/json"})
    public Response pathsStreamingCached(
            @PathParam("name") final String name,
            @Context final GraphDatabaseService db) throws IOException {

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);

                try (Transaction tx = db.beginTx()) {
                    final Node user = db.findNode(Labels.Person, "name", name);

                    if (user != null) {

                        TraversalDescription td = db.traversalDescription()
                                .depthFirst()
                                .expand(PathExpanders.forTypeAndDirection(RelationshipTypes.HAS_CHILD, Direction.OUTGOING))
                                .uniqueness(Uniqueness.RELATIONSHIP_PATH);
                        jg.writeStartArray();
                        for (org.neo4j.graphdb.Path position : td.traverse(user)) {
                            jg.writeStartObject();
                            jg.writeArrayFieldStart("paths");
                            for (Node node : position.nodes()) {
                                jg.writeString((String) names.get(node.getId()));
                            }
                            jg.writeEndArray();
                            jg.writeNumberField("length", position.length());
                            jg.writeEndObject();
                        }
                        jg.writeEndArray();
                    }

                    tx.success();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                jg.flush();
                jg.close();
            }
        };
        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @javax.ws.rs.Path("/paths_streaming_pre_cached/{name}/")
    @Produces({"application/json"})
    public Response pathsStreamingPreCached(
            @PathParam("name") final String name,
            @Context final GraphDatabaseService db) throws IOException {
        if (namesArray == null) {
            NeoStores neoStore = ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(NeoStores.class);
            int highId = ((Number)neoStore.getNodeStore().getHighId()).intValue();

            namesArray = new String[highId];
            try (Transaction tx = db.beginTx()) {
                for (Node n : GlobalGraphOperations.at(db).getAllNodes()) {
                    namesArray[((Number)n.getId()).intValue()] = (String)n.getProperty("name", "");
                }
            }

        }

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);

                try (Transaction tx = db.beginTx()) {
                    final Node user = db.findNode(Labels.Person, "name", name);

                    if (user != null) {

                        TraversalDescription td = db.traversalDescription()
                                .depthFirst()
                                .expand(PathExpanders.forTypeAndDirection(RelationshipTypes.HAS_CHILD, Direction.OUTGOING))
                                .uniqueness(Uniqueness.RELATIONSHIP_PATH);
                        jg.writeStartArray();
                        for (org.neo4j.graphdb.Path position : td.traverse(user)) {
                            jg.writeStartObject();
                            jg.writeArrayFieldStart("paths");
                            for (Node node : position.nodes()) {
                                jg.writeString((String) namesArray[((Number)node.getId()).intValue()]);
                            }
                            jg.writeEndArray();
                            jg.writeNumberField("length", position.length());
                            jg.writeEndObject();
                        }
                        jg.writeEndArray();
                    }

                    tx.success();
                }
                jg.flush();
                jg.close();
            }
        };
        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
    }

    private void doStuffRecursively(Node node, int depth, JsonGenerator jg, ArrayList<Long> nodeIds) throws ExecutionException, IOException {
        boolean end = true;
        nodeIds.add(node.getId());
        for (Relationship rel : node.getRelationships(RelationshipTypes.HAS_CHILD, Direction.OUTGOING)) {
            end = false;
            Node nextNode = rel.getEndNode();
            doStuffRecursively(nextNode, depth + 1, jg, nodeIds);
        }
        if (end) {
            jg.writeStartObject();
            jg.writeArrayFieldStart("paths");
            for (Long nodeId : nodeIds) {
                //jg.writeString((String) names.get(nodeId)); // Use this line if sticking with Guava
                jg.writeString((String) namesArray[((Number)nodeId).intValue()]);
            }
            jg.writeEndArray();
            jg.writeNumberField("length", depth);
            jg.writeEndObject();
        }
        nodeIds.remove(node.getId());

    }

    @GET
    @javax.ws.rs.Path("/longest_paths_streaming_recursively/{name}/")
    @Produces({"application/json"})
    public Response pathsStreamingRecursively(
            @PathParam("name") final String name,
            @Context final GraphDatabaseService db) throws IOException {
        // Remove this if statement if sticking with Guava
        if (namesArray == null) {
            NeoStores neoStore = ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(NeoStores.class);
            int highId = ((Number)neoStore.getNodeStore().getHighId()).intValue();

            namesArray = new String[highId];
            try (Transaction tx = db.beginTx()) {
                for (Node n : GlobalGraphOperations.at(db).getAllNodes()) {
                    namesArray[((Number)n.getId()).intValue()] = (String)n.getProperty("name", "");
                }
            }

        }

        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);

                try (Transaction tx = db.beginTx()) {
                    final Node user = db.findNode(Labels.Person, "name", name);

                    if (user != null) {
                        jg.writeStartArray();
                        doStuffRecursively(user, 0, jg, new ArrayList<Long>());
                        jg.writeEndArray();
                    }

                    tx.success();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                jg.flush();
                jg.close();
            }
        };
        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
    }

}