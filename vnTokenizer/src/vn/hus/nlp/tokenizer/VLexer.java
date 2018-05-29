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
// $ANTLR 3.4 D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g 2011-10-25 18:01:15

package vn.hus.nlp.tokenizer;


import org.antlr.runtime.*;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class VLexer extends Lexer {
    public static final int EOF=-1;
    public static final int AMPERSAND=4;
    public static final int AROBA=5;
    public static final int ASTERISK=6;
    public static final int BSLASH=7;
    public static final int DASH=8;
    public static final int DECIMAL_POINT=9;
    public static final int DIGIT=10;
    public static final int DOLLAR=11;
    public static final int ELLIPSIS=12;
    public static final int ENTITY=13;
    public static final int ENTITY_LOOP=14;
    public static final int EQUAL=15;
    public static final int FSLASH=16;
    public static final int LANGLE=17;
    public static final int NUMBER=18;
    public static final int NUMBERSIGN=19;
    public static final int PLUS=20;
    public static final int POUND=21;
    public static final int PUNCTUATION=22;
    public static final int RANGLE=23;
    public static final int RESIDUAL=24;
    public static final int SIGN=25;
    public static final int SPACE=26;
    public static final int UNDERSCORE=27;
    public static final int UNSIGNED_NUMBER=28;
    public static final int VLETTER=29;
    public static final int VLOWER=30;
    public static final int VUPPER=31;
    public static final int WHITE_SPACE=32;
    public static final int WORD=33;
    public static final int WORD_ALL_CAPS=34;
    public static final int WORD_LOWER=35;
    public static final int WORD_OTHER=36;
    public static final int WORD_UPPER=37;

    private static final Logger LOGGER = Logger.getLogger(VLexer.class);

    	private int numberCount;
    	private int wordCount;
    	private int wordType;
    	private boolean lower;
    	private boolean upperMiddle;
    	private boolean upperStart;

    	@Override
    	public void displayRecognitionError(String[] tokenNames,
    			RecognitionException e) {
    		LOGGER.debug(e);
    		LOGGER.debug("Token names: " + Arrays.toString(tokenNames));
    	}


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public VLexer() {} 
    public VLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public VLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g"; }

    // $ANTLR start "ENTITY"
    public final void mENTITY() throws RecognitionException {
        try {
            int _type = ENTITY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:27:2: ( ENTITY_LOOP )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:27:4: ENTITY_LOOP
            {
            if ( state.backtracking==0 ) { numberCount = wordCount = 0; }

            mENTITY_LOOP(); if (state.failed) return ;


            if ( state.backtracking==0 ) { if (wordCount == 0) _type = NUMBER;
            		  else if (numberCount == 0 && wordCount == 1) _type = wordType; 
            		}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ENTITY"

    // $ANTLR start "ENTITY_LOOP"
    public final void mENTITY_LOOP() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:34:2: ( ( WORD | NUMBER ) ( ( ( NUMBERSIGN )? DIGIT | VLETTER )=> ENTITY_LOOP |) )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:34:4: ( WORD | NUMBER ) ( ( ( NUMBERSIGN )? DIGIT | VLETTER )=> ENTITY_LOOP |)
            {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:34:4: ( WORD | NUMBER )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0 >= 'A' && LA1_0 <= 'Z')||(LA1_0 >= 'a' && LA1_0 <= 'z')||(LA1_0 >= '\u00C0' && LA1_0 <= '\u00C3')||(LA1_0 >= '\u00C8' && LA1_0 <= '\u00CA')||(LA1_0 >= '\u00CC' && LA1_0 <= '\u00CD')||(LA1_0 >= '\u00D2' && LA1_0 <= '\u00D5')||(LA1_0 >= '\u00D9' && LA1_0 <= '\u00DA')||LA1_0=='\u00DD'||(LA1_0 >= '\u00E0' && LA1_0 <= '\u00E3')||(LA1_0 >= '\u00E8' && LA1_0 <= '\u00EA')||(LA1_0 >= '\u00EC' && LA1_0 <= '\u00ED')||(LA1_0 >= '\u00F2' && LA1_0 <= '\u00F5')||(LA1_0 >= '\u00F9' && LA1_0 <= '\u00FA')||LA1_0=='\u00FD'||(LA1_0 >= '\u0102' && LA1_0 <= '\u0103')||(LA1_0 >= '\u0110' && LA1_0 <= '\u0111')||(LA1_0 >= '\u0128' && LA1_0 <= '\u0129')||(LA1_0 >= '\u0168' && LA1_0 <= '\u0169')||(LA1_0 >= '\u01A0' && LA1_0 <= '\u01A1')||(LA1_0 >= '\u01AF' && LA1_0 <= '\u01B0')||(LA1_0 >= '\u1EA0' && LA1_0 <= '\u1EF9')) ) {
                alt1=1;
            }
            else if ( (LA1_0=='+'||LA1_0=='-'||(LA1_0 >= '0' && LA1_0 <= '9')) ) {
                alt1=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }
            switch (alt1) {
                case 1 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:34:5: WORD
                    {
                    mWORD(); if (state.failed) return ;


                    }
                    break;
                case 2 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:34:12: NUMBER
                    {
                    mNUMBER(); if (state.failed) return ;


                    }
                    break;

            }


            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:35:3: ( ( ( NUMBERSIGN )? DIGIT | VLETTER )=> ENTITY_LOOP |)
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0 >= 'A' && LA2_0 <= 'Z')||(LA2_0 >= '\u00C0' && LA2_0 <= '\u00C3')||(LA2_0 >= '\u00C8' && LA2_0 <= '\u00CA')||(LA2_0 >= '\u00CC' && LA2_0 <= '\u00CD')||(LA2_0 >= '\u00D2' && LA2_0 <= '\u00D5')||(LA2_0 >= '\u00D9' && LA2_0 <= '\u00DA')||LA2_0=='\u00DD'||LA2_0=='\u0102'||LA2_0=='\u0110'||LA2_0=='\u0128'||LA2_0=='\u0168'||LA2_0=='\u01A0'||LA2_0=='\u01AF'||LA2_0=='\u1EA0'||LA2_0=='\u1EA2'||LA2_0=='\u1EA4'||LA2_0=='\u1EA6'||LA2_0=='\u1EA8'||LA2_0=='\u1EAA'||LA2_0=='\u1EAC'||LA2_0=='\u1EAE'||LA2_0=='\u1EB0'||LA2_0=='\u1EB2'||LA2_0=='\u1EB4'||LA2_0=='\u1EB6'||LA2_0=='\u1EB8'||LA2_0=='\u1EBA'||LA2_0=='\u1EBC'||LA2_0=='\u1EBE'||LA2_0=='\u1EC0'||LA2_0=='\u1EC2'||LA2_0=='\u1EC4'||LA2_0=='\u1EC6'||LA2_0=='\u1EC8'||LA2_0=='\u1ECA'||LA2_0=='\u1ECC'||LA2_0=='\u1ECE'||LA2_0=='\u1ED0'||LA2_0=='\u1ED2'||LA2_0=='\u1ED4'||LA2_0=='\u1ED6'||LA2_0=='\u1ED8'||LA2_0=='\u1EDA'||LA2_0=='\u1EDC'||LA2_0=='\u1EDE'||LA2_0=='\u1EE0'||LA2_0=='\u1EE2'||LA2_0=='\u1EE4'||LA2_0=='\u1EE6'||LA2_0=='\u1EE8'||LA2_0=='\u1EEA'||LA2_0=='\u1EEC'||LA2_0=='\u1EEE'||LA2_0=='\u1EF0'||LA2_0=='\u1EF2'||LA2_0=='\u1EF4'||LA2_0=='\u1EF6'||LA2_0=='\u1EF8') && (synpred1_VLexer())) {
                alt2=1;
            }
            else if ( ((LA2_0 >= 'a' && LA2_0 <= 'z')||(LA2_0 >= '\u00E0' && LA2_0 <= '\u00E3')||(LA2_0 >= '\u00E8' && LA2_0 <= '\u00EA')||(LA2_0 >= '\u00EC' && LA2_0 <= '\u00ED')||(LA2_0 >= '\u00F2' && LA2_0 <= '\u00F5')||(LA2_0 >= '\u00F9' && LA2_0 <= '\u00FA')||LA2_0=='\u00FD'||LA2_0=='\u0103'||LA2_0=='\u0111'||LA2_0=='\u0129'||LA2_0=='\u0169'||LA2_0=='\u01A1'||LA2_0=='\u01B0'||LA2_0=='\u1EA1'||LA2_0=='\u1EA3'||LA2_0=='\u1EA5'||LA2_0=='\u1EA7'||LA2_0=='\u1EA9'||LA2_0=='\u1EAB'||LA2_0=='\u1EAD'||LA2_0=='\u1EAF'||LA2_0=='\u1EB1'||LA2_0=='\u1EB3'||LA2_0=='\u1EB5'||LA2_0=='\u1EB7'||LA2_0=='\u1EB9'||LA2_0=='\u1EBB'||LA2_0=='\u1EBD'||LA2_0=='\u1EBF'||LA2_0=='\u1EC1'||LA2_0=='\u1EC3'||LA2_0=='\u1EC5'||LA2_0=='\u1EC7'||LA2_0=='\u1EC9'||LA2_0=='\u1ECB'||LA2_0=='\u1ECD'||LA2_0=='\u1ECF'||LA2_0=='\u1ED1'||LA2_0=='\u1ED3'||LA2_0=='\u1ED5'||LA2_0=='\u1ED7'||LA2_0=='\u1ED9'||LA2_0=='\u1EDB'||LA2_0=='\u1EDD'||LA2_0=='\u1EDF'||LA2_0=='\u1EE1'||LA2_0=='\u1EE3'||LA2_0=='\u1EE5'||LA2_0=='\u1EE7'||LA2_0=='\u1EE9'||LA2_0=='\u1EEB'||LA2_0=='\u1EED'||LA2_0=='\u1EEF'||LA2_0=='\u1EF1'||LA2_0=='\u1EF3'||LA2_0=='\u1EF5'||LA2_0=='\u1EF7'||LA2_0=='\u1EF9') && (synpred1_VLexer())) {
                alt2=1;
            }
            else if ( (LA2_0=='+'||LA2_0=='-') && (synpred1_VLexer())) {
                alt2=1;
            }
            else if ( ((LA2_0 >= '0' && LA2_0 <= '9')) && (synpred1_VLexer())) {
                alt2=1;
            }
            else {
                alt2=2;
            }
            switch (alt2) {
                case 1 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:35:5: ( ( NUMBERSIGN )? DIGIT | VLETTER )=> ENTITY_LOOP
                    {
                    mENTITY_LOOP(); if (state.failed) return ;


                    }
                    break;
                case 2 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:35:52: 
                    {
                    }
                    break;

            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ENTITY_LOOP"

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:38:2: ( ( VUPPER | VLOWER ) ( ( DASH )? ( VUPPER | VLOWER ) )* )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:38:5: ( VUPPER | VLOWER ) ( ( DASH )? ( VUPPER | VLOWER ) )*
            {
            if ( state.backtracking==0 ) { upperMiddle = upperStart = lower = false; }

            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:39:3: ( VUPPER | VLOWER )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0 >= 'A' && LA3_0 <= 'Z')||(LA3_0 >= '\u00C0' && LA3_0 <= '\u00C3')||(LA3_0 >= '\u00C8' && LA3_0 <= '\u00CA')||(LA3_0 >= '\u00CC' && LA3_0 <= '\u00CD')||(LA3_0 >= '\u00D2' && LA3_0 <= '\u00D5')||(LA3_0 >= '\u00D9' && LA3_0 <= '\u00DA')||LA3_0=='\u00DD'||LA3_0=='\u0102'||LA3_0=='\u0110'||LA3_0=='\u0128'||LA3_0=='\u0168'||LA3_0=='\u01A0'||LA3_0=='\u01AF'||LA3_0=='\u1EA0'||LA3_0=='\u1EA2'||LA3_0=='\u1EA4'||LA3_0=='\u1EA6'||LA3_0=='\u1EA8'||LA3_0=='\u1EAA'||LA3_0=='\u1EAC'||LA3_0=='\u1EAE'||LA3_0=='\u1EB0'||LA3_0=='\u1EB2'||LA3_0=='\u1EB4'||LA3_0=='\u1EB6'||LA3_0=='\u1EB8'||LA3_0=='\u1EBA'||LA3_0=='\u1EBC'||LA3_0=='\u1EBE'||LA3_0=='\u1EC0'||LA3_0=='\u1EC2'||LA3_0=='\u1EC4'||LA3_0=='\u1EC6'||LA3_0=='\u1EC8'||LA3_0=='\u1ECA'||LA3_0=='\u1ECC'||LA3_0=='\u1ECE'||LA3_0=='\u1ED0'||LA3_0=='\u1ED2'||LA3_0=='\u1ED4'||LA3_0=='\u1ED6'||LA3_0=='\u1ED8'||LA3_0=='\u1EDA'||LA3_0=='\u1EDC'||LA3_0=='\u1EDE'||LA3_0=='\u1EE0'||LA3_0=='\u1EE2'||LA3_0=='\u1EE4'||LA3_0=='\u1EE6'||LA3_0=='\u1EE8'||LA3_0=='\u1EEA'||LA3_0=='\u1EEC'||LA3_0=='\u1EEE'||LA3_0=='\u1EF0'||LA3_0=='\u1EF2'||LA3_0=='\u1EF4'||LA3_0=='\u1EF6'||LA3_0=='\u1EF8') ) {
                alt3=1;
            }
            else if ( ((LA3_0 >= 'a' && LA3_0 <= 'z')||(LA3_0 >= '\u00E0' && LA3_0 <= '\u00E3')||(LA3_0 >= '\u00E8' && LA3_0 <= '\u00EA')||(LA3_0 >= '\u00EC' && LA3_0 <= '\u00ED')||(LA3_0 >= '\u00F2' && LA3_0 <= '\u00F5')||(LA3_0 >= '\u00F9' && LA3_0 <= '\u00FA')||LA3_0=='\u00FD'||LA3_0=='\u0103'||LA3_0=='\u0111'||LA3_0=='\u0129'||LA3_0=='\u0169'||LA3_0=='\u01A1'||LA3_0=='\u01B0'||LA3_0=='\u1EA1'||LA3_0=='\u1EA3'||LA3_0=='\u1EA5'||LA3_0=='\u1EA7'||LA3_0=='\u1EA9'||LA3_0=='\u1EAB'||LA3_0=='\u1EAD'||LA3_0=='\u1EAF'||LA3_0=='\u1EB1'||LA3_0=='\u1EB3'||LA3_0=='\u1EB5'||LA3_0=='\u1EB7'||LA3_0=='\u1EB9'||LA3_0=='\u1EBB'||LA3_0=='\u1EBD'||LA3_0=='\u1EBF'||LA3_0=='\u1EC1'||LA3_0=='\u1EC3'||LA3_0=='\u1EC5'||LA3_0=='\u1EC7'||LA3_0=='\u1EC9'||LA3_0=='\u1ECB'||LA3_0=='\u1ECD'||LA3_0=='\u1ECF'||LA3_0=='\u1ED1'||LA3_0=='\u1ED3'||LA3_0=='\u1ED5'||LA3_0=='\u1ED7'||LA3_0=='\u1ED9'||LA3_0=='\u1EDB'||LA3_0=='\u1EDD'||LA3_0=='\u1EDF'||LA3_0=='\u1EE1'||LA3_0=='\u1EE3'||LA3_0=='\u1EE5'||LA3_0=='\u1EE7'||LA3_0=='\u1EE9'||LA3_0=='\u1EEB'||LA3_0=='\u1EED'||LA3_0=='\u1EEF'||LA3_0=='\u1EF1'||LA3_0=='\u1EF3'||LA3_0=='\u1EF5'||LA3_0=='\u1EF7'||LA3_0=='\u1EF9') ) {
                alt3=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:40:4: VUPPER
                    {
                    mVUPPER(); if (state.failed) return ;


                    if ( state.backtracking==0 ) { upperStart = true; }

                    }
                    break;
                case 2 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:41:6: VLOWER
                    {
                    mVLOWER(); if (state.failed) return ;


                    if ( state.backtracking==0 ) { upperStart = false; lower = true; }

                    }
                    break;

            }


            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:43:3: ( ( DASH )? ( VUPPER | VLOWER ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='-'||(LA6_0 >= 'A' && LA6_0 <= 'Z')||(LA6_0 >= 'a' && LA6_0 <= 'z')||(LA6_0 >= '\u00C0' && LA6_0 <= '\u00C3')||(LA6_0 >= '\u00C8' && LA6_0 <= '\u00CA')||(LA6_0 >= '\u00CC' && LA6_0 <= '\u00CD')||(LA6_0 >= '\u00D2' && LA6_0 <= '\u00D5')||(LA6_0 >= '\u00D9' && LA6_0 <= '\u00DA')||LA6_0=='\u00DD'||(LA6_0 >= '\u00E0' && LA6_0 <= '\u00E3')||(LA6_0 >= '\u00E8' && LA6_0 <= '\u00EA')||(LA6_0 >= '\u00EC' && LA6_0 <= '\u00ED')||(LA6_0 >= '\u00F2' && LA6_0 <= '\u00F5')||(LA6_0 >= '\u00F9' && LA6_0 <= '\u00FA')||LA6_0=='\u00FD'||(LA6_0 >= '\u0102' && LA6_0 <= '\u0103')||(LA6_0 >= '\u0110' && LA6_0 <= '\u0111')||(LA6_0 >= '\u0128' && LA6_0 <= '\u0129')||(LA6_0 >= '\u0168' && LA6_0 <= '\u0169')||(LA6_0 >= '\u01A0' && LA6_0 <= '\u01A1')||(LA6_0 >= '\u01AF' && LA6_0 <= '\u01B0')||(LA6_0 >= '\u1EA0' && LA6_0 <= '\u1EF9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:44:4: ( DASH )? ( VUPPER | VLOWER )
            	    {
            	    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:44:4: ( DASH )?
            	    int alt4=2;
            	    int LA4_0 = input.LA(1);

            	    if ( (LA4_0=='-') ) {
            	        alt4=1;
            	    }
            	    switch (alt4) {
            	        case 1 :
            	            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            	            {
            	            if ( input.LA(1)=='-' ) {
            	                input.consume();
            	                state.failed=false;
            	            }
            	            else {
            	                if (state.backtracking>0) {state.failed=true; return ;}
            	                MismatchedSetException mse = new MismatchedSetException(null,input);
            	                recover(mse);
            	                throw mse;
            	            }


            	            }
            	            break;

            	    }


            	    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:45:4: ( VUPPER | VLOWER )
            	    int alt5=2;
            	    int LA5_0 = input.LA(1);

            	    if ( ((LA5_0 >= 'A' && LA5_0 <= 'Z')||(LA5_0 >= '\u00C0' && LA5_0 <= '\u00C3')||(LA5_0 >= '\u00C8' && LA5_0 <= '\u00CA')||(LA5_0 >= '\u00CC' && LA5_0 <= '\u00CD')||(LA5_0 >= '\u00D2' && LA5_0 <= '\u00D5')||(LA5_0 >= '\u00D9' && LA5_0 <= '\u00DA')||LA5_0=='\u00DD'||LA5_0=='\u0102'||LA5_0=='\u0110'||LA5_0=='\u0128'||LA5_0=='\u0168'||LA5_0=='\u01A0'||LA5_0=='\u01AF'||LA5_0=='\u1EA0'||LA5_0=='\u1EA2'||LA5_0=='\u1EA4'||LA5_0=='\u1EA6'||LA5_0=='\u1EA8'||LA5_0=='\u1EAA'||LA5_0=='\u1EAC'||LA5_0=='\u1EAE'||LA5_0=='\u1EB0'||LA5_0=='\u1EB2'||LA5_0=='\u1EB4'||LA5_0=='\u1EB6'||LA5_0=='\u1EB8'||LA5_0=='\u1EBA'||LA5_0=='\u1EBC'||LA5_0=='\u1EBE'||LA5_0=='\u1EC0'||LA5_0=='\u1EC2'||LA5_0=='\u1EC4'||LA5_0=='\u1EC6'||LA5_0=='\u1EC8'||LA5_0=='\u1ECA'||LA5_0=='\u1ECC'||LA5_0=='\u1ECE'||LA5_0=='\u1ED0'||LA5_0=='\u1ED2'||LA5_0=='\u1ED4'||LA5_0=='\u1ED6'||LA5_0=='\u1ED8'||LA5_0=='\u1EDA'||LA5_0=='\u1EDC'||LA5_0=='\u1EDE'||LA5_0=='\u1EE0'||LA5_0=='\u1EE2'||LA5_0=='\u1EE4'||LA5_0=='\u1EE6'||LA5_0=='\u1EE8'||LA5_0=='\u1EEA'||LA5_0=='\u1EEC'||LA5_0=='\u1EEE'||LA5_0=='\u1EF0'||LA5_0=='\u1EF2'||LA5_0=='\u1EF4'||LA5_0=='\u1EF6'||LA5_0=='\u1EF8') ) {
            	        alt5=1;
            	    }
            	    else if ( ((LA5_0 >= 'a' && LA5_0 <= 'z')||(LA5_0 >= '\u00E0' && LA5_0 <= '\u00E3')||(LA5_0 >= '\u00E8' && LA5_0 <= '\u00EA')||(LA5_0 >= '\u00EC' && LA5_0 <= '\u00ED')||(LA5_0 >= '\u00F2' && LA5_0 <= '\u00F5')||(LA5_0 >= '\u00F9' && LA5_0 <= '\u00FA')||LA5_0=='\u00FD'||LA5_0=='\u0103'||LA5_0=='\u0111'||LA5_0=='\u0129'||LA5_0=='\u0169'||LA5_0=='\u01A1'||LA5_0=='\u01B0'||LA5_0=='\u1EA1'||LA5_0=='\u1EA3'||LA5_0=='\u1EA5'||LA5_0=='\u1EA7'||LA5_0=='\u1EA9'||LA5_0=='\u1EAB'||LA5_0=='\u1EAD'||LA5_0=='\u1EAF'||LA5_0=='\u1EB1'||LA5_0=='\u1EB3'||LA5_0=='\u1EB5'||LA5_0=='\u1EB7'||LA5_0=='\u1EB9'||LA5_0=='\u1EBB'||LA5_0=='\u1EBD'||LA5_0=='\u1EBF'||LA5_0=='\u1EC1'||LA5_0=='\u1EC3'||LA5_0=='\u1EC5'||LA5_0=='\u1EC7'||LA5_0=='\u1EC9'||LA5_0=='\u1ECB'||LA5_0=='\u1ECD'||LA5_0=='\u1ECF'||LA5_0=='\u1ED1'||LA5_0=='\u1ED3'||LA5_0=='\u1ED5'||LA5_0=='\u1ED7'||LA5_0=='\u1ED9'||LA5_0=='\u1EDB'||LA5_0=='\u1EDD'||LA5_0=='\u1EDF'||LA5_0=='\u1EE1'||LA5_0=='\u1EE3'||LA5_0=='\u1EE5'||LA5_0=='\u1EE7'||LA5_0=='\u1EE9'||LA5_0=='\u1EEB'||LA5_0=='\u1EED'||LA5_0=='\u1EEF'||LA5_0=='\u1EF1'||LA5_0=='\u1EF3'||LA5_0=='\u1EF5'||LA5_0=='\u1EF7'||LA5_0=='\u1EF9') ) {
            	        alt5=2;
            	    }
            	    else if (LA5_0 >= '0' && LA5_0 <='9'){
            	    	alt5 = 3;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 5, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt5) {
            	        case 1 :
            	            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:46:5: VUPPER
            	            {
            	            mVUPPER(); if (state.failed) return ;


            	            if ( state.backtracking==0 ) { upperMiddle = true; }

            	            }
            	            break;
            	        case 2 :
            	            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:47:7: VLOWER
            	            {
            	            mVLOWER(); if (state.failed) return ;


            	            if ( state.backtracking==0 ) { lower = true; }

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            if ( state.backtracking==0 ) { wordType = (upperMiddle ? (lower ? WORD_OTHER : WORD_ALL_CAPS)  : 
            				(upperStart ? WORD_UPPER : WORD_LOWER));
            		wordCount++; }

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WORD"

    // $ANTLR start "WORD_UPPER"
    public final void mWORD_UPPER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:55:2: ()
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:55:4: 
            {
            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WORD_UPPER"

    // $ANTLR start "WORD_LOWER"
    public final void mWORD_LOWER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:58:2: ()
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:58:4: 
            {
            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WORD_LOWER"

    // $ANTLR start "WORD_OTHER"
    public final void mWORD_OTHER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:61:2: ()
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:61:4: 
            {
            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WORD_OTHER"

    // $ANTLR start "WORD_ALL_CAPS"
    public final void mWORD_ALL_CAPS() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:64:2: ()
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:64:4: 
            {
            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WORD_ALL_CAPS"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:67:2: ( ( SIGN )? UNSIGNED_NUMBER )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:67:4: ( SIGN )? UNSIGNED_NUMBER
            {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:67:4: ( SIGN )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='+'||LA7_0=='-') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            mUNSIGNED_NUMBER(); if (state.failed) return ;


            if ( state.backtracking==0 ) { numberCount++; }

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "UNSIGNED_NUMBER"
    public final void mUNSIGNED_NUMBER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:71:2: ( ( DIGIT )+ ( ( DECIMAL_POINT DIGIT )=> DECIMAL_POINT UNSIGNED_NUMBER |) )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:71:4: ( DIGIT )+ ( ( DECIMAL_POINT DIGIT )=> DECIMAL_POINT UNSIGNED_NUMBER |)
            {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:71:4: ( DIGIT )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:72:3: ( ( DECIMAL_POINT DIGIT )=> DECIMAL_POINT UNSIGNED_NUMBER |)
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==','||LA9_0=='.') && (synpred2_VLexer())) {
                alt9=1;
            }
            else {
                alt9=2;
            }
            switch (alt9) {
                case 1 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:72:5: ( DECIMAL_POINT DIGIT )=> DECIMAL_POINT UNSIGNED_NUMBER
                    {
                    mDECIMAL_POINT(); if (state.failed) return ;


                    mUNSIGNED_NUMBER(); if (state.failed) return ;


                    }
                    break;
                case 2 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:72:62: 
                    {
                    }
                    break;

            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNSIGNED_NUMBER"

    // $ANTLR start "NUMBERSIGN"
    public final void mNUMBERSIGN() throws RecognitionException {
        try {
            int _type = NUMBERSIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:75:2: ( '#' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:75:4: '#'
            {
            match('#'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NUMBERSIGN"

    // $ANTLR start "AMPERSAND"
    public final void mAMPERSAND() throws RecognitionException {
        try {
            int _type = AMPERSAND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:78:2: ( '&' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:78:4: '&'
            {
            match('&'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AMPERSAND"

    // $ANTLR start "FSLASH"
    public final void mFSLASH() throws RecognitionException {
        try {
            int _type = FSLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:80:8: ( '/' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:80:10: '/'
            {
            match('/'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FSLASH"

    // $ANTLR start "LANGLE"
    public final void mLANGLE() throws RecognitionException {
        try {
            int _type = LANGLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:82:8: ( '<' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:82:10: '<'
            {
            match('<'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LANGLE"

    // $ANTLR start "RANGLE"
    public final void mRANGLE() throws RecognitionException {
        try {
            int _type = RANGLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:84:8: ( '>' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:84:10: '>'
            {
            match('>'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RANGLE"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:86:7: ( '=' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:86:9: '='
            {
            match('='); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EQUAL"

    // $ANTLR start "AROBA"
    public final void mAROBA() throws RecognitionException {
        try {
            int _type = AROBA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:88:7: ( '@' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:88:9: '@'
            {
            match('@'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AROBA"

    // $ANTLR start "PUNCTUATION"
    public final void mPUNCTUATION() throws RecognitionException {
        try {
            int _type = PUNCTUATION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:91:2: ( ( ELLIPSIS )=> ELLIPSIS | '?' | '!' | '.' | ':' | ';' | ',' | '-' | '\"' | '\\'' | '\\u201C' | '\\u201D' | '\\u2018' | '\\u2019' | '�' | '�' | '\\u2026' | '(' | ')' | '[' | ']' | '{' | '}' )
            int alt10=23;
            switch ( input.LA(1) ) {
            case '.':
                {
                int LA10_1 = input.LA(2);

                if ( (LA10_1=='.') && (synpred3_VLexer())) {
                    alt10=1;
                }
                else {
                    alt10=4;
                }
                }
                break;
            case '?':
                {
                alt10=2;
                }
                break;
            case '!':
                {
                alt10=3;
                }
                break;
            case ':':
                {
                alt10=5;
                }
                break;
            case ';':
                {
                alt10=6;
                }
                break;
            case ',':
                {
                alt10=7;
                }
                break;
            case '-':
                {
                alt10=8;
                }
                break;
            case '\"':
                {
                alt10=9;
                }
                break;
            case '\'':
                {
                alt10=10;
                }
                break;
            case '\u201C':
                {
                alt10=11;
                }
                break;
            case '\u201D':
                {
                alt10=12;
                }
                break;
            case '\u2018':
                {
                alt10=13;
                }
                break;
            case '\u2019':
                {
                alt10=14;
                }
                break;
            case '\u00AB':
                {
                alt10=15;
                }
                break;
            case '\u00BB':
                {
                alt10=16;
                }
                break;
            case '\u2026':
                {
                alt10=17;
                }
                break;
            case '(':
                {
                alt10=18;
                }
                break;
            case ')':
                {
                alt10=19;
                }
                break;
            case '[':
                {
                alt10=20;
                }
                break;
            case ']':
                {
                alt10=21;
                }
                break;
            case '{':
                {
                alt10=22;
                }
                break;
            case '}':
                {
                alt10=23;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
                case 1 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:91:4: ( ELLIPSIS )=> ELLIPSIS
                    {
                    mELLIPSIS(); if (state.failed) return ;


                    }
                    break;
                case 2 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:5: '?'
                    {
                    match('?'); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:11: '!'
                    {
                    match('!'); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:17: '.'
                    {
                    match('.'); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:23: ':'
                    {
                    match(':'); if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:29: ';'
                    {
                    match(';'); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:35: ','
                    {
                    match(','); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:41: '-'
                    {
                    match('-'); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:47: '\"'
                    {
                    match('\"'); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:92:53: '\\''
                    {
                    match('\''); if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:93:5: '\\u201C'
                    {
                    match('\u201C'); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:93:16: '\\u201D'
                    {
                    match('\u201D'); if (state.failed) return ;

                    }
                    break;
                case 13 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:93:27: '\\u2018'
                    {
                    match('\u2018'); if (state.failed) return ;

                    }
                    break;
                case 14 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:93:38: '\\u2019'
                    {
                    match('\u2019'); if (state.failed) return ;

                    }
                    break;
                case 15 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:93:49: '�'
                    {
                    match('\u00AB'); if (state.failed) return ;

                    }
                    break;
                case 16 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:93:55: '�'
                    {
                    match('\u00BB'); if (state.failed) return ;

                    }
                    break;
                case 17 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:93:61: '\\u2026'
                    {
                    match('\u2026'); if (state.failed) return ;

                    }
                    break;
                case 18 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:94:5: '('
                    {
                    match('('); if (state.failed) return ;

                    }
                    break;
                case 19 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:94:11: ')'
                    {
                    match(')'); if (state.failed) return ;

                    }
                    break;
                case 20 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:94:17: '['
                    {
                    match('['); if (state.failed) return ;

                    }
                    break;
                case 21 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:94:23: ']'
                    {
                    match(']'); if (state.failed) return ;

                    }
                    break;
                case 22 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:94:29: '{'
                    {
                    match('{'); if (state.failed) return ;

                    }
                    break;
                case 23 :
                    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:94:35: '}'
                    {
                    match('}'); if (state.failed) return ;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PUNCTUATION"

    // $ANTLR start "ELLIPSIS"
    public final void mELLIPSIS() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:97:2: ( '.' '.' '.' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:97:4: '.' '.' '.'
            {
            match('.'); if (state.failed) return ;

            match('.'); if (state.failed) return ;

            match('.'); if (state.failed) return ;

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ELLIPSIS"

    // $ANTLR start "DOLLAR"
    public final void mDOLLAR() throws RecognitionException {
        try {
            int _type = DOLLAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:100:2: ( '$' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:100:4: '$'
            {
            match('$'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DOLLAR"

    // $ANTLR start "ASTERISK"
    public final void mASTERISK() throws RecognitionException {
        try {
            int _type = ASTERISK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:102:9: ( '*' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:102:11: '*'
            {
            match('*'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ASTERISK"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:104:6: ( '+' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:104:8: '+'
            {
            match('+'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "BSLASH"
    public final void mBSLASH() throws RecognitionException {
        try {
            int _type = BSLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:106:8: ( '\\\\' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:106:10: '\\\\'
            {
            match('\\'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BSLASH"

    // $ANTLR start "UNDERSCORE"
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            int _type = UNDERSCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:109:2: ( '_' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:109:4: '_'
            {
            match('_'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNDERSCORE"

    // $ANTLR start "POUND"
    public final void mPOUND() throws RecognitionException {
        try {
            int _type = POUND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:111:7: ( '�' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:111:9: '�'
            {
            match('\u00A3'); if (state.failed) return ;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "POUND"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            int _type = SPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:113:8: ( ( WHITE_SPACE )+ )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:113:10: ( WHITE_SPACE )+
            {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:113:10: ( WHITE_SPACE )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0 >= '\t' && LA11_0 <= '\n')||(LA11_0 >= '\f' && LA11_0 <= '\r')||LA11_0==' ') ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            	    {
            	    if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
            	        input.consume();
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SPACE"

    // $ANTLR start "WHITE_SPACE"
    public final void mWHITE_SPACE() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:116:2: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHITE_SPACE"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:119:2: ( '0' .. '9' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "VLETTER"
    public final void mVLETTER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:122:2: ( VLOWER | VUPPER )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z')||(input.LA(1) >= '\u00C0' && input.LA(1) <= '\u00C3')||(input.LA(1) >= '\u00C8' && input.LA(1) <= '\u00CA')||(input.LA(1) >= '\u00CC' && input.LA(1) <= '\u00CD')||(input.LA(1) >= '\u00D2' && input.LA(1) <= '\u00D5')||(input.LA(1) >= '\u00D9' && input.LA(1) <= '\u00DA')||input.LA(1)=='\u00DD'||(input.LA(1) >= '\u00E0' && input.LA(1) <= '\u00E3')||(input.LA(1) >= '\u00E8' && input.LA(1) <= '\u00EA')||(input.LA(1) >= '\u00EC' && input.LA(1) <= '\u00ED')||(input.LA(1) >= '\u00F2' && input.LA(1) <= '\u00F5')||(input.LA(1) >= '\u00F9' && input.LA(1) <= '\u00FA')||input.LA(1)=='\u00FD'||(input.LA(1) >= '\u0102' && input.LA(1) <= '\u0103')||(input.LA(1) >= '\u0110' && input.LA(1) <= '\u0111')||(input.LA(1) >= '\u0128' && input.LA(1) <= '\u0129')||(input.LA(1) >= '\u0168' && input.LA(1) <= '\u0169')||(input.LA(1) >= '\u01A0' && input.LA(1) <= '\u01A1')||(input.LA(1) >= '\u01AF' && input.LA(1) <= '\u01B0')||(input.LA(1) >= '\u1EA0' && input.LA(1) <= '\u1EF9') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VLETTER"

    // $ANTLR start "VLOWER"
    public final void mVLOWER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:125:2: ( '\\u0061' | '\\u00E0' | '\\u1EA3' | '\\u00E3' | '\\u00E1' | '\\u1EA1' | '\\u0103' | '\\u1EB1' | '\\u1EB3' | '\\u1EB5' | '\\u1EAF' | '\\u1EB7' | '\\u00E2' | '\\u1EA7' | '\\u1EA9' | '\\u1EAB' | '\\u1EA5' | '\\u1EAD' | '\\u0062' | '\\u0063' | '\\u0064' | '\\u0111' | '\\u0065' | '\\u00E8' | '\\u1EBB' | '\\u1EBD' | '\\u00E9' | '\\u1EB9' | '\\u00EA' | '\\u1EC1' | '\\u1EC3' | '\\u1EC5' | '\\u1EBF' | '\\u1EC7' | '\\u0066' | 'g' | 'h' | 'i' | '\\u00EC' | '\\u1EC9' | '\\u0129' | '\\u00ED' | '\\u1ECB' | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | '\\u00F2' | '\\u1ECF' | '\\u00F5' | '\\u00F3' | '\\u1ECD' | '\\u00F4' | '\\u1ED3' | '\\u1ED5' | '\\u1ED7' | '\\u1ED1' | '\\u1ED9' | '\\u01A1' | '\\u1EDD' | '\\u1EDF' | '\\u1EE1' | '\\u1EDB' | '\\u1EE3' | 'p' | 'q' | 'r' | 's' | 't' | 'u' | '\\u00F9' | '\\u1EE7' | '\\u0169' | '\\u00FA' | '\\u1EE5' | '\\u01B0' | '\\u1EEB' | '\\u1EED' | '\\u1EEF' | '\\u1EE9' | '\\u1EF1' | 'v' | 'w' | 'x' | 'y' | '\\u1EF3' | '\\u1EF7' | '\\u1EF9' | '\\u00FD' | '\\u1EF5' | 'z' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z')||(input.LA(1) >= '\u00E0' && input.LA(1) <= '\u00E3')||(input.LA(1) >= '\u00E8' && input.LA(1) <= '\u00EA')||(input.LA(1) >= '\u00EC' && input.LA(1) <= '\u00ED')||(input.LA(1) >= '\u00F2' && input.LA(1) <= '\u00F5')||(input.LA(1) >= '\u00F9' && input.LA(1) <= '\u00FA')||input.LA(1)=='\u00FD'||input.LA(1)=='\u0103'||input.LA(1)=='\u0111'||input.LA(1)=='\u0129'||input.LA(1)=='\u0169'||input.LA(1)=='\u01A1'||input.LA(1)=='\u01B0'||input.LA(1)=='\u1EA1'||input.LA(1)=='\u1EA3'||input.LA(1)=='\u1EA5'||input.LA(1)=='\u1EA7'||input.LA(1)=='\u1EA9'||input.LA(1)=='\u1EAB'||input.LA(1)=='\u1EAD'||input.LA(1)=='\u1EAF'||input.LA(1)=='\u1EB1'||input.LA(1)=='\u1EB3'||input.LA(1)=='\u1EB5'||input.LA(1)=='\u1EB7'||input.LA(1)=='\u1EB9'||input.LA(1)=='\u1EBB'||input.LA(1)=='\u1EBD'||input.LA(1)=='\u1EBF'||input.LA(1)=='\u1EC1'||input.LA(1)=='\u1EC3'||input.LA(1)=='\u1EC5'||input.LA(1)=='\u1EC7'||input.LA(1)=='\u1EC9'||input.LA(1)=='\u1ECB'||input.LA(1)=='\u1ECD'||input.LA(1)=='\u1ECF'||input.LA(1)=='\u1ED1'||input.LA(1)=='\u1ED3'||input.LA(1)=='\u1ED5'||input.LA(1)=='\u1ED7'||input.LA(1)=='\u1ED9'||input.LA(1)=='\u1EDB'||input.LA(1)=='\u1EDD'||input.LA(1)=='\u1EDF'||input.LA(1)=='\u1EE1'||input.LA(1)=='\u1EE3'||input.LA(1)=='\u1EE5'||input.LA(1)=='\u1EE7'||input.LA(1)=='\u1EE9'||input.LA(1)=='\u1EEB'||input.LA(1)=='\u1EED'||input.LA(1)=='\u1EEF'||input.LA(1)=='\u1EF1'||input.LA(1)=='\u1EF3'||input.LA(1)=='\u1EF5'||input.LA(1)=='\u1EF7'||input.LA(1)=='\u1EF9' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VLOWER"

    // $ANTLR start "VUPPER"
    public final void mVUPPER() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:128:2: ( '\\u0041' | '\\u00C0' | '\\u1EA2' | '\\u00C3' | '\\u00C1' | '\\u1EA0' | '\\u0102' | '\\u1EB0' | '\\u1EB2' | '\\u1EB4' | '\\u1EAE' | '\\u1EB6' | '\\u00C2' | '\\u1EA6' | '\\u1EA8' | '\\u1EAA' | '\\u1EA4' | '\\u1EAC' | '\\u0042' | '\\u0043' | '\\u0044' | '\\u0110' | '\\u0045' | '\\u00C8' | '\\u1EBA' | '\\u1EBC' | '\\u00C9' | '\\u1EB8' | '\\u00CA' | '\\u1EC0' | '\\u1EC2' | '\\u1EC4' | '\\u1EBE' | '\\u1EC6' | '\\u0046' | 'G' | 'H' | 'I' | '\\u00CC' | '\\u1EC8' | '\\u0128' | '\\u00CD' | '\\u1ECA' | 'J' | 'K' | 'L' | 'M' | 'N' | 'O' | '\\u00D2' | '\\u1ECE' | '\\u00D5' | '\\u00D3' | '\\u1ECC' | '\\u00D4' | '\\u1ED2' | '\\u1ED4' | '\\u1ED6' | '\\u1ED0' | '\\u1ED8' | '\\u01A0' | '\\u1EDC' | '\\u1EDE' | '\\u1EE0' | '\\u1EDA' | '\\u1EE2' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U' | '\\u00D9' | '\\u1EE6' | '\\u0168' | '\\u00DA' | '\\u1EE4' | '\\u01AF' | '\\u1EEA' | '\\u1EEC' | '\\u1EEE' | '\\u1EE8' | '\\u1EF0' | 'V' | 'W' | 'X' | 'Y' | '\\u1EF2' | '\\u1EF6' | '\\u1EF8' | '\\u00DD' | '\\u1EF4' | 'Z' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= '\u00C0' && input.LA(1) <= '\u00C3')||(input.LA(1) >= '\u00C8' && input.LA(1) <= '\u00CA')||(input.LA(1) >= '\u00CC' && input.LA(1) <= '\u00CD')||(input.LA(1) >= '\u00D2' && input.LA(1) <= '\u00D5')||(input.LA(1) >= '\u00D9' && input.LA(1) <= '\u00DA')||input.LA(1)=='\u00DD'||input.LA(1)=='\u0102'||input.LA(1)=='\u0110'||input.LA(1)=='\u0128'||input.LA(1)=='\u0168'||input.LA(1)=='\u01A0'||input.LA(1)=='\u01AF'||input.LA(1)=='\u1EA0'||input.LA(1)=='\u1EA2'||input.LA(1)=='\u1EA4'||input.LA(1)=='\u1EA6'||input.LA(1)=='\u1EA8'||input.LA(1)=='\u1EAA'||input.LA(1)=='\u1EAC'||input.LA(1)=='\u1EAE'||input.LA(1)=='\u1EB0'||input.LA(1)=='\u1EB2'||input.LA(1)=='\u1EB4'||input.LA(1)=='\u1EB6'||input.LA(1)=='\u1EB8'||input.LA(1)=='\u1EBA'||input.LA(1)=='\u1EBC'||input.LA(1)=='\u1EBE'||input.LA(1)=='\u1EC0'||input.LA(1)=='\u1EC2'||input.LA(1)=='\u1EC4'||input.LA(1)=='\u1EC6'||input.LA(1)=='\u1EC8'||input.LA(1)=='\u1ECA'||input.LA(1)=='\u1ECC'||input.LA(1)=='\u1ECE'||input.LA(1)=='\u1ED0'||input.LA(1)=='\u1ED2'||input.LA(1)=='\u1ED4'||input.LA(1)=='\u1ED6'||input.LA(1)=='\u1ED8'||input.LA(1)=='\u1EDA'||input.LA(1)=='\u1EDC'||input.LA(1)=='\u1EDE'||input.LA(1)=='\u1EE0'||input.LA(1)=='\u1EE2'||input.LA(1)=='\u1EE4'||input.LA(1)=='\u1EE6'||input.LA(1)=='\u1EE8'||input.LA(1)=='\u1EEA'||input.LA(1)=='\u1EEC'||input.LA(1)=='\u1EEE'||input.LA(1)=='\u1EF0'||input.LA(1)=='\u1EF2'||input.LA(1)=='\u1EF4'||input.LA(1)=='\u1EF6'||input.LA(1)=='\u1EF8' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VUPPER"

    // $ANTLR start "SIGN"
    public final void mSIGN() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:131:2: ( '-' | '+' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SIGN"

    // $ANTLR start "DASH"
    public final void mDASH() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:134:2: ( '-' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:134:5: '-'
            {
            match('-'); if (state.failed) return ;

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DASH"

    // $ANTLR start "DECIMAL_POINT"
    public final void mDECIMAL_POINT() throws RecognitionException {
        try {
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:138:2: ( '.' | ',' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( input.LA(1)==','||input.LA(1)=='.' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DECIMAL_POINT"

    // $ANTLR start "RESIDUAL"
    public final void mRESIDUAL() throws RecognitionException {
        try {
            int _type = RESIDUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:140:9: ( '\\u0000' .. '\\uFFFF' )
            // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
            {
            if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\uFFFF') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RESIDUAL"

    public void mTokens() throws RecognitionException {
        // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:8: ( ENTITY | NUMBERSIGN | AMPERSAND | FSLASH | LANGLE | RANGLE | EQUAL | AROBA | PUNCTUATION | DOLLAR | ASTERISK | PLUS | BSLASH | UNDERSCORE | POUND | SPACE | RESIDUAL )
        int alt12=17;
        int LA12_0 = input.LA(1);

        if ( ((LA12_0 >= 'A' && LA12_0 <= 'Z')||(LA12_0 >= '\u00C0' && LA12_0 <= '\u00C3')||(LA12_0 >= '\u00C8' && LA12_0 <= '\u00CA')||(LA12_0 >= '\u00CC' && LA12_0 <= '\u00CD')||(LA12_0 >= '\u00D2' && LA12_0 <= '\u00D5')||(LA12_0 >= '\u00D9' && LA12_0 <= '\u00DA')||LA12_0=='\u00DD'||LA12_0=='\u0102'||LA12_0=='\u0110'||LA12_0=='\u0128'||LA12_0=='\u0168'||LA12_0=='\u01A0'||LA12_0=='\u01AF'||LA12_0=='\u1EA0'||LA12_0=='\u1EA2'||LA12_0=='\u1EA4'||LA12_0=='\u1EA6'||LA12_0=='\u1EA8'||LA12_0=='\u1EAA'||LA12_0=='\u1EAC'||LA12_0=='\u1EAE'||LA12_0=='\u1EB0'||LA12_0=='\u1EB2'||LA12_0=='\u1EB4'||LA12_0=='\u1EB6'||LA12_0=='\u1EB8'||LA12_0=='\u1EBA'||LA12_0=='\u1EBC'||LA12_0=='\u1EBE'||LA12_0=='\u1EC0'||LA12_0=='\u1EC2'||LA12_0=='\u1EC4'||LA12_0=='\u1EC6'||LA12_0=='\u1EC8'||LA12_0=='\u1ECA'||LA12_0=='\u1ECC'||LA12_0=='\u1ECE'||LA12_0=='\u1ED0'||LA12_0=='\u1ED2'||LA12_0=='\u1ED4'||LA12_0=='\u1ED6'||LA12_0=='\u1ED8'||LA12_0=='\u1EDA'||LA12_0=='\u1EDC'||LA12_0=='\u1EDE'||LA12_0=='\u1EE0'||LA12_0=='\u1EE2'||LA12_0=='\u1EE4'||LA12_0=='\u1EE6'||LA12_0=='\u1EE8'||LA12_0=='\u1EEA'||LA12_0=='\u1EEC'||LA12_0=='\u1EEE'||LA12_0=='\u1EF0'||LA12_0=='\u1EF2'||LA12_0=='\u1EF4'||LA12_0=='\u1EF6'||LA12_0=='\u1EF8') ) {
            alt12=1;
        }
        else if ( ((LA12_0 >= 'a' && LA12_0 <= 'z')||(LA12_0 >= '\u00E0' && LA12_0 <= '\u00E3')||(LA12_0 >= '\u00E8' && LA12_0 <= '\u00EA')||(LA12_0 >= '\u00EC' && LA12_0 <= '\u00ED')||(LA12_0 >= '\u00F2' && LA12_0 <= '\u00F5')||(LA12_0 >= '\u00F9' && LA12_0 <= '\u00FA')||LA12_0=='\u00FD'||LA12_0=='\u0103'||LA12_0=='\u0111'||LA12_0=='\u0129'||LA12_0=='\u0169'||LA12_0=='\u01A1'||LA12_0=='\u01B0'||LA12_0=='\u1EA1'||LA12_0=='\u1EA3'||LA12_0=='\u1EA5'||LA12_0=='\u1EA7'||LA12_0=='\u1EA9'||LA12_0=='\u1EAB'||LA12_0=='\u1EAD'||LA12_0=='\u1EAF'||LA12_0=='\u1EB1'||LA12_0=='\u1EB3'||LA12_0=='\u1EB5'||LA12_0=='\u1EB7'||LA12_0=='\u1EB9'||LA12_0=='\u1EBB'||LA12_0=='\u1EBD'||LA12_0=='\u1EBF'||LA12_0=='\u1EC1'||LA12_0=='\u1EC3'||LA12_0=='\u1EC5'||LA12_0=='\u1EC7'||LA12_0=='\u1EC9'||LA12_0=='\u1ECB'||LA12_0=='\u1ECD'||LA12_0=='\u1ECF'||LA12_0=='\u1ED1'||LA12_0=='\u1ED3'||LA12_0=='\u1ED5'||LA12_0=='\u1ED7'||LA12_0=='\u1ED9'||LA12_0=='\u1EDB'||LA12_0=='\u1EDD'||LA12_0=='\u1EDF'||LA12_0=='\u1EE1'||LA12_0=='\u1EE3'||LA12_0=='\u1EE5'||LA12_0=='\u1EE7'||LA12_0=='\u1EE9'||LA12_0=='\u1EEB'||LA12_0=='\u1EED'||LA12_0=='\u1EEF'||LA12_0=='\u1EF1'||LA12_0=='\u1EF3'||LA12_0=='\u1EF5'||LA12_0=='\u1EF7'||LA12_0=='\u1EF9') ) {
            alt12=1;
        }
        else if ( (LA12_0=='-') ) {
            int LA12_3 = input.LA(2);

            if ( ((LA12_3 >= '0' && LA12_3 <= '9')) ) {
                alt12=1;
            }
            else {
                alt12=9;
            }
        }
        else if ( ((LA12_0 >= '0' && LA12_0 <= '9')) ) {
            alt12=1;
        }
        else if ( (LA12_0=='#') ) {
            alt12=2;
        }
        else if ( (LA12_0=='&') ) {
            alt12=3;
        }
        else if ( (LA12_0=='/') ) {
            alt12=4;
        }
        else if ( (LA12_0=='<') ) {
            alt12=5;
        }
        else if ( (LA12_0=='>') ) {
            alt12=6;
        }
        else if ( (LA12_0=='=') ) {
            alt12=7;
        }
        else if ( (LA12_0=='@') ) {
            alt12=8;
        }
        else if ( (LA12_0=='.') ) {
            alt12=9;
        }
        else if ( (LA12_0=='?') ) {
            alt12=9;
        }
        else if ( (LA12_0=='!') ) {
            alt12=9;
        }
        else if ( (LA12_0==':') ) {
            alt12=9;
        }
        else if ( (LA12_0==';') ) {
            alt12=9;
        }
        else if ( (LA12_0==',') ) {
            alt12=9;
        }
        else if ( (LA12_0=='+') ) {
            int LA12_18 = input.LA(2);

            if ( ((LA12_18 >= '0' && LA12_18 <= '9')) ) {
                alt12=1;
            }
            else {
                alt12=12;
            }
        }
        else if ( (LA12_0=='\"') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\'') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\u201C') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\u201D') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\u2018') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\u2019') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\u00AB') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\u00BB') ) {
            alt12=9;
        }
        else if ( (LA12_0=='\u2026') ) {
            alt12=9;
        }
        else if ( (LA12_0=='(') ) {
            alt12=9;
        }
        else if ( (LA12_0==')') ) {
            alt12=9;
        }
        else if ( (LA12_0=='[') ) {
            alt12=9;
        }
        else if ( (LA12_0==']') ) {
            alt12=9;
        }
        else if ( (LA12_0=='{') ) {
            alt12=9;
        }
        else if ( (LA12_0=='}') ) {
            alt12=9;
        }
        else if ( (LA12_0=='$') ) {
            alt12=10;
        }
        else if ( (LA12_0=='*') ) {
            alt12=11;
        }
        else if ( (LA12_0=='\\') ) {
            alt12=13;
        }
        else if ( (LA12_0=='_') ) {
            alt12=14;
        }
        else if ( (LA12_0=='\u00A3') ) {
            alt12=15;
        }
        else if ( ((LA12_0 >= '\t' && LA12_0 <= '\n')||(LA12_0 >= '\f' && LA12_0 <= '\r')||LA12_0==' ') ) {
            alt12=16;
        }
        else if ( ((LA12_0 >= '\u0000' && LA12_0 <= '\b')||LA12_0=='\u000B'||(LA12_0 >= '\u000E' && LA12_0 <= '\u001F')||LA12_0=='%'||LA12_0=='^'||LA12_0=='`'||LA12_0=='|'||(LA12_0 >= '~' && LA12_0 <= '\u00A2')||(LA12_0 >= '\u00A4' && LA12_0 <= '\u00AA')||(LA12_0 >= '\u00AC' && LA12_0 <= '\u00BA')||(LA12_0 >= '\u00BC' && LA12_0 <= '\u00BF')||(LA12_0 >= '\u00C4' && LA12_0 <= '\u00C7')||LA12_0=='\u00CB'||(LA12_0 >= '\u00CE' && LA12_0 <= '\u00D1')||(LA12_0 >= '\u00D6' && LA12_0 <= '\u00D8')||(LA12_0 >= '\u00DB' && LA12_0 <= '\u00DC')||(LA12_0 >= '\u00DE' && LA12_0 <= '\u00DF')||(LA12_0 >= '\u00E4' && LA12_0 <= '\u00E7')||LA12_0=='\u00EB'||(LA12_0 >= '\u00EE' && LA12_0 <= '\u00F1')||(LA12_0 >= '\u00F6' && LA12_0 <= '\u00F8')||(LA12_0 >= '\u00FB' && LA12_0 <= '\u00FC')||(LA12_0 >= '\u00FE' && LA12_0 <= '\u0101')||(LA12_0 >= '\u0104' && LA12_0 <= '\u010F')||(LA12_0 >= '\u0112' && LA12_0 <= '\u0127')||(LA12_0 >= '\u012A' && LA12_0 <= '\u0167')||(LA12_0 >= '\u016A' && LA12_0 <= '\u019F')||(LA12_0 >= '\u01A2' && LA12_0 <= '\u01AE')||(LA12_0 >= '\u01B1' && LA12_0 <= '\u1E9F')||(LA12_0 >= '\u1EFA' && LA12_0 <= '\u2017')||(LA12_0 >= '\u201A' && LA12_0 <= '\u201B')||(LA12_0 >= '\u201E' && LA12_0 <= '\u2025')||(LA12_0 >= '\u2027' && LA12_0 <= '\uFFFF')) ) {
            alt12=17;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 12, 0, input);

            throw nvae;

        }
        switch (alt12) {
            case 1 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:10: ENTITY
                {
                mENTITY(); if (state.failed) return ;


                }
                break;
            case 2 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:17: NUMBERSIGN
                {
                mNUMBERSIGN(); if (state.failed) return ;


                }
                break;
            case 3 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:28: AMPERSAND
                {
                mAMPERSAND(); if (state.failed) return ;


                }
                break;
            case 4 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:38: FSLASH
                {
                mFSLASH(); if (state.failed) return ;


                }
                break;
            case 5 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:45: LANGLE
                {
                mLANGLE(); if (state.failed) return ;


                }
                break;
            case 6 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:52: RANGLE
                {
                mRANGLE(); if (state.failed) return ;


                }
                break;
            case 7 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:59: EQUAL
                {
                mEQUAL(); if (state.failed) return ;


                }
                break;
            case 8 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:65: AROBA
                {
                mAROBA(); if (state.failed) return ;


                }
                break;
            case 9 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:71: PUNCTUATION
                {
                mPUNCTUATION(); if (state.failed) return ;


                }
                break;
            case 10 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:83: DOLLAR
                {
                mDOLLAR(); if (state.failed) return ;


                }
                break;
            case 11 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:90: ASTERISK
                {
                mASTERISK(); if (state.failed) return ;


                }
                break;
            case 12 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:99: PLUS
                {
                mPLUS(); if (state.failed) return ;


                }
                break;
            case 13 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:104: BSLASH
                {
                mBSLASH(); if (state.failed) return ;


                }
                break;
            case 14 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:111: UNDERSCORE
                {
                mUNDERSCORE(); if (state.failed) return ;


                }
                break;
            case 15 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:122: POUND
                {
                mPOUND(); if (state.failed) return ;


                }
                break;
            case 16 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:128: SPACE
                {
                mSPACE(); if (state.failed) return ;


                }
                break;
            case 17 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:1:134: RESIDUAL
                {
                mRESIDUAL(); if (state.failed) return ;


                }
                break;

        }

    }

    // $ANTLR start synpred1_VLexer
    public final void synpred1_VLexer_fragment() throws RecognitionException {
        // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:35:5: ( ( NUMBERSIGN )? DIGIT | VLETTER )
        int alt14=2;
        int LA14_0 = input.LA(1);

        if ( (LA14_0=='#'||(LA14_0 >= '0' && LA14_0 <= '9')) ) {
            alt14=1;
        }
        else if ( ((LA14_0 >= 'A' && LA14_0 <= 'Z')||(LA14_0 >= 'a' && LA14_0 <= 'z')||(LA14_0 >= '\u00C0' && LA14_0 <= '\u00C3')||(LA14_0 >= '\u00C8' && LA14_0 <= '\u00CA')||(LA14_0 >= '\u00CC' && LA14_0 <= '\u00CD')||(LA14_0 >= '\u00D2' && LA14_0 <= '\u00D5')||(LA14_0 >= '\u00D9' && LA14_0 <= '\u00DA')||LA14_0=='\u00DD'||(LA14_0 >= '\u00E0' && LA14_0 <= '\u00E3')||(LA14_0 >= '\u00E8' && LA14_0 <= '\u00EA')||(LA14_0 >= '\u00EC' && LA14_0 <= '\u00ED')||(LA14_0 >= '\u00F2' && LA14_0 <= '\u00F5')||(LA14_0 >= '\u00F9' && LA14_0 <= '\u00FA')||LA14_0=='\u00FD'||(LA14_0 >= '\u0102' && LA14_0 <= '\u0103')||(LA14_0 >= '\u0110' && LA14_0 <= '\u0111')||(LA14_0 >= '\u0128' && LA14_0 <= '\u0129')||(LA14_0 >= '\u0168' && LA14_0 <= '\u0169')||(LA14_0 >= '\u01A0' && LA14_0 <= '\u01A1')||(LA14_0 >= '\u01AF' && LA14_0 <= '\u01B0')||(LA14_0 >= '\u1EA0' && LA14_0 <= '\u1EF9')) ) {
            alt14=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 14, 0, input);

            throw nvae;

        }
        switch (alt14) {
            case 1 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:35:6: ( NUMBERSIGN )? DIGIT
                {
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:35:6: ( NUMBERSIGN )?
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='#') ) {
                    alt13=1;
                }
                switch (alt13) {
                    case 1 :
                        // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:
                        {
                        if ( input.LA(1)=='#' ) {
                            input.consume();
                            state.failed=false;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            MismatchedSetException mse = new MismatchedSetException(null,input);
                            recover(mse);
                            throw mse;
                        }


                        }
                        break;

                }


                mDIGIT(); if (state.failed) return ;


                }
                break;
            case 2 :
                // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:35:26: VLETTER
                {
                mVLETTER(); if (state.failed) return ;


                }
                break;

        }
    }
    // $ANTLR end synpred1_VLexer

    // $ANTLR start synpred2_VLexer
    public final void synpred2_VLexer_fragment() throws RecognitionException {
        // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:72:5: ( DECIMAL_POINT DIGIT )
        // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:72:6: DECIMAL_POINT DIGIT
        {
        mDECIMAL_POINT(); if (state.failed) return ;


        mDIGIT(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred2_VLexer

    // $ANTLR start synpred3_VLexer
    public final void synpred3_VLexer_fragment() throws RecognitionException {
        // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:91:4: ( ELLIPSIS )
        // D:\\data\\home\\cumeo89\\projects\\ePi\\vnTokenizer\\VLexer.g:91:5: ELLIPSIS
        {
        mELLIPSIS(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred3_VLexer

    public final boolean synpred2_VLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_VLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_VLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_VLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_VLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_VLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

}
