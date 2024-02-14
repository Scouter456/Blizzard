package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;

public enum TaskType {
    KILL("kill"),
    BREAK("break"),
    BREW("brewing"),
    COLLECT("collect"),
    FIND_STRUCTURE("find_structure");

    private final String name;

    TaskType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final Codec<TaskType> CODEC = Codec.STRING.xmap(
            TaskType::fromName,
            TaskType::getName
    );

    private static TaskType fromName(String name) {
        for (TaskType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown TaskType: " + name);
    }
}


