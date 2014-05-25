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
	private int exitTime = 1;
	private String vehID = "abc";
	private Boolean isSmall = true;
	private Boolean isLarge = false;
	private int time = 60;
	private int intendedDuration = 1;
	
	private int maxSpaces = Constants.DEFAULT_MAX_CAR_SPACES; //100
	private int maxSmallCarSpaces = Constants.DEFAULT_MAX_SMALL_CAR_SPACES; // = 20;
	private int maxMotorCycleSpaces = Constants.DEFAULT_MAX_MOTORCYCLE_SPACES;// = 20;
	private int maxQueueSize = Constants.DEFAULT_MAX_QUEUE_SIZE; // = 10;
	
	
	@Before
	public void setUp() throws Exception {
		testCarPark = new CarPark();
		testSimulator = new Simulator();
		testCar = new Car(vehID, arrivalTime, isLarge);
		testSmallCar = new Car(vehID, arrivalTime, isSmall);
		testBike = new MotorCycle(vehID, arrivalTime);
	}

	@After
	public void tearDown() throws Exception {
	}

	///////////////////////
	// CONSTRUCTOR TESTS
	//////////////////////
	
	/**
	 * test that a CarPark is correctly constructed with default values;
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 */
	@Test 
	public void carParkConstructed() throws SimulationException, VehicleException {
		fillNormalSpaces();
		fillSmallSpaces();
		fillBikeSpaces();
		assertTrue("This test fills the carpark to capacity, carParkFull should return"
				+ "true", testCarPark.carParkFull());
	}
	
	//test handing teh constructor negatives and zeros? constructor has no exception handler.
	
	/**
	 * Expects an exception when archiving a vehicle in the incorrect state.
	 * @throws VehicleException
	 * @author Steven
	 */
	@Test(expected = VehicleException.class)
	public void archiveDepartingVehiclesIncorrectState() throws VehicleException  {
		
	}
	
	/**
	 * Expects an exception when archiving a vehicle in the incorrect state.
	 * @throws SimulationException if one or more departing vehicles are not in the car park when operation applied.
	 * @author Steven
	 */
	@Test(expected = VehicleException.class)
	public void archiveDepartingVehiclesNotParked() throws SimulationException  {
		
	}
	
	///////////////////////
	// HELPER METHODS
	///////////////////////
	
	/**
	 * helper method to fill the normal car spaces with test cars
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 */
	private void fillNormalSpaces() throws SimulationException, VehicleException {
		for (int i=0; i < maxSpaces; i++) {
			testCarPark.parkVehicle(testCar, time, intendedDuration);
		}
	}
	
	/**
	 * helper method to fill the small spaces with test cars
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 */
	private void fillSmallSpaces() throws SimulationException, VehicleException {
		for (int i=0; i < maxSmallCarSpaces; i++) {
			testCarPark.parkVehicle(testSmallCar, time, intendedDuration);
		}
	}
	
	/**
	 * helper method to fill the motorcycle spaces
	 * @throws SimulationException
	 * @throws VehicleException
	 * @author Steven
	 */
	private void fillBikeSpaces() throws SimulationException, VehicleException {
		for (int i=0; i < maxMotorCycleSpaces; i++) {
			testCarPark.parkVehicle(testBike, time, intendedDuration);
		}
	}
	
	
}
