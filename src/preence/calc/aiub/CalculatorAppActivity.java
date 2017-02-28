package preence.calc.aiub;

import java.text.DecimalFormat;
 
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
 
public class CalculatorAppActivity extends Activity implements OnClickListener {
 
    private TextView mCalculatorDisplay;
    private Boolean userTypingANumber = false;
    private Boolean result=false;
    private CalculatorBrain mCalculatorBrain;
    private static final String DIGITS = "0123456789.";
 
    DecimalFormat df = new DecimalFormat("@##########");
 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        mCalculatorBrain = new CalculatorBrain();
        mCalculatorDisplay = (TextView) findViewById(R.id.textView1);
 
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
 
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSubtract).setOnClickListener(this);
        findViewById(R.id.buttonMultiply).setOnClickListener(this);
        findViewById(R.id.buttonDivide).setOnClickListener(this);
        findViewById(R.id.buttonDecimalPoint).setOnClickListener(this);
        findViewById(R.id.buttonEquals).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        findViewById(R.id.buttonClearMemory).setOnClickListener(this);
        findViewById(R.id.buttonAddToMemory).setOnClickListener(this);
        findViewById(R.id.buttonSubtractFromMemory).setOnClickListener(this);
        findViewById(R.id.buttonRecallMemory).setOnClickListener(this);
    }
    
 
    
    public void onClick(View v) {
 
        String buttonPressed = ((Button) v).getText().toString();
 
        if (DIGITS.contains(buttonPressed)) {
 
            if (userTypingANumber) {
 
                if (buttonPressed.equals(".") && mCalculatorDisplay.getText().toString().contains(".")) {
                   
                } else {
                    mCalculatorDisplay.append(buttonPressed);
                }
 
            } else {
 
                if (buttonPressed.equals(".")) {
                    mCalculatorDisplay.setText(0 + buttonPressed);
                } else {
                    mCalculatorDisplay.setText(buttonPressed);
                }
 
                userTypingANumber = true;
            }
 
        } else {
            // operation was pressed
            if (userTypingANumber) {
 
                mCalculatorBrain.setOperand(Double.parseDouble(mCalculatorDisplay.getText().toString()));
                userTypingANumber = false;
            }
            
            
            mCalculatorBrain.performOperation(buttonPressed);
            mCalculatorDisplay.setText(df.format(mCalculatorBrain.getResult()));
            result=true;
            
           
            /*if(buttonPressed.equals("="))
            {
            	 mCalculatorBrain.performOperation(buttonPressed);
                 mCalculatorDisplay.setText(df.format(mCalculatorBrain.getResult()));
            }*/
            
            
 
        }
 
    }
 
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble("OPERAND", mCalculatorBrain.getResult());
        outState.putDouble("MEMORY", mCalculatorBrain.getMemory());
    }
 
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCalculatorBrain.setOperand(savedInstanceState.getDouble("OPERAND"));
        mCalculatorBrain.setMemory(savedInstanceState.getDouble("MEMORY"));
        mCalculatorDisplay.setText(df.format(mCalculatorBrain.getResult()));
    }
  
}