/*
 * Thanks contribution of Patrick van Bergen 
 * http://techblog.procurios.nl/k/news/view/14605/14863/how-do-i-write-my-own-parser-(for-json).html
 */

package it.classhidra.core.tool.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



public class util_json_parser {
	public final static int TOKEN_NONE = 0;
	public final static int TOKEN_CURLY_OPEN = 1;
	public final static int TOKEN_CURLY_CLOSE = 2;
	public final static int TOKEN_SQUARED_OPEN = 3;
	public final static int TOKEN_SQUARED_CLOSE = 4;
	public final static int TOKEN_COLON = 5;
	public final static int TOKEN_COMMA = 6;
	public final static int TOKEN_STRING = 7;
	public final static int TOKEN_NUMBER = 8;
	public final static int TOKEN_TRUE = 9;
	public final static int TOKEN_FALSE = 10;
	public final static int TOKEN_NULL = 11;

	private final static int BUILDER_CAPACITY = 2000;

	private static int index;
	private static boolean success;

	public static Object decode(String json){
//		if(json==null || json.trim().length()==0)
//			return null;
//		success = true;
//		int start = json.indexOf('{');
//		if(start==-1)
//			return null;
//		else if(start>0)
//			json=json.substring(start,json.length());
//		json=json.trim();
		return decode2(json);
	}
	
	public static String encode(Object json){
		StringBuilder builder = new StringBuilder(BUILDER_CAPACITY);
		boolean success = serializeValue(json, builder);
		return (success ? builder.toString() : null);
	}	

	public static Object decode2(String json){
		success = true;
		if (json != null) {
			char[] charArray = json.toCharArray();
			index = 0;
			Object value = parseValue(charArray);
			return value;
		} else {
			return null;
		}
	}


	private static HashMap<String,Object> parseObject(char[] json){
		HashMap<String,Object> table = new HashMap<String, Object>();
		int token;


		nextToken(json);

		boolean done = false;
		while (!done) {
			token = headToken(json, index);
			if (token == util_json_parser.TOKEN_NONE) {
				success = false;
				return null;
			} else if (token == util_json_parser.TOKEN_COMMA) {
				nextToken(json);
			} else if (token == util_json_parser.TOKEN_CURLY_CLOSE) {
				nextToken(json);
				return table;
			} else {

				// name
				String name = parseString(json);
				if (!success) {
					success = false;
					return null;
				}

				// :
				token = nextToken(json);
				if (token != util_json_parser.TOKEN_COLON) {
					success = false;
					return null;
				}

				// value
				Object value = parseValue(json);
				if (!success) {
					success = false;
					return null;
				}

				table.put(name,value);
			}
		}

		return table;
	}

	private static ArrayList<Object> parseArray(char[] json){
		ArrayList<Object> array = new ArrayList<Object>();

		// [
		nextToken(json);

		boolean done = false;
		while (!done) {
			int token = headToken(json, index);
			if (token == util_json_parser.TOKEN_NONE) {
				success = false;
				return null;
			} else if (token == util_json_parser.TOKEN_COMMA) {
				nextToken(json);
			} else if (token == util_json_parser.TOKEN_SQUARED_CLOSE) {
				nextToken(json);
				break;
			} else {
				Object value = parseValue(json);
				if (!success) {
					return null;
				}

				array.add(value);
			}
		}

		return array;
	}

	private static Object parseValue(char[] json){
		switch (headToken(json, index)) {
			case util_json_parser.TOKEN_STRING:
				return parseString(json);
			case util_json_parser.TOKEN_NUMBER:
				return parseNumber(json)[0];
			case util_json_parser.TOKEN_CURLY_OPEN:
				return parseObject(json);
			case util_json_parser.TOKEN_SQUARED_OPEN:
				return parseArray(json);
			case util_json_parser.TOKEN_TRUE:
				nextToken(json);
				return true;
			case util_json_parser.TOKEN_FALSE:
				nextToken(json);
				return false;
			case util_json_parser.TOKEN_NULL:
				nextToken(json);
				return null;
			case util_json_parser.TOKEN_NONE:
				break;
		}

		success = false;
		return null;
	}

	private static String parseString(char[] json){
		StringBuilder s = new StringBuilder(BUILDER_CAPACITY);
		char c;

		whitespace(json);

		// "
		c = json[index++];

		boolean complete = false;
		while (!complete) {

			if (index == json.length) {
				break;
			}

			c = json[index++];
			if (c == '"') {
				complete = true;
				break;
			} else if (c == '\\') {

				if (index == json.length) {
					break;
				}
				c = json[index++];
				if (c == '"') {
					s.append('"');
				} else if (c == '\\') {
					s.append('\\');
				} else if (c == '/') {
					s.append('/');
				} else if (c == 'b') {
					s.append('\b');
				} else if (c == 'f') {
					s.append('\f');
				} else if (c == 'n') {
					s.append('\n');
				} else if (c == 'r') {
					s.append('\r');
				} else if (c == 't') {
					s.append('\t');
				} else if (c == 'u') {
					int remainingLength = json.length - index;
					if (remainingLength >= 4) {
						// parse the 32 bit hex into an integer codepoint
						int codePoint = 0;
						try{
//							codePoint = Integer.valueOf(new String(json).substring(index,4));
							codePoint = Integer.valueOf(new String(json).substring(index,index+4));
						}catch(Exception e){
							return "";
						}
						// convert the integer codepoint to a unicode char and add to string
						s.append(String.valueOf(codePoint));
						// skip 4 chars
						index += 4;
					} else {
						break;
					}
				}

			} else {
				s.append(c);
			}

		}

		if (!complete) {
			success = false;
			return null;
		}

		return s.toString();
	}

	private static Object[] parseNumber(char[] json){
		Object[] result = new Object[]{0};
		whitespace(json);

		int lastIndex = lastIndexOfNumber(json, index);
		int charLength = (lastIndex - index) + 1;

		success=false;
		try{
			String value = new String(json).substring(index,index+charLength);
			if(value.indexOf('.')==-1){
				if(!success){
					try{
						int number = Integer.valueOf(value);
						success=true;
						result[0] = number;
					}catch(Exception e){			
					}
				}
			}else{
				if(!success){
					try{
						double number = Double.valueOf(value);
						success=true;
						result[0] = number;
					}catch(Exception e){			
					}
				}
			}
			if(!success){
				try{
					double number = Double.valueOf(value);
					success=true;
					result[0] = number;
				}catch(Exception e){			
				}
			}
		}catch(Exception ex){
		}
		
//		success = Double.TryParse(new String(json, index, charLength), NumberStyles.Any, CultureInfo.InvariantCulture, out number);

		index = lastIndex + 1;
		return result;
	}

	private static int lastIndexOfNumber(char[] json, int index){
		int lastIndex;

		for (lastIndex = index; lastIndex < json.length; lastIndex++) {
			if ("0123456789+-.eE".indexOf(json[lastIndex]) == -1) {
				break;
			}
		}
		return lastIndex - 1;
	}

	private static void whitespace(char[] json){
		for (; index < json.length; index++) {
			if (" \t\n\r".indexOf(json[index]) == -1) {
				break;
			}
		}
	}

	private static int headToken(char[] json, int index)
	{
		int saveIndex = index;
		int[] pair = nextToken(json, saveIndex);
		index = pair[1];
		return pair[0];
	}

	private static int nextToken(char[] json){
		whitespace(json);

		if (index == json.length) {
			return util_json_parser.TOKEN_NONE;
		}

		char c = json[index];
		index++;
		switch (c) {
			case '{':
				return util_json_parser.TOKEN_CURLY_OPEN;
			case '}':
				return util_json_parser.TOKEN_CURLY_CLOSE;
			case '[':
				return util_json_parser.TOKEN_SQUARED_OPEN;
			case ']':
				return util_json_parser.TOKEN_SQUARED_CLOSE;
			case ',':
				return util_json_parser.TOKEN_COMMA;
			case '"':
				return util_json_parser.TOKEN_STRING;
			case '0': case '1': case '2': case '3': case '4':
			case '5': case '6': case '7': case '8': case '9':
			case '-':
				return util_json_parser.TOKEN_NUMBER;
			case ':':
				return util_json_parser.TOKEN_COLON;
		}
		index--;

		int remainingLength = json.length - index;

		// false
		if (remainingLength >= 5) {
			if (json[index] == 'f' &&
				json[index + 1] == 'a' &&
				json[index + 2] == 'l' &&
				json[index + 3] == 's' &&
				json[index + 4] == 'e') {
				index += 5;
				return util_json_parser.TOKEN_FALSE;
			}
		}

		// true
		if (remainingLength >= 4) {
			if (json[index] == 't' &&
				json[index + 1] == 'r' &&
				json[index + 2] == 'u' &&
				json[index + 3] == 'e') {
				index += 4;
				return util_json_parser.TOKEN_TRUE;
			}
		}

		// null
		if (remainingLength >= 4) {
			if (json[index] == 'n' &&
				json[index + 1] == 'u' &&
				json[index + 2] == 'l' &&
				json[index + 3] == 'l') {
				index += 4;
				return util_json_parser.TOKEN_NULL;
			}
		}

		return util_json_parser.TOKEN_NONE;
	}
	
	private static int[] nextToken(char[] json, int index2){
		whitespace(json);

		if (index2 == json.length) {
			return new int[]{util_json_parser.TOKEN_NONE,index2};
		}

		char c = json[index2];
		index2++;
		switch (c) {
			case '{':
				return new int[]{util_json_parser.TOKEN_CURLY_OPEN,index2};
			case '}':
				return new int[]{util_json_parser.TOKEN_CURLY_CLOSE,index2};
			case '[':
				return new int[]{util_json_parser.TOKEN_SQUARED_OPEN,index2};
			case ']':
				return new int[]{util_json_parser.TOKEN_SQUARED_CLOSE,index2};
			case ',':
				return new int[]{util_json_parser.TOKEN_COMMA,index2};
			case '"':
				return new int[]{util_json_parser.TOKEN_STRING,index2};
			case '0': case '1': case '2': case '3': case '4':
			case '5': case '6': case '7': case '8': case '9':
			case '-':
				return new int[]{util_json_parser.TOKEN_NUMBER,index2};
			case ':':
				return new int[]{util_json_parser.TOKEN_COLON,index2};
		}
		index2--;

		int remainingLength = json.length - index2;

		// false
		if (remainingLength >= 5) {
			if (json[index2] == 'f' &&
				json[index2 + 1] == 'a' &&
				json[index2 + 2] == 'l' &&
				json[index2 + 3] == 's' &&
				json[index2 + 4] == 'e') {
				index2 += 5;
				return new int[]{util_json_parser.TOKEN_FALSE,index2};
			}
		}

		// true
		if (remainingLength >= 4) {
			if (json[index2] == 't' &&
				json[index2 + 1] == 'r' &&
				json[index2 + 2] == 'u' &&
				json[index2 + 3] == 'e') {
				index2 += 4;
				return new int[]{util_json_parser.TOKEN_TRUE,index2};
			}
		}

		// null
		if (remainingLength >= 4) {
			if (json[index2] == 'n' &&
				json[index2 + 1] == 'u' &&
				json[index2 + 2] == 'l' &&
				json[index2 + 3] == 'l') {
				index2 += 4;
				return new int[]{util_json_parser.TOKEN_NULL,index2};
			}
		}

		return new int[]{util_json_parser.TOKEN_NONE,index2};
	}
	

	private static boolean serializeValue(Object value, StringBuilder builder){
		boolean success = true;

		if (value instanceof String) {
			success = serializeString((String)value, builder);
		} else if (value instanceof HashMap) {
			success = serializeObject((HashMap<?,?>)value, builder);
		} else if (value instanceof ArrayList) {
			success = serializeArray((ArrayList<?>)value, builder);
		} else if ((value instanceof Boolean) && ((Boolean)value == true)) {
			builder.append("true");
		} else if ((value instanceof Boolean) && ((Boolean)value == false)) {
			builder.append("false");
		} else if (value instanceof Number) {
			// thanks to ritchie for pointing out ValueType to me
			builder.append(Double.valueOf(value.toString()));
//			success = SerializeNumber(Convert.ToDouble(value), builder);
		} else if (value == null) {
			builder.append("null");
		} else {
			success = false;
		}
		return success;
	}

	private static boolean serializeObject(HashMap<?,?> anObject, StringBuilder builder){
		builder.append("{");

		Iterator<?> it = anObject.keySet().iterator();
		boolean first = true;
		while (it.hasNext()) {
			String key = (String)it.next();
			Object value = anObject.get(key);

			if (!first) {
				builder.append(", ");
			}

			serializeString(key, builder);
			builder.append(":");
			if (!serializeValue(value, builder)) {
				return false;
			}

			first = false;
		}

		builder.append("}");
		return true;
	}

	private static boolean serializeArray(ArrayList<?> anArray, StringBuilder builder){
		builder.append("[");

		boolean first = true;
		for (int i = 0; i < anArray.size(); i++) {
			Object value = anArray.get(i);

			if (!first) {
				builder.append(", ");
			}

			if (!serializeValue(value, builder)) {
				return false;
			}

			first = false;
		}

		builder.append("]");
		return true;
	}

	private static boolean serializeString(String aString, StringBuilder builder){
		builder.append("\"");

		char[] charArray = aString.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (c == '"') {
				builder.append("\\\"");
			} else if (c == '\\') {
				builder.append("\\\\");
			} else if (c == '\b') {
				builder.append("\\b");
			} else if (c == '\f') {
				builder.append("\\f");
			} else if (c == '\n') {
				builder.append("\\n");
			} else if (c == '\r') {
				builder.append("\\r");
			} else if (c == '\t') {
				builder.append("\\t");
			} else {
				int codepoint = Character.getNumericValue(c);
				if ((codepoint >= 32) && (codepoint <= 126)) {
					builder.append(c);
				} else {
					builder.append("\\u" + "0000"+String.valueOf(codepoint));
				}
			}
		}

		builder.append("\"");
		return true;
	}

 
}
