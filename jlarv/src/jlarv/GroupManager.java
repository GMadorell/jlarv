package jlarv;

import java.util.*;
import java.util.Map.Entry;

/*
	Allows entities to be added to groups to be fetched later on.
    For example, we could have a group called 'hero' and add the hero to it,
    and then we could fetch for all the entities that are from the 'hero'
    group, getting only the hero, obviously.

    Usage:
      Binded to Factories and Systems by default (when they're assigned to a 
      engine, the engine does that in the background), as they're where the 
      GroupManagers will be most used.
      Basic usage is:
        - At the factory, when the entity gets created it might be assigned to a
          group.
        - In some systems, it may be convenient to ask for a certain group
          of entities. For example, a system that only acts on the Hero, may
          call for it using self.group_manager.get('hero')
 */
public class GroupManager {	
	protected Engine                          engine;
	private HashMap<String, ArrayList<Long>>  entitiesByGroup;

	public GroupManager( Engine engine ) {
		entitiesByGroup = new HashMap<String, ArrayList<Long>>();
		this.engine = engine;
	}
	
	/**
	 * Using this constructor is not recommended. Need to assign engine later.
	 */
	public GroupManager() {	
		entitiesByGroup = new HashMap<String, ArrayList<Long>>();
	}

	/**
	 * Adds the given entity into the given group.
	 * An entity can't be in the same group twice.
	 */
	public void add( long entity, String group ) {
		if (entitiesByGroup.containsKey( group ) ) {
			entitiesByGroup.get( group ).add( entity );
		}
		else {
			ArrayList<Long> entitiesList = new ArrayList<Long>();
			entitiesList.add( entity );
			entitiesByGroup.put( group, entitiesList );
		}
	}
	
	/**
	 * Adds the given entity to all the given groups.
	 */
	public void add( long entity, String ... groups) {
	    for ( String group: groups ) {
	        add( entity, group );
	    }
	}
	
	/**
	 * Removes the given entity from the given group.
	 */
	public void remove( long entity, String group ) {
		ArrayList<Long> entitiesList = entitiesByGroup.get( group );
		if ( entitiesList != null ) {
			entitiesList.remove( entitiesList.indexOf( entity ) );
		}
	}
	
	/**
	 * Removes the given entity from the group manager checking against
	 * every single value.
	 */
	public void removeCompletely( long entity ) {
		for ( ArrayList<Long> entityList : entitiesByGroup.values() ) {
			entityList.remove( entityList.indexOf( entity ) );
		}
	}
	
	/**
	 * Returns a ArrayList<Long> containing all the entities that are in every argument.
	 * If used with only one argument, will return only the entities in that component.
	 */
	public ArrayList<Long> get ( String ... args ) {
		ArrayList<Long> entitiesList = entitiesByGroup.get( args[0] );
		ArrayList<Long> auxiliarList = new ArrayList<Long>();
		long entity;
		for ( int i = 1, size = args.length; i < size; i++ ) {	
			auxiliarList = entitiesByGroup.get( args[i] );
			
			// intersection - We must traverse list in inverse order to evade ConcurrentModificationException		
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
	 * Returns a list of all the groups the entity is part of.
	 */
	public ArrayList<String> getGroups( long entity ) {
		ArrayList<String> returnList = new ArrayList<String>();
		// Iterate over all the HashMap (similar to Python's dict.getItems())
		for ( Entry<String, ArrayList<Long>> entry : entitiesByGroup.entrySet() ) {
			if ( entry.getValue().contains( entity ) ) { 
				returnList.add( entry.getKey() );
			}
		}
		return returnList;
	}
	
	/**
	 * @return A boolean depending on whether the given entity is on the given group or not.
	 */
	public boolean isInGroup( long entity, String group ) {
		ArrayList<Long> entitiesSet = entitiesByGroup.get( group );
		return entitiesSet.contains( entity );
	}	
	
	/**
	 * @return A boolean depending on whether the given group exists or not.
	 *          If it actually exists, also returns false if it's empty.
	 */
	public boolean doesGroupExist( String group ) {
		if ( ! entitiesByGroup.containsKey( group ) ) {
			return false;
		} else if ( entitiesByGroup.get( group ).isEmpty() ) {
			return false;
		} else {
		    return true;
		}
	}
	
	/**
	 * Cleans up.
	 */
	public void dispose () {
	    engine = null;
	    entitiesByGroup.clear();
	}
	
	/*
	 * Getters and setters.
	 */
	public HashMap<String, ArrayList<Long>> getEntitiesByGroup() {
		return entitiesByGroup;
	}
	public Engine getEngine() {
		return engine;
	}	
	public void setEngine( Engine engine ) {
		this.engine = engine;
	}

}