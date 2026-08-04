package com.cornkernels.game.entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Entity {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    private final long id;
    private final Map<Class<?>, List<Object>> components = new LinkedHashMap<>();
    private boolean markedForRemoval = false;

    public Entity() {
        this(ID_GENERATOR.getAndIncrement());
    }

    public Entity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public <T> Entity add(@NotNull T component) {
        components.computeIfAbsent(component.getClass(), k -> new ArrayList<>()).add(component);
        return this;
    }

    public boolean has(Class<?> type) {
        List<Object> list = components.get(type);
        return list != null && !list.isEmpty();
    }

    public <T> T get(Class<T> type) {
        List<Object> list = components.get(type);
        if (list == null || list.isEmpty()) return null;
        return type.cast(list.getFirst());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> type) {
        List<Object> list = components.get(type);
        if (list == null || list.isEmpty()) return List.of();
        return (List<T>) list;
    }

    public <T> void remove(Class<T> type, T component) {
        List<Object> list = components.get(type);
        if (list != null) list.remove(component);
    }

    public void removeAll(Class<?> type) {
        components.remove(type);
    }

    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public boolean hasComponents(Class<?>[] componentTypes) {
        if (componentTypes == null || componentTypes.length == 0) return false;

        for (Class<?> componentType : componentTypes) {

            if (!components.containsKey(componentType)) return false;

            List<Object> list = components.get(componentType);
            if (list == null || list.isEmpty()) return false;
        }
        return true;
    }
}
