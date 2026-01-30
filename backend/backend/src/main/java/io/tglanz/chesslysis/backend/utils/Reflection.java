package io.tglanz.chesslysis.backend.utils;

import jakarta.persistence.Entity;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public final class Reflection {

  private Reflection() {}

  public static Set<Class<?>> getAllEntitiesInSamePackage(Class<?> clazz) {
    var packageName = clazz.getPackageName();
    return getAllEntitiesInSamePackage(packageName);
  }

  public static Set<Class<?>> getAllEntitiesInSamePackage(String packageName) {
    Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);

    return reflections.getTypesAnnotatedWith(Entity.class);
  }
}
