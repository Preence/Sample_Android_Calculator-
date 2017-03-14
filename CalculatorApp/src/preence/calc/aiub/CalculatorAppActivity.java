package preence.calc.aiub;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;
 
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.media.MediaPlayer;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
 
public class CalculatorAppActivity extends Activity implements OnClickListener {
 
	public static final String PREFERENCES_FILE_NAME="MyPrefs";
	public static final String FILE_NAME="Storage.dat";
	public static DatabaseHandler db;
	private Button history;
	private String a,b,op;
    private TextView mCalculatorDisplay;
    private Boolean userTypingANumber = false;
    private Boolean result=false;
    private CalculatorBrain mCalculatorBrain;
    private static final String DIGITS = "0123456789.";
    private MediaPlayer media;
 
    DecimalFormat df = new DecimalFormat("@##########");
 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        db=new DatabaseHandler(CalculatorAppActivity.this);
 
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
        findViewById(R.id.history).setOnClickListener(this);
        
        this.history=this.history=(Button)findViewById(R.id.history);
        this.a="";
		this.b="";
		this.op="";
        
        Bundle extras=getIntent().getExtras();
		if(extras!=null) {
			this.history.setText(extras.getString("history"));
			this. mCalculatorDisplay.setText(extras.getString("result"));
			if(extras.getString("history")!=null) {
				String parts[]=extras.getString("history").split(" ");
				this.a=extras.getString("result");
				this.op=parts[1];
				this.b=parts[2];
			}
		}
    }
    
 
    
    public void onClick(View v) {
 
        String buttonPressed = ((Button) v).getText().toString();
        if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Intent i=new Intent(getApplicationContext(),CalculatorBrain.class);
		i.putExtra("history", this.history.getText());
 
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1) {
			if(resultCode==RESULT_OK) {
				Log.d("OnActivity Result",data.getStringExtra("result"));
				this.a=data.getStringExtra("result");
				this.mCalculatorDisplay.setText(this.a);
				InsertDb(this.history.getText().toString(), this.a);
			}
			
		}
	}
    @Override
	protected void onResume() {
		super.onResume();
		Log.d("EntryLog","Inside OnResume of Main Activity");
		this.media=MediaPlayer.create(this,R.raw.p);
		
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
    @Override
	protected void onPause() {
		super.onPause();
		Log.d("EntryLog","Inside OnPause of Main Activity");
		this.media.release();
		this.media=null;
	}
    public void onClear(View arg0) {
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Log.d("DebugLog","Inside onClear");
		
	}
    public void onHistory(View arg0) {
		Log.d("DebugLog","Inside OnHistory");
		Intent i=new Intent(getApplicationContext(),HistoryActivity.class);
		startActivity(i);
	}
    public void InsertDb(String data,String data2) {
		SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy hh:mm");
		String date=df.format(new Date());
		db.addHistory(new History(date,data,data2));
	}
    public void WriteFile(String data,String data2) {
		FileOutputStream fs;
		SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy hh:mm");
		String date=df.format(new Date());
		
		try{
			fs=openFileOutput(FILE_NAME, MODE_PRIVATE|MODE_APPEND);
			fs.write(date.getBytes());
			fs.write(System.getProperty("line.separator").getBytes());
			fs.write(data.getBytes());
			fs.write(System.getProperty("line.separator").getBytes());
			fs.write(data2.getBytes());
			fs.write(System.getProperty("line.separator").getBytes());
			fs.flush();
			fs.close();
		}
		catch(Exception e) {
			Log.d("ExceptionLog",e.getMessage());
		}
	}
   
  
}