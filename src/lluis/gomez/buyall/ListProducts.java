package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListProducts extends ListTemplate {

    private BuyAllDbAdapter mDbHelper;
	
    private Long mRowId;
    
	private Long mListId;
    private String mEstablishment;
    private String mDate;

    private TextView mTitle;
    private TextView mDateText;
  //  private CheckBox mCheckBox;


 //   private static final int INSERT_ID = Menu.FIRST;

    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(BuyAllDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ? extras.getLong(BuyAllDbAdapter.KEY_ROWID)
                                    : null;
        }
        
		mDbHelper = new BuyAllDbAdapter(this);
		super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void fillData() {
    	Cursor c = mDbHelper.fetchListProduct(mRowId);
    	startManagingCursor(c);
    	    	
    	String[] from = new String[] {BuyAllDbAdapter.KEY_PRODUCT, BuyAllDbAdapter.KEY_BRAND};
    	int[] to = new int[] {R.id.name, R.id.text1};
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_product_row, c, from, to);
    	setListAdapter(adapter);
    	
    	
    	Cursor c2 = mDbHelper.fetchList(mRowId);
    	startManagingCursor(c2);
    	mEstablishment = c2.getString(c2.getColumnIndex("establishment"));
    	mDate = c2.getString(c2.getColumnIndex("date"));
    	mTitle = (TextView) findViewById(R.id.textView3);
    	mDateText = (TextView) findViewById(R.id.textView1);
    	mTitle.setText(mEstablishment);
    	mDateText.setText(mDate);
    	    	
    }
   
    

    private void changeQuantity(long id) {
    	 final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  


 		Context mContext = getApplicationContext();
 		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
 		final View dialog = inflater.inflate(R.layout.new_dialog,
 		                               (ViewGroup) findViewById(R.id.layout_root));

 		alertDialog.setTitle("Canvia la quantitat");
 		TextView text = (TextView) dialog.findViewById(R.id.text);
 		text.setText("Quantitat");
 		final EditText edText = (EditText) dialog.findViewById(R.id.editText1);
 		
 		alertDialog.setButton("Confirma", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
  	        	   if (edText.length() <= 0) {
  	        		   edText.setError("Has d'introduir una quantitat.");
  	        	   }
  	        	   
  	        	   /**
  	        	    * probably falla
  	        	    */
  	        	   else {
  	        		   Cursor c = mDbHelper.fetchListProduct(id);
  	        		 	startManagingCursor(c);
  	        	    	String product = c.getString(c.getColumnIndex(BuyAllDbAdapter.KEY_PRODUCT));
  	        	    	String brand = c.getString(c.getColumnIndex(BuyAllDbAdapter.KEY_BRAND));
  	        	    	Integer bought = c.getInt(c.getColumnIndex(BuyAllDbAdapter.KEY_BOUGHT));
  	        		   	String quantity = edText.getText().toString(); 
  	        		   	
  	        		   	mDbHelper.updateListProduct(id, mListId, product, brand, quantity, bought);
  	        		   	
  	        		   	fillData();
  	        	   }
 	           }
 		});
 		alertDialog.setView(dialog);
 		alertDialog.show();
    }
    
    /* 
	private void addProducts() {
    	Intent i = new Intent(this, Products.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, mRowId);
        startActivity(i);
	}
	*/

	@Override
	protected void create() {
    	Intent i = new Intent(this, Products.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, mRowId);
        startActivity(i);		
	}

	@Override
	protected void edit(long id) {
		changeQuantity(id);
	}



	@Override
	protected String getOperation() {
		return "Afegeix un producte";
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Demanar quantity, afegir a la taula ListProducts i tornar a la pantalla previa fent un fillData o similar.
	}

	@Override
	protected void setContent() {
        setContentView(R.layout.list_view);		
	}

	@Override
	protected void onDestroy() {
		 mDbHelper.close();
		 super.onDestroy();
	}

    
}
