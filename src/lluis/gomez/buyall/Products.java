package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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

public class Products extends ListActivity {
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    
    protected long mListId;
    protected BuyAllDbAdapter mDbHelper;
    	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final long productId = id;
        
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  

		alertDialog.setTitle("Afegir producte");

		
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {	
 	        		   	Cursor producte = mDbHelper.fetchProduct(productId);
 	        		   	startManagingCursor(producte);
 	        		   	
 	        		   	String brand = producte.getString(producte.getColumnIndex(BuyAllDbAdapter.KEY_BRAND));
 	        		   	String product = producte.getString(producte.getColumnIndex("name"));
 	        		   	mDbHelper.createListProduct(mListId, product, brand, "quantity", 0);
 	        //   }	
	           }
		});
		//alertDialog.setView(dialog);
		alertDialog.show();

    }
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list);
        mDbHelper = new BuyAllDbAdapter(this);
        mDbHelper.open();
        
        fillData();
        registerForContextMenu(getListView());
    }

    protected void fillData() {
    	Cursor productsCursor = mDbHelper.fetchAllProducts();
    	startManagingCursor(productsCursor);
    	
    	//String dateAux = BuyAllDbAdapter.KEY_DAY + '/' + BuyAllDbAdapter.KEY_MONTH + Á + BuyAllDbAdapter.KEY_YEAR;
    	String[] from = new String[]{BuyAllDbAdapter.KEY_NAME, BuyAllDbAdapter.KEY_BRAND, BuyAllDbAdapter.KEY_TYPE};
    	
    	int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
    	SimpleCursorAdapter products = new SimpleCursorAdapter(this, R.layout.select_product_row, productsCursor, from, to);
    	setListAdapter(products);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       // ++count; // = count + 1; 
        menu.add(0, INSERT_ID, 0, "Crea producte");
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createProduct();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, "Edita");
        menu.add(0, DELETE_ID, 0, "Esborra");

    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId()) {
        	case DELETE_ID:
                mDbHelper.deleteProduct(info.id);
                fillData();
                return true;
            case EDIT_ID:
                editProduct(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    private void createProduct() {
    	Intent i = new Intent(this, ProductEdit.class);
    	startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    private void editProduct(long id) {
    	Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }
    
   /* 
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  


		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View dialog = inflater.inflate(R.layout.new_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));

		alertDialog.setTitle("Introdueix la quantitat");
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Quantitat");
		final EditText edText = (EditText) dialog.findViewById(R.id.editText1);
		
		alertDialog.setButton("Confirma", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
 	        	   if (edText.length() <= 0) {
 	        		   edText.setError("Has d'introduir un nom.");
 	        	   }
 	        	   else {
 	        		   	mEstablishment = edText.getText().toString(); 
 	        		   	mDbHelper.createEstablishment(mEstablishment);

 			       		populateFields();
 	        	   }
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();

    }
    */
    
    
}
