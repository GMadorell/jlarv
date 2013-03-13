package jlarv;
 /*
    Abstract base class from which every system will inherit from.
    Systems implement  the logic around the game.
    Every system will need to override the update method.

    Update method will be called every game loop and must implement the logic 
    of the system (aka, where the magic happens).

    When wanting to get components from entity manager, remember to call for those
    using component_class.__name__, else they will not be recognized.
  */
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
public abstract class System {
	private EntityManager entity_manager;
	private EntityFactory entity_factory;
	private GroupManager group_manager;
	
	/*
	 * Getters and setters.
	 * Setters will be called whenever a System is added into an Engine.
	 */
	protected EntityManager getEntityManager() {
		return entity_manager;
	}	
	protected EntityFactory getEntityFactory() {
		return entity_factory;
	}	
	protected GroupManager getGroupManager() {
		return group_manager;
	}
	
	protected void setEntityManager(EntityManager entity_manager) {
		this.entity_manager = entity_manager;
	}
	protected void setEntityFactory(EntityFactory entity_factory) {
		this.entity_factory = entity_factory;
	}
	protected void setGroupManager(GroupManager group_manager) {
		this.group_manager = group_manager;
	}
	
	/*
	 * Update method will be called from engine in every game tick.
	 * It will iterate over entities and components and process them.
	 */
	public abstract void update();	
}
