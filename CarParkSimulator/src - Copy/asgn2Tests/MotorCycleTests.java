/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 22/04/2014
 * 
 */
package asgn2Tests;
import asgn2Exceptions.*;
import asgn2Simulators.Constants;
import asgn2Vehicles.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hogan
 *
 */
public class MotorCycleTests {
	
	/************************
	 * SETTING UP VARIABLES
	 * @author Steven
	 ***********************/	
	
	Car testBike;
	int parkingTime = 111;
	int intendedDuration = 110;
	int departureTime = 222;
	int arrivalTime = 1;
	int exitTime = 444;
	String vehID = "C12";
	boolean isSmall = true;
	
	@Before
	public void setUp() throws VehicleException {
		testBike = new Car(vehID, arrivalTime, isSmall);
	}

	@After
	public void tearDown() throws VehicleException {
	}

	/////////////////////////////////////
	// CONSTRUCTOR TESTS
	/////////////////////////////////	
	
	/*********************************************************
	 * testBike is constructed and not null
	 **********************************************************/
	@Test
	public void testBike() {
		assertNotNull(testBike);
	}
	
	/********************************************************
	 * Arrival Time is set and retrieved
	 * @throws VehicleException
	 *********************************************************/
	@Test
	public void testGetArrivalTime() throws VehicleException {
		arrivalTime = 1;
		Vehicle Car = new Car(vehID, arrivalTime, isSmall);
		assertEquals(testBike.getArrivalTime(), arrivalTime);
	}
	
	/********************************************************
	 * Arrival Time is set and retrieved
	 * @throws VehicleException
	 *********************************************************/
	@Test //get vehicle ID
	public void testGetVehID() throws VehicleException {
		vehID = "C69";
		Vehicle testBike = new Car(vehID, arrivalTime, isSmall);
		assertEquals(testBike.getVehID(), vehID);
	}
	
	/********************************************************
	 * isSmall returns correct value.
	 * @throws VehicleException
	 *********************************************************/
	@Test
	public void testIsSmall() {
		this.isSmall = true;
		assertEquals(testBike.isSmall(), true);	
	}

	///////////////////////////////////////
	// TESTING PARKING
	//////////////////////////////////////	
	
	/*********************************************************
	 * Test that enterParked() mutates the isParked propery of Vehicle.
	 * isParked returns the value. This tests both methods.
	 * @throws VehicleException
	 *********************************************************/
	@Test 
	public void testEnterParkedState() throws VehicleException {
		testBike.enterParkedState(parkingTime, intendedDuration);
		assertEquals(testBike.isParked(), true);
	}
	
	/******************************************
	 * Global variable wasParked is set by enterParked state. Can be accessed
	 * with wasParked().
	 * @throws VehicleException on enterParkedState when isParked() = true;
	 * @author Steven
	 *******************************************/
	@Test
	public void testWasParked() throws VehicleException {
		testBike.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(testBike.wasParked(), true);
	}
	
	/******************************************
	 * Parking time is set when the vehicle enters into a parking state.
	 * getParkingTime should return the same time as used in the args
	 * for enterParkedState.
	 * @throws VehicleException
	 * @author Steven
	 *******************************************/
	@Test 
	public void testParkingTime() throws VehicleException {
		testBike.enterParkedState(this.parkingTime,  intendedDuration);
		assertEquals(testBike.getParkingTime(), this.parkingTime);
	}
	
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test //exit parking
	public void testExitParking() throws VehicleException {
		testBike.enterParkedState(parkingTime, intendedDuration);
		testBike.exitParkedState(departureTime);
		assertEquals(testBike.isParked(), false);
	}
	
	/****************************
	 * TEST PARKING EXCEPTIONS
	 * @author Steven
	 *****************************/
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//enter parked state duration <= 0 throws VehicleException
	public void testEnterParkedExceptionDurationZero() throws VehicleException {
		testBike.enterParkedState(0, intendedDuration);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//vehicle already parked throws VehicleException
	public void testEnterParkedExceptionAlreadyParked() throws VehicleException {
		testBike.enterParkedState(parkingTime, intendedDuration);
		testBike.enterParkedState(parkingTime, intendedDuration);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//vehicle already queued throws VehicleException
	public void testEnterParkedExceptionAlreadyQueued() throws VehicleException {
		testBike.enterQueuedState();
		testBike.enterParkedState(parkingTime, intendedDuration);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//intended duration < minimum throws VehicleException
	public void testEnterParkedExceptionDurationMin() throws VehicleException {
		int min = Constants.MINIMUM_STAY;
		testBike.enterParkedState(parkingTime, min -1);
	}
	
	/***
	 * PARKING EXCEPTIONS FOR NEGATIVE INPUT
	 * @author steven
	 ***/
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//departing before arrival throws VehicleException
	public void testExitParkedExceptionNegativeDeparture() throws VehicleException {
		int exit = -1;
		testBike = new Car(vehID, arrivalTime, isSmall);
		testBike.exitQueuedState(exit);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//enter parked state with negative parkingTime
	public void testEnterParkedExceptionNegativeTime() throws VehicleException {
		int time = -1;
		testBike.enterParkedState(time, intendedDuration);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//enter parked state with negative intendedDuration
	public void testEnterParkedExceptionNegativeDuration() throws VehicleException {
		int duration = -1;
		testBike.enterParkedState(parkingTime, duration);
	}
	
	/***
	 * PARKING EXCEPTIONS FOR 'ZERO' INPUT
	 * @author Steven
	 ***/
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//departing before arrival throws VehicleException
	public void testExitParkedExceptionZeroDeparture() throws VehicleException {
		int exit = 0;
		testBike = new Car(vehID, arrivalTime, isSmall);
		testBike.exitQueuedState(exit);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//enter parked state with negative parkingTime
	public void testEnterParkedExceptionZeroTime() throws VehicleException {
		int time = 0;
		testBike.enterParkedState(time, intendedDuration);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test(expected = VehicleException.class)
	//enter parked state with negative intendedDuration
	public void testEnterParkedExceptionZeroDuration() throws VehicleException {
		int duration = 0;
		testBike.enterParkedState(parkingTime, duration);
	}
	

	/*********************
	 * TESTING QUEING
	 * @author Steven
	 ********************/
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test //is queued
	public void testEnterQueuedState() throws VehicleException {
		testBike.enterQueuedState();
		assertEquals(testBike.isQueued(), true);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test //was queued
	public void testWasQueued() throws VehicleException {
		testBike.enterQueuedState();
		assertEquals(testBike.wasQueued(), true);
	}
	
	/********************************************************
	 * 
	 * @throws VehicleException
	 *********************************************************/
	@Test //exit queue
	public void testExitQueue() throws VehicleException {
		testBike.enterQueuedState();
		testBike.exitQueuedState(departureTime);
		assertEquals(testBike.isQueued(), false);
	}
	
	/*********************
	 * TESTING QUEING EXCEPTIONS
	 * @author Steven
	 ********************/
	
	@Test(expected = VehicleException.class)
	//vehicle already qued
	public void testEnterQueuedExceptionAlreadyQueued() throws VehicleException {
		testBike.enterQueuedState();
		testBike.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	//vehicle already parked
	public void testEnterQueuedExceptionAlreadyParked() throws VehicleException {
		testBike.enterParkedState(parkingTime, intendedDuration);
		testBike.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	//exit queued when parked throws VehicleException
	public void testExitQueuedAlreadyParked() throws VehicleException {
		testBike.enterParkedState(parkingTime, intendedDuration);
		testBike.exitQueuedState(exitTime);
	}
	
	@Test(expected = VehicleException.class)
	//exit que when not queued throws VehicleException
	public void testExitQueuedAlreadyQueued() throws VehicleException {
		testBike.exitQueuedState(exitTime);
	}
	
	@Test(expected = VehicleException.class)
	//departing before arrival throws VehicleException
	public void testExitQueuedExitBeforeArrive() throws VehicleException {
		int arrival = 300;
		int exit = 299;
		testBike = new Car(vehID, arrival, isSmall);
		testBike.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//negative exit time throws VehicleException
	public void testExitQueuedNegativeDeparture() throws VehicleException {
		int exit = -1;
		testBike = new Car(vehID, arrivalTime, isSmall);
		testBike.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//zero exit time throws VehicleException
	public void testExitQueuedZeroDeparture() throws VehicleException {
		int exit = 0;
		testBike = new Car(vehID, arrivalTime, isSmall);
		testBike.exitQueuedState(exit);
	}
	
	/*******************
	 * OTHER TESTS
	 ******************/
	
	@Test
	//vehicle turned away, driver is dissatisfied
	public void testIsSatisfiedTurnedAway() throws VehicleException {
		testBike.enterQueuedState();
		testBike.exitQueuedState(arrivalTime);
		assertEquals(testBike.isSatisfied(), false);	
	}
	
	@Test
	//Queueing too long = dissatisfied
	public void testIsSatisfiedQueueTooLong() throws VehicleException {
		testBike.enterQueuedState();
		testBike.exitQueuedState(Constants.MAXIMUM_QUEUE_TIME + 1);
		assertEquals(testBike.isSatisfied(), false);	
	}
}
