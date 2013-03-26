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
	private PriorityQueue<System> systems;
	private EntityManager entity_manager;
	private GroupManager group_manager;
	private EntityFactory entity_factory;
	
	/**
	 * Basic constructor.
	 * Needs a factory.
	 * Initializes the variables and binds the factory.
	 */
	public Engine(EntityFactory entity_factory) {
		systems = new PriorityQueue<System>();
		entity_manager = new EntityManager();
		group_manager = new GroupManager(this);
		
		setEntityFactory(entity_factory);
	}
	
	/**
	 * Secondary constructor, needs to assign and bind the entity factory later.
	 */
	public Engine() {
		systems = new PriorityQueue<System>();
		entity_manager = new EntityManager();
		group_manager = new GroupManager(this);
	}

	/*
	 * Getter methods.
	 */
	protected PriorityQueue<System> getSystems() {
		return systems;
	}

	protected EntityManager getEntityManager() {
		return entity_manager;
	}

	protected GroupManager getGroupManager() {
		return group_manager;
	}

	protected EntityFactory getEntityFactory() {
		return entity_factory;
	}
	
	/**
	 * Setter for the entity factory, also binds it to the entity manager and the
	 * group manager.
	 */
	protected void setEntityFactory(EntityFactory entity_factory) {
		this.entity_factory = entity_factory;
		this.entity_factory.setEntityManager(entity_manager);
		this.entity_factory.setGroupManager(group_manager);
	}
	
	/**
	 * Adds the given system to the priority queue using the given priority and also
	 * binds the managers and the factory to it.
	 * Priority works as the lowest value will be the first to update.
	 */
	public void addSystem(System system) {
		// Bind the system
		system.setEntityManager(entity_manager);
		system.setGroupManager(group_manager);
		system.setEntityFactory(entity_factory);
		
		systems.add(system);
	}
	
	/**
	 * Changes the given system's priority so it updates after/before.
	 * Systems update from lower priority to higher.
	 */	
	public void changeSystemPriority(System system, int new_priority) {
		systems.remove(system);
		system.setPriority(new_priority);
		systems.add(system);
	}
	
	
	/**
	 * Removes the given system from the priority queue.
	 * @return True if success or false if failed to find it inside the system's
	 *          PriorityQueue.
	 */
	public boolean removeSystem(System system) {
		return systems.remove(system);
	}
	
	/**
	 * Updates every system in priority order.
	 * @param delta The time elapsed since last update step.
	 */
	public void update(float delta) {
		for (System system : systems) {
			system.update(delta);
		}
	}
	
	/**
	 * Empties the engine, setting every container to null so they can be 
	 * garbage collected.
	 * TODO: check against circular references
	 *  that prevent garbage collection.
	 */
	public void clean() {
		systems = null;
		entity_manager = null;
		entity_factory = null;
		group_manager = null;
	}	
}
