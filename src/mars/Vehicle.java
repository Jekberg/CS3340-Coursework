package mars;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

class Vehicle extends Entity{
	public boolean carryingSample;
	
	public Vehicle(Location l){
		super(l);	
		this.carryingSample = false;
	}

	public void act(Field f, Mothership m, ArrayList<Rock> rocksCollected)
	{
		actAdvanced(f, m, rocksCollected);
		//actCollaborative(f, m, rocksCollected);
		//actSimple(f, m, rocksCollected);
	}
	
	public void actAdvanced(Field f, Mothership m, ArrayList<Rock> rocksCollected){	
		rule1DropSample(f);
		
		rule5DropTrail(f);
		
		rule3PickupSample(f, rocksCollected);
		
		rule7SearchForTrail(f);
		
		rule8FollowTrail(f);
		
		// Default action
		rule4MoveRandomly(f);
	}
	
	public void actCollaborative(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		rule1DropSample(f);
		
		rule5DropTrail(f);
		
		rule3PickupSample(f, rocksCollected);
		
		rule6FollowTrail(f);
		
		// Default action
		rule4MoveRandomly(f);
	}

	public void actSimple(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		
		rule1DropSample(f);
		
		rule2ReturnToShip(f);
		
		rule3PickupSample(f, rocksCollected);
		
		// Default action
		rule4MoveRandomly(f);
	}
	
	private void rule1DropSample(Field f) {
		if(carryingSample && f.isNeighbourTo(location, Mothership.class))
			carryingSample = false;
	}
	
	private void rule2ReturnToShip(Field f) {
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
	
	private void rule3PickupSample(Field f, ArrayList<Rock> rocksCollected) {
		if (f.isNeighbourTo(location, Rock.class))
		{
			var rocLoc = f.getNeighbour(location, Rock.class);
			rocksCollected.add((Rock) f.getObjectAt(rocLoc));
			f.clearLocation(rocLoc);
			carryingSample = true;
		}
	}
	
	private void rule4MoveRandomly(Field f) {
		
		var newLocation = f.freeAdjacentLocation(location);
		if(newLocation != null)
		{
			f.clearLocation(location);
			f.place(this, newLocation);
			location = newLocation;
		}
	}
	
	private void rule5DropTrail(Field f) {
		if(carryingSample && !f.isNeighbourTo(location, Mothership.class))
		{
			f.dropCrumbs(location, 2);
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
	
	private void rule6FollowTrail(Field f) {
		if(f.getCrumbQuantityAt(location) != 0)
		{
			f.pickUpACrumb(location);
			var currentSignal = f.getSignalStrength(location);			
			var newLoc = f.getAllfreeAdjacentLocations(location)
				.stream()
				.filter(loc -> currentSignal >= f.getSignalStrength(loc))
				.filter(loc -> f.getCrumbQuantityAt(loc) != 0)
				.min((l, r) -> f.getSignalStrength(l) * f.getSignalStrength(r))
				.orElse(location);
			
			if(newLoc == location)
				newLoc = f.getAllfreeAdjacentLocations(location)
				.stream()
				.filter(loc -> currentSignal >= f.getSignalStrength(loc))
				.min((l, r) -> f.getSignalStrength(l) * f.getSignalStrength(r))
				.orElse(location);
			
			f.clearLocation(location);
			f.place(this, newLoc);
			location = newLoc;
		}
	}
	
	public void rule7SearchForTrail(Field f) {
		if(!carryingSample && f.getCrumbQuantityAt(location) == 0)
		{
			var newLoc = f.getAllfreeAdjacentLocations(location)
				.stream()
				.filter(loc -> f.getCrumbQuantityAt(loc) != 0)
				.findAny();
			
			if(newLoc.isPresent())
			{
				f.clearLocation(location);
				f.place(this, newLoc.get());
				location = newLoc.get();
			}
		}
	}
	
	private void rule8FollowTrail(Field f) {
		if(!carryingSample && f.getCrumbQuantityAt(location) != 0)
		{
			f.pickUpACrumb(location);
			var currentSignal = f.getSignalStrength(location);			
			var newLoc = f.getAllfreeAdjacentLocations(location)
				.stream()
				.filter(loc -> currentSignal >= f.getSignalStrength(loc))
				.filter(loc -> f.getCrumbQuantityAt(loc) != 0)
				.min((l, r) -> f.getSignalStrength(l) * f.getSignalStrength(r))
				.orElse(location);
			
			if(newLoc == location)
				newLoc = f.getAllfreeAdjacentLocations(location)
				.stream()
				.filter(loc -> currentSignal >= f.getSignalStrength(loc))
				.min((l, r) -> f.getSignalStrength(l) * f.getSignalStrength(r))
				.orElse(location);
			
			f.clearLocation(location);
			f.place(this, newLoc);
			location = newLoc;
		}
	}
}
