package org.amaze.commons.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@SuppressWarnings( "rawtypes" )
public final class StringUtils
{
	private static final ConcurrentHashMap<String, String> camelToUnderScore = new ConcurrentHashMap<String, String>();

	public static final String NEW_LINE = System.getProperty( "line.separator" );

	// function returns all strings values between two number strings.
	// strings must the same length, the resulting string array will contain strings of the same length
	// taking account of leading zeros.
	//
	// eg 0100 - 0105 will return { 0100, 0101, 0102, 0103, 0104, 0105 }
	public static List<String> getRangeList( String from, String to )
	{
		// Validate parameters
		if ( from == null )
			throw new IllegalArgumentException( "The from parameter cannot be null" );
		if ( to == null )
			throw new IllegalArgumentException( "The to parameter cannot be null" );
		if ( from.length() == 0 )
			throw new IllegalArgumentException( "The from parameter cannot be empty" );
		if ( to.length() == 0 )
			throw new IllegalArgumentException( "The to parameter cannot be empty" );
		if ( from.length() != to.length() )
			throw new IllegalArgumentException( "The from and to parameters must be same length" );

		// Build the list of return numbers
		List<String> strs = new ArrayList<String>();
		int strLength = from.length();
		long startNumber = Long.parseLong( from );
		long endNumber = Long.parseLong( to );

		// Sanity check
		if ( startNumber > endNumber )
		{
			long temp = startNumber;
			startNumber = endNumber;
			endNumber = temp;
		}

		for ( long i = startNumber; i <= endNumber; i++ )
		{
			String currentNumberString = Long.toString( i );

			// if the string has been truncated because of leading zeros, pad it out
			if ( currentNumberString.length() < strLength )
			{
				currentNumberString = StringUtils.padLeft( currentNumberString, strLength - currentNumberString.length(), '0' );
			}

			strs.add( currentNumberString );
		}

		return strs;
	}

	public static final int searchStringArray( String[] strings, String searchFor )
	{
		for ( int i = 0; i < strings.length; i++ )
		{
			if ( searchFor.equals( strings[i] ) )
			{
				return i;
			}
		}
		return -1;
	}

	public static final String removeLeading( String string, char removeChar )
	{
		int index = 0;
		int length = string.length();
		while ( ( index < length ) && ( string.charAt( index ) == removeChar ) )
			index++;
		if ( index > 0 )
			return string.substring( index );
		else
			return string;
	}

	public static final String removeTrailing( String string, char removeChar )
	{
		int index = string.length();
		while ( ( index > 0 ) && ( string.charAt( index - 1 ) == removeChar ) )
			index--;
		if ( index < string.length() )
			return string.substring( 0, index );
		else
			return string;
	}

	public static final String removeLeadingTrailing( String string, char removeChar )
	{
		return removeTrailing( removeLeading( string, removeChar ), removeChar );
	}

	public static final String merge( Collection stringList, String delimiter )
	{
		StringBuilder sb = new StringBuilder( 200 );

		int i = 0;
		for ( Object obj : stringList )
		{
			if ( i != 0 )
			{
				sb.append( delimiter );
			}

			if ( obj != null )
				sb.append( obj.toString() );

			i++;
		}
		return sb.toString();
	}

	public static final <T> String merge( String[] strings, T delimiter )
	{
		StringBuilder sb = new StringBuilder( 200 );
		for ( int i = 0; i < strings.length; i++ )
		{
			if ( i != 0 )
			{
				sb.append( delimiter );
			}
			sb.append( strings[i] );
		}
		return sb.toString();
	}

	public static boolean checkCharacters( String str, String validCharacters )
	{
		// null check
		if ( str == null )
			return true;

		// This function returns whether the string is composed entirely of the characters
		// contained in validCharacters
		int strLength = str.length();
		int validCharLength = validCharacters.length();

		for ( int i = 0; i < strLength; i++ )
		{
			boolean matched = false;
			char currentChar = str.charAt( i );
			for ( int j = 0; j < validCharLength; j++ )
			{
				// if character is valid go to the next one
				if ( currentChar == validCharacters.charAt( j ) )
				{
					matched = true;
					break;
				}
			}

			// if this character wasn't matched return false
			if ( matched == false )
				return false;
		}

		return true;
	}

	public static String stripInvalidCharacters( String str, String validCharacters )
	{
		// This function removes all non valid characters from the digits
		int strLength = str.length();
		StringBuilder sb = new StringBuilder( strLength );
		int varCharlength = validCharacters.length();

		for ( int i = 0; i < strLength; i++ )
		{
			char currentChar = str.charAt( i );

			// loop over valid chars
			for ( int j = 0; j < varCharlength; j++ )
			{
				// if a character matches a valid char add it to the buffer
				if ( currentChar == validCharacters.charAt( j ) )
				{
					sb.append( currentChar );
					break;
				}
			}
		}

		return sb.toString();
	}

	public static String padLeft( String str, int padLength )
	{
		return padLeft( str, padLength, ' ' );
	}

	public static String padFillLeft( String str, int padLength, char padChar )
	{
		if ( str.length() >= padLength )
			return str;

		StringBuilder sb = new StringBuilder( padLength );

		for ( int i = 0; i < padLength - str.length(); i++ )
		{
			sb.append( padChar );
		}

		sb.append( str );

		return sb.toString();
	}

	public static String padFillRight( String str, int padLength, char padChar )
	{
		if ( str.length() >= padLength )
			return str;

		StringBuilder sb = new StringBuilder( padLength );

		sb.append( str );

		for ( int i = 0; i < padLength - str.length(); i++ )
		{
			sb.append( padChar );
		}

		return sb.toString();
	}

	public static String padLeft( String str, int padLength, char padChar )
	{
		if ( padLength < 0 )
			throw new IllegalArgumentException( "Invalid padLength passed in: " + padLength );

		char[] padding = new char[padLength];

		for ( int i = 0; i < padLength; i++ )
		{
			padding[i] = padChar;
		}

		return new String( padding ) + str;
	}

	public static String padRight( String str, int padLength )
	{
		return padRight( str, padLength, ' ' );
	}

	public static String padRight( String str, int padLength, char padChar )
	{
		if ( padLength < 0 )
			throw new IllegalArgumentException( "Invalid padLength passed in: " + padLength );

		char[] padding = new char[padLength];

		for ( int i = 0; i < padLength; i++ )
		{
			padding[i] = padChar;
		}

		return str + new String( padding );
	}

	// convert a camel string to lower case segments separated by dots '.'
	public static String camelCaseToDots( String camelString )
	{
		return camelCaseSeparate( camelString, '.' );
	}

	// convert a camel string to lower case segments separated by underscores
	public static String camelCaseToUnderScore( String str )
	{
		String underscore = camelToUnderScore.get( str );

		if ( null == underscore )
		{
			underscore = camelCaseSeparate( str, '_' );
			camelToUnderScore.put( str, underscore );
		}
		return underscore;
	}

	// convert a camel string to lower case segments separated by 'separator'
	public static <T> String camelCaseSeparate( String str, T separator )
	{
		int strLength = str.length();

		if ( strLength == 0 )
			return "";

		StringBuilder sb = new StringBuilder( strLength + 10 );

		for ( int i = 0; i < strLength; i++ )
		{
			// for an upper case that is not the first character insert an underscore
			char charAtLocation = str.charAt( i );
			if ( Character.isUpperCase( charAtLocation ) )
			{
				if ( i != 0 )
					sb.append( separator );
				sb.append( Character.toLowerCase( charAtLocation ) );
			}
			else
			{
				sb.append( charAtLocation );
			}
		}

		return sb.toString();

	}

	public static String underScoreToCamelCase( String str )
	{
		// Loop through turning, e.g. evt_id -> EvtId

		int length = str.length();

		if ( length == 0 )
			return "";

		StringBuilder sb = new StringBuilder( length );
		boolean upperNext = true;
		for ( int i = 0; i < length; i++ )
		{
			// Get the next character
			char c = str.charAt( i );

			// If an underscore then ignore but set upperNext
			if ( c == '_' )
			{
				upperNext = true;
			}
			else
			{
				// after an underscore or first character
				sb.append( upperNext || i == 0 ? Character.toUpperCase( c ) : c );
				upperNext = false;
			}
		}
		return sb.toString();
	}

	public static String left( String str, int leftCount )
	{
		if ( leftCount < 0 )
			throw new IllegalArgumentException( "Invalid leftCount passed in: " + leftCount );
		if ( leftCount >= str.length() )
			return str;
		return str.substring( 0, leftCount );
	}

	public static String right( String str, int rightCount )
	{
		if ( rightCount < 0 )
			throw new IllegalArgumentException( "Invalid rightCount passed in: " + rightCount );
		if ( rightCount >= str.length() )
			return str;
		return str.substring( str.length() - rightCount );
	}

	public static boolean isEmpty( String str )
	{
		return ( str == null || str.length() == 0 );
	}

	public static String replace( String str, String match, String replace )
	{
		int index = str.indexOf( match );

		// optimise for no match
		if ( index == -1 )
			return str;

		int currentIndex = 0;

		int length = str.length();
		StringBuilder sb = new StringBuilder( length );
		int matchlength = match.length();

		while ( index != -1 )
		{
			// copy all the chars from the source before the match string
			for ( int i = currentIndex; i < index; i++ )
			{
				sb.append( str.charAt( i ) );
			}

			sb.append( replace );

			// reset the current index to the last index skipping over the match string
			currentIndex = index + matchlength;

			index = str.indexOf( str, currentIndex );
		}

		// copy the last part of the string in, after the last match
		for ( int i = currentIndex; i < length; i++ )
		{
			sb.append( str.charAt( i ) );
		}

		return sb.toString();
	}

	// character version of split
	// might be faster than than a single char string
	public static String[] split( String str, char delimiter )
	{
		// Short-cut empty or null strings
		if ( ( str == null ) || ( str.length() == 0 ) )
			return new String[0];

		List<String> strs = new ArrayList<String>();

		int currentIndex = 0;
		int nextIndex = str.indexOf( delimiter );

		while ( nextIndex != -1 )
		{
			// add the string
			strs.add( str.substring( currentIndex, nextIndex ) );

			// move the read ptr to the character beyond the delimiter
			currentIndex = nextIndex + 1;

			// find the next delimiter
			nextIndex = str.indexOf( delimiter, currentIndex );
		}

		// add the last string after the last delimiter
		strs.add( str.substring( currentIndex ) );

		return strs.toArray( new String[0] );
	}

	// string version of split
	public static String[] split( String str, String delimiter )
	{
		// Validate the delimiter
		int delimiterLength;

		if ( ( delimiter == null ) || ( ( delimiterLength = delimiter.length() ) == 0 ) )
			throw new IllegalArgumentException( "Null or empty delimiter passed" );

		// Short-cut empty or null strings
		if ( ( str == null ) || ( str.length() == 0 ) )
			return new String[0];

		// Single char string, call the faster single char version
		if ( delimiterLength == 1 )
		{
			return split( str, delimiter.charAt( 0 ) );
		}

		List<String> strs = new ArrayList<String>();

		int currentIndex = 0;
		int nextIndex = str.indexOf( delimiter );

		while ( nextIndex != -1 )
		{
			// add the string
			strs.add( str.substring( currentIndex, nextIndex ) );

			// move the read ptr to the character beyond the delimiter
			currentIndex = nextIndex + delimiterLength;

			// find the next delimiter
			nextIndex = str.indexOf( delimiter, currentIndex );
		}

		// add the last string after the last delimiter
		strs.add( str.substring( currentIndex ) );

		return strs.toArray( new String[0] );
	}

	public static List<String> splitToList( String str, String delimiter )
	{
		// Validate the delimiter
		if ( ( delimiter == null ) || ( delimiter.length() == 0 ) )
			throw new IllegalArgumentException( "Null or empty delimiter passed" );

		// Short-cut empty or null strings
		if ( ( str == null ) || ( str.length() == 0 ) )
			return new ArrayList<String>();

		List<String> strs = new ArrayList<String>();

		int currentIndex = 0;
		int nextIndex = str.indexOf( delimiter );

		while ( nextIndex != -1 )
		{
			// add the string
			strs.add( str.substring( currentIndex, nextIndex ) );

			// move the read ptr to the character beyond the delimiter
			currentIndex = nextIndex + delimiter.length();

			// find the next delimiter
			nextIndex = str.indexOf( delimiter, currentIndex );
		}

		// add the last string after the last delimiter
		strs.add( str.substring( currentIndex ) );

		return strs;
	}

	public static Set<String> splitToSet( String str, String delimiter )
	{
		// Validate the delimiter
		if ( ( delimiter == null ) || ( delimiter.length() == 0 ) )
			throw new IllegalArgumentException( "Null or empty delimiter passed" );

		// Short-cut empty or null strings
		if ( ( str == null ) || ( str.length() == 0 ) )
			return new HashSet<String>();

		Set<String> strs = new HashSet<String>();

		int currentIndex = 0;
		int nextIndex = str.indexOf( delimiter );

		while ( nextIndex != -1 )
		{
			// add the string
			strs.add( str.substring( currentIndex, nextIndex ) );

			// move the read ptr to the character beyond the delimiter
			currentIndex = nextIndex + delimiter.length();

			// find the next delimiter
			nextIndex = str.indexOf( delimiter, currentIndex );
		}

		// add the last string after the last delimiter
		strs.add( str.substring( currentIndex ) );

		return strs;
	}

	// string version of split
	public static String[] splitMergeDelimiter( String str, String delimiter )
	{
		// Validate the delimiter
		if ( ( delimiter == null ) || ( delimiter.length() == 0 ) )
			throw new IllegalArgumentException( "Null or empty delimiter passed" );

		// Short-cut empty or null strings
		if ( ( str == null ) || ( str.length() == 0 ) )
			return new String[0];

		List<String> strs = new ArrayList<String>();

		int currentIndex = 0;
		int nextIndex = str.indexOf( delimiter );

		while ( nextIndex != -1 )
		{
			// if the next delimiter is at the start of the string then skip it
			if ( nextIndex == currentIndex )
			{
				currentIndex += delimiter.length();

				nextIndex = str.indexOf( delimiter, currentIndex );

				continue;
			}

			// add the string
			strs.add( str.substring( currentIndex, nextIndex ) );

			// move the read ptr to the character beyond the delimiter
			currentIndex = nextIndex + delimiter.length();

			// find the next delimiter
			nextIndex = str.indexOf( delimiter, currentIndex );
		}

		// add the last string after the last delimiter
		strs.add( str.substring( currentIndex ) );

		return strs.toArray( new String[0] );
	}

	public static int findPositionOfCamelCaseSuffix( String str, int suffixWordCount )
	{
		// Validate the word count
		if ( suffixWordCount <= 0 )
			throw new IllegalArgumentException( "The suffixWordCount must be greater than 0" );

		int upperCount = 0;
		for ( int i = str.length() - 1; i >= 0; i-- )
		{
			if ( Character.isUpperCase( str.charAt( i ) ) )
			{
				upperCount++;

				if ( upperCount == suffixWordCount )
					return i;
			}
		}

		// No match
		return -1;
	}

	public static String getCamelCaseSuffix( String str, int suffixWordCount )
	{
		// Find the position
		int position = StringUtils.findPositionOfCamelCaseSuffix( str, suffixWordCount );

		// Either return empty if not found, or the suffix
		return ( position == -1 ? "" : str.substring( position ) );
	}

	public static String removeCamelCaseSuffix( String str, int suffixWordCount )
	{
		// Find the position
		int position = StringUtils.findPositionOfCamelCaseSuffix( str, suffixWordCount );

		// Either return empty if not found, or the string minus the suffix
		return ( position == -1 ? "" : str.substring( 0, position ) );
	}

	public static String lowerCaseFirstLetter( String str )
	{
		if ( str == null || str.length() == 0 )
			return str;

		return str.substring( 0, 1 ).toLowerCase() + str.substring( 1 );
	}

	public static int nthIndexOf( String str, char ch, int nthInstance )
	{
		if ( str == null )
			throw new NullPointerException( "String is null" );

		int instanceCount = 0;

		for ( int i = 0; i < str.length(); i++ )
		{
			if ( str.charAt( i ) == ch )
			{
				instanceCount++;
				if ( instanceCount == nthInstance )
				{
					return i;
				}
			}
		}

		return -1;
	}

	public static int nthIndexOfReverse( String str, char ch, int nthInstance )
	{
		if ( str == null )
			throw new NullPointerException( "String is null" );

		int instanceCount = 0;

		for ( int i = str.length() - 1; i >= 0; i-- )
		{
			if ( str.charAt( i ) == ch )
			{
				instanceCount++;
				if ( instanceCount == nthInstance )
				{
					return i;
				}
			}
		}

		return -1;
	}

	public static String fill( char ch, int count )
	{
		char[] chArr = new char[count];
		Arrays.fill( chArr, ch );
		return new String( chArr );
	}

	public static String firstWord( String str )
	{
		str = str.trim();
		int index = str.indexOf( ' ' );

		if ( index == -1 )
		{
			return str;
		}
		else
		{
			return str.substring( 0, index );
		}
	}

	public static String consumeFirstWord( String str )
	{
		str = str.trim();
		int index = str.indexOf( ' ' );

		if ( index == -1 )
		{
			return "";
		}
		else
		{
			return str.substring( index ).trim();
		}
	}

	public static String byteArrayTo255String( byte[] b )
	{
		char[] arr2 = new char[b.length];

		for ( int i = 0; i < b.length; i++ )
		{
			arr2[i] = ( char ) ( b[i] + 128 );
		}

		return new String( arr2 );
	}

	public static byte[] encodeUTF8( String str )
	{
		try
		{
			return str.getBytes( "UTF-8" );
		}
		catch ( UnsupportedEncodingException e )
		{
			throw new UnsupportedOperationException( "Could not find UTF-8 encoding" );
		}
	}

	public static String consumeFromStringBuilder( StringBuilder sb, int length )
	{
		String consumed = sb.substring( 0, length );

		sb.delete( 0, length );

		return consumed;
	}

	public static String substringLength( String str, int index, int length )
	{
		int indexTo = ( index + length );
		return str.substring( index, indexTo );
	}

	public static String removeLeft( String str, int leftRemove )
	{
		if ( leftRemove >= str.length() )
			return "";

		return str.substring( leftRemove );
	}

	public static String removeRight( String str, int rightRemove )
	{
		if ( rightRemove >= str.length() )
			return "";

		return str.substring( 0, str.length() - rightRemove );
	}

	public static String substringUpTo( String str, char ch )
	{
		int index = str.indexOf( ch );

		if ( index == -1 )
			return str;

		return str.substring( 0, index );
	}

	public static String substringDownTo( String str, char ch )
	{
		int index = str.lastIndexOf( ch );

		if ( index == -1 )
			return str;

		return str.substring( index + 1 );
	}

	public static String parse( String baseMessage, String placeholderString, Object[] args )
	{
		// Loop around all the parameters and replace the '%x' placedholders
		// with the corresponding entries in the args array (from %1 to %x)
		for ( int i = 0; i < args.length; i++ )
		{
			if ( args[i] == null )
			{
				baseMessage = baseMessage.replace( placeholderString + ( i + 1 ), "NULL" );
			}
			else
			{
				baseMessage = baseMessage.replace( placeholderString + ( i + 1 ), args[i].toString() );
			}
		}
		return baseMessage;
	}

	public static Pattern getReplaceAllPattern( String pattern )
	{
		// initialise the reg exp pattern objects
		String patternString = pattern;

		// add ^ and $ to ensure complete matching when we do the replacement
		if ( !patternString.startsWith( "^" ) )
			patternString = "^" + patternString;

		if ( !patternString.endsWith( "$" ) )
			patternString = patternString + "$";

		// return the compiled pattern
		return Pattern.compile( patternString );
	}

	public static String toUpperCase( String str )
	{
		StringBuilder sb = new StringBuilder( str );
		int length = sb.length();

		for ( int i = 0; i < length; i++ )
		{
			char ch = sb.charAt( i );

			if ( ch >= 'a' && ch <= 'z' )
			{
				ch = ( char ) ( ch - 'a' + 'A' );
				sb.setCharAt( i, ch );
			}
		}

		return sb.toString();
	}

	public static String toLowerCase( String str )
	{
		StringBuilder sb = new StringBuilder( str );
		int length = sb.length();

		for ( int i = 0; i < length; i++ )
		{
			char ch = sb.charAt( i );

			if ( ch >= 'A' && ch <= 'Z' )
			{
				ch = ( char ) ( ch - 'A' + 'a' );
				sb.setCharAt( i, ch );
			}
		}

		return sb.toString();
	}

	public static String token( String str, char delim, int token )
	{
		int beginIndex = 0;

		// find the start of the substring
		if ( token == 0 )
		{
			beginIndex = 0;
		}
		else
		{
			beginIndex = nthIndexOf( str, delim, token );

			// if start index not found then no string match
			if ( beginIndex == -1 )
				return null;

			// move the begin index past the delimiter
			beginIndex++;
		}

		// find the end index
		int endIndex = str.indexOf( delim, beginIndex );

		// set to end of string if not matched
		if ( endIndex == -1 )
			endIndex = str.length();

		return str.substring( beginIndex, endIndex );
	}

	public static String removeWhitespace( String str )
	{
		int length = str.length();
		StringBuilder sb = new StringBuilder( length );

		for ( int i = 0; i < length; i++ )
		{
			char ch = str.charAt( i );

			if ( ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' )
				continue;

			sb.append( ch );
		}

		return sb.toString();
	}

	public static String convertBooleanArrayToString( boolean[] values )
	{
		StringBuilder sb = new StringBuilder( values.length );
		for ( boolean value : values )
		{
			sb.append( value ? "1" : "0" );
		}
		return sb.toString();
	}

	public static void convertStringToBooleanArray( String str, boolean[] values )
	{
		int length = str.length();
		for ( int i = 0; i < values.length && i < length; i++ )
		{
			values[i] = str.charAt( i ) == '0' ? false : true;
		}
	}

	public static String getWord( String line, String delimiter, int position )
	{
		if ( position > 0 )
		{
			StringTokenizer st = new StringTokenizer( line, delimiter );
			int i = 1;
			while ( st.hasMoreTokens() )
			{
				String word = st.nextToken();
				if ( i++ == position )
					return word;
			}
		}
		return null;
	}

	public static String getCommaSeperatedString( List<String> list )
	{
		StringBuffer sb = new StringBuffer();
		for ( String eachString : list )
		{
			sb.append( eachString + "," );
		}
		return sb.substring( 0, sb.length() - 1 );
	}

}
