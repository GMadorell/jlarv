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
	private HashMap<String, ArrayList<Integer>> entities_by_group =
			new HashMap<String, ArrayList<Integer>>();

	public GroupManager(Engine engine) {
		this.engine = engine;
	}
	
	/**
	 * Using this constructor is not recommended. Need to assign engine later.
	 */
	public GroupManager() {	
	}
	
	protected HashMap<String, ArrayList<Integer>> getEntitiesByGroup() {
		return entities_by_group;
	}
	protected Engine getEngine() {
		return engine;
	}
	
	protected void setEngine(Engine engine) {
		this.engine = engine;
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
			ArrayList<Integer> entities_list = new ArrayList<Integer>();
			entities_list.add(entity);
			entities_by_group.put(group, entities_list);
		}
	}
	
	/**
	 * Removes the given entity from the given group.
	 */
	public void remove(int entity, String group) {
		ArrayList<Integer> entities_list = entities_by_group.get(group);
		if (entities_list != null){
			entities_list.remove(entities_list.indexOf(entity));
		}
	}
	
	/**
	 * Removes the given entity from the group manager checking against
	 * every single value.
	 */
	public void removeCompletely(int entity) {
		for (ArrayList<Integer> entity_list : entities_by_group.values()) {
			entity_list.remove(entity_list.indexOf(entity));
		}
	}
	
	/**
	 * Returns a [TODO: specify what should it return, right now returns] ArrayList<Integer>
	 * containing all the entities that are in every argument.
	 * If used with only one argument, will return only the entities in that component.
	 */
	public ArrayList<Integer> get (String ... args){
		ArrayList<Integer> entities_list = entities_by_group.get(args[0]);
		ArrayList<Integer> auxiliar_list = new ArrayList<Integer>();
		int entity;
		for (int i = 1, size = args.length; i < size; i++) {	
			auxiliar_list = entities_by_group.get(args[i]);
			
			// Intersection - We must traverse list in inverse order to evade ConcurrentModificationException		
			for (int j = entities_list.size()-1; j >= 0; j--) {
				entity = entities_list.get(j);
				if (!auxiliar_list.contains(entity)) {
					entities_list.remove(j);
				}
			}
		}
		return entities_list;
	}
	
	/**
	 * Returns a set of all the groups the entity is part of.
	 */
	public ArrayList<String> getGroups(int entity){
		ArrayList<String> return_list = new ArrayList<String>();
		// Iterate over all the HashMap (similar to Python's dict.getItems())
		for (Entry<String, ArrayList<Integer>> entry : entities_by_group.entrySet()) {
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
		ArrayList<Integer> entities_set = entities_by_group.get(group);
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