package jlarv;
 /*
    Abstract base class from which every system will inherit from.
    Systems implement  the logic around the game.
    Every system will need to override the update method.

    Update method will be called every game loop and must implement the logic 
    of the system (aka, where the magic happens).

    When wanting to get components from entity manager, remember to call for those
    using SomeComponent.class, else they will not be recognized.
  */
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
public abstract class System implements Comparable<System> {
    
	protected EntityManager  entityManager;
	protected EntityFactory  entityFactory;
	protected GroupManager   groupManager;
	private   int 		      priority;
	
	public System ( int priority ) {
		this.priority = priority;
	}
		
	/**
	 * Update method will be called from engine in every game tick.
	 * It will iterate over entities and components and process them.
	 */
	public abstract void update( float delta );
	
	/**
	 * Cleans up the system.
	 */
	public void dispose() {
	    entityManager = null;
	    entityFactory = null;
	    groupManager = null;
    }
	
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
		
	/**
     * Getters and setters.
     * Setters will be called whenever a System is added into an Engine.
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }   
    public EntityFactory getEntityFactory() {
        return entityFactory;
    }   
    public GroupManager getGroupManager() {
        return groupManager;
    }
    public int getPriority() {
        return priority;
    }
    
    public void setEntityManager( EntityManager entityManager ) {
        this.entityManager = entityManager;
    }
    public void setEntityFactory( EntityFactory entityFactory ) {
        this.entityFactory = entityFactory;
    }
    public void setGroupManager( GroupManager groupManager ) {
        this.groupManager = groupManager;
    }
    public void setPriority ( int priority ) {
        this.priority = priority;
    }
    
}
