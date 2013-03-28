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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
public abstract class System implements Comparable<System> {
	private EntityManager  entity_manager;
	private EntityFactory  entity_factory;
	private GroupManager   group_manager;
	private int 		    priority;
	
	public System ( int priority ) {
		this.priority = priority;
	}
	public System () {
		this.priority = 0;
	}
	/**
	 * Getters and setters.
	 * Setters will be called whenever a System is added into an Engine.
	 */
	public EntityManager getEntityManager() {
		return entity_manager;
	}	
	public EntityFactory getEntityFactory() {
		return entity_factory;
	}	
	public GroupManager getGroupManager() {
		return group_manager;
	}
	public int getPriority() {
		return priority;
	}
	
	public void setEntityManager( EntityManager entity_manager ) {
		this.entity_manager = entity_manager;
	}
	public void setEntityFactory( EntityFactory entity_factory ) {
		this.entity_factory = entity_factory;
	}
	public void setGroupManager( GroupManager group_manager ) {
		this.group_manager = group_manager;
	}
	public void setPriority ( int priority ) {
		this.priority = priority;
	}
	
	/**
	 * Update method will be called from engine in every game tick.
	 * It will iterate over entities and components and process them.
	 */
	public abstract void update( float delta );
	
	/**
	 * Implement Comparator interface.
	 * Allows us to iterate over the systems in priority order.
	 * The system with the lower priority will be the first to update.
	 */
	@Override 
	public final int compareTo( System other ) {
		if ( this.priority < other.priority ) {
			return -1;
		} else if ( this.priority == other.priority ) {
			return 0;
		} else {
			return 1;
		}
	}
}
