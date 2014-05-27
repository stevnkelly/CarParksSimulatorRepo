/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 29/04/2014
 * 
 */
package asgn2Tests;


import static org.junit.Assert.*;

import asgn2CarParks.CarPark;
import asgn2Vehicles.MotorCycle;
import asgn2Exceptions.SimulationException;
import asgn2Vehicles.Vehicle;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Simulators.Constants;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hoga
 *
 */
public class CarParkTests {
	
	private CarPark testCarPark;
	private Car testCar;
	private Car testSmallCar;
	private MotorCycle testBike;
	private Simulator testSimulator;
	
	private int arrivalTime = 1;
	private int exitTime = 300;
	private String carVehID = "C0";
	private String smallCarVehID = "S0";
	private String bikeVehID = "M0";
	private Boolean isSmall = true;
	private Boolean isLarge = false;
	private int time = 150;
	private int intendedDuration = Constants.MINIMUM_STAY + 1;
	
	private int minStay = Constants.MINIMUM_STAY;
	private int maxSpaces = Constants.DEFAULT_MAX_CAR_SPACES; //100
	private int maxSmallCarSpaces = Constants.DEFAULT_MAX_SMALL_CAR_SPACES; // = 20;
	private int maxMotorCycleSpaces = Constants.DEFAULT_MAX_MOTORCYCLE_SPACES;// = 20;
	private int maxQueueSize = Constants.DEFAULT_MAX_QUEUE_SIZE; // = 10;
	private int lastEntry = Constants.CLOSING_TIME-60;
	private int closingTime = Constants.CLOSING_TIME;
	
	
	@Before
	public void setUp() throws Exception {
		testCarPark = new CarPark();
		testSimulator = new Simulator();
		testCar = new Car(carVehID, arrivalTime, isLarge);
		testSmallCar = new Car(smallCarVehID, arrivalTime, isSmall);
		testBike = new MotorCycle(bikeVehID, arrivalTime);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	
	///////////////////////////////////////
	// UNPARK VEHICLES - EXCEPTIONS
	//////////////////////////////////////
	
	/*********************************************
	 * Throw an exception when attempting to unpark a car that is unparked.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 ********************************************/
	@Test(expected = SimulationException.class)
	public void unParkVehiclesExceptionNotParked() throws SimulationException, VehicleException {
		testCarPark.unparkVehicle(testCar, time);
	}
	
	/*********************************************
	 * Throw an exception when attempting to unpark a car that is queued.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 ********************************************/
	@Test(expected = SimulationException.class)
	public void unParkVehiclesExceptionAlreadyQueued() throws SimulationException, VehicleException {
		testCar.enterQueuedState();
		testCarPark.unparkVehicle(testCar, time);
	}
	
	/*********************************************
	 * Throw an exception if unparking vehicle does not exist in the park.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 ********************************************/
	@Test(expected = SimulationException.class)
	public void unparkVehiclesExceptionNotInPark() throws SimulationException, VehicleException {
		testCarPark.unparkVehicle(testCar, time);
	}
	
	/*********************************************
	 * Throw an exception if unparking before min stay.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 ********************************************/
	@Test(expected = VehicleException.class)
	public void unparkVehicleException() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, 2, intendedDuration);
		testCarPark.unparkVehicle(testCar, 1);
	}
	
	/////////////////////////////////////////
	// PARK VEHICLES - INCREMENT COUNTERS
	/////////////////////////////////////////
	
	
	/*********************************************
	 * Test that park vehicle increments the numCars vehicle counter
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehiclesIncrementNumCars() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, time, intendedDuration);
		int numCars = testCarPark.getNumCars();
		assertEquals(1, numCars);
	}
	
	/*********************************************
	 * Test that park vehicle increments the numSmallCars vehicle counter
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehiclesIncrementNumSmallCars() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testSmallCar, time, intendedDuration);
		int numSmallCars = testCarPark.getNumCars();
		assertEquals(1, numSmallCars);
	}
	
	/*********************************************
	 * Test that park vehicle increments the num motor bikes vehicle counter
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehiclesIncrementNumBikes() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testBike, time, intendedDuration);
		int numBikes = testCarPark.getNumMotorCycles();
		assertEquals(1, numBikes);
	}
	
	/////////////////////////////////////////
	// PARK VEHICLES - VEHICLE ADDED TO SPACES
	/////////////////////////////////////////
	
	/*********************************************
	 * Test that park vehicle adds a car to spaces
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehiclesCarSpaces() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, time, intendedDuration);
		Boolean isEmpty = testCarPark.carParkEmpty();
		assertTrue("IsEmpty should be false if a car was added", !isEmpty);
	}
	
	/*********************************************
	 * Test that park vehicle adds a small car to spaces
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehiclesSmallCarSpaces() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testSmallCar, time, intendedDuration);
		Boolean isEmpty = testCarPark.carParkEmpty();
		assertTrue("IsEmpty should be false if a car was added", !isEmpty);
	}
	
	/*********************************************
	 * Test that park vehicle adds a bike to spaces
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehicleBikeSpaces() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testBike, time, intendedDuration);
		Boolean isEmpty = testCarPark.carParkEmpty();
		assertTrue("Attempted to park a bike in empty car park, but test found that the"
				+ "car park was empty", !isEmpty);
	}
	
	/////////////////////////////////////////
	// PARK VEHICLES - CHANGE VEHICLE STATE
	/////////////////////////////////////////
	
	/*********************************************
	 * Test that park vehicle changes state of car
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehicleCarIsParked() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, time, intendedDuration);
		Boolean isParked = testCar.isParked();
		assertTrue("IsEmpty should be false if a car was added", isParked);
	}
	
	/*********************************************
	 * Test that park vehicle changes state of small car
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehicleSmallCarIsParked() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testSmallCar, time, intendedDuration);
		Boolean isParked = testSmallCar.isParked();
		assertTrue("IsEmpty should be false if a car was added", isParked);
	}
	
	/*********************************************
	 * Test that park vehicle changes state of bike
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test
	public void parkVehicleBikeIsParked() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testBike, time, intendedDuration);
		Boolean isParked = testBike.isParked();
		assertTrue("IsEmpty should be false if a car was added", isParked);
	}
	
	/////////////////////////////////////////
	// PARK VEHICLES - EXCEPTIONS
	/////////////////////////////////////////
	
	/*********************************************
	 * Test exception handling if no spaces are available.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 ********************************************/
	@Test(expected = SimulationException.class)
	public void parkVehiclesExceptionNoSpaces() throws SimulationException, VehicleException {
		fillCarPark();
		testCarPark.parkVehicle(testCar, time, intendedDuration);
	}
	
	/*********************************************
	 * Park vehicle exception handling if already parked.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 ********************************************/
	@Test(expected = VehicleException.class)
	public void parkVehiclesExceptionAlreadyParked() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, time, intendedDuration);
		testCarPark.parkVehicle(testCar, time, intendedDuration);
	}
	
	/*******************************************
	 * Park vehicle exception handling if time constraints are violated.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 *******************************************/
	@Test(expected = VehicleException.class)
	public void parkVehicleExceptionTimingConstraints() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, Constants.CLOSING_TIME +1, intendedDuration);
	}
	
	///////////////////////////////////
	// PROCESS QUEUE - EXCEPTIONS
	///////////////////////////////////	
	
	/**
	 * Silently process elements in the queue, whether empty or not. If possible, add them to the car park. 
	 * Includes transition via exitQueuedState where appropriate
	 * Block when we reach the first element that can't be parked. 
	 * @param time int holding current simulation time 
	 * @throws SimulationException if no suitable spaces available when parking attempted
	 * @throws VehicleException if state is incorrect, or timing constraints are violated
	 * @author Izaac
	 */
	
	/*******************************************
	 * Process Queue functions with an empty Queue.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 *******************************************/
	@Test
	public void processQueueEmpty() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, time, intendedDuration); //there is one car in the carpark
		testCarPark.processQueue(time, testSimulator); 
		int newNumCars;
		int originalNumCars = 1;
		Boolean numCarsUnchanged = true;
		Boolean queueIsEmpty = true;
		
		newNumCars = testCarPark.getNumCars();
		numCarsUnchanged = (originalNumCars == newNumCars);
		
		assertTrue("The Process Queue has an empty queue, it should not be parking cars.",
				(queueIsEmpty && numCarsUnchanged));
	} 
		
	/*******************************************
	 * Process Queue exception handling if time constraints are violated.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steve
	 *******************************************/
	@Test(expected = VehicleException.class)
	public void processQueueTimeConstraints() throws SimulationException, VehicleException {
		fillQueue();
		testCarPark.processQueue(Constants.CLOSING_TIME, testSimulator);
	}
	
	/*******************************************
	 * Process Queue exception handling if state is incorrect.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 *******************************************/
	@Test(expected = VehicleException.class)
	public void processQueueStateCheck() throws SimulationException, VehicleException {
		testCarPark.enterQueue(testCar);
		testCar.exitQueuedState(exitTime);
		testCarPark.processQueue(Constants.CLOSING_TIME, testSimulator);
	}
	
	///////////////////////////////////
	// TRY PROCESS VEHICLES NEW VEHICLES
	///////////////////////////////////
	
	/*******************************************
	 * 
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 *******************************************/
	/*@Test
	public void tryProcessNewVehicles() throws SimulationException, VehicleException {
		testCarPark.tryProcessNewVehicles(arrivalTime, sim);
		fail(); //i dont know if it is possible to test this class because of RNG.
				//could test that a vehicle is created.. but then i cant be sure..
	}*/
	
	/*******************************************
	 * Test fails if no suitable spaces available when operation commences. 
	 * @throws SimulationException
	 * @throws VehicleException
	 * 
	 * @author Steven
	 *******************************************/
	/*@Test(expected = SimulationException.class)
	public void tryProcessNewVehiclesNoSpace() throws SimulationException, VehicleException {
		fail(); //if the carpark is full people go to the queus, if the queues are full 
				//they are turned away.. what is this test?
	}*/
	
	/*******************************************
	 * Test fails when vehicle creation violates constraints
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 *******************************************/
	@Test(expected = VehicleException.class)
	public void tryProcessNewVehiclesViolatesConstraints() throws SimulationException, VehicleException {
		testCarPark.tryProcessNewVehicles(Constants.CLOSING_TIME +1, testSimulator);
	}
	
	////////////////////////////////////
	// EXIT QUEUE
	///////////////////////////////////
	
	/*************************************
	 * Testing that when a vehicle exits the que, the queue size is diminished by one.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 *************************************/
	@Test
	public void exitQueue() throws SimulationException, VehicleException {
		fillQueue();
		testCarPark.exitQueue(testCar, exitTime);
		assertEquals(maxQueueSize -1, testCarPark.numVehiclesInQueue());
	}
	
	/******************************************************
	 * throws a simulation exception when exiting a queue but not in the queue
	 * constraints are violated
	 * @author Steven
	 *************************************************/
	@Test(expected = VehicleException.class)
	public void exitQueueNotInQueue() throws SimulationException, VehicleException {
		fillQueue();
		testCar = new Car("C00", intendedDuration, isSmall);
		testCarPark.exitQueue(testCar, exitTime);
	}
	
	/*********************************************
	 * throws a simulation exception when exiting a que but already parked
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test(expected = VehicleException.class)
	public void exitQueueExceptionAlreadyParked() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, time, intendedDuration);
		testCarPark.parkVehicle(testCar, time, intendedDuration);
	}
	
	/*********************************************
	 * throws a simulation exception when time constraints are violated
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ********************************************/
	@Test(expected = VehicleException.class)
	public void exitQueueTimeConstraintsViolated() throws SimulationException, VehicleException {
		fillQueue();
		testCarPark.exitQueue(testCar, Constants.CLOSING_TIME +1); 
	}
	
	////////////////////////////////
	// ENTER QUEUE
	///////////////////////////////
	
	/*************************************
	 * Testing that when a vehicle exits the que, the queue size is diminished by one.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 *************************************/
	@Test
	public void enterQueueSetState() throws SimulationException, VehicleException {
		//correctly set to Qued state
		testCarPark.enterQueue(testCar);
		assertTrue("The vehicle should be set to Queued", testCar.isQueued());
	}
	
	/*************************************
	 * Testing that when a vehicle exits the que, the queue size is incremented by one.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 *************************************/
	@Test
	public void enterQueueIncrememntQueue() throws SimulationException, VehicleException {
		//correctly set to Qued state.
		testCarPark.enterQueue(testCar);
		assertEquals(1, testCarPark.numVehiclesInQueue());
	}
	
	/*************************************
	 * Testing that when a vehicle is added to the que, it can be correctly identified by vehID.
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 *************************************/
	@Test
	public void enterQueueCheckID() throws SimulationException, VehicleException {
		//correctly set to Qued state.
		testCar = new Car("C69", intendedDuration, isSmall);
		testCarPark.enterQueue(testCar);
		testCarPark.exitQueue(testCar, exitTime);
	}
	
	/******************************************************
	 * throws a simulation exception when queue is full
	 * constraints are violated
	 * @author Steven
	 *************************************************/
	@Test(expected = SimulationException.class)
	public void enterQueueFull() throws SimulationException, VehicleException {
		fillQueue();
		testCarPark.enterQueue(testCar);
	}
	
	/******************************************************
	 * throws a simulation exception when vehicle is not queued
	 * constraints are violated
	 * @author Steven
	 *************************************************/
	@Test(expected = VehicleException.class)
	public void enterQueueNotInQueue() throws SimulationException, VehicleException {
		testCar.enterQueuedState();
		testCar.enterQueuedState();
	}
	
	///////////////////////////////
	// ARCHIVE DEPARTING VEHICLE
	///////////////////////////////
	
	/**
	 * Expects an exception when archiving a vehicle in the incorrect state.
	 * @throws VehicleException
	 * @author Steven
	 */
	@Test(expected = VehicleException.class)
	public void archiveDepartingVehiclesIncorrectState() throws VehicleException  {
		
	}
	
	@Test
	public void testing() throws SimulationException, VehicleException {
		fillCarPark();
		testCarPark.archiveDepartingVehicles(300, true);
		assertEquals(1,1);
		
		
	} 
	
	/**
	 * Expects an exception when archiving a vehicle in the incorrect state.
	 * @throws SimulationException if one or more departing vehicles are not in the car park when operation applied.
	 * @author Steven
	 */
	@Test(expected = VehicleException.class)
	public void archiveDepartingVehiclesNotParked() throws SimulationException  {
		
	}
	
	////////////////////////////////
	// SIMPLE GETTERS - CARPARK FULL
	/////////////////////////////////
	
	/************************************************
	 * Calling this method on a full Carpark should return true.
	 * @throws VehicleException
	 * @throws SimulationException
	 * @author Steven
	 **********************************************/
	@Test
	public void carParkFullTrue() throws VehicleException, SimulationException {
		fillCarPark();
		assertTrue("Calling this method on a full Carpark should return true.", testCarPark.carParkFull());
	}
	
	/************************************************
	 * Calling this method on an empty Carpark should return False.
	 * @throws VehicleException
	 * @throws SimulationException
	 * @author Steven
	 ***********************************************/
	@Test
	public void carParkFullFalse() throws VehicleException, SimulationException {
		assertTrue("Calling this method on a full Carpark should return true.", !testCarPark.carParkFull());
	}
	
	///////////////////////////////////
	// SIMPLE GETTERS - CAR PARK EMPTY
	//////////////////////////////////
	
	/************************************************
	 * Calling this method on a full Carpark should return false.
	 * @throws VehicleException
	 * @throws SimulationException
	 * @author Steven
	 ***********************************************/
	@Test
	public void carParkEmptyFalse() throws VehicleException, SimulationException {
		fillCarPark();
		Boolean carParkEmpty = testCarPark.carParkEmpty();
		assertTrue("Calling this method on a full Carpark should return false.", !carParkEmpty);
	}
	
	/************************************************
	 * Calling this method on an empty Carpark should return true.
	 * @throws VehicleException
	 * @throws SimulationException
	 * @author Steven
	 ***********************************************/
	@Test
	public void carParkEmptyTrue() throws VehicleException, SimulationException {
		Boolean carParkEmpty = testCarPark.carParkEmpty();
		assertTrue("Calling this method on an empty Carpark should return true.", carParkEmpty);
	}

	/////////////////////////////////////////
	// SIMPLE GETTERS - NUM VEHICLES IN QUE
	////////////////////////////////////////
	
	//numVehiclesInQueue
	/************************************************
	 * Calling this method on a full list should be equal to the maxQueueSize.
	 * @throws VehicleException
	 * @throws SimulationException
	 * @author Steven
	 ***********************************************/
	@Test
	public void numVehiclesInQueueFilled() throws VehicleException, SimulationException {
		fillQueue();
		assertEquals(testCarPark.numVehiclesInQueue(), maxQueueSize);
	}
	
	/************************************************
	 * Calling this method on an empty Queue should return zero.
	 * @throws VehicleException
	 * @throws SimulationException
	 * @author Steven
	 ***********************************************/
	@Test
	public void numVehiclesInQueueZero() throws VehicleException, SimulationException {
		assertEquals(testCarPark.numVehiclesInQueue(), 0);
	}
	
	//getNumCars() Boundary cases
	/************************************************
	 * checks to see that getNumCars correctly performs addition of normal, small, and bikes.
	 * @author Steven
	 ***********************************************/
	@Test
	public void getNumCars() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testCar, arrivalTime, intendedDuration);
		int numCars = testCarPark.getNumCars();
		assertEquals(numCars, 1);
	}
	
	/************************************************
	 * checks to see that getNumCars correctly performs addition of normal, small, and bikes.
	 * @author Steven
	 ***********************************************/
	@Test
	public void getNumSmallCars() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testSmallCar, arrivalTime, intendedDuration);
		int numSmallCars = testCarPark.getNumSmallCars();
		assertEquals(numSmallCars, 1);
	}
	
	/************************************************
	 * test numBikes
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author
	 ***********************************************/
	@Test
	public void getNumMotorCycles() throws SimulationException, VehicleException {
		testCarPark.parkVehicle(testBike, arrivalTime, intendedDuration);
		int numBikes = testCarPark.getNumMotorCycles();
		assertEquals(numBikes, 1);
	}
	
	///////////////////////
	// HELPER METHODS
	///////////////////////
	
	/***********************************************
	 * Fills the que with unique cars
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Izaac
	 ***********************************************/
	private void fillQueue() throws SimulationException, VehicleException {
		for (int i=0; i < maxQueueSize; i++) {
			testCar = new Car("C" + i, intendedDuration, isLarge);
			testCarPark.enterQueue(testCar);
		}
	}
	
	/*******************************************
	 * helper method to fill the car park to capacity
	 * @throws SimulationException
	 * @throws VehicleException
	 ***********************************************/
	
	private void fillCarPark() throws SimulationException, VehicleException {
		fillNormalSpaces();
		fillSmallSpaces();
		fillBikeSpaces();
	}
	
	
	/**
	 * helper method to fill the normal car spaces with test cars
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 */
	private void fillNormalSpaces() throws SimulationException, VehicleException {
		
		for (int i=0; i < maxSpaces; i++) {
			testCar = new Car("C" + i, intendedDuration, isLarge);
			testCarPark.parkVehicle(testCar, time, intendedDuration);
		}
	}
	

	/************************************************
	 * helper method to fill the small spaces with test cars
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ***********************************************/
	private void fillSmallSpaces() throws SimulationException, VehicleException {
		
		for (int i=0; i < maxSmallCarSpaces; i++) {
			testSmallCar = new Car("S" + i, intendedDuration, isSmall);
			testCarPark.parkVehicle(testSmallCar, time, intendedDuration);
		}
	}
	
	/************************************************
	 * helper method to fill the motorcycle spaces
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 ***********************************************/
	private void fillBikeSpaces() throws SimulationException, VehicleException {

		for (int i=0; i < maxMotorCycleSpaces; i++) {
			testBike = new MotorCycle("M" + i, intendedDuration);
			testCarPark.parkVehicle(testBike, time, intendedDuration);
		}
	}

	
}
