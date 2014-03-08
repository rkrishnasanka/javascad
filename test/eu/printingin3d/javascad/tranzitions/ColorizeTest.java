package eu.printingin3d.javascad.tranzitions;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.TestModel;

public class ColorizeTest {
	@BeforeClass
	public static void init() {
		Language.OpenSCAD.setCurrent();
	}

	@Test
	public void testWithoutAlpha() {
		Color color = new Color(100, 120, 200);
		AssertEx.assertEqualsWithoutWhiteSpaces("color([0.3922, 0.4706, 0.7843]) (model)", new Colorize(color, new TestModel("(model)")).toScad());
	}
	
	@Test
	public void testWithAlpha() {
		Color color = new Color(100, 120, 200, 55);
		AssertEx.assertEqualsWithoutWhiteSpaces("color([0.3922, 0.4706, 0.7843, 0.2157]) (model)", new Colorize(color, new TestModel("(model)")).toScad());
	}

}
