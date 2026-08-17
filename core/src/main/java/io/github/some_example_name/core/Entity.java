package io.github.some_example_name.core;
import java.util.HashMap;
import java.util.Map;

public class Entity{
    private String id;
    private Map<Class<? extends Component>, Component> components;
    public Entity(String id){
        this.id=id;
        this.components=new HashMap<>();
    }
    public String getId(){return id;}
    public <T extends Component> void addComponent(Class<T> type, T component) {
        components.put(type, component);
    }
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type) {
        return (T) components.get(type);
    }
    public boolean hasComponent(Class<? extends Component> type) {
        return components.containsKey(type);
    }
    public void removeComponent(Class<? extends Component> type) {
        components.remove(type);
    }
}
