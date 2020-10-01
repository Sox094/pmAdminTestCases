package entity;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Node implements Serializable {
    private String name;
    private NodeType type;
    private Map<String, String> properties;
    private long id;

    public Node() {
        this.properties = new HashMap();
    }

    public Node(String name, NodeType type, Map<String, String> properties) {
        this.name = name;
        this.type = type;
        this.properties = (Map)(properties == null ? new HashMap() : properties);
    }

    public Node(String name, NodeType type) {
        this.name = name;
        this.type = type;
    }

    public Node(long id, String name, NodeType type, Map<String, String> properties) {
        this.name = name;
        this.type = type;
        this.properties = (Map)(properties == null ? new HashMap() : properties);
        this.id = id;
    }

    public Node addProperty(String key, String value) {
        if (key != null && value != null) {
            this.properties.put(key, value);
            return this;
        } else {
            throw new IllegalArgumentException("a node cannot have a property with a null key or value");
        }
    }

    public String getName() {
        return this.name;
    }

    public NodeType getType() {
        return this.type;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o instanceof Node) {
            Node n = (Node)o;
            return this.name.equals(n.name);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return this.name + ":" + this.type + ":" + this.properties;
    }

    public static Map<String, String> toProperties(String... pairs) {
        HashMap<String, String> props = new HashMap();

        for(int i = 0; i < pairs.length - 1; ++i) {
            String var10001 = pairs[i];
            ++i;
            props.put(var10001, pairs[i]);
        }

        return props;
    }
}

