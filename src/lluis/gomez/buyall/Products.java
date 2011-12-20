package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Products extends ListTemplate {
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    
    private long mListId;
    	
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
    protected void fillData() {
    	Cursor productsCursor = mDbHelper.fetchAllProducts();
    	startManagingCursor(productsCursor);
    	
    	//String dateAux = BuyAllDbAdapter.KEY_DAY + '/' + BuyAllDbAdapter.KEY_MONTH + � + BuyAllDbAdapter.KEY_YEAR;
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
	protected void edit(long id) {
		Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);		
	}

	@Override
	protected String getElementName() {
		return "Product";
	}

	@Override
	protected void setContent() {
        setContentView(R.layout.products_list);		
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
