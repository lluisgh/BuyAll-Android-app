package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Establishments extends ListTemplate {    
    private long mRowId;
   
	@Override
	protected void create() {
		mRowId = mDbHelper.createEstablishment("");
    	edit(mRowId);	
	}

	@Override
	protected void edit(long rowid) {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  

		mRowId = rowid;
		
    	Cursor cursor = mDbHelper.fetchEstablishment(mRowId);
    	startManagingCursor(cursor);
    	

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View dialog = inflater.inflate(R.layout.new_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));

		alertDialog.setTitle("Edita establiment");
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Nom");
		final EditText edText = (EditText) dialog.findViewById(R.id.editText1);
		edText.setText(cursor.getString(cursor.getColumnIndex(BuyAllDbAdapter.KEY_NAME)));
		
		alertDialog.setButton("Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
 	        	   if (edText.length() <= 0) edText.setError("Has d'introduir un nom.");
 	        	   else {
 	        		   mDbHelper.updateEstablishment(mRowId, edText.getText().toString());
 	        		   fillData();
 	        	   }
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();		
	}

	@Override
	protected void fillData() {
		Cursor listsCursor = mDbHelper.fetchAllEstablishments();
    	startManagingCursor(listsCursor);
    	
    	//String dateAux = BuyAllDbAdapter.KEY_DAY + '/' + BuyAllDbAdapter.KEY_MONTH + Á + BuyAllDbAdapter.KEY_YEAR;
    	String[] from = new String[]{BuyAllDbAdapter.KEY_NAME};
    	
    	int[] to = new int[]{R.id.text1};
    	SimpleCursorAdapter lists = new SimpleCursorAdapter(this, R.layout.lists_row, listsCursor, from, to);
    	setListAdapter(lists);		
	}

	@Override
	protected String getElementName() {
		return "Establiment";
	}

	@Override
	protected void setContent() {
		setContentView(R.layout.name_list);
		TextView noElements = (TextView) findViewById(android.R.id.empty);
		noElements.setText("No hi ha establiments.");
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("Establiments");

	}

	@Override
	protected void initializeDbHelper() {
		mDbHelper = new BuyAllDbAdapter(this);		
	}
	
    
    
}
