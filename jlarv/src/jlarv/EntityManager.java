package jlarv;

import java.util.*;
/*
 	EntityManager is a object that acts as the 'database' of the system.
    It's used for looking up entities, getting their list of components, creating
    them (managing that they all have unique ID's), etc.

    When using components as arguments, you can use either a component of that
    type as the argument or (recommended) calling for the class_name of the component
    like so: EntityManager.getEntitiesHavingComponent(HealthComponent.__name__),
    except if the class explicitly says the opposite (read comments).

    NOTE: restricted to one component type per entity, so the same entity cannot
          have, as an example, two instances of HealthComponent. In order to change
          that, 2nd dictionary value should be a list instead of a single component.

 */
public class EntityManager {	
	private ArrayList<Integer> entities;
	// Map components using a double map.
	// String = Component name.
	// Integer = Entity.
	// Component = the Component instance associated to that entity
	private HashMap<String, HashMap<Integer, Component>> components_by_class;
	private int lowest_assigned_id;
	
	public EntityManager() {
		components_by_class = new HashMap<String, HashMap<Integer, Component>>();
		entities = new ArrayList<Integer>();
		lowest_assigned_id = 0;
	}

	protected ArrayList<Integer> getEntities() {
		return entities;
	}

	protected HashMap<String, HashMap<Integer, Component>> getComponentsByClass() {
		return components_by_class;
	}
	
	/**
	 * Generates a new unique id used for assigning it to a entity.
	 * @return New unique ID (int).
	 */
	private int generateNewId() {
		return lowest_assigned_id++;
	}
	
	/**
	 * Creates and returns a new entity.
	 * @return Entity ID (int).
	 */
	public int createEntity() {
		int new_id = generateNewId();
		entities.add(new_id);
		return new_id;
	}
	
	
	
	
}
