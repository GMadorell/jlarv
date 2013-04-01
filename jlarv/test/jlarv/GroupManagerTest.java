package jlarv;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

/**
 * Class used to test if the GroupManager class really works or we failed
 * really hard.
 * @author Gerard Madorell Sala
 */
public class GroupManagerTest {

	Engine engine;
	GroupManager gm;
	// Data to debug components (simulate components names)
	ArrayList<String> comp_names;
	String comp_name_1, comp_name_2, comp_name_3, comp_name_4, comp_name_5;
	// Data to simulate entities (unique ids)
	ArrayList<Integer> entities;
	int ent1, ent2, ent3, ent4, ent5, ent6, ent7, ent8;
	
	private void setup() {
		engine = new Engine();
		gm = new GroupManager();
		comp_names = new ArrayList<String>();
		entities = new ArrayList<Integer>();
		
		comp_name_1 = "MoveComponent";
		comp_name_2 = "PhysicsComponent";
		comp_name_3 = "RenderComponent";
		comp_name_4 = "CallableComponent";
		comp_name_5 = "StateComponent";		
		comp_names.add(comp_name_1);
		comp_names.add(comp_name_2);
		comp_names.add(comp_name_3);
		comp_names.add(comp_name_4);
		comp_names.add(comp_name_5);
		
		ent1=1; ent2=2; ent3=3; ent4=4;ent5=5;ent6=6;ent7=7;ent8=8;
		entities.add(ent1);
		entities.add(ent1);
		entities.add(ent2);
		entities.add(ent3);
		entities.add(ent4);
		entities.add(ent5);
		entities.add(ent6);
		entities.add(ent7);
		entities.add(ent8);		
	}
	
	/**
	 * Adds some components to the group manager.
	 * If it worked well, we should have:
	 * 		comp_name_1: ent1, ent2
	 * 		comp_name_2: ent1, ent2
	 * 		comp_name_3: ent1, ent2, ent3
	 * 		comp_name_4, ent1, ent2, ent3, ent4
	 * 		comp_name_5: ent1, ent2, ent4, ent5
	 */
	private void add() {
		for (String comp : comp_names) {
			gm.add(ent1, comp);
			gm.add(ent2, comp);
		}
		gm.add(ent3, comp_name_3);
		gm.add(ent3, comp_name_4);
		gm.add(ent4, comp_name_4);
		gm.add(ent4, comp_name_5);
		gm.add(ent5, comp_name_5);
	}
	
	@Test
	public void testConstructor() {
		setup();
		
		gm = new GroupManager(engine);
		assertEquals(gm.getEngine(), engine);
		
		engine = new Engine();
		gm.setEngine(engine);
		assertEquals(gm.getEngine(), engine);		
	}
	
	@Test
	public void testAddGet() {
		setup();  
		
		// Add entities to groups
		add();
//		java.lang.System.out.println(gm.getEntitiesByGroup());
		// Test get with one argument
		assertTrue(gm.get(comp_name_3).contains(ent1));
		assertTrue(gm.get(comp_name_3).contains(ent2));
		assertTrue(gm.get(comp_name_3).contains(ent3));
		assertTrue(!gm.get(comp_name_1).contains(ent3));
		assertTrue(!gm.get(comp_name_1).contains(ent4));
		assertTrue(!gm.get(comp_name_1).contains(ent5));
		
		// Test get with multiple arguments
		ArrayList<Integer> get_set = gm.get(comp_name_1, comp_name_2, comp_name_3);
//		java.lang.System.out.println(gm.getEntitiesByGroup());
//		java.lang.System.out.println(get_set);
		assertTrue(get_set.contains(ent1));
		assertTrue(get_set.contains(ent2));
		assertTrue(!get_set.contains(ent3));
		assertTrue(!get_set.contains(ent4));
		
		get_set = gm.get(comp_name_3, comp_name_4, comp_name_5);
//		java.lang.System.out.println(gm.getEntitiesByGroup());
//		java.lang.System.out.println(get_set);
		assertTrue(get_set.contains(ent1));
		assertTrue(get_set.contains(ent2));
		assertTrue(!get_set.contains(ent3));
		assertTrue(!get_set.contains(ent4));
		assertTrue(!get_set.contains(ent5));
		
		get_set = gm.get(comp_name_1, comp_name_2, comp_name_3, comp_name_4, comp_name_5);
		assertTrue(get_set.contains(ent1));
		assertTrue(get_set.contains(ent2));
		assertTrue(!get_set.contains(ent3));
		assertTrue(!get_set.contains(ent4));
		assertTrue(!get_set.contains(ent5));		
	}
	
	@Test
	public void testAddMultipleArgs() {
	    setup();
	    
	    gm.add(ent1, comp_name_1, comp_name_2, comp_name_3);
	    assertTrue(gm.getGroups(ent1).contains(comp_name_1));
	    assertTrue(gm.getGroups(ent1).contains(comp_name_2));
	    assertTrue(gm.getGroups(ent1).contains(comp_name_3));
	    assertFalse(gm.getGroups(ent1).contains(comp_name_4));
	    assertFalse(gm.getGroups(ent1).contains(comp_name_5));
	    
	    gm.add(ent2, comp_name_1, comp_name_2);
	    assertTrue(gm.getGroups(ent2).contains(comp_name_1));
	    assertTrue(gm.getGroups(ent2).contains(comp_name_2));
	    assertFalse(gm.getGroups(ent2).contains(comp_name_3));
	    assertFalse(gm.getGroups(ent2).contains(comp_name_4));
	    assertFalse(gm.getGroups(ent2).contains(comp_name_5));
	   
	}
	
	@Test
	public void testRemoveAndRemoveAll() {
		setup();
		add();
		
		// First, we check that the entities initially pertain at the group
		ArrayList<Integer> get_set = gm.get(comp_name_3);
//		java.lang.System.out.println(gm.getEntitiesByGroup());
		assertTrue(get_set.contains(ent1));
		assertTrue(get_set.contains(ent2));
		assertTrue(get_set.contains(ent3));
		// Remove them
		gm.remove(ent1, comp_name_3);
		gm.remove(ent2, comp_name_3);
		gm.remove(ent3, comp_name_3);	
		// Check if they're no longer in the group
		get_set = gm.get(comp_name_3);
		assertTrue(!get_set.contains(ent1));
		assertTrue(!get_set.contains(ent2));
		assertTrue(!get_set.contains(ent3));
		
		setup();
		add();
		
		// Check if the entity is in all the groups
		assertTrue(gm.get(comp_name_1).contains(ent1));
		assertTrue(gm.get(comp_name_2).contains(ent1));
		assertTrue(gm.get(comp_name_3).contains(ent1));
		assertTrue(gm.get(comp_name_4).contains(ent1));
		assertTrue(gm.get(comp_name_5).contains(ent1));
		// Remove it completely
		gm.removeCompletely(ent1);
		// Check if the entity is no longer inside the group manager
		assertTrue(!gm.get(comp_name_1).contains(ent1));
		assertTrue(!gm.get(comp_name_2).contains(ent1));
		assertTrue(!gm.get(comp_name_3).contains(ent1));
		assertTrue(!gm.get(comp_name_4).contains(ent1));
		assertTrue(!gm.get(comp_name_5).contains(ent1));		
	}
	
	@Test
	public void testGetGroups() {
		setup();
		add();
		
		// Try if they contain the original groups
		assertTrue(gm.getGroups(ent1).containsAll(comp_names));
		assertTrue(gm.getGroups(ent2).containsAll(comp_names));
		assertTrue(gm.getGroups(ent3).contains(comp_name_3));
		assertTrue(gm.getGroups(ent3).contains(comp_name_4));
		assertTrue(gm.getGroups(ent4).contains(comp_name_4));
		assertTrue(gm.getGroups(ent4).contains(comp_name_5));
		assertTrue(gm.getGroups(ent5).contains(comp_name_5));
		
		// Try if they don't contain the groups they aren't supposed to
		assertTrue(!gm.getGroups(ent1).contains("You're not supposed to have this group"));
		assertTrue(!gm.getGroups(ent2).contains("You're not supposed to have this group"));
		assertTrue(!gm.getGroups(ent3).contains(comp_name_1));
		assertTrue(!gm.getGroups(ent3).contains(comp_name_2));
		assertTrue(!gm.getGroups(ent3).contains(comp_name_5));
		assertTrue(!gm.getGroups(ent4).contains(comp_name_1));
		assertTrue(!gm.getGroups(ent4).contains(comp_name_2));
		assertTrue(!gm.getGroups(ent4).contains(comp_name_3));
		assertTrue(!gm.getGroups(ent5).contains(comp_name_1));
		assertTrue(!gm.getGroups(ent5).contains(comp_name_2));
		assertTrue(!gm.getGroups(ent5).contains(comp_name_3));
	}
	
	@Test
	public void testIsInGroup() {
		setup();
		add();
		
		// Try if the entity is in the checked group
		for (String group : comp_names) {
			assertTrue(gm.isInGroup(ent1, group));
			assertTrue(gm.isInGroup(ent2, group));
		}
		assertTrue(gm.isInGroup(ent3, comp_name_3));
		assertTrue(gm.isInGroup(ent3, comp_name_4));
		
		assertFalse(gm.isInGroup(ent3, comp_name_1));
		assertFalse(gm.isInGroup(ent3, comp_name_2));
		assertFalse(gm.isInGroup(ent3, comp_name_5));
		assertFalse(gm.isInGroup(ent4, comp_name_1));
		assertFalse(gm.isInGroup(ent5, comp_name_1));
	}
	
	@Test
	public void doesGroupExist() {
		setup();
		assertFalse(gm.doesGroupExist(comp_name_1));
		assertFalse(gm.doesGroupExist(comp_name_2));
		assertFalse(gm.doesGroupExist(comp_name_3));
		assertFalse(gm.doesGroupExist(comp_name_4));
		assertFalse(gm.doesGroupExist(comp_name_5));
		
		add();
		assertTrue(gm.doesGroupExist(comp_name_1));
		assertTrue(gm.doesGroupExist(comp_name_2));
		assertTrue(gm.doesGroupExist(comp_name_3));
		assertTrue(gm.doesGroupExist(comp_name_4));
		assertTrue(gm.doesGroupExist(comp_name_5));
	}
	

}
