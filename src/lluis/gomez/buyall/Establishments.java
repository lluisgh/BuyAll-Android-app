package lluis.gomez.buyall;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Establishments extends ListTemplate {       
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mDbHelper = new BuyAllDbAdapter(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void create() {
		mRowId = mDbHelper.createEstablishment("");
    	edit();	
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
	protected String getInsertOperation() {
		return "Crea un establiment";
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
	protected void onDestroy() {
		 mDbHelper.close();
		 super.onDestroy();
	}

	@Override
	protected void delete() {
		mDbHelper.deleteEstablishment(mRowId);
	}

	@Override
	protected String getDeleteOperation() {
		return "Segur que vols esborrar aquest establiment?";
	}

	@Override
	protected Cursor fetch() {
		return mDbHelper.fetchEstablishment(mRowId);
	}

	@Override
	protected String getEditOperation() {
		return "Edita l'establiment";
	}

	@Override
	protected void update(String name) {
		mDbHelper.updateEstablishment(mRowId, name);
	}
	
    
    
}
