lexer grammar VLexer;

options {
	language = Java;
}

@lexer::header {
/*******************************************************************************
 * Copyright (c) 2012 ePi Technologies.
 * 
 * This file is part of VNLP: a Natural Language Processing framework 
 * for Vietnamese.
 * 
 * VNLP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VNLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with VNLP.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package vn.com.epi.tokenizer;
}

@rulecatch { } 

@nameSPACE { vnTokenizer.net.tokenizer }

@members {

	private int numberCount;
	private int wordCount;
	private int wordType;
	private boolean lower;
	private boolean upperMiddle;
	private boolean upperStart;

}

ENTITY	
	:	{ numberCount = wordCount = 0; }
		ENTITY_LOOP
		{ if (wordCount == 0) $type = NUMBER;
		  else if (numberCount == 0 && wordCount == 1) $type = wordType; 
		};

fragment ENTITY_LOOP
	:	(WORD | NUMBER)
		( (NUMBERSIGN? DIGIT | VLETTER) => ENTITY_LOOP | );
	
fragment WORD	
	: 	{ upperMiddle = upperStart = lower = false; }
		( 
			VUPPER { upperStart = true; }
			| VLOWER { upperStart = false; lower = true; }
		) 
		(
			DASH?
			(
				VUPPER { upperMiddle = true; }
				| VLOWER { lower = true; } 
			)
		)*
		{ wordType = (upperMiddle ? (lower ? WORD_OTHER : WORD_ALL_CAPS)  : 
				(upperStart ? WORD_UPPER : WORD_LOWER));
		wordCount++; };

fragment WORD_UPPER 
	:	;
	
fragment WORD_LOWER
	:	;
	
fragment WORD_OTHER
	:	;
	
fragment WORD_ALL_CAPS
	:	;

fragment NUMBER	
	:	SIGN? UNSIGNED_NUMBER
		{ numberCount++; };

fragment UNSIGNED_NUMBER
	:	DIGIT+ 
		( (DECIMAL_POINT DIGIT) => DECIMAL_POINT UNSIGNED_NUMBER | );

NUMBERSIGN
	:	'#';
	
AMPERSAND
	:	'&';
	
FSLASH	:	'/';

LANGLE	:	'<';

RANGLE	:	'>';

EQUAL	:	'=';

AROBA	:	'@';

PUNCTUATION
	:	(ELLIPSIS) => ELLIPSIS
		| '?' | '!' | '.' | ':' | ';' | ',' | '-' | '"' | '\'' 
		| '\u201C' | '\u201D' | '\u2018' | '\u2019' | '�' | '�' | '\u2026'
		| '(' | ')' | '[' | ']' | '{' | '}';

fragment ELLIPSIS
	:	'.' '.' '.';

DOLLAR	
	:	'$';
	
ASTERISK:	'*';

PLUS	:	'+';

BSLASH	:	'\\';

UNDERSCORE
	:	'_';

POUND	:	'�';

SPACE 	:	WHITE_SPACE+;

fragment WHITE_SPACE
	:	'\t' | ' ' | '\r' | '\n'| '\u000C';
	
fragment DIGIT 	
	:	'0'..'9';
			
fragment VLETTER 
	:	VLOWER | VUPPER;			
			
fragment VLOWER
	:	'\u0061' | '\u00E0' | '\u1EA3' | '\u00E3' | '\u00E1' | '\u1EA1' | '\u0103' | '\u1EB1' | '\u1EB3' | '\u1EB5' | '\u1EAF' | '\u1EB7' | '\u00E2' | '\u1EA7' | '\u1EA9' | '\u1EAB' | '\u1EA5' | '\u1EAD' | '\u0062' | '\u0063' | '\u0064' | '\u0111' | '\u0065' | '\u00E8' | '\u1EBB' | '\u1EBD' | '\u00E9' | '\u1EB9' | '\u00EA' | '\u1EC1' | '\u1EC3' | '\u1EC5' | '\u1EBF' | '\u1EC7' | '\u0066' | 'g' | 'h' | 'i' | '\u00EC' | '\u1EC9' | '\u0129' | '\u00ED' | '\u1ECB' | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | '\u00F2' | '\u1ECF' | '\u00F5' | '\u00F3' | '\u1ECD' | '\u00F4' | '\u1ED3' | '\u1ED5' | '\u1ED7' | '\u1ED1' | '\u1ED9' | '\u01A1' | '\u1EDD' | '\u1EDF' | '\u1EE1' | '\u1EDB' | '\u1EE3' | 'p' | 'q' | 'r' | 's' | 't' | 'u' | '\u00F9' | '\u1EE7' | '\u0169' | '\u00FA' | '\u1EE5' | '\u01B0' | '\u1EEB' | '\u1EED' | '\u1EEF' | '\u1EE9' | '\u1EF1' | 'v' | 'w' | 'x' | 'y' | '\u1EF3' | '\u1EF7' | '\u1EF9' | '\u00FD' | '\u1EF5' | 'z';
	
fragment VUPPER
	:	'\u0041' | '\u00C0' | '\u1EA2' | '\u00C3' | '\u00C1' | '\u1EA0' | '\u0102' | '\u1EB0' | '\u1EB2' | '\u1EB4' | '\u1EAE' | '\u1EB6' | '\u00C2' | '\u1EA6' | '\u1EA8' | '\u1EAA' | '\u1EA4' | '\u1EAC' | '\u0042' | '\u0043' | '\u0044' | '\u0110' | '\u0045' | '\u00C8' | '\u1EBA' | '\u1EBC' | '\u00C9' | '\u1EB8' | '\u00CA' | '\u1EC0' | '\u1EC2' | '\u1EC4' | '\u1EBE' | '\u1EC6' | '\u0046' | 'G' | 'H' | 'I' | '\u00CC' | '\u1EC8' | '\u0128' | '\u00CD' | '\u1ECA' | 'J' | 'K' | 'L' | 'M' | 'N' | 'O' | '\u00D2' | '\u1ECE' | '\u00D5' | '\u00D3' | '\u1ECC' | '\u00D4' | '\u1ED2' | '\u1ED4' | '\u1ED6' | '\u1ED0' | '\u1ED8' | '\u01A0' | '\u1EDC' | '\u1EDE' | '\u1EE0' | '\u1EDA' | '\u1EE2' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U' | '\u00D9' | '\u1EE6' | '\u0168' | '\u00DA' | '\u1EE4' | '\u01AF' | '\u1EEA' | '\u1EEC' | '\u1EEE' | '\u1EE8' | '\u1EF0' | 'V' | 'W' | 'X' | 'Y' | '\u1EF2' | '\u1EF6' | '\u1EF8' | '\u00DD' | '\u1EF4' | 'Z';
	
fragment SIGN
	: 	'-' | '+';

fragment DASH
	: 	'-';

	
fragment DECIMAL_POINT
	: 	'.' | ',';

RESIDUAL: 	'\u0000'..'\uFFFF';
