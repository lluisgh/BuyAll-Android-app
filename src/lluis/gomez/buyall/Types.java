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


public class Types extends ListTemplate {

	private long mRowId;
	
	@Override
	protected void create() {
    	mRowId = mDbHelper.createType("");
		edit(mRowId);			
	}

	@Override
	protected void edit(long id) {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  

		mRowId = id;
    	Cursor cursor = mDbHelper.fetchType(mRowId);
    	startManagingCursor(cursor);
    	

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View dialog = inflater.inflate(R.layout.new_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));

		alertDialog.setTitle("Edita tipus");
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Nom");
		final EditText edText = (EditText) dialog.findViewById(R.id.editText1);
		edText.setText(cursor.getString(cursor.getColumnIndex(BuyAllDbAdapter.KEY_NAME)));
		
		cursor.close();
		
		alertDialog.setButton("Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
 	        	   if (edText.length() <= 0) edText.setError("Has d'introduir un nom.");
 	        	   else {
 	        		   mDbHelper.updateType(mRowId, edText.getText().toString());
 	        		   fillData();
 	        	   }
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();				
	}

	@Override
	protected void fillData() {
		Cursor listsCursor = mDbHelper.fetchAllTypes();
    	startManagingCursor(listsCursor);
    	
    	String[] from = new String[]{BuyAllDbAdapter.KEY_NAME};
    	
    	int[] to = new int[]{R.id.text1};
    	SimpleCursorAdapter lists = new SimpleCursorAdapter(this, R.layout.lists_row, listsCursor, from, to);
    	setListAdapter(lists);	
    	listsCursor.close();
		
	}

	@Override
	protected String getElementName() {
		return "Tipus";
	}

	@Override
	protected void setContent() {
		setContentView(R.layout.name_list);
		TextView noElements = (TextView) findViewById(android.R.id.empty);
		noElements.setText("No hi ha tipus.");
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("Tipus");

	}

	@Override
	protected void initializeDbHelper() {
		mDbHelper = new BuyAllDbAdapter(this);		
	}

}
