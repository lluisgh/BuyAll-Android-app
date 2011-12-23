package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListProducts extends ListTemplate {

    private static final int EDIT_ID = Menu.FIRST + 1;

    
	private Long mListId;
    private String mEstablishment;
    private String mDate;

    private TextView mTitle;
    private TextView mDateText;
   // private CheckBox mCheckBox;


 //   private static final int INSERT_ID = Menu.FIRST;

    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mListId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(BuyAllDbAdapter.KEY_ROWID);
        if (mListId == null) {
            Bundle extras = getIntent().getExtras();
            mListId = (extras != null) ? extras.getLong(BuyAllDbAdapter.KEY_ROWID)
                                    : null;
        }
        
		mDbHelper = new BuyAllDbAdapter(this);
		super.onCreate(savedInstanceState);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EDIT_ID, 0, "Edita la llista");
        return true;
    }
	
	
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case EDIT_ID:
                editList();
                return true;
        }        
        return super.onMenuItemSelected(featureId, item);

    }
    
    private void editList() {
    	Intent i = new Intent(this, ListEdit.class);
        i.putExtra(BuyAllDbAdapter.KEY_ROWID, mListId);
        startActivity(i);
    }
    
    @Override
    protected void fillData() {
    	Cursor c = mDbHelper.fetchProductsOf(mListId);
    	startManagingCursor(c);
    	//TODO no funcionava aixo boolean bought = c.getInt(c.getColumnIndex(BuyAllDbAdapter.KEY_BOUGHT)) == 1;
    	
    	//String[] from = new String[] {BuyAllDbAdapter.KEY_NAME, BuyAllDbAdapter.KEY_BRAND, BuyAllDbAdapter.KEY_QUANTITY, BuyAllDbAdapter.KEY_BOUGHT};
    	//int[] to = new int[] {R.id.name, R.id.brand, R.id.quantity};
    	CheckListAdapter adapter = new CheckListAdapter(this, c);
    	setListAdapter(adapter);
    	
    	
    	Cursor c2 = mDbHelper.fetchList(mListId);
    	startManagingCursor(c2);
    	mEstablishment = c2.getString(c2.getColumnIndex("establishment"));
    	mDate = c2.getString(c2.getColumnIndex("date"));
    	mTitle = (TextView) findViewById(R.id.textView3);
    	mDateText = (TextView) findViewById(R.id.textView1);
    	mTitle.setText(mEstablishment);
    	mDateText.setText(mDate);
    	//mCheckBox = (CheckBox) findViewById(R.id.checkBox1);
    	//TODO mCheckBox.setChecked(bought);
    }
   
    

    private void changeQuantity() {
    	 final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  


 		Context mContext = getApplicationContext();
 		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
 		final View dialog = inflater.inflate(R.layout.new_dialog,
 		                               (ViewGroup) findViewById(R.id.layout_root));

 		alertDialog.setTitle("Escull la quantitat");
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
  	        		   Cursor c = mDbHelper.fetchListProduct(mRowId);
  	        		 	startManagingCursor(c);
  	        	    	Integer bought = c.getInt(c.getColumnIndex(BuyAllDbAdapter.KEY_BOUGHT));
  	        		   	String quantity = edText.getText().toString(); 
  	        		   	
  	        		   	mDbHelper.updateListProduct(mRowId, quantity, bought);
  	        		   	
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
    	Intent i = new Intent(this, ProductsToAdd.class);
        i.putExtra(BuyAllDbAdapter.KEY_LIST, mListId);
        startActivity(i);		
	}

	@Override
	protected void edit() {
		changeQuantity();
	}



	@Override
	protected String getInsertOperation() {
		return "Afegeix un producte";
	}

	@Override
	protected void setContent() {
        setContentView(R.layout.list_view);		
	}

	@Override
	protected void onDestroy() {
		//TODO aqu’ guardarˆ les checkboxes
		mDbHelper.close();
		super.onDestroy();
	}

	@Override
	protected void delete() {
		mDbHelper.deleteListProduct(mRowId);
	}

	@Override
	protected String getDeleteOperation() {
		return "Segur que vols esborrar aquest product de la llista?";
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
		cb.setChecked(!cb.isChecked());
		int bought;
		if (cb.isChecked()) bought = 1;
		else bought = 0;
		Cursor c = mDbHelper.fetchListProduct(id);
		int i = c.getCount();
		int prova = c.getColumnIndex(BuyAllDbAdapter.KEY_QUANTITY);
		mDbHelper.updateListProduct(mRowId, c.getString(c.getColumnIndexOrThrow(BuyAllDbAdapter.KEY_QUANTITY)), bought);
	}

	@Override
	protected Cursor fetch() {
		return null;
	}

	@Override
	protected String getEditOperation() {
		return "Canvia la quantitat";
	}

	@Override
	protected void update(String name) {}

    
}
