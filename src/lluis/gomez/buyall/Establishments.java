package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Establishments extends ListActivity {    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    private BuyAllDbAdapter mDbHelper;
    
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.name_list);
        mDbHelper = new BuyAllDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }
    
    private void fillData() {
    	Cursor listsCursor = mDbHelper.fetchAllEstablishments();
    	startManagingCursor(listsCursor);
    	
    	//String dateAux = BuyAllDbAdapter.KEY_DAY + '/' + BuyAllDbAdapter.KEY_MONTH + Á + BuyAllDbAdapter.KEY_YEAR;
    	String[] from = new String[]{BuyAllDbAdapter.KEY_NAME};
    	
    	int[] to = new int[]{R.id.text1};
    	SimpleCursorAdapter lists = new SimpleCursorAdapter(this, R.layout.lists_row, listsCursor, from, to);
    	setListAdapter(lists);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, "Crea Establiment");
        return true;
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createEstablishment();
                return true;
        }        
        return super.onMenuItemSelected(featureId, item);

    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);

    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId()) {
        	case DELETE_ID:
                mDbHelper.deleteEstablishment(info.id);
                fillData();
                return true;
            case EDIT_ID:
                editEstablishment(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    private void createEstablishment() {
    	editEstablishment(mDbHelper.createEstablishment(""));
    }
    
    private void editEstablishment(long id) {
    	final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  

    	Cursor cursor = mDbHelper.fetchEstablishment(id);
    	startManagingCursor(cursor);
    	

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View dialog = inflater.inflate(R.layout.new_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));

		alertDialog.setTitle("Crea establiment");
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Nom");
		final EditText edText = (EditText) dialog.findViewById(R.id.editText1);
		edText.setText(cursor.getString(cursor.getColumnIndex(BuyAllDbAdapter.KEY_NAME)));
		
		alertDialog.setButton("Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
 	        	   if (edText.length() <= 0) {
 	        		   edText.setError("Has d'introduir un nom.");
 	        	   }
 	        	   else {
 	        		   	String establishment = edText.getText().toString(); 
 	        		   	mDbHelper.updateEstablishment(id, establishment);
 	        	   }
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();
    }
    
    
}
