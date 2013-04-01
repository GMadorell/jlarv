package jlarv;

import java.util.*;
import java.util.Map.Entry;

/*
	Allows entities to be added into groups to be fetched later on.
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
	private Engine                               engine;
	private HashMap<String, ArrayList<Integer>>  entitiesByGroup;

	public GroupManager( Engine engine ) {
		entitiesByGroup = new HashMap<String, ArrayList<Integer>>();
		this.engine = engine;
	}
	
	/**
	 * Using this constructor is not recommended. Need to assign engine later.
	 */
	public GroupManager() {	
		entitiesByGroup = new HashMap<String, ArrayList<Integer>>();
	}

	/**
	 * Adds the given entity into the given group.
	 * An entity can't be in the same group twice.
	 */
	public void add( int entity, String group ) {
		if (entitiesByGroup.containsKey( group ) ) {
			entitiesByGroup.get( group ).add( entity );
		}
		else {
			ArrayList<Integer> entitiesList = new ArrayList<Integer>();
			entitiesList.add( entity );
			entitiesByGroup.put( group, entitiesList );
		}
	}
	
	/**
	 * Removes the given entity from the given group.
	 */
	public void remove( int entity, String group ) {
		ArrayList<Integer> entitiesList = entitiesByGroup.get( group );
		if ( entitiesList != null ) {
			entitiesList.remove( entitiesList.indexOf( entity ) );
		}
	}
	
	/**
	 * Removes the given entity from the group manager checking against
	 * every single value.
	 */
	public void removeCompletely( int entity ) {
		for ( ArrayList<Integer> entityList : entitiesByGroup.values() ) {
			entityList.remove( entityList.indexOf( entity ) );
		}
	}
	
	/**
	 * Returns a ArrayList<Integer> containing all the entities that are in every argument.
	 * If used with only one argument, will return only the entities in that component.
	 */
	public ArrayList<Integer> get ( String ... args ) {
		ArrayList<Integer> entitiesList = entitiesByGroup.get( args[0] );
		ArrayList<Integer> auxiliarList = new ArrayList<Integer>();
		int entity;
		for ( int i = 1, size = args.length; i < size; i++ ) {	
			auxiliarList = entitiesByGroup.get( args[i] );
			
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
	 * Returns a set of all the groups the entity is part of.
	 */
	public ArrayList<String> getGroups( int entity ){
		ArrayList<String> returnList = new ArrayList<String>();
		// Iterate over all the HashMap (similar to Python's dict.getItems())
		for ( Entry<String, ArrayList<Integer>> entry : entitiesByGroup.entrySet() ) {
			if ( entry.getValue().contains( entity ) ) { 
				returnList.add( entry.getKey() );
			}
		}
		return returnList;
	}
	
	/**
	 * @return A boolean depending on whether the given entity is on the given group or not.
	 */
	public boolean isInGroup( int entity, String group ) {
		ArrayList<Integer> entitiesSet = entitiesByGroup.get( group );
		return entitiesSet.contains( entity );
	}	
	
	/**
	 * @return A boolean depending on whether the given group exists or not.
	 *          If it actually exists, also returns false if it's empty.
	 */
	public boolean doesGroupExist( String group ) {
		if ( ! entitiesByGroup.containsKey( group ) ) {
			return false;
		}
		if ( entitiesByGroup.get( group ).isEmpty() ) {
			return false;
		}
		return true;
	}
	
	/*
	 * Getters and setters.
	 */
	public HashMap<String, ArrayList<Integer>> getEntitiesByGroup() {
		return entitiesByGroup;
	}
	public Engine getEngine() {
		return engine;
	}	
	public void setEngine( Engine engine ) {
		this.engine = engine;
	}

}