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
public class CarTests {
	
	/************************
	 * SETTING UP VARIABLES
	 * @author Steven
	 ***********************/	
	
	Car TestCar;
	int parkingTime = 1;
	int intendedDuration = 1;
	int departureTime = 1;
	int arrivalTime = 1;
	int exitTime = 1;
	String vehID = "abc";
	boolean isSmall = true;
	
	@Before
	public void setUp() throws Exception {
		TestCar = new Car(vehID, arrivalTime, isSmall);
	}

	@After
	public void tearDown() throws Exception {
	}

	/************************
	 * CONSTRUCTOR TESTS
	 * @author Steven
	 ***********************/	

	@Test //constructed
	public void testCar() {
		assertNotNull(TestCar);
	}
	
	@Test //arrival time
	public void testGetArrivalTime() {
		assertEquals(TestCar.getArrivalTime(), arrivalTime);
	}
	
	@Test //get vehicle ID
	public void testGetVehID() {
		assertEquals(TestCar.getVehID(), vehID);
	}

	@Test //is small
	public void testIsSmall() {
		assertEquals(TestCar.isSmall(), true);	
	}

	/*********************
	 * TESTING PARKING
	 * @author Steven
	 ********************/	
	
	
	@Test //is parked
	public void testEnterParkedState() throws Exception {
		TestCar.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(TestCar.isParked(), true);
	}
	
	@Test //was parked
	public void testWasParked() throws Exception {
		TestCar.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(TestCar.wasParked(), true);
	}
	
	@Test //parking time
	public void testParkingTime() throws Exception {
		TestCar.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(TestCar.getParkingTime(), parkingTime);
	}
	
	@Test //exit parking
	public void testExitParking() throws Exception {
		TestCar.exitParkedState(departureTime);
		assertEquals(TestCar.isParked(), false);
	}
	
	/****************************
	 * TEST PARKING EXCEPTIONS
	 * @author Steven
	 *****************************/
	
	@Test(expected = VehicleException.class)
	//enter parked state duration <= 0 throws exception
	public void testEnterParkedExceptionDurationZero() throws VehicleException {
		TestCar.enterParkedState(0, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//vehicle already parked throws exception
	public void testEnterParkedExceptionAlreadyParked() throws VehicleException {
		TestCar.enterParkedState(parkingTime, intendedDuration);
		TestCar.enterParkedState(parkingTime, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//vehicle already queued throws exception
	public void testEnterParkedExceptionAlreadyQueued() throws VehicleException {
		TestCar.enterQueuedState();
		TestCar.enterParkedState(parkingTime, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//intended duration < minimum throws exception
	public void testEnterParkedExceptionDurationMin() throws VehicleException {
		int min = Constants.MINIMUM_STAY;
		TestCar.enterParkedState(parkingTime, min -1);
	}
	
	/***
	 * PARKING EXCEPTIONS FOR NEGATIVE INPUT
	 * @author steven
	 ***/
	
	@Test(expected = VehicleException.class)
	//departing before arrival throws exception
	public void testExitParkedExceptionNegativeDeparture() throws VehicleException {
		int exit = -1;
		TestCar = new Car(vehID, arrivalTime, isSmall);
		TestCar.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative parkingTime
	public void testEnterParkedExceptionNegativeTime() throws VehicleException {
		int time = -1;
		TestCar.enterParkedState(time, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative intendedDuration
	public void testEnterParkedExceptionNegativeDuration() throws VehicleException {
		int duration = -1;
		TestCar.enterParkedState(parkingTime, duration);
	}
	
	/***
	 * PARKING EXCEPTIONS FOR 'ZERO' INPUT
	 * @author Steven
	 ***/
	
	@Test(expected = VehicleException.class)
	//departing before arrival throws exception
	public void testExitParkedExceptionZeroDeparture() throws VehicleException {
		int exit = 0;
		TestCar = new Car(vehID, arrivalTime, isSmall);
		TestCar.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative parkingTime
	public void testEnterParkedExceptionZeroTime() throws VehicleException {
		int time = 0;
		TestCar.enterParkedState(time, intendedDuration);
	}
	
	@Test(expected = VehicleException.class)
	//enter parked state with negative intendedDuration
	public void testEnterParkedExceptionZeroDuration() throws VehicleException {
		int duration = 0;
		TestCar.enterParkedState(parkingTime, duration);
	}
	

	/*********************
	 * TESTING QUEING
	 * @author Steven
	 ********************/
	
	@Test //is queued
	public void testEnterQueuedState() throws Exception {
		TestCar.enterQueuedState();
		assertEquals(TestCar.isQueued(), true);
	}
	
	@Test //was queued
	public void testWasQueued() throws Exception {
		TestCar.enterQueuedState();
		assertEquals(TestCar.wasQueued(), true);
	}
	
	@Test //exit queue
	public void testExitQueue() throws Exception {
		TestCar.exitQueuedState(departureTime);
		assertEquals(TestCar.isQueued(), false);
	}
	
	/*********************
	 * TESTING QUEING EXCEPTIONS
	 * @author Steven
	 ********************/
	
	@Test(expected = VehicleException.class)
	//vehicle already qued
	public void testEnterQueuedExceptionAlreadyQueued() throws VehicleException {
		TestCar.enterQueuedState();
		TestCar.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	//vehicle already parked
	public void testEnterQueuedExceptionAlreadyParked() throws VehicleException {
		TestCar.enterParkedState(parkingTime, intendedDuration);
		TestCar.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	//exit queued when parked throws exception
	public void testExitQueuedAlreadyParked() throws VehicleException {
		TestCar.enterParkedState(parkingTime, intendedDuration);
		TestCar.exitQueuedState(exitTime);
	}
	
	@Test(expected = VehicleException.class)
	//exit que when not queued throws exception
	public void testExitQueuedAlreadyQueued() throws VehicleException {
		TestCar.exitQueuedState(exitTime);
	}
	
	@Test(expected = VehicleException.class)
	//departing before arrival throws exception
	public void testExitQueuedExitBeforeArrive() throws VehicleException {
		int arrival = 300;
		int exit = 299;
		TestCar = new Car(vehID, arrival, isSmall);
		TestCar.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//negative exit time throws exception
	public void testExitQueuedNegativeDeparture() throws VehicleException {
		int exit = -1;
		TestCar = new Car(vehID, arrivalTime, isSmall);
		TestCar.exitQueuedState(exit);
	}
	
	@Test(expected = VehicleException.class)
	//zero exit time throws exception
	public void testExitQueuedZeroDeparture() throws VehicleException {
		int exit = 0;
		TestCar = new Car(vehID, arrivalTime, isSmall);
		TestCar.exitQueuedState(exit);
	}
	
	/*******************
	 * OTHER TESTS
	 ******************/
	
	@Test //test departure time
	public void testDepartureTime() {
		int departure = arrivalTime + intendedDuration;
		assertEquals(TestCar.getDepartureTime(), departure);
	}
	
	@Test
	//vehicle turned away, driver is dissatisfied
	public void testIsSatisfiedTurnedAway() throws VehicleException {
		TestCar.enterQueuedState();
		TestCar.exitQueuedState(arrivalTime);
		assertEquals(TestCar.isSatisfied(), false);	
	}
	
	@Test
	//Queueing too long = dissatisfied
	public void testIsSatisfiedQueueTooLong() throws VehicleException {
		TestCar.enterQueuedState();
		TestCar.exitQueuedState(Constants.MAXIMUM_QUEUE_TIME + 1);
		assertEquals(TestCar.isSatisfied(), false);	
	}
}
