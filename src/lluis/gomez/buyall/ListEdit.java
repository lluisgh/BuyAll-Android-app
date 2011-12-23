package lluis.gomez.buyall;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ListEdit extends Activity {
	private Spinner mEstablishmentSpinner;
	private DatePicker mDatePicker;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String mDateText;
	private Long mRowId;
	private String mEstablishment;
	private BuyAllDbAdapter mDbHelper;
	private ArrayList<String> mArray;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * Accés a la BD
		 */
		mDbHelper = new BuyAllDbAdapter(this);
		
		/**
		 * S'obté el layout
		 */
		setContentView(R.layout.list_edit);
		
		/**
		 * S'associen instàncies amb elements de la interfície
		 */
		mEstablishmentSpinner = (Spinner) findViewById(R.id.establishment_spinner);
        mDatePicker = (DatePicker) findViewById(R.id.date_picker);
						
		mEstablishmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				Cursor	c = (Cursor) mEstablishmentSpinner.getSelectedItem();
				mEstablishment = c.getString(c.getColumnIndex("name"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				mEstablishment = null;
			}
			
			
		});
		
		/**
		 * 
		 */
		mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(BuyAllDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ? extras.getLong(BuyAllDbAdapter.KEY_ROWID)
                                    : null;
        }
        
        //mDbHelper.createEstablishment("Supermercat");
        
        mYear = mDatePicker.getYear();
        mMonth = mDatePicker.getMonth();
        mDay = mDatePicker.getDayOfMonth();
		//mDateText = Integer.toString(mDay) + '/' + Integer.toString(mMonth + 1) + '/' + Integer.toString(mYear);	

        mEstablishment = null;
        
        /**
         * Omple els camps de la interfície
         */
		populateFields();
	}
	
	public void onClickButton(View v) {
		if (v.getId() == R.id.createEstablishmentButton) createEstablishment();
		else if (v.getId() == R.id.confirmButton) createList();
	}
	
	public void createList() {
		mDay = mDatePicker.getDayOfMonth();
		mMonth = mDatePicker.getMonth();
		mYear = mDatePicker.getYear();
		mDateText = Integer.toString(mDay) + '/' + Integer.toString(mMonth + 1) + '/' + Integer.toString(mYear);	
		if (mRowId == null) {
			long id = mDbHelper.createList(mEstablishment, mDay, mMonth, mYear, mDateText);
			if (id > 0) mRowId = id;
			
		} else {
			mDbHelper.updateList(mRowId, mEstablishment, mDay, mMonth, mYear, mDateText);
		}
		Intent i = new Intent(this, ListProducts.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, mRowId);
		finish();
        startActivity(i);
	}
	
	private void createEstablishment() {
		
	    final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  


		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View dialog = inflater.inflate(R.layout.new_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));

		alertDialog.setTitle("Crea establiment");
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Nom");
		final EditText edText = (EditText) dialog.findViewById(R.id.editText1);
		edText.setError("Has d'introduir un nom");
		alertDialog.setButton("Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
 	        	   if (edText.length() > 0) {
 	        		   	mEstablishment = edText.getText().toString(); 
 	        		   	mDbHelper.createEstablishment(mEstablishment);

 			       		populateFields();
 	        	   }
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();
	}

	private void populateFields() {
		Cursor c = mDbHelper.fetchAllEstablishments();
		startManagingCursor(c);
		
		// create an array to specify which fields we want to display
		String[] from = new String[]{BuyAllDbAdapter.KEY_NAME};
		// create an array of the display item we want to bind our data to
		int[] to = new int[]{android.R.id.text1};
		// create simple cursor adapter
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		// get reference to our spinner
		mEstablishmentSpinner.setAdapter(adapter);		
		
		mArray = new ArrayList<String>();
  		for (c.moveToFirst(); c.moveToNext(); c.isAfterLast()) {
  			
  			mArray.add(c.getString(1));
  		}
		
		if (mRowId != null) {	
			
			Cursor list = mDbHelper.fetchList(mRowId);	
			startManagingCursor(list);
			
			mYear = list.getInt(list.getColumnIndex("year"));
			mMonth = list.getInt(list.getColumnIndex("month"));
			mDay = list.getInt(list.getColumnIndex("day"));
			if (mEstablishment == null) mEstablishment = list.getString(list.getColumnIndex("establishment"));
			
			int index = mArray.indexOf(mEstablishment);
			mEstablishmentSpinner.setSelection(index + 1);
			
			mDateText = Integer.toString(mDay) + '/' + Integer.toString(mMonth + 1) + '/' + Integer.toString(mYear);
			mDatePicker.updateDate(mYear, mMonth, mDay);//(mYear, mMonth, mDay, dateSetListener);
		}
		
	}
	
	@Override
 	protected void onDestroy() {
	 	mDbHelper.close();
	 	super.onDestroy();
 	}

	 
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}
	@Override
	protected void onResume() {
		super.onResume();
        populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
        outState.putSerializable(BuyAllDbAdapter.KEY_ROWID, mRowId);
	}
	
	private void saveState() {
		mDay = mDatePicker.getDayOfMonth();
		mMonth = mDatePicker.getMonth();
		mYear = mDatePicker.getYear();
		mDateText = Integer.toString(mDay) + '/' + Integer.toString(mMonth + 1) + '/' + Integer.toString(mYear);	
	}
	
	
}
