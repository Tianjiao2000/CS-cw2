package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_2_FunctionalTest {
	
private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	/*
	 * For class Option
	 */
	// determine whether two options are not equal: if one option is null
	@Test
	public void testEqualsNull() {
		Option opt1 = new Option("fff", Type.BOOLEAN);
		assertFalse(opt1.equals(null));
	}
	// determine whether two options are not equal: by equals their type
	@Test
	public void testEqualsOtherType() {
		Option opt1 = new Option("fff", Type.NOTYPE);
		Option opt2 = new Option("eee", Type.BOOLEAN);
		assertFalse(opt1.equals(opt2));
	}
	// determine whether two options are not equal: if one option's name is null
	@Test
	public void testEqualsOtherNameNull() {
		Option opt1 = new Option(null, Type.BOOLEAN);
		Option opt2 = new Option("eee", Type.BOOLEAN);
		assertFalse(opt1.equals(opt2));
	}
	// determine whether two options are not equal: if one option's name is null???????????//////
	@Test
	public void testEqualsOtherNameNullTwo() {
		Option opt1 = new Option("eee", Type.BOOLEAN);
		Option opt2 = new Option(null, Type.BOOLEAN);
		assertFalse(opt1.equals(opt2));
	}
	// determine whether two options are not equal: all option name is null, invalid option
	@Test(expected = NullPointerException.class)
	public void testEqualsOtherNameNullThree() {
		Option opt1 = new Option(null, Type.BOOLEAN);
		Option opt2 = new Option(null, Type.BOOLEAN);
		assertFalse(opt1.equals(opt2));
	}
	// two options are identical to they equals to each other
	@Test
	public void testEquals() {
		Option opt1 = new Option("fff", Type.BOOLEAN);
		Option opt2 = new Option("fff", Type.BOOLEAN);
		assertFalse(!opt1.equals(opt2));
	}
	// determine whether two options are not equal: null type?????????????????????????
	@Test
	public void testEqualsNullType() {
		Option opt1 = new Option("fff", null);
		Option opt2 = new Option("fff", Type.BOOLEAN);
		assertFalse(opt1.equals(opt2));
	}
	@Test
	public void testEqualsNullTypeTwo() {
		Option opt1 = new Option("fff", Type.BOOLEAN);
		Option opt2 = new Option("fff", null);
		assertFalse(opt1.equals(opt2));
	}
	// test equals to itself
	@Test
	public void testEqualsSameOption() {
		Option opt1 = new Option("fff", Type.BOOLEAN);
		assertFalse(!opt1.equals(opt1));
	}
	// for notype
	@Test
	public void OptionNoType() {
		Option option = new Option("option",Type.STRING);
		parser.addOption(option);
		option.setType(Type.NOTYPE);
		assertEquals(0,parser.getInteger("option"));
	 }
	
	/*
	 * Parser tests
	 */
	// when the parse content is null
	@Test
	public void testParserNull() {
		assertEquals(parser.parse(null), -1);
	}
	// when the parse content is empty
	@Test
	public void testParserNoLength() {
		assertEquals(parser.parse(""), -2);
	}
	// when the parse entry is blank
	@Test(expected = RuntimeException.class)
	public void testParserEntrySplitBlank() {
		parser.parse("--option= ");
	}
	// when the parse entry length smaller and equals to one
	@Test(expected = RuntimeException.class)
	public void testParserEntrySplitLength() {
		parser.parse("-o");
	}
	// when the parser content's lower case equals to false, value is 0
	@Test
	public void testParserLowerCase() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		assertEquals(parser.parse("--option=false"), 0);
	}
	// when the parser content equals to zero, value is 0
	@Test
	public void testParserZero() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		assertEquals(parser.parse("--option=0"), 0);
	}
	// ??????????????????????????????assert
	@Test
	public void testParserUnescapedSingle() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=\'o\'");
		assertEquals(parser.getString("option"), "o");
	}
	// ??????????????????????????????
	@Test
	public void testParserUnescapedDouble() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=\"o\"");
		assertEquals(parser.getString("option"), "o");
	}
	// when the parser content start with ' end with "
	@Test
	public void testParserUnescapedSingleDouble() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=\'o\"");
		assertEquals(parser.getString("option"), "\'o\"");
	}
	// when the parser content start with " end with '
	@Test
	public void testParserUnescapedDoubleSingle() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=\"o\'");
		assertEquals(parser.getString("option"), "\"o\'");
	}
	// replace the content using option name by --
	@Test
	public void testParserReplaceOption() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=eee");
		parser.replace("--option", "eee", "fff");
		assertEquals(parser.getString("option"), "fff");
	}
	// ?????????????????***************
	@Test
	public void testParserReplace() {
		parser.addOption(new Option("option1", Type.STRING), "op1");
		parser.addOption(new Option("option2", Type.STRING), "op2") ;
		parser.parse("--option1=opt1 -op2=opt2");
		parser.replace("op2", "opt1", "opt2");
		assertEquals(parser.getString("op2"), "opt2");
	}
	// ??????????????????***********
	@Test
	public void testParserIntegerTypeInt() {
		parser.addOption(new Option("option", Type.INTEGER), "o");
		parser.parse("--option=eee");
		parser.getInteger("option");
	}
	// when option type is boolean, value is false
	@Test
	public void testParserIntegerTypeBoolean() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		parser.parse("--option=false");
		assertEquals(parser.getInteger("option"), 0);
	}
	// when option type is boolean, value is 0?????????????
	@Test
	public void testParserGetBoolean() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		parser.parse("--option=0");
		assertEquals(parser.getBoolean("option"), false);
	}
	// when option type is character, value is 0, integer of 0 is its ascii 48
	@Test
	public void testParserGetIntegerCha() {
		parser.addOption(new Option("option", Type.CHARACTER), "o");
		parser.parse("--option=0");
		assertEquals(parser.getInteger("option"), 48);
	}
	// when option type is boolean, replace the value true to false
	@Test
	public void testParserBooleanReplaceFalse() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		parser.parse("--option=true");
		parser.replace("option", "true", "false");
		assertEquals(parser.getBoolean("option"), false);
	}
	// when option type is boolean, replace the value true to false
	@Test
	public void testParserBooleanReplaceZero() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		parser.parse("--option=1");
		parser.replace("option", "1", "0");
		assertEquals(parser.getBoolean("option"), false);
	}
	
	/*
	 * OptionMap tests
	 */
	// when option type is NOTYPE, option is illegal
	@Test(expected = IllegalArgumentException.class)
	public void testMapStoreNotype() {
		parser.addOption(new Option("option", Type.NOTYPE), "o");
		//assertEquals(parser.getBoolean("option"), false);
	}
	// add another option with same name but has shortcut
	@Test
	public void testMapStoreShortcutExist() {
		//Option option = new Option("option", Type.STRING);
		parser.addOption(new Option("option", Type.STRING));
		parser.addOption(new Option("option", Type.STRING), "o");
		assertFalse(!parser.shortcutExists("o"));
	}
	// add another option with same name but has no shortcut
	@Test
	public void testMapStoreShortcut() {
		//Option option = new Option("option", Type.STRING);
		parser.addOption(new Option("option", Type.STRING));
		parser.addOption(new Option("option", Type.STRING));
		assertFalse(parser.shortcutExists(""));
	}
	// when using parser, optionMap does not contains the option????????replace by next
	@Test
	public void testOptionMapGetOption() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--o=newo");
		assertEquals(parser.getString("option"), "");
	}
	// use wrong parameter in getString, shortcut name should followed by -
	@Test(expected = RuntimeException.class)
	public void testOptionMapSetOption() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--o=newo");
		assertEquals(parser.getString("--o"), "newo");
	}
	// when using parser, optionMap does not contains the shortcut????????replace by next
	@Test
	public void testOptionMapGetShortcut() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("-option=newo");
		assertEquals(parser.getString("option"), "");
	}
	// use wrong parameter in getString, option name should followed by --
	@Test(expected = RuntimeException.class)
	public void testOptionMapSetShortcutn() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("-option=newo");
		assertEquals(parser.getString("-option"), "");
	}
	// when option name is null, option invalid
	@Test(expected = IllegalArgumentException.class)
	public void testOptionValidNull() {
		parser.addOption(new Option(null, Type.STRING), "o");
	}
	// when option name is empty, option invalid
	@Test(expected = IllegalArgumentException.class)
	public void testOptionValidEmpty() {
		parser.addOption(new Option("", Type.STRING), "o");
	}
	// when shortcut is null, option invalid
	@Test(expected = IllegalArgumentException.class)
	public void testOptionValidShortcutNull() {
		parser.addOption(new Option("option", Type.STRING), null);
	}
	// when shortcut contains invalid character, option invalid
	@Test(expected = IllegalArgumentException.class)
	public void testOptionValidShortCut() {
		parser.addOption(new Option("option", Type.BOOLEAN), "dewde%");
	}
	// when option not exist
	@Test
	public void testOptionMapOptionNotExist() {
		assertFalse(parser.optionOrShortcutExists("option"));
	}
	// when shortcut exist
	@Test
	public void testOptionMapShortcutExist() {
		parser.addOption(new Option("option", Type.STRING), "o");
		assertFalse(!parser.optionOrShortcutExists("o"));
	}
	// when the option exist
	@Test
	public void testOptionMapOptionExist() {
		parser.addOption(new Option("option", Type.STRING), "o");
		assertFalse(!parser.optionOrShortcutExists("option"));
	}
	// set shortcut when name is null
	@Test 
	public void testSetShortcutNull() {
		parser.setShortcut(null, "o");
		assertEquals(parser.shortcutExists("o"), false);
	}
	// set shortcut when name is not null
	@Test 
	public void testSetShortcut() {
		parser.addOption(new Option("option", Type.STRING));
		parser.setShortcut("option", "o");
		assertEquals(parser.shortcutExists("o"), true);
	}
	
	/*
	 * Original tests
	 */

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
