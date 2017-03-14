package preence.calc.aiub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CalculatorBrain extends Activity {
	
	 private double mOperand;
	    private double mWaitingOperand;
	    private String mWaitingOperator;
	    private double mCalculatorMemory;
	 
	    // operator types
	    public static final String ADD = "+";
	    public static final String SUBTRACT = "-";
	    public static final String MULTIPLY = "*";
	    public static final String DIVIDE = "/";
	 
	    public static final String CLEAR = "AC" ;
	    public static final String CLEARMEMORY = "MC";
	    public static final String ADDTOMEMORY = "M+";
	    public static final String SUBTRACTFROMMEMORY = "M-";
	    public static final String RECALLMEMORY = "MR";
	    public static final String history = "HIS";
	   
	   
	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle extras=getIntent().getExtras();
			//this.performOperation(mWaitingOperator);
		}
	   
	   
	 
	    // public static final String EQUALS = "=";
	 
	    public CalculatorBrain() {
	        mOperand = 0;
	        mWaitingOperand = 0;
	        mWaitingOperator = "";
	        mCalculatorMemory = 0;
	    }
	 
	    public void setOperand(double operand) {
	        mOperand = operand;
	    }
	 
	    public double getResult() {
	        return mOperand;
	    }
	 
	    public void setMemory(double calculatorMemory) {
	        mCalculatorMemory = calculatorMemory;
	    }
	 
	    public double getMemory() {
	        return mCalculatorMemory;
	    }
	 
	    public String toString() {
	        return Double.toString(mOperand);
	    }
	 
	    protected double performOperation(String operator) {
	    	Intent intent=new Intent();
			Bundle extra=new Bundle();
	         
		 if (operator.equals(CLEAR)) {
	            mOperand = 0;
	            mWaitingOperator = "";
	            mWaitingOperand = 0;
	            // mCalculatorMemory = 0;
	        } else if (operator.equals(CLEARMEMORY)) {
	            mCalculatorMemory = 0;
	        } else if (operator.equals(ADDTOMEMORY)) {
	            mCalculatorMemory += mOperand;
	        } else if (operator.equals(SUBTRACTFROMMEMORY)) {
	            mCalculatorMemory -= mOperand;
	        } else if (operator.equals(RECALLMEMORY)) {
	            mOperand = mCalculatorMemory;        
	        }	       
	         else {
	            performWaitingOperation();
	            mWaitingOperator = operator;
	            mWaitingOperand = mOperand;
	            setResult(RESULT_OK,intent);
				intent.putExtra("result", mOperand);
	        }
	 
	        return mOperand;
	    }
	 
	    protected void performWaitingOperation() {
	 
	        if (mWaitingOperator.equals(ADD)) {
	            mOperand = mWaitingOperand + mOperand;
	        } else if (mWaitingOperator.equals(SUBTRACT)) {
	            mOperand = mWaitingOperand - mOperand;
	        } else if (mWaitingOperator.equals(MULTIPLY)) {
	            mOperand = mWaitingOperand * mOperand;
	        } else if (mWaitingOperator.equals(DIVIDE)) {
	            if (mOperand != 0) {
	                mOperand = mWaitingOperand / mOperand;
	            }
	        }
	 
	    }
	}


