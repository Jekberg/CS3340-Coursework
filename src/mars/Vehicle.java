package mars;

import java.util.ArrayList;
import java.util.Random;

class Vehicle extends Entity{
	public boolean carryingSample;
	
	public Vehicle(Location l){
		super(l);	
		this.carryingSample = false;
	}

	public void act(Field f, Mothership m, ArrayList<Rock> rocksCollected)
	{
		//actCollaborative(f, m, rocksCollected);
		actSimple(f, m, rocksCollected);
	}
	
	
	public void actCollaborative(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		//complete this method
	}

	public void actSimple(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		
		if(carryingSample && f.isNeighbourTo(location, Mothership.class))
			carryingSample = false;
		
		if(carryingSample && !f.isNeighbourTo(location, Mothership.class))
		{
			var currentSignal = f.getSignalStrength(location);
			
			var newLoc = f.getAllfreeAdjacentLocations(location)
				.stream()
				.filter(loc -> currentSignal <= f.getSignalStrength(loc))
				.max((l, r) -> f.getSignalStrength(l) - f.getSignalStrength(r))
				.orElse(location);
			f.clearLocation(location);
			f.place(this, newLoc);
			location = newLoc;
			
		}
		
		if (f.isNeighbourTo(location, Rock.class))
		{
			var rocLoc = f.getNeighbour(location, Rock.class);
			//rocksCollected.add((Rock) f.getObjectAt(rocLoc));
			f.clearLocation(rocLoc);
			carryingSample = true;
		}
		
		// Default action
		moveRandomly(f);
	}
	
	private void PickupSample() {}
	
	private void moveRandomly(Field f) {
		
		
		var newLocation = f.freeAdjacentLocation(location);
		if(newLocation != null)
		{
			f.clearLocation(location);
			f.place(this, newLocation);
			location = newLocation;
		}
	}
}
