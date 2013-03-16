package jlarv;

/*
  Abstract class for every component, every one of them should extend it.

  It's utility is to keep the code more organized.
  Also allows for type checking when debugging.
	
  A component is just a ton of data, almost always with just a constructor
  that sets up some variables.
  Can also have some methods like getters and setters to modify
  and/or access the data, but can't have any logic around it.
  Logic is 100% done by the Systems.  
 */
public abstract class Component {
	
	//TODO: this should be static (so we can say MoveComponent.getName()), 
	//       but have no idea how to do it
	public String getName() {
		return this.getClass().getSimpleName();
	}
}
