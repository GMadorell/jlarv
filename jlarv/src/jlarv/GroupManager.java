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
	private Engine engine;
	private HashMap<String, HashSet<Integer>> entities_by_group =
			new HashMap<String, HashSet<Integer>>();

	public GroupManager(Engine engine) {
		this.engine = engine;
	}
	
	protected Engine getEngine() {
		return engine;
	}

	/**
	 * Adds the given entity into the given group.
	 * An entity can't be in the same group twice.
	 */
	public void add(int entity, String group) {
		if (entities_by_group.containsKey(group)) {
			entities_by_group.get(group).add(entity);
		}
		else {
			HashSet<Integer> entities_set = new HashSet<Integer>();
			entities_set.add(entity);
			entities_by_group.put(group, entities_set);
		}
	}
	
	/**
	 * Removes the given entity from the given group.
	 * @return true if success, false if failed.
	 */
	public boolean remove(int entity, String group) {
		HashSet<Integer> entities_set = entities_by_group.get(group);
		if (entities_set != null){
			return entities_set.remove(entity);
		}
		else 
			return false;
	}
	
	/**
	 * Removes the given entity from the group manager checking against
	 * every single value.
	 */
	public void removeCompletely(int entity) {
		for (HashSet<Integer> entity_set : entities_by_group.values()) {
			entity_set.remove(entity);
		}
	}
	
	/**
	 * Returns a TODO: specify what should it return, right now returns HashSet<Integer>
	 * containing all the entities that are in every argument.
	 * If used with only one argument, will return only the entities in that component.
	 */
	public HashSet<Integer> get (String ... args){
		HashSet<Integer> return_set = new HashSet<Integer>();
		HashSet<Integer> aux_set = new HashSet<Integer>();
		for (String arg : args) {			
			// In 1st iter, assign the value of the returned set to the first arg set.
			if (return_set.isEmpty()) {
				return_set = entities_by_group.get(arg);
			}
			// In the other iterations, do an intersection.
			else {
				aux_set = entities_by_group.get(arg);
				for (int entity : return_set) {
					if (!aux_set.contains(entity)) {
						return_set.remove(entity);
					}
				}
			}
		}
		return return_set;
	}
	
	/**
	 * Returns a list of all the groups the entity is part of.
	 */
	public ArrayList<String> getGroups(int entity){
		ArrayList<String> return_list = new ArrayList<String>();
		// Iterate over all the HashMap (similar to Python's dict.getItems())
		for (Entry<String, HashSet<Integer>> entry : entities_by_group.entrySet()) {
			if (entry.getValue().contains(entity)){ 
				return_list.add(entry.getKey());
			}
		}
		return return_list;
	}
	
	/**
	 * @return A boolean depending on whether the given entity is on the given group or not.
	 */
	public boolean isInGroup(int entity, String group) {
		HashSet<Integer> entities_set = entities_by_group.get(group);
		return entities_set.contains(entity);
	}	
	
	/**
	 * @return A boolean depending on whether the given group exists or not.
	 *          If it actually exists, also returns false if it's empty.
	 */
	public boolean doesGroupExist(String group) {
		if (!entities_by_group.containsKey(group)) {
			return false;
		}
		if (entities_by_group.get(group).isEmpty()) {
			return false;
		}
		return true;
	}

}