package jlarv;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class EntityManagerTest {

	Component physics_comp = new PhysicsComponent();
	Component movement_comp = new MovementComponent();
	Component render_comp = new RenderComponent();
	Engine engine;
	GroupManager group_manager;
	EntityManager em;
	
	// Data to debug entities
	ArrayList<Integer> entities;
	
	
	private void setup() {
		entities = new ArrayList<Integer>();
		engine = new Engine();
		group_manager = new GroupManager();
		em = new EntityManager();
	}
	
	/**
	 * Creates some entities and adds them.
	 */
	private void createEntities() {
		for(int i = 0; i < 1000; i++) {
			entities.add(em.createEntity());
		}
	}
	
	/**
	 * Adds some components to the entities.
	 * entities [0], [1], [2], will have all the components
	 * entities [3], [4] will have physics and movement
	 * entities [5], [6] will have only render
	 * entities [7], [8] will have only movement
	 */
	private void add() {
		for (int i = 0; i < 3; i++) {
			em.addComponent(entities.get(i), physics_comp);
			em.addComponent(entities.get(i), render_comp);
			em.addComponent(entities.get(i), movement_comp);
		}
		for (int i = 3; i < 5; i++) {
			em.addComponent(entities.get(i), physics_comp);
			em.addComponent(entities.get(i), movement_comp);
		}
		for (int i = 5; i < 7; i++) {
			em.addComponent(entities.get(i), render_comp);
		}
		for (int i = 7; i < 9; i++) {
			em.addComponent(entities.get(i), movement_comp);
		}
		
	}
	
	@Test
	public void testCreateEntities() {
		setup();
		createEntities();		
	}
	
	@Test
	public void testAddGet() {
		setup();
		createEntities();
		add();
//		java.lang.System.out.println(em.getComponentsByClass());
//		java.lang.System.out.println(PhysicsComponent.class);
//		em.getComponent(entities.get(0), PhysicsComponent.class);
		assertEquals(em.getComponent(entities.get(0), PhysicsComponent.class), physics_comp);
		assertEquals(em.getComponent(entities.get(0), RenderComponent.class), render_comp);
		assertEquals(em.getComponent(entities.get(0), MovementComponent.class), movement_comp);
		
		assertEquals(em.getComponent(entities.get(1), PhysicsComponent.class), physics_comp);
		assertEquals(em.getComponent(entities.get(1), RenderComponent.class), render_comp);
		assertEquals(em.getComponent(entities.get(1), MovementComponent.class), movement_comp);
		
		assertEquals(em.getComponent(entities.get(2), PhysicsComponent.class), physics_comp);
		assertEquals(em.getComponent(entities.get(2), RenderComponent.class), render_comp);
		assertEquals(em.getComponent(entities.get(2), MovementComponent.class), movement_comp);
		
		assertEquals(em.getComponent(entities.get(3), PhysicsComponent.class), physics_comp);
		assertEquals(em.getComponent(entities.get(3), MovementComponent.class), movement_comp);
		
		assertEquals(em.getComponent(entities.get(4), PhysicsComponent.class), physics_comp);
		assertEquals(em.getComponent(entities.get(4), MovementComponent.class), movement_comp);
		
		assertEquals(em.getComponent(entities.get(5), RenderComponent.class), render_comp);
		assertEquals(em.getComponent(entities.get(6), RenderComponent.class), render_comp);
	}
	
	@Test
	public void testHasComponentAndRemove() {
		setup();
		createEntities();
		
		// The entities shouldn't have any component right now
		assertFalse(em.hasComponent(entities.get(0), PhysicsComponent.class));
		assertFalse(em.hasComponent(entities.get(0), RenderComponent.class));
		assertFalse(em.hasComponent(entities.get(0), MovementComponent.class));
		
		// Add some components
		add();
		
		// Now, they should have some components
		assertTrue(em.hasComponent(entities.get(0), PhysicsComponent.class));
		assertTrue(em.hasComponent(entities.get(0), RenderComponent.class));
		assertTrue(em.hasComponent(entities.get(0), MovementComponent.class));
		
		// Remove them
		em.removeComponent(entities.get(0), PhysicsComponent.class);
		em.removeComponent(entities.get(0), RenderComponent.class);
		em.removeComponent(entities.get(0), MovementComponent.class);
		
		// Check possession of the component again
		assertFalse(em.hasComponent(entities.get(0), PhysicsComponent.class));
		assertFalse(em.hasComponent(entities.get(0), RenderComponent.class));
		assertFalse(em.hasComponent(entities.get(0), MovementComponent.class));
	}
	
	@Test
	public void testRemoveEntity() {
		setup();
		createEntities();
		add();
		assertTrue(em.getEntities().contains(entities.get(0)));
		em.removeEntity(entities.get(0));
		assertFalse(em.getEntities().contains(entities.get(0)));
	}
	
	@Test
	public void getEntitiesHavingComponent() {
		setup();
		createEntities();
		add();
		int i, max, max2 = 9;
		ArrayList<Integer> set = em.getEntitiesHavingComponent(PhysicsComponent.class);
		max = 5;
		for (i = 0; i < max; i++) {
			assertTrue(set.contains(entities.get(i)));
		}
		for (i = max; i < max2; i++) {
			assertFalse(set.contains(entities.get(i)));
		}
		
		set = em.getEntitiesHavingComponent(RenderComponent.class);
		max = 3;
		for (i = 0; i < max; i++) {
			assertTrue(set.contains(entities.get(i)));
		}
		assertFalse(set.contains(entities.get(3)));
		assertFalse(set.contains(entities.get(4)));
		assertTrue(set.contains(entities.get(5)));
		assertTrue(set.contains(entities.get(6)));
		assertFalse(set.contains(entities.get(7)));
		assertFalse(set.contains(entities.get(8)));
	}
	
	@Test
	public void getEntitiesHavingComponents() {
		setup();
		createEntities();
		add();
		
		ArrayList<Integer> set = em.getEntitiesHavingComponents(
				PhysicsComponent.class,
				RenderComponent.class,
				MovementComponent.class);
//		java.lang.System.out.println(set);
		assertTrue(set.contains(entities.get(0)));
		assertTrue(set.contains(entities.get(1)));
		assertTrue(set.contains(entities.get(2)));
		assertFalse(set.contains(entities.get(3)));
		assertFalse(set.contains(entities.get(4)));
		assertFalse(set.contains(entities.get(5)));	
		
		set = em.getEntitiesHavingComponents(
				PhysicsComponent.class,
				RenderComponent.class);
		assertTrue(set.contains(entities.get(0)));
		assertTrue(set.contains(entities.get(1)));
		assertTrue(set.contains(entities.get(2)));
		assertFalse(set.contains(entities.get(3)));
		assertFalse(set.contains(entities.get(4)));
		assertFalse(set.contains(entities.get(5)));	
		
		set = em.getEntitiesHavingComponents(
				PhysicsComponent.class,
				MovementComponent.class);
		assertTrue(set.contains(entities.get(0)));
		assertTrue(set.contains(entities.get(1)));
		assertTrue(set.contains(entities.get(2)));
		assertTrue(set.contains(entities.get(3)));
		assertTrue(set.contains(entities.get(4)));
		assertFalse(set.contains(entities.get(5)));
		assertFalse(set.contains(entities.get(6)));
		assertFalse(set.contains(entities.get(7)));	
	}
	
	@Test
	public void getComponentsOfEntity() {
		setup();
		createEntities();
		
		ArrayList<Component> list = em.getComponentsOfEntity(entities.get(0));
		assertTrue(list.isEmpty());
		
		add();
		
		list = em.getComponentsOfEntity(entities.get(0));	
		assertTrue(list.contains(physics_comp));
		assertTrue(list.contains(render_comp));
		assertTrue(list.contains(movement_comp));
		
		list = em.getComponentsOfEntity(entities.get(2));
		assertTrue(list.contains(physics_comp));
		assertTrue(list.contains(render_comp));
		assertTrue(list.contains(movement_comp));
		
		list = em.getComponentsOfEntity(entities.get(3));
		assertTrue(list.contains(physics_comp));
		assertFalse(list.contains(render_comp));
		assertTrue(list.contains(movement_comp));
		
		list = em.getComponentsOfEntity(entities.get(5));
		assertFalse(list.contains(physics_comp));
		assertTrue(list.contains(render_comp));
		assertFalse(list.contains(movement_comp));
	}
	
	@Test
	public void getComponentsOfType() {
		setup();
		createEntities();		
		add();
		
		ArrayList<Component> list = em.getComponentsOfType(PhysicsComponent.class);
		assertTrue(list.size() == 5);
		list = em.getComponentsOfType(RenderComponent.class);
		assertTrue(list.size() == 5);
		list = em.getComponentsOfType(MovementComponent.class);
		assertTrue(list.size() == 7);
		
	}

}
