package jlarv;

import java.util.ArrayList;
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
	 * @param cleanUp If true, disposes the system.
	 */	
	public void changeSystemPriority( System system, int newPriority, boolean cleanUp ) {
	    systems.remove( system );
	    if ( cleanUp ) {
	        system.dispose();
        }
	    system.setPriority( newPriority );
		systems.add( system );
	}	

    /**
	 * Removes the given system from the priority queue.
	 * @param cleanUp If true, disposes the system.
	 * @return True if success or false if failed to find it inside the system's
	 *          PriorityQueue.
	 */
	public boolean removeSystem( System system, boolean cleanUp ) {
	    if ( cleanUp ) {
	        system.dispose();
	    }	    
		return systems.remove( system );
	}
	
	/**
	 * Removes all the systems of the given class.
	 */
	public void removeSystemsClass( Class<? extends System> type ) {
		ArrayList<System> toBeDeleted = new ArrayList<System>();
		for ( System system : systems ) {
			if ( system.getClass() == type ) {
				toBeDeleted.add( system );
			}
		}
		
		for ( System system : toBeDeleted ) {
			systems.remove( system );
			system.dispose();
		}
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
	public void dispose() {
		for ( System system : systems) {
		    system.dispose();
		}
		entityManager.dispose();
		entityFactory.dispose();
		groupManager.dispose();
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
	
	public void setGroupManager( GroupManager manager ) {
		manager.setEngine( this );
		this.groupManager = manager;
	}
	
	public void setEntityManager( EntityManager manager ) {
		this.entityManager = manager;
	}
}
