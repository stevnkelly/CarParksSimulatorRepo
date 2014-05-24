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
	Car TestCar;
	int parkingTime = 1;
	int intendedDuration = 1;
	int departureTime = 1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		TestCar = new Car("abc1", 1, true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link asgn2Vehicles.Car#toString()}.
	 */
	@Test
	public void testToString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Car#Car(java.lang.String, int, boolean)}.
	 */
	@Test
	public void testCar() {
		assertNotNull(TestCar);
	}

	/**
	 * Test method for {@link asgn2Vehicles.Car#isSmall()}.
	 */
	@Test
	public void testIsSmall() {
		assertEquals(TestCar.isSmall(), true);	
	}

	/*********************
	 * TESTING PARKING
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
	
	@Test //parking duration
	public void testIntendedDuration() throws Exception {
		TestCar.enterParkedState(parkingTime,  intendedDuration);
		assertEquals(TestCar.getDepartureTime(), intendedDuration);
	}

	/*********************
	 * TESTING QUES
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
	
	
	

}
