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
	
	Car TestBike;
	int parkingTime = 1;
	int intendedDuration = 1;
	int departureTime = 1;
	int arrivalTime = 1;
	int exitTime = 1;
	String vehID = "abc";
	boolean isSmall = true;
	
	@Before
	public void setUp() throws Exception {
		TestBike = new Car(vehID, arrivalTime, isSmall);
	}

	@After
	public void tearDown() throws Exception {
	}

	/************************
	 * CONSTRUCTOR TESTS
	 * @author Steven
	 ***********************/	

	@Test //constructed
	public void TestBike() {
		assertNotNull(TestBike);
	}
	
	@Test //arrival time
	public void testGetArrivalTime() {
		assertEquals(TestBike.getArrivalTime(), arrivalTime);
	}
	
	@Test //get vehicle ID
	public void testGetVehID() {
		assertEquals(TestBike.getVehID(), vehID);
	}

	/*********************
	 * TESTING PARKING
	 * @author Steven
	 ********************/	
	
	
	@Test //is parked
	public void testEnterParkedState() throws Exception {
		TestBike.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(TestBike.isParked(), true);
	}
	
	@Test //was parked
	public void testWasParked() throws Exception {
		TestBike.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(TestBike.wasParked(), true);
	}
	
	@Test //parking time
	public void testParkingTime() throws Exception {
		TestBike.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(TestBike.getParkingTime(), parkingTime);
	}
	
	@Test //exit parking
	public void testExitParking() throws Exception {
		TestBike.exitParkedState(departureTime);
		assertEquals(TestBike.isParked(), false);
	}
	
	/****************************
	 * TEST PARKING EXCEPTIONS
	 * @author Steven
	 *****************************/
	
	@Test(expected = VehicleException.class)
	//enter parked state duration <= 0 throws exception
	public void testEnterParkedExceptionDurationZero() throws VehicleException {
		TestBike.enterParkedState(0, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//vehicle already parked throws exception
	public void testEnterParkedExceptionAlreadyParked() throws VehicleException {
		TestBike.enterParkedState(parkingTime, intendedDuration);
		TestBike.enterParkedState(parkingTime, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//vehicle already queued throws exception
	public void testEnterParkedExceptionAlreadyQueued() throws VehicleException {
		TestBike.enterQueuedState();
		TestBike.enterParkedState(parkingTime, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//intended duration < minimum throws exception
	public void testEnterParkedExceptionDurationMin() throws VehicleException {
		int min = Constants.MINIMUM_STAY;
		TestBike.enterParkedState(parkingTime, min -1);
	}
	
	/***
	 * PARKING EXCEPTIONS FOR NEGATIVE INPUT
	 * @author steven
	 ***/
	
	@Test(expected = VehicleException.class)
	//departing before arrival throws exception
	public void testExitParkedExceptionNegativeDeparture() throws VehicleException {
		int exit = -1;
		TestBike = new Car(vehID, arrivalTime, isSmall);
		TestBike.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative parkingTime
	public void testEnterParkedExceptionNegativeTime() throws VehicleException {
		int time = -1;
		TestBike.enterParkedState(time, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative intendedDuration
	public void testEnterParkedExceptionNegativeDuration() throws VehicleException {
		int duration = -1;
		TestBike.enterParkedState(parkingTime, duration);
	}
	
	/***
	 * PARKING EXCEPTIONS FOR 'ZERO' INPUT
	 * @author Steven
	 ***/
	
	@Test(expected = VehicleException.class)
	//departing before arrival throws exception
	public void testExitParkedExceptionZeroDeparture() throws VehicleException {
		int exit = 0;
		TestBike = new Car(vehID, arrivalTime, isSmall);
		TestBike.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative parkingTime
	public void testEnterParkedExceptionZeroTime() throws VehicleException {
		int time = 0;
		TestBike.enterParkedState(time, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative intendedDuration
	public void testEnterParkedExceptionZeroDuration() throws VehicleException {
		int duration = 0;
		TestBike.enterParkedState(parkingTime, duration);
	}
	

	/*********************
	 * TESTING QUEING
	 * @author Steven
	 ********************/
	
	@Test //is queued
	public void testEnterQueuedState() throws Exception {
		TestBike.enterQueuedState();
		assertEquals(TestBike.isQueued(), true);
	}
	
	@Test //was queued
	public void testWasQueued() throws Exception {
		TestBike.enterQueuedState();
		assertEquals(TestBike.wasQueued(), true);
	}
	
	@Test //exit queue
	public void testExitQueue() throws Exception {
		TestBike.exitQueuedState(departureTime);
		assertEquals(TestBike.isQueued(), false);
	}
	
	/*********************
	 * TESTING QUEING EXCEPTIONS
	 * @author Steven
	 ********************/
	
	@Test(expected = VehicleException.class)
	//vehicle already qued
	public void testEnterQueuedExceptionAlreadyQueued() throws VehicleException {
		TestBike.enterQueuedState();
		TestBike.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	//vehicle already parked
	public void testEnterQueuedExceptionAlreadyParked() throws VehicleException {
		TestBike.enterParkedState(parkingTime, intendedDuration);
		TestBike.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	//exit queued when parked throws exception
	public void testExitQueuedAlreadyParked() throws VehicleException {
		TestBike.enterParkedState(parkingTime, intendedDuration);
		TestBike.exitQueuedState(exitTime);
	}
	
	@Test(expected = VehicleException.class)
	//exit que when not queued throws exception
	public void testExitQueuedAlreadyQueued() throws VehicleException {
		TestBike.exitQueuedState(exitTime);
	}
	
	@Test(expected = VehicleException.class)
	//departing before arrival throws exception
	public void testExitQueuedExitBeforeArrive() throws VehicleException {
		int arrival = 300;
		int exit = 299;
		TestBike = new Car(vehID, arrival, isSmall);
		TestBike.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//negative exit time throws exception
	public void testExitQueuedNegativeDeparture() throws VehicleException {
		int exit = -1;
		TestBike = new Car(vehID, arrivalTime, isSmall);
		TestBike.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//zero exit time throws exception
	public void testExitQueuedZeroDeparture() throws VehicleException {
		int exit = 0;
		TestBike = new Car(vehID, arrivalTime, isSmall);
		TestBike.exitQueuedState(exit);
	}
	
	/*******************
	 * OTHER TESTS
	 ******************/
	
	@Test //test departure time
	public void testDepartureTime() {
		int departure = arrivalTime + intendedDuration;
		assertEquals(TestBike.getDepartureTime(), departure);
	}
	
	@Test
	//vehicle turned away, driver is dissatisfied
	public void testIsSatisfiedTurnedAway() throws VehicleException {
		TestBike.enterQueuedState();
		TestBike.exitQueuedState(arrivalTime);
		assertEquals(TestBike.isSatisfied(), false);	
	}
	
	@Test
	//Queueing too long = dissatisfied
	public void testIsSatisfiedQueueTooLong() throws VehicleException {
		TestBike.enterQueuedState();
		TestBike.exitQueuedState(Constants.MAXIMUM_QUEUE_TIME + 1);
		assertEquals(TestBike.isSatisfied(), false);	
	}
}
