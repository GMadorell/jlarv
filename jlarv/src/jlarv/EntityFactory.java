package jlarv;

/*
    Entity factory is just a helper, it doesn't have any methods.
    When implementing a game, every entity will be created from here, so it's
    really only a class to put together all the building of entities for easier
    reading and usage of the rest of the code.

    It has a createX for every entity in the game.
    Handles the creation of the entities and also assigning components to them.

    USAGE:
      When creating the entity factory for your own game, just subclass it and add
      a createX for every X entity you want in your game (remember to import
      all the components you are going to use beforehand).
      The entity factory may be needed when you create an Engine
      instance, so you will need to create it before the engine.
 */
public abstract class EntityFactory {
	private EntityManager entity_manager;
	private GroupManager group_manager;
	
	/*
	 * Getter methods.
	 */
	protected EntityManager getEntityManager() {
		return entity_manager;
	}
	protected GroupManager getGroupManager() {
		return group_manager;
	}
	
	/*
	 * Setter methods.
	 * This methods are called from the engine whenever we add the
	 * entity manager to it.
	 */
	protected void setEntityManager(EntityManager entity_manager) {
		this.entity_manager = entity_manager;
	}
	protected void setGroupManager(GroupManager group_manager) {
		this.group_manager = group_manager;
	}
	
	
	

}
