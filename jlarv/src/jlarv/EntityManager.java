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
	/* Map components using a double map of structure:
	     - String = Component name.
	  	 - Integer = Entity unique ID.
	  	 - Component = the Component associated to that entity.*/
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
	
	/**
	 * Removes the given entity from the entity manager (completely).
	 * @param entity The entity which will be erased.
	 */
	public void removeEntity(int entity) {
		for(HashMap<Integer, Component> value : components_by_class.values()) {
			value.remove(entity);
		}
	}
	
	/**
	 * Adds the given component to the given entity.
	 * Overrides the actual component if a new one is passed.
	 */
	public void addComponent(int entity, Component component) {
		String component_name = component.getClass().getName();	
		if (components_by_class.containsKey(component_name)) {
			HashMap<Integer, Component> entity_map = components_by_class.get(component_name);
			entity_map.put(entity, component);
		}
		else {
			HashMap<Integer, Component> entity_map = new HashMap<Integer, Component>();
			entity_map.put(entity, component);
			components_by_class.put(component_name, entity_map);
		}
	}
	
	/**
	 * Removes the given component from the given entity.
	 * @param component_name The class name of the component we want to remove.
	 */
	public void removeComponent(int entity, String component_name) {
		if (components_by_class.containsKey(component_name)) {
			//TODO test what happens when we try to remove a component that isn't linked to given entity.
			components_by_class.get(component_name).remove(entity);  
		}
	}
	
	/**
	 * Returns a boolean depending on whether the given entity has the given component or not.
	 * @param component_name The class name of the component we want to check.
	 */
	public boolean hasComponent(int entity, String component_name) {
		if (!components_by_class.containsKey(component_name))
			return false;
		if (!components_by_class.get(component_name).containsKey(entity))
			return false;
		return true;
	}
	
	/**
	 * Given an entity and a component, returns the component if the entity has it.
	 * @param component_name The class name of the component we want to retrieve.
	 */
	public Component getComponent(int entity, String component_name) {
		//TODO test what happens when we try to get a component that isn't linked to given entity.
		return components_by_class.get(component_name).get(entity);
	}
	
	/**
	 * Returns a list of all the entities that have the given component.
	 * @param component_name The class name of the component we want to process.
	 */
	public ArrayList<Integer> getEntitiesHavingComponent(String component_name) {
		ArrayList<Integer> entities_list = new ArrayList<Integer>();
		for (int entity : components_by_class.get(component_name).keySet()) {
			entities_list.add(entity);
		}
		return entities_list;
	}
	
	/**
	 * Returns a list of all the entities that have all the given components.
	 * It's a exclusive method, meaning that it does an intersection, returning
	 * only the entities that have every single component.
	 * @param components The class name of the components we want to process.
	 */
	public ArrayList<Integer> getEntitiesHavingComponents(String ... components) {
		ArrayList<Integer> entities_list = getEntitiesHavingComponent(components[0]); //avoid 1 iteration
		ArrayList<Integer> auxiliar_list = new ArrayList<Integer>();
		String component_name;
		// Iterate over the arguments
		for (int i = 1, len = components.length; i < len; i++) {
			component_name = components[i];
			auxiliar_list = getEntitiesHavingComponent(component_name);
			// Intersection
			for (int entity : entities_list) {
				if (!auxiliar_list.contains(entity)) {
					entities_list.remove(entity);
				}
			}			
		}
		return entities_list;		
	}
	
	/**
	 * Returns a list of all the components that the given entity has at the moment.
	 */
	public ArrayList<Component> getComponentsOfEntity(int entity) {
		ArrayList<Component> components_list = new ArrayList<Component>();
		for (HashMap<Integer, Component> entities_map : components_by_class.values()) {
			if (entities_map.containsKey(entity)) {
				components_list.add(entities_map.get(entity));
			}
		}
		return components_list;
 	}
	
	/**
	 * Returns a list of all the components of the type given. 
	 * @param component_name The class name of the component we want to process.
	 */
	public ArrayList<Component> getComponentsOfType(String component_name) {
		ArrayList<Component> components_list = new ArrayList<Component>();
		for (Component component_inside : components_by_class.get(component_name).values()) {
			components_list.add(component_inside);
		}
		return components_list;
	}	
}
