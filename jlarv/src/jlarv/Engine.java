package jlarv;

import java.util.PriorityQueue;

/*
 The engine is the glue that puts everything together.
 It holds:
 - an instance of a systems list (PriorityQueue): this.systems
 - an instance of an entity manager:  this.entity_manager
 - an instance of a group manager:    this.group_manager
 - an instance of an entity_factory:  this.entity_factory

 When updating the game, one should only do a Engine.update()
 call, as the Engine will be responsible for updating every
 single system in the right order (given that we insert
 the systems in the right priority).
 */
public class Engine {
    
	private PriorityQueue<System>  systems;
	protected EntityManager          entityManager;
	protected GroupManager           groupManager;
	protected EntityFactory          entityFactory;
	
	/**
	 * Recommended constructor.
	 * Only use the other one in case the entity factory isn't known beforehand.
	 */
	public Engine( EntityFactory entityFactory ) {
		systems = new PriorityQueue<System>();
		entityManager = new EntityManager();
		groupManager = new GroupManager( this );
		
		setEntityFactory( entityFactory );
	}
	
	/**
	 * Secondary constructor, needs to assign and bind the entity factory later.
	 */
	public Engine() {
		systems = new PriorityQueue<System>();
		entityManager = new EntityManager();
		groupManager = new GroupManager( this );
	}	
	
	/**
	 * Adds the given systems to the priority queue using the given priority and also
	 * binds the managers and the factory to it.
	 * Priority works as the lowest value will be the first to update.
	 */
	public void addSystems( System ... systems ) {
	    for ( System system : systems ) {
        	// Bind the system
        	system.setEntityManager( entityManager );
        	system.setGroupManager( groupManager );
        	system.setEntityFactory( entityFactory );
        	
        	this.systems.add( system );
	    }
	}
	
	/**
	 * Changes the given system's priority so it updates after/before.
	 * Systems update from lower priority to higher.
	 * @param cleanUp If true, sets all the system variables to null;
	 */	
	public void changeSystemPriority( System system, int newPriority, boolean cleanUp ) {
	    systems.remove( system );
	    if ( cleanUp ) {
	        cleanUpSystem( system );
        }
	    system.setPriority( newPriority );
		systems.add( system );
	}	

    /**
	 * Removes the given system from the priority queue.
	 * @param cleanUp If true, sets all the system variables to null;
	 * @return True if success or false if failed to find it inside the system's
	 *          PriorityQueue.
	 */
	public boolean removeSystem( System system, boolean cleanUp ) {
	    if ( cleanUp ) {
	        cleanUpSystem( system );
	    }	    
		return systems.remove( system );
	}
	
	/**
	 * Updates every system in priority order.
	 * @param delta The time elapsed since last update step.
	 */
	public void update( float delta ) {
		for ( System system : systems ) {
			system.update( delta );
		}
	}
	
	/**
	 * Empties the engine, setting every container to null so they can be 
	 * garbage collected. 
	 */
	// TODO: check against circular references that prevent garbage collection.
	//       This can be done iterating over the systems and setting all their pointers to null and doing the
	//       same with factories and managers.
	public void clean() {
		for ( System system : systems) {
		    removeSystem( system, true );
		}
		entityManager = null;
		entityFactory = null;
		groupManager = null;
	}
	
	private void cleanUpSystem( System system ) {
	    system.setEntityFactory( null );
	    system.setEntityManager( null );
	    system.setGroupManager( null );
    }
	
	/*
	 * Getter methods.
	 */
	public PriorityQueue<System> getSystems() {
		return systems;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public EntityFactory getEntityFactory() {
		return entityFactory;
	}
	
	/**
	 * Setter for the entity factory, also binds it to the entity manager and the
	 * group manager.
	 */
	public void setEntityFactory( EntityFactory entityFactory ) {
		this.entityFactory = entityFactory;
		this.entityFactory.setEntityManager( entityManager );
		this.entityFactory.setGroupManager( groupManager );
	}
}
