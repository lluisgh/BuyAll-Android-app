package lluis.gomez.buyall;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class Products extends ListTemplate {
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mDbHelper = new BuyAllDbAdapter(this);
		super.onCreate(savedInstanceState);
	}
    
   
	@Override
    protected void fillData() {
    	Cursor productsCursor = mDbHelper.fetchAllProducts();
    	startManagingCursor(productsCursor);
    	
    	String[] from = new String[]{BuyAllDbAdapter.KEY_NAME, BuyAllDbAdapter.KEY_BRAND, BuyAllDbAdapter.KEY_TYPE};
    	
    	int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
    	SimpleCursorAdapter products = new SimpleCursorAdapter(this, R.layout.select_product_row, productsCursor, from, to);
    	setListAdapter(products);
    }
    
    
   
	@Override
	protected void create() {
    	Intent i = new Intent(this, ProductEdit.class);
    	startActivityForResult(i, ACTIVITY_CREATE);
		
	}

	@Override
	protected void edit() {
		Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, mRowId);
        startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected String getInsertOperation() {
		return "Crea un producte";
	}

	@Override
	protected void setContent() {
        setContentView(R.layout.products_list);
	}


	@Override
	protected void onDestroy() {
		 mDbHelper.close();
		 super.onDestroy();
	}


	@Override
	protected void delete() {
		mDbHelper.deleteProduct(mRowId);
	}


	@Override
	protected String getDeleteOperation() {
		return "Segur que vols esborrar aquest producte?";
	}


	@Override
	protected Cursor fetch() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String getEditOperation() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void update(String name) {
		// TODO Auto-generated method stub
		
	}
   
    
    
}
