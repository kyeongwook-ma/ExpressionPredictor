package char_recognizer;

public class ExpChar {
	public final static int NUMBER = 37; 
	
	public final static int P_ZERO = 0;
	public final static int P_ONE = 1;
	public final static int P_TWO = 2;
	public final static int P_THREE = 3;
	public final static int P_FOUR = 4;
	public final static int P_FIVE = 5;
	public final static int P_SIX = 6;
	public final static int P_SEVEN = 7;
	public final static int P_EIGHT = 8;
	public final static int P_NINE = 9;
	
	public final static int P_PLUS = 10;
	public final static int P_MINUS = 11;
	public final static int P_OPENBRACKET = 12;
	public final static int P_CLOSEBRACKET = 13;
	public final static int P_SMULTI = 14;
	public final static int P_XMULTI = 15;
	public final static int P_DMULTI = 16;
	public final static int P_SDIVIDE = 17;
	public final static int P_DDIVIDE = 18;
	
	public final static int P_S = 19;
	public final static int P_I = 20;
	public final static int P_N = 21;
	public final static int P_C = 22;
	public final static int P_O = 23;
	public final static int P_T = 24;
	public final static int P_A = 25;
	public final static int P_X = 26;
	public final static int P_Y = 27;
	public final static int P_Z = 28;
	public final static int P_H = 29;
	public final static int P_E = 30;
	public final static int P_G = 31;
	public final static int P_L = 32;
	
	public final static int P_EQUAL = 33;
	public final static int P_DOT = 34;
	public final static int P_PI = 35;
	public final static int P_NOTHING = 36;
	
	public static int getStringToExpChar(String c) {
		switch(c) {
		case "0" : return ExpChar.P_ZERO;
		case "1" : return ExpChar.P_ONE;
		case "2" : return ExpChar.P_TWO;
		case "3" : return ExpChar.P_THREE;
		case "4" : return ExpChar.P_FOUR;
		case "5" : return ExpChar.P_FIVE;
		case "6" : return ExpChar.P_SIX;
		case "7" : return ExpChar.P_SEVEN;
		case "8" : return ExpChar.P_EIGHT;
		case "9" : return ExpChar.P_NINE;
		case "+" : return ExpChar.P_PLUS;
		case "-" : return ExpChar.P_MINUS;
		case "(" : return ExpChar.P_OPENBRACKET;
		case ")" : return ExpChar.P_CLOSEBRACKET;
		case "*" : return ExpChar.P_SMULTI;
		case "&*" : return ExpChar.P_XMULTI;
		case "~*" : return ExpChar.P_DMULTI;
		case "/" : return ExpChar.P_SDIVIDE;
		case "&/" : return ExpChar.P_DDIVIDE;
		case "s" : return ExpChar.P_S;
		case "i" : return ExpChar.P_I;
		case "n" : return ExpChar.P_N;
		case "c" : return ExpChar.P_C;
		case "o" : return ExpChar.P_O;
		case "t" : return ExpChar.P_T;
		case "a" : return ExpChar.P_A;
		case "x" : return ExpChar.P_X;
		case "y" : return ExpChar.P_Y;
		case "z" : return ExpChar.P_Z;
		case "h" : return ExpChar.P_H;
		case "e" : return ExpChar.P_E;
		case "g" : return ExpChar.P_G;
		case "l" : return ExpChar.P_L;
		case "=" : return ExpChar.P_EQUAL;
		case "." : return ExpChar.P_DOT;
		case "&P" : return ExpChar.P_PI;
		default : return ExpChar.P_NOTHING;
		}
	}
	
	public static String getExpCharToString(int expchar) {
		switch(expchar) {
		case ExpChar.P_ZERO : return "0";
		case ExpChar.P_ONE : return "1";
		case ExpChar.P_TWO : return "2";
		case ExpChar.P_THREE : return "3";
		case ExpChar.P_FOUR : return "4";
		case ExpChar.P_FIVE : return "5";
		case ExpChar.P_SIX : return "6";
		case ExpChar.P_SEVEN : return "7";
		case ExpChar.P_EIGHT : return "8";
		case ExpChar.P_NINE : return "9";
		case ExpChar.P_PLUS : return "+";
		case ExpChar.P_MINUS : return "-";
		case ExpChar.P_OPENBRACKET : return "(";
		case ExpChar.P_CLOSEBRACKET : return ")";
		case ExpChar.P_SMULTI : ;
		case ExpChar.P_XMULTI : ;
		case ExpChar.P_DMULTI : return "*";
		case ExpChar.P_SDIVIDE : ;
		case ExpChar.P_DDIVIDE : return "/";
		case ExpChar.P_S : return "s";
		case ExpChar.P_I : return "i";
		case ExpChar.P_N : return "n";
		case ExpChar.P_C : return "c";
		case ExpChar.P_O : return "o";
		case ExpChar.P_T : return "t";
		case ExpChar.P_A : return "a";
		case ExpChar.P_X : return "x";
		case ExpChar.P_Y : return "y";
		case ExpChar.P_Z : return "z";
		case ExpChar.P_H : return "h";
		case ExpChar.P_E : return "e";
		case ExpChar.P_G : return "g";
		case ExpChar.P_L : return "l";
		case ExpChar.P_EQUAL : return "=";
		case ExpChar.P_DOT : return ".";
		case ExpChar.P_PI : return "PI";
		default : return "_";
		}
	}
}
