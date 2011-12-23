package lluis.gomez.buyall;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Types extends ListTemplate {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mDbHelper = new BuyAllDbAdapter(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void create() {
    	mRowId = mDbHelper.createType("");
		edit();			
	}

	@Override
	protected void fillData() {
		Cursor listsCursor = mDbHelper.fetchAllTypes();
    	startManagingCursor(listsCursor);
    	
    	String[] from = new String[]{BuyAllDbAdapter.KEY_NAME};
    	
    	int[] to = new int[]{R.id.text1};
    	SimpleCursorAdapter lists = new SimpleCursorAdapter(this, R.layout.lists_row, listsCursor, from, to);
    	setListAdapter(lists);		
		
	}

	@Override
	protected String getInsertOperation() {
		return "Crea un tipus";
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
	protected void onDestroy() {
		 mDbHelper.close();
		 super.onDestroy();
	}

	@Override
	protected void delete() {
		mDbHelper.deleteType(mRowId);
	}

	@Override
	protected String getDeleteOperation() {
		return "Segur que vols esborrar aquest tipus?";
	}

	@Override
	protected Cursor fetch() {
		return mDbHelper.fetchType(mRowId);
	}

	@Override
	protected String getEditOperation() {
		return "Edita el tipus";
	}
	
	@Override
	protected void update(String name) {
		mDbHelper.updateType(mRowId, name);
	}

}
