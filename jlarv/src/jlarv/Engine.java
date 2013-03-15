package jlarv;

/*
 The engine is the glue that puts everything together.
 It holds:
 - an instance of a systems list (PriorityList): this.systems
 - an instance of an entity manager:  this.entity_manager
 - an instance of a group manager:    this.group_manager
 - an instance of an entity_factory:  this.entity_factory

 When updating the game, one should only do a Engine.update()
 call, as the Engine will be responsible for updating every
 single system in the right order (given that we insert
 the systems in the right priority).
 */
public class Engine {
	private PriorityList systems; //TODO implement this as java's priority queue
	private EntityManager entity_manager;
	private GroupManager group_manager;
	private EntityFactory entity_factory;
	
	/**
	 * Basic constructor.
	 * Needs a factory.
	 * Initializes the variables and binds the factory.
	 * TODO: javadocs
	 */
	public Engine(EntityFactory entity_factory) {
		systems = new PriorityList();
		entity_manager = new EntityManager();
		group_manager = new GroupManager(this);
		
		setEntityFactory(entity_factory);
	}
	
	/**
	 * Secondary constructor, needs to assign and bind the entity factory later.
	 */
	public Engine() {
		systems = new PriorityList;
		entity_manager = new EntityManager();
		group_manager = new GroupManager();
	}

	/*
	 * Getter methods.
	 */
	protected PriorityList getSystems() {
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
	 * TODO: add javadoc parameters 
	 */
	public void addSystem(System system, int priority) {
		// Bind the system
		system.setEntityManager(entity_manager);
		system.setGroupManager(group_manager);
		system.setEntityFactory(entity_factory);
		systems.add(system, priority); // Add the system to the priority queue
	}
	
	/**
	 * Changes the given system's priority so it updates after/before.
	 * TODO javadocs
	 */	
	public void changeSystemPriority(System system, int new_priority) {
		//TODO: search if system is inside the queue or not
		systems.change(system, new_priority);
	}
	
	
	/**
	 * Removes the given system from the priority queue.
	 * TODO javadocs
	 */
	public void removeSystem(System system) {
		systems.remove(system);
	}
	
	/**
	 * Updates every system in priority order.
	 */
	public void update() {
		for (System system : systems) {
			system.update();
		}
	}
	
	/**
	 * Empties the engine, setting every container to null so they can be 
	 * garbage collected.
	 * TODO: check against circular references
	 *  that dodge garbage collection.
	 */
	public void clean() {
		systems = null;
		entity_manager = null;
		entity_factory = null;
		group_manager = null;
	}	
}
