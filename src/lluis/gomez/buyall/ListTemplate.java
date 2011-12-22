package lluis.gomez.buyall;

import android.app.ListActivity;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContent();
		initializeDbHelper();
		//mDbHelper = new BuyAllDbAdapter(this);
        fillData();
		registerForContextMenu(getListView());
	}

	protected abstract void setContent();
	protected abstract void initializeDbHelper();
	protected abstract void fillData();

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, "Crea " + getElementName());
        return true;
    }
	
	protected abstract String getElementName();
	
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
    	switch(item.getItemId()) {
        	case DELETE_ID:
                mDbHelper.deleteEstablishment(info.id);
                fillData();
                return true;
            case EDIT_ID:
                edit(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        edit(id);
	}

    protected abstract void edit(long id);
    
	 @Override
	 protected void onDestroy() {
		 mDbHelper.close();
		 super.onDestroy();
	 }

	
}
