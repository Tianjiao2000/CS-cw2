package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task1_1_FunctionalTest {

	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	//[Bug #1 - Easy, 1PT]
	// empty option/shortcut invalid
	@Test
	public void testOne() {
		parser.addOption(new Option("option", Type.STRING), "");
	 	assertFalse(parser.optionOrShortcutExists(""));
	}
		
	// [Bug #2 - Easy, 1PT]
	// boolean false
	@Test 
	public void testTwo() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		assertEquals(false, parser.getBoolean("option"));
	}
	
	// [Bug #3 - Medium, 2PTS]
	// integer value for boolean type wrong
	@Test
	public void testThree() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		parser.parse("--option=option1");
		assertEquals(parser.getInteger("option"), 1);
	}
	
	// [Bug #4 - Medium, 2PTS]
	// shortcut length exceed
	@Test
	public void testFour() {
		parser.addOption(new Option("option", Type.STRING), "gdyvxbfkebhfxvnjekrxvbcjeknvbdjknsfdkalmdwjhfbsdnkadefhbdsnkdaheg");
		assertEquals(parser.shortcutExists("gdyvxbfkebhfxvnjekrxvbcjeknvbdjknsfdkalmdwjhfbsdnkadefhbdsnkdaheg"), true);
	}
	
	//[Bug #5 - Medium, 2PTS]
	// negative value cannot be assigned correctly
	@Test
	public void testFive() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=-1");
		assertEquals(parser.getInteger("--option"), -1);
	}
	
	// [Bug #6 - Easy, 1PT]
	// upper and lower case should be different
	@Test
	public void testSix() {
		Option opLowercase = new Option("test", Type.INTEGER);
		Option opUppercase = new Option("Test", Type.INTEGER);
		parser.addOption(opLowercase, "l");
		parser.addOption(opUppercase, "u");
		assertNotEquals(opLowercase, opUppercase);
	}
	
	// [Bug #7 - Hard, 3PTS]
	// extreme large number cannot be assigned correctly
	@Test
	public void testSeven() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=123456789");
		assertEquals(parser.getInteger("option"), 123456789);
	}
	
	// [Bug #8 - Medium, 2PTS]
	// same option different type does not overwrite
	@Test
	public void testEight() {
		Option opString = new Option("option", Type.STRING);
		Option opBoolean = new Option("option", Type.BOOLEAN);
		parser.addOption(opString, "op1");
		parser.addOption(opBoolean, "op2");
		assertEquals(opString.getType(), opBoolean.getType());
	}
	
	// [Bug #9 - Easy, 1PT]
	// if parser is " " then return 0
	@Test
	public void testNine() {
		assertEquals(0, parser.parse(" "));
	}
	
	// [Bug #10 - Easy, 1PT]
	// character value should be '\0' when option value is sting "option"
	@Test
	public void testTen() {
		parser.addOption(new Option("option", Type.CHARACTER), "o");
		assertEquals('\0', parser.getCharacter("option"));
	}
	
	// [Bug #11 - Hard, 3PTS]
	// invalid character in option name
	@Test(expected = IllegalArgumentException.class)
	public void testEleven() {
		parser.addOption(new Option("op%tion", Type.BOOLEAN), "o");
	}
	
	//[Bug #12 - Hard, 3PTS]
	// replace not work as expected
	@Test 
	public void testTwelve() {
		parser.addOption(new Option("option1", Type.STRING), "o1");
		parser.addOption(new Option("option2", Type.STRING), "o2") ;
		parser.parse("--option1=option1 -o2=option2");
		parser.replace("-o2", "option2", "option3");
		assertEquals(parser.getString("-o2"), "option3");
	}
	
	// [Bug #13 - Medium, 2PTS]
	// = within '' should be returned
	@Test(expected = RuntimeException.class)
	public void testThirteen() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option='='");
		assertEquals(parser.getString("option"), "=");
	}
	
	
	// [Bug #14 -  Hard, 3PTS]
	// parser cannot contains \\n
	@Test
	public void testFourteen() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=\\n");
		assertEquals(parser.getString("option"), "\\n");
	}
	
	// [Bug #15 - Medium, 2PTS]
	// value exceed range
	@Test
	public void testFifteen() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=9999999999");
		assertEquals(parser.getInteger("option"), 0);
	}
	
	// [Bug #16 - Medium, 2PTS]
	// should be exception
	@Test(expected = RuntimeException.class)
	public void testSixteen() {
		parser.getString(null);
	}
	
	// [Bug #17 - Hard, 3PTS]
	// option exceed length
	@Test
	public void testSeventeen() {
		parser.addOption(new Option("dnjcjkdbhrbcndsjbffhdsfjxmnDJnbshfgjcnesulhenxrue", Type.STRING));
		assertEquals(parser.optionExists("dnjcjkdbhrbcndsjbffhdsfjxmnDJnbshfgjcnesulhenxrue"), true);
	}
	
	// [Bug #18 - Easy, 1PT]
	// replace failed with space in name
	@Test
	public void testEighteen() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=eee");
		parser.replace("option   ", "eee", "fff");
		assertEquals(parser.getString("option"), "fff");
	}
	
	// [Bug #19 - Medium, 2PTS]
	// quote with - should be returned
	@Test(expected = RuntimeException.class)
	public void testNineteen() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=\"-\"");
		assertEquals(parser.getString("option"), "-");
	}
	
	// [Bug #20 - Hard, 3PTS]
	// option with space
	@Test
	public void testTwenty() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=op 1");
		assertEquals(parser.getString("option"), "op");
	}
}
