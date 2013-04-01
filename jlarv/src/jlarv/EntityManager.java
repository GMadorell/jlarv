package jlarv;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

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
    
    /* Holds all the active entities */
	private ArrayList<Integer> entities;
	
	/* Map components using a double map of structure:
	     - Class<? extends Component> = Type of the component, so we can add them via SomeComponent.class.
	  	 - Integer = Entity unique ID.
	  	 - Component = the Component associated to that entity.*/
	private HashMap<Class<? extends Component>, HashMap<Integer, Component>> componentsByClass;
	
	/* Serves the purpose of never having two entities with the same ID (much like a database primary key) */
	private int lowestAssignedId;
	
	/* Allows to recycle the IDs after entities have been deleted from the entity manager */
	private ArrayDeque<Integer> unassignedIDs;	
	
	public EntityManager() {
		componentsByClass = new HashMap<Class<? extends Component>, HashMap<Integer, Component>>();
		entities = new ArrayList<Integer>();
		lowestAssignedId = Integer.MIN_VALUE;
		unassignedIDs = new ArrayDeque<Integer>();
	}
	
	/**
	 * Generates a new unique id used for assigning it to a entity.
	 * @return New unique ID (int).
	 */
	/* Synchronized means that only one thread can execute this block of code at the same time,
	 * meaning that we will not be able to generate two new id's at the same time and thus we will not
	 * have two entities with the same ID */
	private synchronized int generateNewId() {
	    if ( unassignedIDs.size() > 0 ) {
	        return unassignedIDs.pop();
	    } else if ( lowestAssignedId < Integer.MAX_VALUE ) {
	        return lowestAssignedId++;
	    } else {
	        throw new Error("ERROR - maximum entities ID reached.");
	    }
	    
	}
	
	/**
	 * Creates and returns a new entity.
	 * @return Entity ID (int).
	 */
	public int createEntity() {
		int new_id = generateNewId();
		entities.add( new_id );
		return new_id;
	}
	
	/**
	 * Removes the given entity from the entity manager (completely).
	 * @param entity The entity which will be erased.
	 */
	public synchronized void removeEntity( int entity ) {
	    // Delete it from all the maps.
		for ( HashMap<Integer, Component> value : componentsByClass.values() ) {
			value.remove( entity );			
		}
		entities.remove( entities.indexOf( entity ) );
		// Add it's ID to be recycled later on
        unassignedIDs.push(entity);
	}
	
	/**
	 * Adds the given component to the given entity.
	 * Overrides the actual component if a new one is given.
	 */
	public void addComponent( int entity, Component component ) {
	    Class<? extends Component> componentType = component.getClass();	
		if ( componentsByClass.containsKey( componentType ) ) {
			HashMap<Integer, Component> entity_map = componentsByClass.get( componentType );
			entity_map.put( entity, component );
		} else {
			HashMap<Integer, Component> entity_map = new HashMap<Integer, Component>();
			entity_map.put( entity, component );
			componentsByClass.put( componentType, entity_map );
		}
	}
	
	/**
	 * Adds all the given components to the given entity.
	 * Overrides the anterior components if they existed.
	 */
	public void addComponents( int entity, Component ... components ) {
		for ( Component component : components ) {
			addComponent( entity, component );
		}
	}
	
	/**
	 * Removes the given component from the given entity.
	 * It fails (on purpose) if the component was never added to the entity manager,
	 * that helps debugging in some cases.
	 * You can use removeComponentSafe if that's a problem.
	 * @param componentType The class type of the component we want to remove (SomeComponent.class).
	 */
	public void removeComponent( int entity, Class<? extends Component> componentType ) {
		componentsByClass.get( componentType ).remove( entity ); 
	}
	
	/**
	 * Removes the given component from the given entity.
	 * Safe version of removeComponent, meaning that this method will not fail
	 * if the component was never added to the entity manager.
	 * @param componentType The class type of the component we want to remove (SomeComponent.class).
	 */
	public void removeComponentSafe( int entity, Class<? extends Component> componentType ) {
		if ( componentsByClass.containsKey( componentType ) ) {
			componentsByClass.get( componentType ).remove( entity );  
		}
	}
	
	/**
	 * Returns a boolean depending on whether the given entity has the given component or not.
	 * @param componentType The class type of the component we want to check (SomeComponent.class).
	 */
	public boolean hasComponent( int entity, Class<? extends Component> componentType ) {
		if ( ! componentsByClass.containsKey( componentType ) )
			return false;
		return componentsByClass.get( componentType ).containsKey( entity );
	}
	
	/**
	 * Returns a boolean depending on whether the given component name was ever introduced
	 * to the manager (added at least once).
	 * @param componentType The class type of the component we want to remove (SomeComponent.class).
	 */
	public boolean doesComponentExist( Class<? extends Component> componentType ) {
		return componentsByClass.containsKey( componentType );
	}
	
	/**
	 * Given an entity and a component, returns the component if the entity has it.
	 * It fails (on purpose) if the entity doesn't have the component, it helps when debugging.
	 * If you're really unsure about that possibility, perhaps using hasComponent might be a good idea.
	 * @param componentType The class type of the component we want to get (SomeComponent.class).
	 */
	public Component getComponent( int entity, Class<? extends Component> componentType ) {
		return componentsByClass.get( componentType ).get( entity );
	}
	
	/**
	 * Returns a set of all the entities that have the given component.
	 * Fails (on purpose) if the component wasn't ever introduced (added at least once)
	 * to the manager. If that really bothers you, you can try to use doesComponentExist().
	 * @param componentType The class type of the component we want to process (SomeComponent.class).
	 */
	public ArrayList<Integer> getEntitiesHavingComponent( Class<? extends Component> componentType ) {
		ArrayList<Integer> entities_list = new ArrayList<Integer>();
		for ( int entity : componentsByClass.get( componentType ).keySet() ) {
			entities_list.add( entity );
		}
		return entities_list;
	}
	
	/**
	 * Returns a set of all the entities that have all the given components.
	 * It's a exclusive method, meaning that it does an intersection, returning
	 * only the entities that have every single component.
	 * @param componentType The class types of the components we want to process (SomeComponent.class).
	 */
	@SafeVarargs // Allows us to use to evade warnings on every method call.
	public final ArrayList<Integer> getEntitiesHavingComponents( Class<? extends Component> ... components ) {
		ArrayList<Integer> entitiesList = getEntitiesHavingComponent( components[0] ); //avoid 1 iteration
		ArrayList<Integer> auxiliarList = new ArrayList<Integer>();
		Class<? extends Component> componentType;
		int entity;
		// Iterate over the arguments
		for ( int i = 1, len = components.length; i < len; i++ ) {
			componentType = components[i];
			auxiliarList = getEntitiesHavingComponent( componentType );
			// Intersection - We must traverse list in inverse order to evade ConcurrentModificationException		
			for ( int j = entitiesList.size()-1; j >= 0; j-- ) {
				entity = entitiesList.get( j );
				if ( ! auxiliarList.contains( entity ) ) {
					entitiesList.remove( j );
				}
			}
		}
		return entitiesList;		
	}
	
	/**
	 * Returns a list of all the components that the given entity has at the moment.
	 */
	public ArrayList<Component> getComponentsOfEntity( int entity ) {
		ArrayList<Component> componentsList = new ArrayList<Component>();
		for ( HashMap<Integer, Component> entities_map : componentsByClass.values() ) {
			if ( entities_map.containsKey( entity ) ) {
				componentsList.add( entities_map.get( entity ) );
			}
		}
		return componentsList;
 	}
	
	/**
	 * Returns a list of all the components of the type given. 
	 * Fails (on purpose) if the component name isn't even on the dictionary.
	 * @param componentType The class type of the component we want to process (SomeComponent.class).
	 */
	public ArrayList<Component> getComponentsOfType( Class<? extends Component> componentType ) {
		ArrayList<Component> componentsList = new ArrayList<Component>();
		for ( Component componentInside : componentsByClass.get( componentType ).values() ) {
			componentsList.add( componentInside );
		}
		return componentsList;
	}	
	
	/*
	 * Getters and setters.
	 */
	public ArrayList<Integer> getEntities() {
		return entities;
	}

	public HashMap<Class<? extends Component>, HashMap<Integer, Component>> getComponentsByClass() {
		return componentsByClass;
	}
}
