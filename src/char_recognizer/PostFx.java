package char_recognizer;
import java.util.ArrayList;


public class PostFx {
	private ArrayList<PredictDataVO> data;
	private int up;
	private int down;
	
	public PostFx() {
		this.data = new ArrayList<>();
		this.up = 0;
		this.down = 0;
	}
	
	public PostFx(ArrayList<PredictDataVO> src) {
		this.setData(src);
		this.up = 0;
		this.down = 0;
	}
	
	public ArrayList<PredictDataVO> getData() {
		return data;
	}
	
	public void setData(ArrayList<PredictDataVO> src) {
		data = new ArrayList<PredictDataVO>(src);
		this.up = 0;
		this.down = 0;
	}
	
	public void fixPredictExpression() {
		//Classify Zero(0) and Minus(-)
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).expchar == ExpChar.getStringToExpChar("0") 
					&& (data.get(i).rect.width/4 > data.get(i).rect.height))
				data.get(i).expchar = ExpChar.getStringToExpChar("-");
			if(data.get(i).expchar == ExpChar.getStringToExpChar("-") 
					&& (data.get(i).rect.width < data.get(i).rect.height))
				data.get(i).expchar = ExpChar.getStringToExpChar("0");
		}
		
		//When a certain percentage above is treated as superscript.
		for(int i = 1; i < data.size(); i++) {
			if(data.get(i-1).expchar == ExpChar.P_CLOSEBRACKET) {
				if((data.get(i-1).rect.y + data.get(i-1).rect.height/2) 
						>= (data.get(i).rect.y + data.get(i).rect.height)) {
					data.get(i).up = true;
				}
			}
			if(((data.get(i-1).rect.y + data.get(i-1).rect.height/3) 
					>= (data.get(i).rect.y + data.get(i).rect.height))
				&&((data.get(i-1).rect.y + data.get(i-1).rect.height/15) 
					>= (data.get(i).rect.y + data.get(i).rect.height/2))) {
				data.get(i).up = true;
			}
			if((data.get(i-1).expchar >= ExpChar.getStringToExpChar("0")
					&& data.get(i-1).expchar <= ExpChar.getStringToExpChar("9")
				|| (data.get(i-1).expchar == ExpChar.getStringToExpChar("t"))
				|| (data.get(i-1).expchar == ExpChar.getStringToExpChar("h")))) {
				if(data.get(i-1).rect.y + data.get(i-1).rect.height/2 
						>= data.get(i).rect.y + data.get(i).rect.height) {
					data.get(i).up = true;
				}
			}
			if((data.get(i).expchar == ExpChar.getStringToExpChar("."))
				&& ((data.get(i-1).rect.y + data.get(i-1).rect.height/3)
					>= (data.get(i).rect.y + data.get(i).rect.height/2))) {
				data.get(i).up = true;
			}
		}
		
		//The following letter is below a certain percentage of the subscript is treated as.
		for(int i = 0; i < data.size()-1; i++) {
			if((data.get(i).rect.y + data.get(i).rect.height*7/8) 
					<= (data.get(i+1).rect.y + data.get(i+1).rect.height/2)) {
				if(i != 0) {
					if((data.get(i).expchar == ExpChar.P_PLUS)
						|| (data.get(i).expchar == ExpChar.P_MINUS)
						|| (data.get(i).expchar == ExpChar.P_SMULTI)
						|| (data.get(i).expchar == ExpChar.P_DDIVIDE)
						|| (data.get(i).expchar == ExpChar.P_SDIVIDE)) {
						data.get(i).down = false;
					}
					else {
						data.get(i).down = true;
					}
				}
			}
		}
		
		//Superscript letters are located at the end of the string, when you insert a closing parenthesis.
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).up == true) {
				for(int j = i; j < data.size(); j++) {
					if(data.get(j).down == true)
						break;
					if(j == data.size() - 1) 
						data.get(j).down = true;
				}
			}
		}
		
		//Distinguish subscripts.
		for(int i = 1; i < data.size(); i++) {
			if((data.get(i-1).rect.y + data.get(i-1).rect.height/3 <= data.get(i).rect.y)
				&& (data.get(i-1).rect.y + data.get(i-1).rect.height*3/4 <= data.get(i).rect.y + data.get(i).rect.height)
				&& (data.get(i-1).down == false)
				&& ((data.get(i-1).rect.height * data.get(i-1).rect.width)/3 
					>= data.get(i).rect.height * data.get(i).rect.width)
				&& ((data.get(i-1).expchar <= ExpChar.getStringToExpChar("+"))
					|| (data.get(i-1).expchar >= ExpChar.getStringToExpChar("&/")))) {
				data.get(i).subsdown = true;
				data.get(i).subsup = true;
			}
				
		}
	}
	
	public String toString() {
		String result = new String("");
		
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).up == true) 
				result += "^(";
			if(data.get(i).subsdown == true)
				result += "_(";
			result += ExpChar.getExpCharToString(data.get(i).expchar);
			if(data.get(i).down == true)
				result += ")";
			if(data.get(i).subsup == true)
				result += ")";
		}
		
		return result;
	}
	
	public String fixStringExpression(String exp) {
		String expression = new String(exp);
		
		//Modifies the text does not fit the context.
		for(int i = 0; i < expression.length(); i++) {
			if(expression.charAt(i) == 'c')	{
				expression = expression.replace("c0s", "cos");
				expression = expression.replace("c08", "cos");
				expression = expression.replace("co8", "cos");
				expression = expression.replace("ccs", "cos");
				expression = expression.replace("c0t", "cot");
				expression = expression.replace("co(", "cot");
				expression = expression.replace("c0(", "cot");
			}
					
			if(expression.charAt(i) == 's')	{
				expression = expression.replace("s1n", "sin");
				expression = expression.replace("sln", "sin");
				expression = expression.replace("snn", "sin");
				expression = expression.replace("san", "sin");
				expression = expression.replace("sPIn", "sin");
				expression = expression.replace("s2n", "sin");
				expression = expression.replace("s2l2", "sin");
				expression = expression.replace("s212", "sin");
				expression = expression.replace("sel2", "sin");
				expression = expression.replace("se12", "sin");
				expression = expression.replace("sill", "sin");
				expression = expression.replace("si1l", "sin");
				expression = expression.replace("sil1", "sin");
				expression = expression.replace("si11", "sin");
				expression = expression.replace("sl11", "sin");
				expression = expression.replace("sll1", "sin");
				expression = expression.replace("sl1l", "sin");
				expression = expression.replace("slll", "sin");
				expression = expression.replace("s11l", "sin");
				expression = expression.replace("s1l1", "sin");
				expression = expression.replace("si12", "sin");
				expression = expression.replace("s112", "sin");
				expression = expression.replace("sl12", "sin");
				expression = expression.replace("sil2", "sin");
				expression = expression.replace("s1l2", "sin");
				expression = expression.replace("sll2", "sin");
				expression = expression.replace("oos", "cos");
				expression = expression.replace("60s", "cos");
				expression = expression.replace("s0c", "sec");
				expression = expression.replace("s6c", "sec");
				expression = expression.replace("scc", "sec");
			}
			
			if(expression.charAt(i) == '8')	{
				expression = expression.replace("8in", "sin");
				expression = expression.replace("81n", "sin");
				expression = expression.replace("8ln", "sin");
			}
			
			if(expression.charAt(i) == 'g' || expression.charAt(i) == '9') {
				expression = expression.replace("1og", "log");
				expression = expression.replace("1o9", "log");
				expression = expression.replace("10g", "log");
				expression = expression.replace("109", "log");
				expression = expression.replace("lo9", "log");
				expression = expression.replace("l0g", "log");
				expression = expression.replace("l09", "log");
			}
			
			if(expression.charAt(i) == 'l') { 
				expression = expression.replace("lll", "ln");
				expression = expression.replace("l1l", "ln");
				expression = expression.replace("ll1", "ln");
				expression = expression.replace("l11", "ln");
			}
			
			if(expression.charAt(i) == 't') {
				expression = expression.replace("tall", "tan");
				expression = expression.replace("ta1l", "tan");
				expression = expression.replace("tal1", "tan");
				expression = expression.replace("ta11", "tan");
				expression = expression.replace("ta12", "tan");
				expression = expression.replace("tal2", "tan");
				expression = expression.replace("toll", "tan");
				expression = expression.replace("tol1", "tan");
				expression = expression.replace("to1l", "tan");
				expression = expression.replace("to11", "tan");
				expression = expression.replace("to12", "tan");
				expression = expression.replace("tol2", "tan");
				expression = expression.replace("t011", "tan");
				expression = expression.replace("t01l", "tan");
				expression = expression.replace("t0l1", "tan");
				expression = expression.replace("t0ll", "tan");
				expression = expression.replace("t012", "tan");
				expression = expression.replace("t0l2", "tan");
			}
			
			if(expression.charAt(i) == 'n') {                                 
				expression = expression.replace("1n", "ln");
				expression = expression.replace("1an", "tan");
				expression = expression.replace("lan", "tan");
				expression = expression.replace("ton", "tan");
				expression = expression.replace("t0n", "tan");
				expression = expression.replace("oan", "tan");
				expression = expression.replace("(an", "tan");
				expression = expression.replace("1in", "sin");
			}
			
			if(expression.charAt(i) < 58 || expression.charAt(i) > 47) {
				for( int j = 0; j < 10; j++)
					for( int k = 0; k < 10; k++)
						expression = expression.replace(j+"x"+k, j+"*"+k);			
			}
		}
		
		//Count Member Variable Up and Down.
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).up == true)
				this.up++;
			if(data.get(i).down == true)
				this.down++;
		}
		
		//Last parenthesized.
		for(int i = 0; i < (this.up - this.down); i++)
			expression += ")";
		
		return expression;
	}
}