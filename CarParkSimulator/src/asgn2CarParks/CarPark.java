/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2CarParks 
 * 21/04/2014
 * 
 */
package asgn2CarParks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * The CarPark class provides a range of facilities for working with a car park in support 
 * of the simulator. In particular, it maintains a collection of currently parked vehicles, 
 * a queue of vehicles wishing to enter the car park, and an historical list of vehicles which 
 * have left or were never able to gain entry. 
 * 
 * The class maintains a wide variety of constraints on small cars, normal cars and motorcycles 
 * and their access to the car park. See the method javadoc for details. 
 * 
 * The class relies heavily on the asgn2.Vehicle hierarchy, and provides a series of reports 
 * used by the logger. 
 * 
 * @author hogan
 *
 */
public class CarPark {
	
	private Queue<Vehicle> queue;
	private ArrayList<Vehicle> spaces; //simulates the car park storage for all currently parked cars.
	private ArrayList<Vehicle> alternativeSpaces; //tracks the vehicles currently parking in an alternative park.
	private ArrayList<Vehicle> archive;
	
	private String vehicleType;
	private int parkingTime;
	private int intendedDuration;
	
	private int numCars = 0;
	private int numSmallCars = 0;
	private int numBikes = 0;
	
	private int numDissatisfied = 0;
	private int count = 0; //total number of vehicles. incremented when a vehicle is created.
	private int alternativeCount = 0; //total number of alternatively parked vehicles. Incremented and decremented when parked.
	
	private String status = "";
	
	private int minStay = Constants.MINIMUM_STAY;
	private int maxQueueTime = Constants.MAXIMUM_QUEUE_TIME;
	private int lastEntry = Constants.CLOSING_TIME-60;
	
	private int maxCarSpaces = Constants.DEFAULT_MAX_CAR_SPACES; //100
	private int maxSmallCarSpaces = Constants.DEFAULT_MAX_SMALL_CAR_SPACES; // = 20;
	private int maxMotorCycleSpaces = Constants.DEFAULT_MAX_MOTORCYCLE_SPACES;// = 20;
	private int maxQueueSize = Constants.DEFAULT_MAX_QUEUE_SIZE; // = 10;
	private int closingTime = Constants.CLOSING_TIME; //18*60
	private int maxSpaces = maxCarSpaces + maxSmallCarSpaces + maxMotorCycleSpaces;
	
	private int availableCarSpaces = maxCarSpaces;
	private int availableSmallCarSpaces = maxSmallCarSpaces;
	private int availableBikesSpaces = maxMotorCycleSpaces;
	
	/**
	 * CarPark constructor sets the basic size parameters. 
	 * Uses default parameters
	 */
	public CarPark() {
		this(Constants.DEFAULT_MAX_CAR_SPACES,Constants.DEFAULT_MAX_SMALL_CAR_SPACES,
				Constants.DEFAULT_MAX_MOTORCYCLE_SPACES,Constants.DEFAULT_MAX_QUEUE_SIZE);
	}
	
	/**
	 * CarPark constructor sets the basic size parameters. 
	 * @param maxCarSpaces maximum number of spaces allocated to cars in the car park 
	 * @param maxSmallCarSpaces maximum number of spaces (a component of maxCarSpaces) 
	 * 						 restricted to small cars
	 * @param maxMotorCycleSpaces maximum number of spaces allocated to MotorCycles
	 * @param maxQueueSize maximum number of vehicles allowed to queue
	 */
	public CarPark(int maxCarSpaces,int maxSmallCarSpaces, int maxMotorCycleSpaces, int maxQueueSize) {
		this.maxCarSpaces = maxCarSpaces;
		this.maxSmallCarSpaces = maxSmallCarSpaces;
		this.maxMotorCycleSpaces = maxMotorCycleSpaces;
		this.maxSpaces = maxCarSpaces + maxSmallCarSpaces + maxMotorCycleSpaces;
		spaces = new ArrayList<Vehicle>();
		queue = new LinkedList<Vehicle>();
		archive = new ArrayList<Vehicle>();
	}

	/**
	 * Archives vehicles exiting the car park after a successful stay. Includes transition via 
	 * Vehicle.exitParkedState(). 
	 * @param time int holding time at which vehicle leaves
	 * @param force boolean forcing departure to clear car park 
	 * @throws VehicleException if vehicle to be archived is not in the correct state 
	 * @throws SimulationException if one or more departing vehicles are not in the car park when operation applied
	 */
	public void archiveDepartingVehicles(int time,boolean force) throws VehicleException, SimulationException {
		//copy of ArrayList spaces to prevent conflicts when writing to that vehicle.
		ArrayList<Vehicle> copyOfSpaces = new ArrayList<Vehicle>(spaces);
		int currentTime = time;
		boolean forceDepart = force;
		
		for(Vehicle departingVehicle : copyOfSpaces) { //each item in spaces
			
			exceptionIfNotParked(departingVehicle); //if departing vehicle is not parked.
			exceptionIfNotInCarPark(departingVehicle); //if not in car park.
			
			if (currentTime >= departingVehicle.getDepartureTime() || forceDepart) {
				unparkVehicle(departingVehicle, time); //change state, remove from queue, add to archive.
			}
		}
	}
	
	/**
	 * Method to archive new vehicles that don't get parked or queued and are turned 
	 * away
	 * @param v Vehicle to be archived
	 * @throws SimulationException if vehicle is currently queued or parked
	 */
	public void archiveNewVehicle(Vehicle v) throws SimulationException {
		Vehicle newVehicle = v;
		exceptionIfNotNew(newVehicle);
		archive.add(newVehicle);
		this.numDissatisfied++;
	}
	
	
	/**
	 * Archive vehicles which have stayed in the queue too long 
	 * @param time int holding current simulation time 
	 * @throws VehicleException if one or more vehicles not in the correct state or if timing constraints are violated
	 */
	public void archiveQueueFailures(int time) throws SimulationException, VehicleException {
		ArrayList<Vehicle> copyOfQueue = new ArrayList<Vehicle>(queue);
		
		for(Vehicle newVehicle : copyOfQueue) {
			exceptionIfNotQueued(newVehicle);
			//exceptionTimeConstraints(maxQueueTime +1, time);

			int timeSpentInQueue = time - newVehicle.getArrivalTime();
			
			if(timeSpentInQueue >= Constants.MAXIMUM_QUEUE_TIME) {				
				exitQueue(newVehicle, time);
				archive.add(newVehicle);
				this.numDissatisfied++;
			}
		}
	}
	
	/**
	 * Simple status showing whether carPark is empty
	 * @return true if car park empty, false otherwise
	 */
	public boolean carParkEmpty() {
		return spaces.isEmpty();
	}
	
	/**
	 * Simple status showing whether carPark is full
	 * @return true if car park full, false otherwise
	 */
	public boolean carParkFull() {
		return (this.spaces.size() == this.maxSpaces);
	}
	
	/**
	 * State dump intended for use in logging the final state of the carpark
	 * All spaces and queue positions should be empty and so we dump the archive
	 * @return String containing dump of final carpark state 
	 */
	public String finalState() {
		String str = "Vehicles Processed: count:" + 
				this.count + ", logged: " + this.archive.size() 
				+ "\nVehicle Record: \n";
		for (Vehicle v : this.archive) {
			str += v.toString() + "\n\n";
		}
		return str + "\n";
	}
	
	/**
	 * Simple getter for number of cars in the car park 
	 * @return number of cars in car park, including small cars
	 */
	public int getNumCars() {
		return numCars;
	}
	
	/**
	 * Simple getter for number of motorcycles in the car park 
	 * @return number of MotorCycles in car park, including those occupying 
	 * 			a small car space
	 */
	public int getNumMotorCycles() {
		return numBikes;
	}
	
	/**
	 * Simple getter for number of small cars in the car park 
	 * @return number of small cars in car park, including those 
	 * 		   not occupying a small car space. 
	 */
	public int getNumSmallCars() {
		return numSmallCars;
	}
	
	/**
	 * Method used to provide the current status of the car park. 
	 * Uses private status String set whenever a transition occurs. 
	 * Example follows (using high probability for car creation). At time 262, 
	 * we have 276 vehicles existing, 91 in car park (P), 84 cars in car park (C), 
	 * of which 14 are small (S), 7 MotorCycles in car park (M), 48 dissatisfied (D),
	 * 176 archived (A), queue of size 9 (CCCCCCCCC), and on this iteration we have 
	 * seen: car C go from Parked (P) to Archived (A), C go from queued (Q) to Parked (P),
	 * and small car S arrive (new N) and go straight into the car park<br>
	 * 262::276::P:91::C:84::S:14::M:7::D:48::A:176::Q:9CCCCCCCCC|C:P>A||C:Q>P||S:N>P|
	 * @return String containing current state 
	 */
	public String getStatus(int time) {
		String str = time +"::"
		+ this.count + "::" 
		+ "P:" + this.spaces.size() + "::"
		+ "C:" + this.numCars + "::S:" + this.numSmallCars 
		+ "::M:" + this.numBikes 
		+ "::D:" + this.numDissatisfied 
		+ "::A:" + this.archive.size()  
		+ "::Q:" + this.queue.size(); 
		for (Vehicle v : this.queue) {
			if (v instanceof Car) {
				if (((Car)v).isSmall()) {
					str += "S";
				} else {
					str += "C";
				}
			} else {
				str += "M";
			}
		}
		str += this.status;
		this.status="";
		return str+"\n";
	}
	
	/**
	 * State dump intended for use in logging the initial state of the carpark.
	 * Mainly concerned with parameters. 
	 * @return String containing dump of initial carpark state 
	 */
	public String initialState() {
		return "CarPark [maxCarSpaces: " + this.maxSpaces
				+ " maxSmallCarSpaces: " + this.maxSmallCarSpaces 
				+ " maxMotorCycleSpaces: " + this.maxMotorCycleSpaces 
				+ " maxQueueSize: " + this.maxQueueSize + "]";
	}
	
	/**
	 * Helper to set vehicle message for transitions 
	 * @param v Vehicle making a transition (uses S,C,M)
	 * @param source String holding starting state of vehicle (N,Q,P) 
     * @param target String holding finishing state of vehicle (Q,P,A) 
     * @return String containing transition in the form: |(S|C|M):(N|Q|P)>(Q|P|A)|
	 */
	private String setVehicleMsg(Vehicle v,String source, String target) {
		String str="";
		if (v instanceof Car) {
			if (((Car)v).isSmall()) {
				str+="S";
			} else {
				str+="C";
			}
		} else {
			str += "M";
		}
		return "|"+str+":"+source+">"+target+"|";
	}
	
	/**
	 * Method to add vehicle successfully to the car park store. 
	 * Precondition is a test that spaces are available. 
	 * Includes transition via Vehicle.enterParkedState.
	 * @param v Vehicle to be added 
	 * @param time int holding current simulation time
	 * @param intendedDuration int holding intended duration of stay 
	 * @throws SimulationException if no suitable spaces are available for parking 
	 * @throws VehicleException if vehicle not in the correct state or timing constraints are violated
	 * @author Steven
	 */
	public void parkVehicle(Vehicle v, int time, int intendedDuration) throws SimulationException, VehicleException {
		vehicleType = getVehicleType(v); //extract vehicle type from vehicle id
		Vehicle parkingVehicle = v;
		this.parkingTime = time;
		this.intendedDuration = intendedDuration;
		
		exceptionIfNoSpaces(parkingVehicle); //exception if there is no where to park
											 //test combinations of park types
		exceptionTimeConstraints(lastEntry, time); //exception if parking after last entry
		exceptionIfParked(parkingVehicle);
		exceptionTimeConstraints(closingTime, time);
			
		parkingVehicle.enterParkedState(this.parkingTime, this.intendedDuration); //vehicle enters a parked state
		
		switch (vehicleType) { //switch on vehicle type
            case "C": //normal car
            	parkCar(parkingVehicle);
            	numCars++;
            	count++;
            	break;
            case "S": //small car
            	parkSmallCar(parkingVehicle);
            	numSmallCars++; 
            	numCars++;
            	count++;
                break;
            case "M": //motorbike
            	parkMotorCycle(parkingVehicle);
            	numBikes++;
            	count++;
                break;
		} //end switch
	} 

	/**
	 * Method determines, given a vehicle of a particular type, whether there are spaces available for that 
	 * type in the car park under the parking policy in the class header.  
	 * @param v Vehicle to be stored. 
	 * @return true if space available for v, false otherwise 
	 */
	public boolean spacesAvailable(Vehicle v) {
		Boolean spacesAvailable = false;
		if (v != null) {
			vehicleType = getVehicleType(v); //extract vehicle type from vehicle id
			switch (vehicleType) { //switch on vehicle type
	            case "C": //normal car
	            	spacesAvailable = !carsFull(); //if car spaces are not full, there is room for more!
	            	break;
	            case "S": //small car
	            	spacesAvailable = !smallCarsFull(); //if small car spaces are not full, there is room for more!
	            	break;
	            case "M": //motorbike
	            	spacesAvailable = !bikesFull(); //if motor bike spaces are not full, there is room for more!
	            	break;
	        } //end switch
		}//end if
		return spacesAvailable;
	}
	
	/**
	 * Method to remove vehicle from the carpark. 
	 * For symmetry with parkVehicle, include transition via Vehicle.exitParkedState.  
	 * So vehicle should be in parked state prior to entry to this method. 
	 * @param v Vehicle to be removed from the car park 
	 * @throws VehicleException if Vehicle is not parked, is in a queue, or violates timing constraints 
	 * @throws SimulationException if vehicle is not in car park
	 */
	public void unparkVehicle(Vehicle v,int departureTime) throws VehicleException, SimulationException {
		Vehicle departingVehicle = v;
		Iterator<Vehicle> spacesIterator = spaces.iterator();

		exceptionIfNotInCarPark(departingVehicle); //tested
		//exceptionTimeConstraints(departureTime, minStay); //tested
		exceptionIfQueued(departingVehicle); //tested
		exceptionIfNotParked(departingVehicle); //tested
		
		while(spacesIterator.hasNext()) {
		
			if(spacesIterator.next().equals(departingVehicle)) { //find the vehicle in the car park
				
				spacesIterator.remove(); //remove the vehicle from the car park
				break;
			}
		}	
		departingVehicle.exitParkedState(departureTime);
		this.archive.add(departingVehicle);
		decrementCounters(departingVehicle);
	}
	
	////////////////////////////////////////////
	// HELPER METHODS FOR PARKING
	/////////////////////////////////////////////
	
	/***************************************
	 * decrements counters depening on the vehicle type.
	 * is called by unparkVehicle.
	 * @param departingVehicle
	 * @author Steven
	 ***********************************/
	private void decrementCounters(Vehicle departingVehicle) {
		vehicleType = getVehicleType(departingVehicle);

		switch (vehicleType) {
			case "C": 
				numCars--;
				break;
			case "S":
				numCars--;
				numSmallCars--;
				break;
			case "M":
				numBikes--;
				break;
		}
	}
	
	private void parkCar(Vehicle parkingVehicle) {
		availableCarSpaces--;
		spaces.add(parkingVehicle);
		//System.out.println(spaces.get(0).getVehID());
	}
	
	private void parkSmallCar(Vehicle parkingVehicle) {
    	
		if (!smallCarsFull()) { //fill the small spaces first.
    		availableSmallCarSpaces--;
    		spaces.add(parkingVehicle);
    		
    	} else { //before moving into the general car spaces.
    		availableCarSpaces--;
    		alternativeParking(parkingVehicle);
    	}
	}
	
	private void parkMotorCycle(Vehicle parkingVehicle) {

    	if (!bikesFull()) { //fill the bike spaces first.
    		availableBikesSpaces--;
    		spaces.add(parkingVehicle);
    		
    	} else { //before moving into the small vehicle spaces.
    		availableSmallCarSpaces--;
    		alternativeParking(parkingVehicle);
    	}
	}
	
	/**
	 * Helper method to increment global space count, alternative space count, and store the vehicle
	 * in the alternative spaces list.
	 * @param parkingVehicle
	 */
	private void alternativeParking(Vehicle parkingVehicle) {
		spaces.add(parkingVehicle);
		alternativeSpaces.add(parkingVehicle);
		alternativeCount++;
	}

	/******************************************
	 * returns true if all normal car spaces have been occupied.
	 * @return
	 *******************************************/
	private boolean carsFull() {
		if (availableCarSpaces == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/********************************************
	 * true if small car spaces and car spaces are both full.
	 * @return
	 **************************************/
	private boolean smallCarsFull() {
		if ((availableSmallCarSpaces == 0) && (availableCarSpaces == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	/******************************************
	 * true if both bike spaces and small car spaces are all full.
	 *******************************************/
	private boolean bikesFull() {
		if ((availableBikesSpaces == 0) && (availableSmallCarSpaces == 0) ) {
			return true;
		} else {
			return false;
		}
	}
	
	////////////////////////////////////
	// QUEUE METHODS
	///////////////////////////////
	
	/**
	 * Silently process elements in the queue, whether empty or not. If possible, add them to the car park. 
	 * Includes transition via exitQueuedState where appropriate
	 * Block when we reach the first element that can't be parked. 
	 * @param time int holding current simulation time 
	 * @throws SimulationException if no suitable spaces available when parking attempted
	 * @throws VehicleException if state is incorrect, or timing constraints are violated
	 * @author Steven
	 */
	public void processQueue(int time, Simulator sim) throws VehicleException, SimulationException {
		int duration;
		Vehicle queuedVehicle;
		
		while (spacesAvailable(queue.peek())) { //when a position becomes available park the vehicle.
			queuedVehicle = queue.element(); //retrieve the first vehicle in the que, but not remove it.
			
			exceptionIfNotQueued(queuedVehicle); //make sure the vehicle is in a Queued State.
			exceptionTimeConstraints(closingTime, time); //exception if after close.
			
			exitQueue(queuedVehicle, time); //remove first vehicle from the que, and change it's state.
			duration = sim.setDuration(); 
			parkVehicle(queuedVehicle, time, duration);	//park the now de-queued vehicle.
			//status += setVehicleMsg(parkingVehicle, queue, park);
		}	
	}
	
	/**
	 * Method to add vehicle successfully to the queue
	 * Precondition is a test that spaces are available
	 * Includes transition through Vehicle.enterQueuedState 
	 * @param v Vehicle to be added 
	 * @throws SimulationException if queue is full  
	 * @throws VehicleException if vehicle not in the correct state 
	 * @author Steven
	 */
	public void enterQueue(Vehicle v) throws SimulationException, VehicleException {
		Vehicle queueingVehicle = v;
		
		exceptionIfQueueFull(queueingVehicle); //exception if queue is full
		exceptionIfParked(queueingVehicle); //exception if parked

		queue.add(queueingVehicle); //add vehicle to queue.
		queueingVehicle.enterQueuedState(); //change vehicle state to queued.
		count++;
	}
	
	/**
	 * Method to remove vehicle from the queue after which it will be parked or 
	 * archived. Includes transition through Vehicle.exitQueuedState.  
	 * @param v Vehicle to be removed from the queue 
	 * @param exitTime int time at which vehicle exits queue
	 * @throws SimulationException if vehicle is not in queue 
	 * @throws VehicleException if the vehicle is in an incorrect state or timing 
	 * constraints are violated
	 * @author Steven
	 */
	public void exitQueue(Vehicle v,int exitTime) throws SimulationException, VehicleException {
		Vehicle exitingVehicle = v;

		exceptionTimeConstraints(closingTime, exitTime); //exception if exit after close
		exceptionIfNotQueued(exitingVehicle); //exception is not already in queued state
		exceptionIfParked(exitingVehicle); //exception if in parked state
		exceptionIfNotInQueue(exitingVehicle); //exception if not in queue
		
		exitingVehicle.exitQueuedState(exitTime); //removing queued state;
		queue.remove(exitingVehicle); //remove from queue;
		
	}
	
	/**
	 * Simple status showing number of vehicles in the queue 
	 * @return number of vehicles in the queue
	 */
	public int numVehiclesInQueue() {
		int numVehiclesInQueue = queue.size();
		return numVehiclesInQueue;
	}
	
	/**
	 * Simple status showing whether queue is empty
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
		if (queue.peek() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Simple status showing whether queue is full
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
		if (numVehiclesInQueue() == maxQueueSize) {
			return true;
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
	    String NEW_LINE = System.getProperty("line.separator");
	
	    result.append("This CarPark: "+NEW_LINE);
	    result.append("Number of large cars: "+ (this.numCars-this.numSmallCars) +NEW_LINE);
	    result.append("Number of small cars: "+ this.numSmallCars +NEW_LINE);
	    result.append("Total number of cars: "+ this.numCars +NEW_LINE);
	    result.append("Number of bikes: "+ this.numBikes +NEW_LINE);
	    result.append("Total vehicles: "+ this.count +NEW_LINE);
	    result.append("Size of queue: "+ this.queue.size() +NEW_LINE);
	    result.append("Number of dissatisfied customers: "+this.numDissatisfied+NEW_LINE);
	    result.append("Archive: "+this.archive.size()+NEW_LINE);
	    result.append("");
	    return result.toString();
	}
	
	//////////////////////////////////
	// CREATING & PROCESSING VEHICLES
	//////////////////////////////////
	
	/**
	 * Method to try to create new vehicles (one trial per vehicle type per time point) 
	 * and to then try to park or queue (or archive) any vehicles that are created 
	 * @param sim Simulation object controlling vehicle creation 
	 * @throws SimulationException if no suitable spaces available when operation attempted 
	 * @throws VehicleException if vehicle creation violates constraints 
	 */
	public void tryProcessNewVehicles(int time,Simulator sim) throws VehicleException, SimulationException {
		//can not run after closing time.
		exceptionTimeConstraints(closingTime, time);

		Boolean createNewCar = false;
		Boolean createNewSmallCar = false;
		Boolean createNewBike = false;
		int duration;
		
		//RNG vehicle tests
		createNewCar = sim.newCarTrial();
		createNewSmallCar = createNewCar && sim.smallCarTrial();
		createNewBike = sim.motorCycleTrial();
		duration = sim.setDuration();
		
		//check to see if a car has arrived.
		if (createNewSmallCar) {
			processNewVehicle("S", time, duration);
			
		} else if (createNewCar) {
			processNewVehicle("C", time, duration);
		} 
		
		//check if a bike has arrived
		if (createNewBike) { 
			processNewVehicle("M", time, duration);	
		} 
	}
	
	/******************************
	 * Helper method for tryProcessVehicle. Performs switch based on vehicleType and issues
	 * instructinos to create and attempt to park / queue / archive the vehicle appropriately.
	 * @param vehicleType
	 * @param time
	 * @param duration
	 * @throws VehicleException
	 * @throws SimulationException
	 *********************************/
	private void processNewVehicle(String vehicleType, int time, int duration) throws VehicleException, SimulationException {
		Vehicle newVehicle;
		Boolean isSmall = true;
		
		switch (vehicleType) {
			case "C": 
				newVehicle = createCar(!isSmall, vehicleType, time);
				processCar(newVehicle, time, duration);
				break;
			case "S":
				newVehicle = createCar(isSmall, vehicleType, time);
				processSmallCar(newVehicle, time, duration);
				break;
			case "M":
				newVehicle = createBike(vehicleType, time);
				processBike(newVehicle, time, duration);
				break;
		}
	}
	
	/**********************************
	 * returns a small or normal car
	 * @param isSmall
	 * @param vehicleType
	 * @return Vehicle newCar
	 * @throws VehicleException
	 **********************************/
	private Vehicle createCar(Boolean isSmall, String vehicleType, int time) throws VehicleException {
		String newVehId = vehicleType + count;
		Vehicle newCar = new Car(newVehId, time, isSmall);
		return newCar;
	}
	
	/**********************************
	 * returns a new bike
	 * @param isSmall
	 * @param vehicleType
	 * @return Vehicle newCar
	 * @throws VehicleException
	 **********************************/
	private Vehicle createBike(String vehicleType, int time) throws VehicleException {
		String newVehId = vehicleType + count;
		Vehicle newBike = new MotorCycle(newVehId, time);
		return newBike;
	}
	
	/**
	 * send the car where it belongs
	 * @param newCar
	 * @param time
	 * @param duration
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	private void processCar(Vehicle newCar, int time, int duration) throws VehicleException, SimulationException {
		Boolean queueEmpty = this.queueEmpty();

		if (spacesAvailable(newCar)) { 
			this.parkVehicle(newCar, time, duration);
		} else if (queueEmpty) { 
			this.enterQueue(newCar);
		} else {
			this.archiveNewVehicle(newCar);
		}
	}
	
	/**
	 * send the small car to where it belongs
	 * @param newCar
	 * @param time
	 * @param duration
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	private void processSmallCar(Vehicle newCar, int time, int duration) throws VehicleException, SimulationException {
		Boolean queueEmpty = this.queueEmpty();

		if (spacesAvailable(newCar)) { 
			this.parkVehicle(newCar, time, duration);
		} else if (queueEmpty) { 
			this.enterQueue(newCar);
		} else {
			this.archiveNewVehicle(newCar);
		}
	}
	
	/**
	 * send the bike to where it belongs
	 * @param newBike
	 * @param time
	 * @param duration
	 * @throws VehicleException
	 * @throws SimulationException
	 */
	private void processBike(Vehicle newBike, int time, int duration) throws VehicleException, SimulationException {
		Boolean queueEmpty = this.queueEmpty();

		if (spacesAvailable(newBike)) { 
			this.parkVehicle(newBike, time, duration);
		} else if (queueEmpty) { 
			this.enterQueue(newBike);
		} else {
			this.archiveNewVehicle(newBike);
		}
	}
	
	
	/**
	 * Helper method to extract the vehicle type from a vehicle object. Type is set in the vehicle id when the vehicle is
	 * created.
	 * @param v vehicle to get type of
	 * @return vehicle type as string
	 * @author Steven
	 */
	private String getVehicleType(Vehicle v) {
		String vehicleType = v.getVehID().substring(0, 1);
		return vehicleType;
	}
	
	//////////////////////////////
	// EXCEPTION HANDLERS
	//////////////////////////////
	
	/***************************
	 * Exception when joining a carpark with no spaces.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfNoSpaces(Vehicle v) throws SimulationException {
		if (!spacesAvailable(v)) { //if no spaces are available to park this vehicle
			throw new SimulationException("Woah!! No spaces bro!");
		}
	}
	
	/***************************
	 * Exception when Vehicle is not in car park.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfNotInCarPark(Vehicle v) throws SimulationException {
		Boolean inCarPark = spaces.contains(v);
		if (!inCarPark) { 
			throw new SimulationException("Vehicle is not in car park.");
		}
	}
	
	/***************************
	 * Exception when Vehicle is not in Queue.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfNotInQueue(Vehicle v) throws SimulationException {
		Boolean inQueue = queue.contains(v);
		if (!inQueue) { 
			throw new SimulationException("Vehicle is not in the queue.");
		}
	}
	
	/***************************
	 * Exception when joining a queue that is already full.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfQueueFull(Vehicle v) throws SimulationException {
		if (queueFull()) { //if the queue is full.
			throw new SimulationException("Attempting to join a full queue.");
		}
	}

	/***************************
	 * Handle violations of timing constraints. 
	 * We assume that no vehicle enters the car park in the final hour, and that all remaining 
	 * vehicles leave the car park at 12 midnight. Cannot leave a carpark (or queue?) before 
	 * minimumStay. 
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionTimeConstraints(int leftTime, int rightTime) throws VehicleException {
		if (leftTime <= rightTime) {
			throw new VehicleException("Wrong place at the wrong time!!");
		}
	}
	
	/***************************
	 * Exception handler for when the vehicle should be queued but is not.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfNotQueued(Vehicle v) throws VehicleException {
		if (!v.isQueued()) {
			throw new VehicleException("The Queue process does not apply to non-qued vehicles");
		}
	}
	
	/***************************
	 * Exception handler for when the vehicle should be not queued.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfQueued(Vehicle v) throws VehicleException {
		if (v.isQueued()) {
			throw new VehicleException("Vehicle should not be queued.");
		}
	}
	
	/***************************
	 * Exception handler for when the vehicle should be parked but is not.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfNotParked(Vehicle v) throws VehicleException {
		if (!v.isParked()) {
			throw new VehicleException("The vehicle was expected to be in a Parked state.");
		}
	}
	
	/***************************
	 * Exception handler for when the vehicle should not be parked.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfParked(Vehicle v) throws VehicleException {
		if (v.isParked()) {
			throw new VehicleException("The vehicle was not expected to be in a Parked state.");
		}
	}

	/***************************
	 * Exception if vehicle is not new.
	 * @param v
	 * @throws VehicleException
	 * @author Steven
	 ***********************************/
	private void exceptionIfNotNew(Vehicle v) throws SimulationException {
		if (v.isParked() || v.isQueued()) {
			throw new SimulationException("The vehicle was either parked or queued, so it is not new.");
		}
	}

}
