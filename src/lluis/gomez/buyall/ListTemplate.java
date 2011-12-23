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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public abstract class ListTemplate extends ListActivity {

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    
	protected BuyAllDbAdapter mDbHelper;
	protected Long mRowId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContent();
        fillData();
		registerForContextMenu(getListView());
	}

	protected abstract void setContent();
	protected abstract void fillData();

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, getInsertOperation());
        return true;
    }
	
	protected abstract String getInsertOperation();
	
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                create();
                return true;
        }        
        return super.onMenuItemSelected(featureId, item);

    }
    
    protected abstract void create();
    
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
    	mRowId = info.id;
    	switch(item.getItemId()) {
        	case DELETE_ID:
                deleteElement();
                return true;
            case EDIT_ID:
                edit();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    private void deleteElement() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  
		alertDialog.setTitle("Confirmaci—");
		alertDialog.setMessage(getDeleteOperation());
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "S’", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	  delete();
	              fillData();

	           }
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.cancel();
	           }
		});
		alertDialog.show();				
    }
    
    protected abstract String getDeleteOperation();
    protected abstract void delete();
    
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mRowId =  id;
        edit();
	}
	
	protected abstract Cursor fetch();
	protected abstract String getEditOperation();
	protected abstract void update(String name);
	
    protected void edit() {
    	final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  

    	Cursor cursor = fetch();
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
		
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
 	        	   if (edText.length() <= 0) edText.setError("Has d'introduir un nom.");
 	        	   else {
 	        		   update(edText.getText().toString());
 	        		   fillData();
 	        	   }
	           }
		});
		
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelála", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	  delete();
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();		
    }
    
	/*
    @Override
	protected abstract void onDestroy();
	*/
}
