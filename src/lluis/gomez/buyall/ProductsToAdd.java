package lluis.gomez.buyall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ProductsToAdd extends Products {

	private Long mListId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mListId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(BuyAllDbAdapter.KEY_LIST);
        if (mListId == null) {
            Bundle extras = getIntent().getExtras();
            mListId = (extras != null) ? extras.getLong(BuyAllDbAdapter.KEY_LIST)
                                    : null;
        }
        
		mDbHelper = new BuyAllDbAdapter(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        mRowId =  id;
        chooseQuantity();
	}
	private void chooseQuantity() {
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
 	        		   	String quantity = edText.getText().toString();  	        		   	
 	        		   	mDbHelper.createListProduct(mListId, mRowId, quantity, 0); 	        		   	
 	        		   	//fillData();
 	        		   	finish();
 	        	   }
	           }
		});
		alertDialog.setView(dialog);
		alertDialog.show();
   }
   

}
