package com.liveramp.megadesk.gear;

import java.util.List;

import com.liveramp.megadesk.attempt.Outcome;
import com.liveramp.megadesk.dependency.Dependency;
import com.liveramp.megadesk.node.Node;
import com.liveramp.megadesk.persistence.Persistence;

public interface Gear {

  Node getNode();

  Persistence getPersistence();

  List<Dependency> dependencies();

  Outcome run() throws Exception;
}