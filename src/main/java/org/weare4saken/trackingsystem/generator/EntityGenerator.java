package org.weare4saken.trackingsystem.generator;

import java.util.List;

@FunctionalInterface
public interface EntityGenerator<T> {

    List<T> generate(Long l);
}
