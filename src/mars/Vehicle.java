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
		actCollaborative(f, m, rocksCollected);
		//actSimple(f, m, rocksCollected);
	}
	
	
	public void actCollaborative(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		// Rule 1
		DropSample(f);
		
		// Rule 5
		DropTrail(f);
		
		// Rule 2
		TravelUpGradient(f);
		
		// Rule 6
		FollowTrail(f);
		
		// Rule 3
		PickupSample(f, rocksCollected);
		
		// Rule 4
		// Default action
		moveRandomly(f);
	}

	public void actSimple(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		
		// Rule 1
		DropSample(f);
		
		// Rule 2
		TravelUpGradient(f);
		
		// Rule 3
		PickupSample(f, rocksCollected);
		
		// Rule 4
		// Default action
		moveRandomly(f);
	}
	
	private void DropSample(Field f) {
		if(carryingSample && f.isNeighbourTo(location, Mothership.class))
			carryingSample = false;
	}
	
	private void DropTrail(Field f) {
		if(carryingSample && !f.isNeighbourTo(location, Mothership.class))
			f.dropCrumbs(location, 1);
	}
	
	private void TravelUpGradient(Field f) {
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
	}
	
	private void FollowTrail(Field f) {
		if(f.getCrumbQuantityAt(location) != 0)
		{
			f.pickUpACrumb(location);
			var currentSignal = f.getSignalStrength(location);
			var newLoc = f.getAllfreeAdjacentLocations(location)
				.stream()
				.filter(loc -> currentSignal >= f.getSignalStrength(loc))
				.min((l, r) -> f.getSignalStrength(l) - f.getSignalStrength(r))
				.orElse(location);
			f.clearLocation(location);
			f.place(this, newLoc);
			location = newLoc;
		}
	}
	
	private void PickupSample(Field f, ArrayList<Rock> rocksCollected) {
		if (f.isNeighbourTo(location, Rock.class))
		{
			var rocLoc = f.getNeighbour(location, Rock.class);
			rocksCollected.add((Rock) f.getObjectAt(rocLoc));
			f.clearLocation(rocLoc);
			carryingSample = true;
		}
	}
	
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
