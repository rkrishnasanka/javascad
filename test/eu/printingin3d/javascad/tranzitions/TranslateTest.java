package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.TestModel;

public class TranslateTest {
	private static final double EPSILON = 0.001;
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}

	@Test
	public void testGetTranslate() {
		assertEqualsWithoutWhiteSpaces("translate([10,20,30])", Translate.getTranslate(new Coords3d(10, 20, 30)));
	}
	
	@Test
	public void testGetTranslatePovRay() {
		Language.POVRay.setCurrent();
		
		assertEqualsWithoutWhiteSpaces("translate <10,20,30>", Translate.getTranslate(new Coords3d(10, 20, 30)));
	}
	
	@Test
	public void getTranslateShouldReturnAnEmptyStringIfMoveIsZero() {
		assertEqualsWithoutWhiteSpaces("", Translate.getTranslate(Coords3d.ZERO));
	}
	
	@Test
	public void getTranslateShouldReturnAnEmptyStringIfMoveIsZeroPovRay() {
		Language.POVRay.setCurrent();
		
		assertEqualsWithoutWhiteSpaces("", Translate.getTranslate(Coords3d.ZERO));
	}
	
	@Test
	public void testToScad() {
		Translate translate = new Translate(new TestModel("(model)"), new Coords3d(30, 20, 10));
		assertEqualsWithoutWhiteSpaces("translate([30,20,10]) (model)", translate.toScad());
	}
	
	@Test
	public void testToPovRay() {
		Language.POVRay.setCurrent();
		
		Translate translate = new Translate(new TestModel("(model)"), new Coords3d(30, 20, 10));
		assertEqualsWithoutWhiteSpaces("object { (model) translate <30,20,10> #attributes }", translate.innerToScad());
	}
	
	@Test
	public void toScadWithZeroMoveShouldDoNothing() {
		Translate translate = new Translate(new TestModel("(model)"), Coords3d.ZERO);
		assertEqualsWithoutWhiteSpaces("(model)", translate.toScad());
	}
	
	@Test
	public void testBoundariesWhenMoveIsZero() {
		Boundaries3d boundaries = new Boundaries3d(
				new Boundary(50.2, 13.3), 
				new Boundary(18.3, 78.3), 
				new Boundary(10.0, 43.2));
		Translate translate = new Translate(new TestModel("(model)", boundaries), Coords3d.ZERO);
		Boundaries3d newBoundaries = translate.getBoundaries();
		
		Assert.assertEquals(13.3, newBoundaries.getX().getMin(), EPSILON);
		Assert.assertEquals(50.2, newBoundaries.getX().getMax(), EPSILON);
		Assert.assertEquals(18.3, newBoundaries.getY().getMin(), EPSILON);
		Assert.assertEquals(78.3, newBoundaries.getY().getMax(), EPSILON);
		Assert.assertEquals(10.0, newBoundaries.getZ().getMin(), EPSILON);
		Assert.assertEquals(43.2, newBoundaries.getZ().getMax(), EPSILON);
	}
	
	@Test
	public void testBoundaries() {
		Boundaries3d boundaries = new Boundaries3d(
				new Boundary(50.2, 13.3), 
				new Boundary(18.3, 78.3), 
				new Boundary(10.0, 43.2));
		Translate translate = new Translate(new TestModel("(model)", boundaries), new Coords3d(30, 20, 10));
		Boundaries3d newBoundaries = translate.getBoundaries();
		
		Assert.assertEquals(43.3, newBoundaries.getX().getMin(), EPSILON);
		Assert.assertEquals(80.2, newBoundaries.getX().getMax(), EPSILON);
		Assert.assertEquals(38.3, newBoundaries.getY().getMin(), EPSILON);
		Assert.assertEquals(98.3, newBoundaries.getY().getMax(), EPSILON);
		Assert.assertEquals(20.0, newBoundaries.getZ().getMin(), EPSILON);
		Assert.assertEquals(53.2, newBoundaries.getZ().getMax(), EPSILON);
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfModelIsNull() {
		new Translate(null, new Coords3d(30, 20, 10));
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfMoveIsNull() {
		new Translate(new TestModel("(model)"), null);
	}
}
