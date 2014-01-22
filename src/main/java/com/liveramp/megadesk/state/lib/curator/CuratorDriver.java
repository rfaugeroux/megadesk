package com.liveramp.megadesk.state.lib.curator;

import com.liveramp.megadesk.state.Driver;
import com.liveramp.megadesk.state.Persistence;
import com.liveramp.megadesk.state.Reference;
import com.liveramp.megadesk.state.lib.filesystem_tools.JavaSerialization;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.locks.ReadWriteLock;

public class CuratorDriver<VALUE> implements Driver<VALUE> {

  private final CuratorReadWriteLock executionLock;
  private final CuratorReadWriteLock persistenceLock;
  private final CuratorPersistence<VALUE> persistence;
  private final CuratorReference<VALUE> reference;

  public CuratorDriver(String name) {
    CuratorFramework framework = CuratorConfiguration.INSTANCE.getFramework();
    String basePath = CuratorConfiguration.INSTANCE.getPathMaker().makePath(name);

    this.executionLock = new CuratorReadWriteLock(new InterProcessReadWriteLock(framework, basePath + "/executionLock"));
    this.persistenceLock = new CuratorReadWriteLock(new InterProcessReadWriteLock(framework, basePath + "/persistenceLock"));
    this.persistence = new CuratorPersistence<VALUE>(framework, basePath + "/valueStorage", new JavaSerialization());
    this.reference = new CuratorReference<VALUE>(name);
  }

  @Override
  public Reference<VALUE> reference() {
    return reference;
  }

  @Override
  public ReadWriteLock executionLock() {
    return executionLock;
  }

  @Override
  public ReadWriteLock persistenceLock() {
    return persistenceLock;
  }

  @Override
  public Persistence<VALUE> persistence() {
    return persistence;
  }

  @Override
  public int compareTo(Driver<VALUE> valueDriver) {
    return new CompareToBuilder().append(reference, valueDriver.reference()).toComparison();
  }
}