package io.github.some_example_name.core;
import java.util.ArrayList;
import java.util.List;
public class GameEngine{
    private List<Entity>entities;
    private List<System>systems;
    public GameEngine(){
        this.entities=new ArrayList<>();
        this.systems=new ArrayList<>();
    }
    public void addEntity(Entity entity){
        entities.add(entity);
    }
    public void removeEntity(Entity entity){
        entities.remove(entity);
    }
    public void addSystem(System system){
        systems.add(system);
    }
    public void update(float delta){
        for(System system:systems){
            system.update(delta,entities);
        }
    }
    public List<Entity>getEntities(){
        return entities;
    }
}
