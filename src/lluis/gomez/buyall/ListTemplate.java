package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
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
	
    protected abstract void edit();
    
	/*
    @Override
	protected abstract void onDestroy();
	*/
}
