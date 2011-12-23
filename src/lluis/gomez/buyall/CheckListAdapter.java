package lluis.gomez.buyall;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class CheckListAdapter extends ResourceCursorAdapter {

	public CheckListAdapter(Context context, Cursor cur) {
		super(context, R.layout.products_list, cur);
	}

	@Override
	public View newView(Context context, Cursor cur, ViewGroup parent) {
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return li.inflate(R.layout.view_product_row, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cur) {
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView brand = (TextView) view.findViewById(R.id.brand);
		TextView quantity = (TextView) view.findViewById(R.id.quantity);
		CheckBox cbListCheck = (CheckBox) view.findViewById(R.id.checkBox1);

		if (cur != null) {
			name.setText(cur.getString(cur.getColumnIndexOrThrow(BuyAllDbAdapter.KEY_NAME)));
			brand.setText(cur.getString(cur.getColumnIndexOrThrow(BuyAllDbAdapter.KEY_BRAND)));
			quantity.setText(cur.getString(cur.getColumnIndexOrThrow(BuyAllDbAdapter.KEY_QUANTITY)));
			cbListCheck.setChecked((cur.getInt(
					cur.getColumnIndexOrThrow(BuyAllDbAdapter.KEY_BOUGHT)) == 0 ? false
					: true));
		}
	}
}