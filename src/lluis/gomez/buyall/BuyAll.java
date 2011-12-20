package lluis.gomez.buyall;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BuyAll extends ListActivity {
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;


    private static final int INSERT_ID = Menu.FIRST;
    
    private static final int MANAGE_PRODUCTS = Menu.FIRST + 1;
    private static final int MANAGE_TYPES = Menu.FIRST + 2;
    private static final int MANAGE_ESTABLISHMENTS = Menu.FIRST + 3;
    
   // private static final int ADD_PRODUCTS = Menu.FIRST + 3;
    private static final int DELETE_ID = Menu.FIRST + 4;
    private static final int EDIT_ID = Menu.FIRST + 5;

   // private static int count = 0;
    private BuyAllDbAdapter mDbHelper;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.lists_list);
        mDbHelper = new BuyAllDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }
    
    private void fillData() {
    	Cursor listsCursor = mDbHelper.fetchAllLists();
    	startManagingCursor(listsCursor);
    	
    	//String dateAux = BuyAllDbAdapter.KEY_DAY + '/' + BuyAllDbAdapter.KEY_MONTH + Á + BuyAllDbAdapter.KEY_YEAR;
    	String[] from = new String[]{BuyAllDbAdapter.KEY_ESTABLISHMENT, BuyAllDbAdapter.KEY_DATE};
    	
    	int[] to = new int[]{R.id.text1, R.id.text2};
    	SimpleCursorAdapter lists = new SimpleCursorAdapter(this, R.layout.lists_row, listsCursor, from, to);
    	setListAdapter(lists);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       // ++count; // = count + 1; 
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
       menu.add(0, MANAGE_PRODUCTS, 0, "Productes");
        menu.add(0, MANAGE_TYPES, 0, "Tipus");
        menu.add(0, MANAGE_ESTABLISHMENTS, 0, "Establiments");


        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createList();
                return true;
            case MANAGE_PRODUCTS:
            	Intent i = new Intent(this, Products.class);
            	startActivity(i);
            	return true;
            case MANAGE_TYPES:
            	Intent i2 = new Intent(this, Types.class);
            	startActivity(i2);
            	return true;
            case MANAGE_ESTABLISHMENTS:
            	Intent i3 = new Intent(this, Establishments.class);
            	startActivity(i3);
            	return true;

        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    //    menu.add(0, ADD_PRODUCTS, 0, "Afegeix productes");

        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);


    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId()) {
        	case DELETE_ID:
                mDbHelper.deleteList(info.id);
                fillData();
                return true;
            case EDIT_ID:
                editList(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    private void createList() {
    	Intent i = new Intent(this, ListEdit.class);
    	startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void editList(long id) {
    	Intent i = new Intent(this, ListEdit.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ListViewProducts.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, id);
        startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }  
    

}
