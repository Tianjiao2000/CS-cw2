package st;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class Task3_TDD2 {
	
	private Parser parser;
	
	@Before
	public void setup() {
		parser = new Parser();
	}
	
	// new added tests
	@Test
	public void underscoreRangeUnchanged() {
		parser.addAll("option1-1", "o1 o2", "String");
		assertFalse(!parser.optionExists("option1"));
		assertFalse(!parser.optionMatch("option1", "o1"));
	}
	
	@Test
	public void underscoreRangeUnchangedTwo() {
		parser.addAll("optiona-a", "o1", "String");
		assertFalse(!parser.optionExists("optiona"));
		assertFalse(!parser.optionMatch("optiona", "o1"));
	}
	
	@Test
	public void groupEndLength() {
		parser.addAll("optionA-CD", "o1", "String");
		assertFalse(parser.optionExists("optionA"));
	}
	
	@Test
	public void groupFrontRange() {
		parser.addAll("option}-z", "o1", "String");
		assertFalse(parser.optionExists("option}"));
	}
	
	@Test
	public void invalidOptionNoShortcut() {
		parser.addAll("option1-%", "String");
		assertFalse(parser.optionExists("1"));
	}
	
	@Test
	public void InvalidOptionNoShortcutTwo() {
		parser.addAll("optionA-B   option1", "String");
		assertFalse(!parser.optionExists("optionB"));
		assertFalse(!parser.optionExists("optionA"));
		assertFalse(!parser.optionExists("option1"));
		assertFalse(parser.optionExists(" "));
	}
	
	// addAll
	@Test
	public void optionExist() {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4", 
				"String Integer Boolean Character");
		assertEquals(parser.optionExists("option1") && parser.optionExists("option2") 
				&& parser.optionExists("option3") && parser.optionExists("option4") ,true);
	}
	
	@Test
	public void optionExistNoShortcut() {
		parser.addAll("option1 option2 option3 option4", "String Integer Boolean Character");
		assertEquals(parser.optionExists("option1") && parser.optionExists("option2") 
				&& parser.optionExists("option3") && parser.optionExists("option4") ,true);
	}
	
	@Test
	public void shortcutExist() {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4", 
				"String Integer Boolean Character");
		assertEquals(parser.shortcutExists("o1") && parser.shortcutExists("o2") 
				&& parser.shortcutExists("o3") && parser.shortcutExists("o4") ,true);
	}
	
	@Test
	public void matchshortcut() {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4", 
				"String Integer Boolean Character");
		assertEquals(parser.optionMatch("option1", "o1") && parser.optionMatch("option2", "o2")
				&& parser.optionMatch("option3", "o3") && parser.optionMatch("option4", "o4"), true);
	}
	
	@Test
	public void matchType() {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4", 
				"String Integer Boolean Character");
		assertEquals(parser.getType("option1"), Type.STRING);
		assertEquals(parser.getType("option2"), Type.INTEGER);
		assertEquals(parser.getType("option3"), Type.BOOLEAN);
		assertEquals(parser.getType("option4"), Type.CHARACTER);
	}
	
	@Test
	public void matchTypeNoShortcut() {
		parser.addAll("option1 option2 option3 option4", "String Integer Boolean Character");
		assertEquals(parser.getType("option1"), Type.STRING);
		assertEquals(parser.getType("option2"), Type.INTEGER);
		assertEquals(parser.getType("option3"), Type.BOOLEAN);
		assertEquals(parser.getType("option4"), Type.CHARACTER);
	}
	
	@Test
	public void matchExtraType() {
		parser.addAll("option5", "Integer String");
		assertEquals(parser.getType("option5"), Type.INTEGER);
	}
	
	@Test
	public void valueValid() {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4", 
				"String Integer Boolean Character");
		parser.parse("-o1=test -o2=33 -o3=true --option4=a");
		assertEquals(parser.getString("option1"), "test");
		assertEquals(parser.getInteger("option2"), 33);
		assertEquals(parser.getBoolean("option3"), true);
		assertEquals(parser.getCharacter("option4"), 'a');
	}
	
	@Test
	public void getValueByShortcut() {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4", 
				"String Integer Boolean Character");
		parser.parse("-o1=test -o2=33 -o3=true --option4=a");
		assertEquals(parser.getString("o1"), "test");
		assertEquals(parser.getInteger("o2"), 33);
		assertEquals(parser.getBoolean("o3"), true);
		assertEquals(parser.getCharacter("o4"), 'a');
	}
	
	@Test
	public void optionExtraSpace() {
		parser.addAll("option1 option2   ","o1 o2", "String Integer");
		parser.parse("-o1=test -o2=33");
		assertFalse(parser.optionExists(" "));
		assertFalse(parser.shortcutExists(" "));
	}
	
	@Test
	public void shortcutExtraSpace() {
		parser.addAll("option1 option2","o1 o2   ", "String Integer");
		parser.parse("-o1=test -o2=33");
		assertFalse(parser.optionExists(" "));
		assertFalse(parser.shortcutExists(" "));
	}
	
	@Test
	public void typeExtraSpace() {
		parser.addAll("option1 option2","o1 o2", "String Integer   ");
		parser.parse("-o1=test -o2=33");
		assertFalse(parser.optionExists(" "));
		assertFalse(parser.shortcutExists(" "));
	}
	
	@Test
	public void optionNull() {
		parser.addAll(null,"o1 o2", "String Integer");
		assertEquals(parser.shortcutExists("o1"), false);
		assertEquals(parser.shortcutExists("o2"), false);
	}
	
	@Test
	public void optionEmpty() {
		parser.addAll("","o1 o2", "String Integer");
		assertEquals(parser.shortcutExists("o1"), false);
		assertEquals(parser.shortcutExists("o2"), false);
	}
	
	@Test
	public void optionNullNoShortcut() {
		parser.addAll(null, "String Integer");
		assertEquals(parser.shortcutExists("o1"), false);
		assertEquals(parser.shortcutExists("o2"), false);
	}
	
	@Test
	public void optionEmptyNoShortcut() {
		parser.addAll("", "String Integer");
		assertEquals(parser.shortcutExists("o1"), false);
		assertEquals(parser.shortcutExists("o2"), false);
	}
	
	@Test
	public void shortcutNull() {
		parser.addAll("option1 option2", null, "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option2"), true);
	}
	
	@Test
	public void shortcutEmpty() {
		parser.addAll("option1 option2", "", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option2"), true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeNull() {
		parser.addAll("option1 option2" ,"o1 o2", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeEmpty() {
		parser.addAll("option1 option2" ,"o1 o2", "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeInvalid() {
		parser.addAll("option1 option2" ,"o1 o2", "not a type");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeNotype() {
		parser.addAll("option1 option2" ,"o1 o2", "Notype");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeNullNoShortcut() {
		parser.addAll("option1 option2", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeEmptyNoShortcut() {
		parser.addAll("option1 option2", "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeInvalidNoShortcut() {
		parser.addAll("option1 option2", "not a type");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void typeNotypeNoShortcut() {
		parser.addAll("option1 option2", "Notype");
	}
	
	@Test
	public void moreOption() {
		parser.addAll("option1 option2 option3 option4","o1 o2", 
				"String Integer Boolean Character");
		assertFalse(!parser.optionExists("option3"));
		assertFalse(!parser.optionExists("option4"));
		assertFalse(!parser.shortcutExists("o2"));
		assertFalse(parser.shortcutExists("o3"));
	}
	
	@Test
	public void lessOption() {
		parser.addAll("option1 option2","o1 o2 o3 o4", "String Integer Boolean Character");
		assertFalse(parser.optionExists("option3"));
		assertFalse(parser.shortcutExists("o3"));
		assertFalse(parser.shortcutExists("o4"));
	}
	
	@Test
	public void lessShortcut() {
		parser.addAll("option1 option2","o1", "String Integer");
		parser.parse("-o1=1");
		parser.parse("--option2=2");
		assertFalse(!parser.shortcutExists("o1"));
		assertFalse(parser.optionExists("option3"));
		assertFalse(!parser.optionExists("option2"));
		assertEquals("1",parser.getString("option1"));
		assertEquals(2,parser.getInteger("option2"));
	}
	
	@Test
	public void moreShortcut() {
		parser.addAll("option1","o1 o2 o3", "String");
		assertFalse(parser.optionExists("option3"));
		assertFalse(parser.optionExists("option2"));
		assertFalse(!parser.shortcutExists("o1"));
	}
	
	@Test
	public void moreType() {
		parser.addAll("option1 option2","o1 o2 o3 o4", "String Integer Boolean Character");
		assertEquals(parser.getType("option2"), Type.INTEGER);
		assertEquals(parser.getType("option1"), Type.STRING);
	}
	
	@Test
	public void lessType() {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4", "String Integer");
		assertEquals(parser.getType("option1"), Type.STRING);
		assertEquals(parser.getType("option2"), Type.INTEGER);
		assertEquals(parser.getType("option3"), Type.INTEGER);
		assertEquals(parser.getType("option4"), Type.INTEGER);
	}
	
	@Test
	public void optionNameInvalid() {
		parser.addAll("option1 option%", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option%"), false);
		assertEquals(parser.shortcutExists("o2"), false);
	}
	
	@Test
	public void shortcutInvalid() {
		parser.addAll("option1 option2", "o# o2", "String Integer");
		assertEquals(parser.shortcutExists("o2"), true);
		assertEquals(parser.shortcutExists("o#"), false);
	}
	
	@Test
	public void optionNameInvalidTwo() {
		parser.addAll("option1 option\'", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option\'"), false);
		assertEquals(parser.shortcutExists("o2"), false);
	}
	
	@Test
	public void shortcutInvalidTwo() {
		parser.addAll("option1 option2", "o'/ o2", "String Integer");
		assertEquals(parser.shortcutExists("o2"), true);
		assertEquals(parser.shortcutExists("o'/"), false);
	}
	
	@Test
	public void optionNameInvalidThree() {
		parser.addAll("option1 option\"", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option\""), false);
		assertEquals(parser.shortcutExists("o2"), false);
	}
	
	@Test
	public void shortcutInvalidThree() {
		parser.addAll("option1 option2", "o\" o2", "String Integer");
		assertEquals(parser.shortcutExists("o2"), true);
		assertEquals(parser.shortcutExists("o\""), false);
	}
	
	@Test
	public void sameOption() {
		parser.addAll("option1 option1", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.shortcutExists("o2"), true);
	}
	
	// group
	@Test
	public void groupInitMatch() {
		parser.addAll("option7-9 optiona-c", "o7-12", "Integer String");
		assertEquals(parser.optionMatch("option7", "o7") && parser.optionMatch("option8", "o8")
				&& parser.optionMatch("option9", "o9") && parser.optionMatch("optiona", "o10")
				&& parser.optionMatch("optionb", "o11") && parser.optionMatch("optionc", "o12"), true);
	}
	
	@Test
	public void groupInitValue() {
		parser.addAll("option7-9 optiona-c optionA-B", "Integer String");
		parser.parse("--option7=1 --option8=2 --option9=3");
		parser.parse("--optiona=Test --optionb=TestOption --optionc=TestAgain");
		parser.parse("--optionA=Test --optionB=TestOption");
		assertEquals(parser.getInteger("option7"), 1);
		assertEquals(parser.getInteger("option8"), 2);
		assertEquals(parser.getInteger("option9"), 3);
		assertEquals(parser.getString("optiona"), "Test");
		assertEquals(parser.getString("optionb"), "TestOption");
		assertEquals(parser.getString("optionc"), "TestAgain");
		assertEquals(parser.getString("optionA"), "Test");
		assertEquals(parser.getString("optionB"), "TestOption");
	}
	
	@Test
	public void groupInitValueByShortcut() {
		parser.addAll("option7-9 optiona-c", "o7-12", "Integer String");
		parser.parse("-o7=1 -o8=2 -o9=3");
		parser.parse("-o10=Test -o11=TestOption -o12=TestAgain");
		assertEquals(parser.getInteger("option7"), 1);
		assertEquals(parser.getInteger("option8"), 2);
		assertEquals(parser.getInteger("option9"), 3);
		assertEquals(parser.getString("optiona"), "Test");
		assertEquals(parser.getString("optionb"), "TestOption");
		assertEquals(parser.getString("optionc"), "TestAgain");
	}
	
	@Test
	public void groupLessShortcut() {
		parser.addAll("option7-9 optiona-c optionA-B", "o7-12", "Integer String Boolean");
		assertEquals(parser.shortcutExists("o7"), true);
		assertEquals(parser.shortcutExists("o8"), true);
		assertEquals(parser.shortcutExists("o9"), true);
		assertEquals(parser.shortcutExists("o10"), true);
		assertEquals(parser.shortcutExists("o11"), true);
		assertEquals(parser.shortcutExists("o12"), true);
	}
	
	@Test
	public void groupMoreShortcut() {
		parser.addAll("option7-9", "o7-12", "Integer");
		assertEquals(parser.shortcutExists("o7"), true);
		assertEquals(parser.shortcutExists("o8"), true);
		assertEquals(parser.shortcutExists("o9"), true);
	}
	
	@Test
	public void groupInitLessType() {
		parser.addAll("option7-9 optiona-c optionA-B", "o7-12", "Integer String");
		assertEquals(parser.getType("option7"), Type.INTEGER);
		assertEquals(parser.getType("option8"), Type.INTEGER);
		assertEquals(parser.getType("option9"), Type.INTEGER);
		assertEquals(parser.getType("optiona"), Type.STRING);
		assertEquals(parser.getType("optionb"), Type.STRING);
		assertEquals(parser.getType("optionc"), Type.STRING);
		assertEquals(parser.getType("optionA"), Type.STRING);
		assertEquals(parser.getType("optionB"), Type.STRING);
	}
	
	@Test
	public void groupInitMoreType() {
		parser.addAll("option7-9 optiona-c optionA-B", "o7-12", "Integer String Boolean Character");
		assertEquals(parser.getType("option7"), Type.INTEGER);
		assertEquals(parser.getType("option8"), Type.INTEGER);
		assertEquals(parser.getType("option9"), Type.INTEGER);
		assertEquals(parser.getType("optiona"), Type.STRING);
		assertEquals(parser.getType("optionb"), Type.STRING);
		assertEquals(parser.getType("optionc"), Type.STRING);
		assertEquals(parser.getType("optionA"), Type.BOOLEAN);
		assertEquals(parser.getType("optionB"), Type.BOOLEAN);
	}
	
	@Test
	public void singleGroupInitMatch() {
		parser.addAll("option7-9", "o7-12", "Integer");
		assertEquals(parser.optionMatch("option7", "o7") && parser.optionMatch("option8", "o8")
				&& parser.optionMatch("option9", "o9") , true);
		assertEquals(parser.shortcutExists("o10"), false);
	}
	
	@Test
	public void multiplegroupOptionGroupShortcut() {
		parser.addAll("option7-9 optiona-c", "o7-19 o10-12", "Integer");
		assertEquals(parser.optionMatch("option7", "o7") && parser.optionMatch("option8", "o8")
				&& parser.optionMatch("option9", "o9") && parser.optionMatch("optiona", "o10" ) 
				&& parser.optionMatch("optionb", "o11") && parser.optionMatch("optionc", "o12") , true);
	}
	
	@Test
	public void groupOptionMatchSingleShortcut() {
		parser.addAll("option7-9 optiona-c", "o7 o8 o9 o10 o11 o12", "Integer String Boolean");
		assertEquals(parser.optionMatch("option7", "o7") && parser.optionMatch("option8", "o8")
				&& parser.optionMatch("option9", "o9") && parser.optionMatch("optiona", "o10")
				&& parser.optionMatch("optionb", "o11") && parser.optionMatch("optionc", "o12"), true);
	}
	
	@Test
	public void singleOptionMatchGroupShortcut() {
		parser.addAll("option7 option8 option9 optiona optionb optionc", "o7-12", "Integer String Boolean");
		assertEquals(parser.optionMatch("option7", "o7") && parser.optionMatch("option8", "o8")
				&& parser.optionMatch("option9", "o9") && parser.optionMatch("optiona", "o10")
				&& parser.optionMatch("optionb", "o11") && parser.optionMatch("optionc", "o12"), true);
	}
	
	
	@Test
	public void optionInitDesc() {
		parser.addAll("option3-1 optionc-a optionB-A", "o1-8", "String Integer Boolean");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option2"), true);
		assertEquals(parser.optionExists("option3"), true);
		assertEquals(parser.optionExists("optionc"), true);
		assertEquals(parser.optionExists("optionb"), true);
		assertEquals(parser.optionExists("optiona"), true);
		assertEquals(parser.optionExists("optionA"), true);
		assertEquals(parser.optionExists("optionB"), true);
		assertEquals(parser.optionMatch("option3", "o1") && parser.optionMatch("option2", "o2")
				&& parser.optionMatch("option1", "o3") && parser.optionMatch("optionc", "o4")
				&& parser.optionMatch("optionb", "o5") && parser.optionMatch("optiona", "o6")
				&& parser.optionMatch("optionB", "o7") && parser.optionMatch("optionA", "o8"), true);
	}
	
	@Test
	public void shortcutInitDesc() {
		parser.addAll("option1-3 optionA-B", "o5-1", "String Integer Boolean");
		assertEquals(parser.optionMatch("option1", "o5") && parser.optionMatch("option2", "o4")
				&& parser.optionMatch("option3", "o3") && parser.optionMatch("optionA", "o2")
				&& parser.optionMatch("optionB", "o1"), true);
	
	}
	
	@Test
	public void fakeGroupOption() {
		parser.addAll("option1 -", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("-"), false);
	}
	
	@Test
	public void fakeOptionEnd() {
		parser.addAll("option1 option-", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option-"), false);
	}
	
	@Test
	public void fakeOptionInitFront() {
		parser.addAll("option1 -n", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("-n"), false);
	}
	
	@Test
	public void fakeOptionInitWrongRange() {
		parser.addAll("option1 option2-3-4", "o1 o2", "String Integer");
		assertEquals(parser.optionExists("option1"), true);
		assertEquals(parser.optionExists("option2"), false);
		assertEquals(parser.optionExists("option2-3-4"), false);
	}
	
	@Test
	public void fakeGroupShortcut() {
		parser.addAll("option1-3", "-", "String");
		assertEquals(parser.shortcutExists("-"), false);
	}
	
	@Test
	public void fakeShortcutEnd() {
		parser.addAll("option1-3", "o1 o-", "String");
		assertEquals(parser.shortcutExists("o1"), true);
		assertEquals(parser.shortcutExists("o-"), false);
	}
	
	@Test
	public void fakeShortcutInitFront() {
		parser.addAll("option1-3", "o1 -o", "String");
		assertEquals(parser.shortcutExists("o1"), true);
		assertEquals(parser.shortcutExists("-o"), false);
	}
	
	@Test
	public void fakeShortcutInitWrongRange() {
		parser.addAll("option1-4", "o1 o1-2-3", "String");
		assertEquals(parser.shortcutExists("o1"), true);
		assertEquals(parser.shortcutExists("o1-2-3"), false);
	}
	
	// if option is optiona-cdd, the new parser consider this option invalid
	// because the part after - is not a letter
	@Test
	public void invalidLetterStringOption() {
		parser.addAll("optiona-cdd", "o1", "String");
		assertEquals(parser.optionExists("optiona-cdd"), false);
	}
	
	@Test
	public void invalidLetterStringShortcut() {
		parser.addAll("optiona", "oa-cdd", "String");
		assertEquals(parser.shortcutExists("oa-cdd"), false);
	}
	
	@Test
	public void invalidGroupName() {
		parser.addAll("option%-c", "o1", "String");
		assertEquals(parser.optionExists("option%-c"), false);
	}
	
	@Test
	public void invalidGroupShortcutName() {
		parser.addAll("optiona-c", "o%-3", "String");
		assertEquals(parser.shortcutExists("o%-3"), false);
	}
	
	@Test
	public void fakeGroupInvalidEnd() {
		parser.addAll("optiona-%", "o1", "String");
		assertEquals(parser.optionExists("optiona-%"), false);
	}
	
	@Test
	public void fakeShortcutInvalidEnd() {
		parser.addAll("optiona-c", "oa-%", "String");
		assertEquals(parser.shortcutExists("oa-%"), false);
	}
	
	@Test
	public void differentNumberWithLetter() {
		parser.addAll("optiona-1", "o1", "String");
		assertEquals(parser.optionExists("optiona-1"), false);
	}
	
	@Test
	public void differentNumberWithLetterShortcut() {
		parser.addAll("optiona-c", "oa-1", "String");
		assertEquals(parser.shortcutExists("oa-1"), false);
	}
	
	@Test
	public void differentNumberWithLetterTwo() {
		parser.addAll("option1-a", "o1", "String");
		assertEquals(parser.optionExists("option1-a"), false);
	}
	
	@Test
	public void differentNumberWithLetterShortcutTwo() {
		parser.addAll("optiona-c", "o1-a", "String");
		assertEquals(parser.shortcutExists("o1-a"), false);
	}
	
	@Test
	public void differentLetterWithLetter() {
		parser.addAll("optiona-C", "o1", "String");
		assertEquals(parser.optionExists("optiona-C"), false);
	}
	
	@Test
	public void differentLetterWithLetterShortcut() {
		parser.addAll("optiona-c", "oa-C", "String");
		assertEquals(parser.shortcutExists("oa-C"), false);
	}
	
	@Test
	public void differentLetterWithLetterTwo() {
		parser.addAll("optionA-c", "o1", "String");
		assertEquals(parser.optionExists("optionA-c"), false);
	}
	
	@Test
	public void differentLetterWithLetterShortcutTwo() {
		parser.addAll("optiona-c", "oA-c", "String");
		assertEquals(parser.shortcutExists("oA-c"), false);
	}
	
	@Test
	public void matchShortcutWithInvalidOption() {
		parser.addAll("optiona-C option4-5 option%", "o1 o2 o3", "String");
		assertEquals(parser.optionMatch("option4", "o2"), true);
		assertEquals(parser.shortcutExists("o1"), false);
		assertEquals(parser.shortcutExists("o3"), false);
	}
	
	@Test
	public void matchShortcutWithInvalidOptionTwo() {
		parser.addAll("optiona-C option4-5 option%", "o1 o2 o3 o4", "String");
		assertEquals(parser.optionMatch("option4", "o2"), true);
		assertEquals(parser.optionMatch("option5", "o4"), true);
		assertEquals(parser.shortcutExists("o1"), false);
		assertEquals(parser.shortcutExists("o3"), false);
	}
	
	@Test
	public void matchTypeWithInvalidOption() {
		parser.addAll("optiona-C option4-5 option", "o1 o2 o3", "String Integer Boolean");
		assertEquals(parser.getType("option4"), Type.INTEGER);
		assertEquals(parser.getType("option5"), Type.INTEGER);
		assertEquals(parser.getType("option"), Type.BOOLEAN);
	}
	
	// if optiona-a, value unchanged, then same optiona once
	@Test
	public void matchOptionRangeUnchange() {
		parser.addAll("optiona-a", "o1 o2", "String");
		assertEquals(parser.optionMatch("optiona", "o1"), true);
		assertEquals(parser.optionExists("optiona"), true);
		assertEquals(parser.optionExists("optiona-a"), false);
		assertEquals(parser.shortcutExists("o2a"), false);
	}
	
	@Test
	public void shortcutRangeUnchange() {
		parser.addAll("optiona-c", "o1-1", "String");
		assertEquals(parser.optionMatch("optiona", "o1"), true);
		assertEquals(parser.shortcutExists("o1"), true);
		assertEquals(parser.shortcutExists("o1-1"), false);
	}
	
	@Test
	public void addAllUnderscore() {
		parser.addAll("___option7-9","o1","Integer");
		assertEquals(parser.optionExists("___option7"), true);
		assertEquals(parser.optionExists("___option8"), true);
		assertEquals(parser.optionExists("___option9"), true);
	}
	
	@Test
	public void addAllUnderscoreShortcut() {
		parser.addAll("option7-9","___o7-9","Integer");
		assertEquals(parser.shortcutExists("___o7"), true);
		assertEquals(parser.shortcutExists("___o8"), true);
		assertEquals(parser.shortcutExists("___o9"), true);
	}
}
