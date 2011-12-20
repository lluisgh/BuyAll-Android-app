package lluis.gomez.buyall;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ProductEdit extends Activity {
	private Spinner mTypeSpinner;
	private BuyAllDbAdapter mDbHelper;
	
	private String mName;
	private EditText mNameText;
	private EditText mBrandText;
	private String mBrand;
	private String mType;
	
	private Long mRowId;
	private ArrayList<String> mArray;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDbHelper = new BuyAllDbAdapter(this);
		mDbHelper.open();

		setContentView(R.layout.product_edit);
		
		mTypeSpinner = (Spinner) findViewById(R.id.spinner1);
		mNameText = (EditText) findViewById(R.id.editText1);
		mBrandText = (EditText) findViewById(R.id.editText2);

		Button createType = (Button) findViewById(R.id.createType);
		Button confirmButton = (Button) findViewById(R.id.confirmButton);
		
		mTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
					Cursor	c = (Cursor) mTypeSpinner.getSelectedItem();
					mType = c.getString(c.getColumnIndex("name"));				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				mType = null;
			}
			
		});
		mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(BuyAllDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ? extras.getLong(BuyAllDbAdapter.KEY_ROWID)
                                    : null;
        }
		
        mType = null;
        
        populateFields();
        
        createType.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createType();
			}
		});
        
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	if (mNameText.length() <= 0) {
	        		   mNameText.setError("Has d'introduir un nom.");
        	   } else {
	        		
        		   setResult(RESULT_OK);
                   finish();
        	   }
            	
            }

        });	
	}
	
	private void createType() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  


		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View dialog = inflater.inflate(R.layout.new_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));

		alertDialog.setTitle("Crea tipus");
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Nom");
		final EditText edText = (EditText) dialog.findViewById(R.id.editText1);
		
		alertDialog.setButton("Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
 	        	   if (edText.length() <= 0) {
 	        		   edText.setError("Has d'introduir un nom.");
 	        	   }
 	        	   else {
 	        		   	mType = edText.getText().toString(); 
 	        		   	mDbHelper.createType(mType);
 			       		populateFields();
 	        	   }
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();
	}
	
	private void populateFields() {
		Cursor c = mDbHelper.fetchAllTypes();
		startManagingCursor(c);
		String[] from = new String[]{BuyAllDbAdapter.KEY_NAME};
		int[] to = new int[]{android.R.id.text1};
		SimpleCursorAdapter adapter =
			  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
			adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
			
		mTypeSpinner.setAdapter(adapter);
		mArray = new ArrayList<String>();
  		for (c.moveToFirst(); c.moveToNext(); c.isAfterLast()) {
  			
  			mArray.add(c.getString(1));
  		}
		
		if (mRowId != null) {	
			Cursor product = mDbHelper.fetchProduct(mRowId);
			startManagingCursor(product);
			mName = product.getString(product.getColumnIndex("name"));
			mBrand = product.getString(product.getColumnIndex("brand"));
			if (mType == null) mType = product.getString(product.getColumnIndex("type"));
			
			int index = mArray.indexOf(mType);
			mTypeSpinner.setSelection(index + 1);
			
		}
		
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
		mName = mNameText.getText().toString();
		mBrand = mBrandText.getText().toString();
			
		if (mRowId == null) {
			long id = mDbHelper.createProduct(mName, mBrand, mType);
			if (id > 0) mRowId = id;
			
		} else {
			mDbHelper.updateProduct(mRowId, mName, mBrand, mType);
		}
	}
}
