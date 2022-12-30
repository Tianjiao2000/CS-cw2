package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private OptionMap optionMap;
	
	public Parser() {
		optionMap = new OptionMap();
	}
	
	public void addOption(Option option, String shortcut) {
		optionMap.store(option, shortcut);
	}
	
	public void addOption(Option option) {
		optionMap.store(option, "");
	}
	
	public boolean optionExists(String key) {
		return optionMap.optionExists(key);
	}
	
	public boolean shortcutExists(String key) {
		return optionMap.shortcutExists(key);
	}
	
	public boolean optionOrShortcutExists(String key) {
		return optionMap.optionOrShortcutExists(key);
	}
	
	public int getInteger(String optionName) {
		String value = getString(optionName);
		Type type = getType(optionName);
		int result;
		switch (type) {
			case STRING:
			case INTEGER:
				try {
					result = Integer.parseInt(value);
				} catch (Exception e) {
			        try {
			            new BigInteger(value);
			        } catch (Exception e1) {
			        }
			        result = 0;
			    }
				break;
			case BOOLEAN:
				result = getBoolean(optionName) ? 1 : 0;
				break;
			case CHARACTER:
				result = (int) getCharacter(optionName);
				break;
			default:
				result = 0;
		}
		return result;
	}
	
	public boolean getBoolean(String optionName) {
		String value = getString(optionName);
		return !(value.toLowerCase().equals("false") || value.equals("0") || value.equals(""));
	}
	
	public String getString(String optionName) {
		return optionMap.getValue(optionName);
	}
	
	public char getCharacter(String optionName) {
		String value = getString(optionName);
		return value.equals("") ? '\0' :  value.charAt(0);
	}
	
	public void setShortcut(String optionName, String shortcutName) {
		optionMap.setShortcut(optionName, shortcutName);
	}
	
	public void replace(String variables, String pattern, String value) {
			
		variables = variables.replaceAll("\\s+", " ");
		
		String[] varsArray = variables.split(" ");
		
		for (int i = 0; i < varsArray.length; ++i) {
			String varName = varsArray[i];
			String var = (getString(varName));
			var = var.replace(pattern, value);
			if(varName.startsWith("--")) {
				String varNameNoDash = varName.substring(2);
				if (optionMap.optionExists(varNameNoDash)) {
					optionMap.setValueWithOptionName(varNameNoDash, var);
				}
			} else if (varName.startsWith("-")) {
				String varNameNoDash = varName.substring(1);
				if (optionMap.shortcutExists(varNameNoDash)) {
					optionMap.setValueWithOptionShortcut(varNameNoDash, var);
				} 
			} else {
				if (optionMap.optionExists(varName)) {
					optionMap.setValueWithOptionName(varName, var);
				}
				if (optionMap.shortcutExists(varName)) {
					optionMap.setValueWithOptionShortcut(varName, var);
				} 
			}

		}
	}
	
	private List<CustomPair> findMatches(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    List<CustomPair> pairs = new ArrayList<CustomPair>();
	    while (matcher.find()) {
	    	CustomPair pair = new CustomPair(matcher.start(), matcher.end());
	    	pairs.add(pair);
	    }
	    return pairs;
	}
	
	
	public int parse(String commandLineOptions) {
		if (commandLineOptions == null) {
			return -1;
		}
		int length = commandLineOptions.length();
		if (length == 0) {
			return -2;
		}	
		
		List<CustomPair> singleQuotePairs = findMatches(commandLineOptions, "(?<=\')(.*?)(?=\')");
		List<CustomPair> doubleQuote = findMatches(commandLineOptions, "(?<=\")(.*?)(?=\")");
		List<CustomPair> assignPairs = findMatches(commandLineOptions, "(?<=\\=)(.*?)(?=[\\s]|$)");
		
		
		for (CustomPair pair : singleQuotePairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
	    	
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);
		}
		
		for (CustomPair pair : doubleQuote) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\'", "{S_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
			
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}
		
		for (CustomPair pair : assignPairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll("\'", "{S_QUOTE}").
					  replaceAll("-", "{DASH}");
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}

		commandLineOptions = commandLineOptions.replaceAll("--", "-+").replaceAll("\\s+", " ");


		String[] elements = commandLineOptions.split("-");
		
		
		for (int i = 0; i < elements.length; ++i) {
			String entry = elements[i];
			
			if(entry.isBlank()) {
				continue;
			}

			String[] entrySplit = entry.split("[\\s=]", 2);
			
			boolean isKeyOption = entry.startsWith("+");
			String key = entrySplit[0];
			key = isKeyOption ? key.substring(1) : key;
			String value = "";
			
			if(entrySplit.length > 1 && !entrySplit[1].isBlank()) {
				String valueWithNoise = entrySplit[1].trim();
				value = valueWithNoise.split(" ")[0];
			}
			
			// Explicitly convert boolean.
			if (getType(key) == Type.BOOLEAN && (value.toLowerCase().equals("false") || value.equals("0"))) {
				value = "";
			}
			
			value = value.replace("{S_QUOTE}", "\'").
						  replace("{D_QUOTE}", "\"").
						  replace("{SPACE}", " ").
						  replace("{DASH}", "-").
						  replace("{EQUALS}", "=");
			
			boolean isUnescapedValueInQuotes = (value.startsWith("\'") && value.endsWith("\'")) ||
					(value.startsWith("\"") && value.endsWith("\""));
			
			value = value.length() > 1 && isUnescapedValueInQuotes ? value.substring(1, value.length() - 1) : value;
			
			if(isKeyOption) {
				optionMap.setValueWithOptionName(key, value);
			} else {
				optionMap.setValueWithOptionShortcut(key, value);
				
			}			
		}

		return 0;
		
	}
	
	/* @param - String message: option message
	 * if the option contains -, this class is responsible for split it
	 * @return - ArrayList with options after split, null if invalid option
	 */
	public ArrayList groupInitialization(String message) {
		String[] tempgroup = message.split("-");
		Character front = ' ';
		String frontString = "";
		String end = "";
		// if option invalid then may cat exception when assigning front and end
		try {
			end = tempgroup[1];
			try {
				front = tempgroup[0].charAt(tempgroup[0].length() - 1);
				frontString = tempgroup[0].substring(0, tempgroup[0].length() - 1);
			} catch (Exception e) {}
		} catch (Exception e) {}
		
		if (tempgroup.length != 2) {
			return null;
		}
		
		ArrayList messageSplit = new ArrayList();
		// when group option is integer
		if ((int) front >= 48 && (int) front <= 57 && end.matches("[0-9]*")) {
			Integer step = Math.abs(((int) front - 48) - Integer.parseInt(end)) + 1;
			Integer i = (int) front - 48;
			if(i == Integer.parseInt(end)) {
				messageSplit.add(frontString + String.valueOf(i));
			} else {
				while (i != Integer.parseInt(end)) {
					messageSplit.add(frontString + String.valueOf(i));
					if ((int) front - 48 < Integer.parseInt(end)) {
						i = i + 1;
					} else {
						i = i - 1;
					}
				}
				messageSplit.add(frontString + String.valueOf(i));
			}
		// when group option is a-z or A-Z
		} else if (((int) front >= 65 
				&& (int) front <= 90 
				&& end.matches("[A-Z]*") 
				&& end.length() == 1) 
				|| ((int) front >= 97 
				&& (int) front <= 122) 
				&& end.matches("[a-z]*") 
				&& end.length() == 1) {
			Integer step = Math.abs(((int) front) - ((int) end.charAt(0))) + 1;
			Character i = front;
			if(i == end.charAt(0)) {
				messageSplit.add(frontString + String.valueOf(i));
			} else {
				while (i != end.charAt(0)) {
					messageSplit.add(frontString + String.valueOf(i));
					if ((int) front < (int) end.charAt(0)) {
						i = (char) ((int) i + 1);
					} else {
						i = (char) ((int) i - 1);
					}
				}
				messageSplit.add(frontString + String.valueOf(i));
			}
		// other cases, means option invalid
		} else {
			return null;
		}
		return messageSplit;
	}
	
	/*
	 * @param - String option: option name
	 * determine whether this option is valid
	 */
	private Boolean isOptionValid(String option) {
		if (option == null) {
    		return false;
    	}
    	if (option.isEmpty()) {
    		return false;
    	}
    	if (!option.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
    		return false;
    	}
		return true;
	}
	
	/*
	 * @param - String option: option name
	 * @param - String shortcut: shortcut
	 * @param - String type: option type
	 * add all options into OptionMap
	 * @thrown - IllegalArgumentException: when option type is invalid
	 */
	public void addAll(String option, String shortcut, String type) {
		// invalid option
		if (option == null || option.isEmpty()) {
			return ;
		}
		// no shortcut, call addAll function without shortcut
		if (shortcut == null || shortcut.isEmpty()) {
			addAll(option, type);
			return ;
		}
		// invalid type, invalid option
		if (type == null) {
			throw new IllegalArgumentException("Illegal argument provided in type.");
		}
		String[] optionTempList = option.split(" ");
		String[] shortcutTempList = shortcut.split(" ");
		String[] typeTempList = type.split(" ");
		List shortcutList = new ArrayList();
		List shortcuts = new ArrayList();
		// split the shortcut first
		for (int i = 0; i < shortcutTempList.length; i++) {
			// group shortcut
			if (shortcutTempList[i].contains("-")) {
				ArrayList smallShortcut = groupInitialization(shortcutTempList[i]);
				if (smallShortcut == null) {
				} else {
					for (int j = 0; j < smallShortcut.size(); j++) {
						shortcuts.add(smallShortcut.get(j));
					}
				}
			} else {
				shortcuts.add(shortcutTempList[i]);
			}
		}
		// if option name invalid, corresponding shortcut is omitted
		Integer index = 0;
		for (int i = 0; i < optionTempList.length; i++) {
			Boolean groupNotValid = false;
			if (optionTempList[i].contains("-")) {
				ArrayList validOption = groupInitialization(optionTempList[i]);
				if (validOption == null) {
					groupNotValid = true;
				}
			} else {
				groupNotValid = !isOptionValid(optionTempList[i]);
			}
			// is option valid is false or groupvalid is false
			if (!groupNotValid) {
				try {
					shortcutList.add(shortcuts.get(i));
				} catch (IndexOutOfBoundsException e) {
					
				}
			}
			index = i;
		}
		// transfer shortcut, extra shortcut omitted
		if (index <= shortcuts.size()) {
			for (int i = index + 1; i < shortcuts.size(); i ++) {
				shortcutList.add(shortcuts.get(i));
			}
		}
		List optionList = new ArrayList();
		List typeList = new ArrayList();
		// split option name
		for (int i = 0; i < optionTempList.length; i++) {
			// group option
			if (optionTempList[i].contains("-")) {
				ArrayList smallOption = groupInitialization(optionTempList[i]);
				if (smallOption == null) {
				} else {
					for (int j = 0; j < smallOption.size(); j++) {
						optionList.add(smallOption.get(j));
						try {
							typeList.add(typeTempList[i]);
						} catch (ArrayIndexOutOfBoundsException e) {
							typeList.add(typeTempList[typeTempList.length - 1]);
						}
					}
				}
			} else {
				// option name valid or not
				if (optionTempList[i] == null || optionTempList[i].isEmpty() || 
						!optionTempList[i].matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
				} else {
					optionList.add(optionTempList[i]);
					try {
						typeList.add(typeTempList[i]);
					} catch (ArrayIndexOutOfBoundsException e) {
						typeList.add(typeTempList[typeTempList.length - 1]);
					}
				}
			}
		}
		// separate shortcut
		if (shortcutList.size() < optionList.size()) {
			for (int i = shortcutList.size(); i < optionList.size(); i++) {
				shortcutList.add("i%m");
			}
		}
		String[] finalOptionList = new String[optionList.size()];
		String[] finalShortcutList = new String[shortcutList.size()];
		String[] finalTypeList = new String[typeList.size()];
		for (int i = 0; i < optionList.size(); i++) {
			finalOptionList[i] = (String) optionList.get(i);
			finalTypeList[i] = (String) typeList.get(i);
		}
		for (int i = 0; i < shortcutList.size(); i++) {
			finalShortcutList[i] = (String) shortcutList.get(i);
		}
		// save options
		for (int i = 0; i < optionList.size(); i++) {
			// if option invalid then omitted
			if (finalOptionList[i] == null || finalOptionList[i].isEmpty() || 
					!finalOptionList[i].matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
				continue;
			}
			// if shortcut invalid then save option as no shortcut
			if (finalShortcutList[i].equals("i%m") || !finalShortcutList[i].matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
				addOption(new Option((finalOptionList[i]), setType(finalTypeList[i])));
			} else {
				// save option with shortcut
				addOption(new Option((finalOptionList[i]), setType(finalTypeList[i])),
						finalShortcutList[i]);
			}
		}
	}
	
	/*
	 * @param - String option: option name
	 * @param = String type: option type
	 * add all options when it has no shortcut
	 */
	public void addAll(String option, String type) {
		// invalid option
		if (option == null || option.isEmpty()) {
			return ;
		}
		// invalid type
		if (type == null) {
			throw new IllegalArgumentException("Illegal argument provided in type.");
		}
		String[] optionTempList = option.split(" ");
		String[] typeTempList = type.split(" ");

		List optionList = new ArrayList();
		List typeList = new ArrayList();
		// split option
		for (int i = 0; i < optionTempList.length; i++) {
			if (optionTempList[i].contains("-")) {
				ArrayList smallOption = groupInitialization(optionTempList[i]);
				if (smallOption == null) {
				} else {
					for (int j = 0; j < smallOption.size(); j++) {
						optionList.add(smallOption.get(j));
						try {
							typeList.add(typeTempList[i]);
						} catch (ArrayIndexOutOfBoundsException e) {
							typeList.add(typeTempList[typeTempList.length - 1]);
						}
					}
				}
			} else {
				if (optionTempList[i] == null || optionTempList[i].isEmpty() || 
						!optionTempList[i].matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
				} else {
					optionList.add(optionTempList[i]);
					try {
						typeList.add(typeTempList[i]);
					} catch (ArrayIndexOutOfBoundsException e) {
						typeList.add(typeTempList[typeTempList.length - 1]);
					}
				}
			}
		}
		String[] finalOptionList = new String[optionList.size()];
		String[] finalTypeList = new String[typeList.size()];
		for (int i = 0; i < optionList.size(); i++) {
			finalOptionList[i] = (String) optionList.get(i);
			finalTypeList[i] = (String) typeList.get(i);
		}
		// add option
		for (int i = 0; i < optionList.size(); i++) {
			if (finalOptionList[i] == null || finalOptionList[i].isEmpty() || 
					!finalOptionList[i].matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
				continue;
			}
			addOption(new Option((finalOptionList[i]), setType(finalTypeList[i])));
		}
	}
	
	/*
	 * @param - String inputType: type of option
	 * transfer the type from string to Type
	 * @thrown - IllegalArgumentException: option type invalid
	 * @return - Type: option type in Type format
	 */
	private Type setType(String inputType) {
		if (inputType.equals("String")) {
			return Type.STRING;
		} else if (inputType.equals("Boolean")) {
			return Type.BOOLEAN;
		} else if (inputType.equals("Integer")) {
			return Type.INTEGER;
		} else if (inputType.equals("Character")) {
			return Type.CHARACTER;
		} else {
			throw new IllegalArgumentException("Illegal argument provided in type.");
		}
	}
	
	public Type getType(String option) {
		Type type = optionMap.getType(option);
		return type;
	}
	
	public boolean optionMatch(String option,String shortcut){
		return optionMap.getShortcut(shortcut).getName().equals(option);
	}
	
	@Override
	public String toString() {
		return optionMap.toString();
	}

	
	private class CustomPair {
		
		CustomPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	    private int x;
	    private int y;
	    
	    public int getX() {
	    	return this.x;
	    }
	    
	    public int getY() {
	    	return this.y;
	    }
	}
}
