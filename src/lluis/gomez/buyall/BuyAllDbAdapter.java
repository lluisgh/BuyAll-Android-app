package lluis.gomez.buyall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BuyAllDbAdapter {
    public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
    public static final String KEY_BRAND = "brand";
    public static final String KEY_DAY = "day";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_DATE = "date";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_BOUGHT = "bought";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ESTABLISHMENT = "establishment";
    public static final String KEY_LIST = "list_id";
    public static final String KEY_PRODUCT = "product";
    
    
    private static final String TAG = "BuyAllDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

	
	private static final String DATABASE_CREATE_TYPES = 
		"create table types (_id integer primary key autoincrement, "
		+ "name text)";
	
	private static final String DATABASE_CREATE_ESTABLISHMENTS = 
		"create table establishments (_id integer primary key autoincrement, "
		+ "name text)";
	
	private static final String DATABASE_CREATE_LISTS = 
		"create table lists (_id integer primary key autoincrement, "
		+ "establishment text, day integer, month integer, year integer, date text)";
	
	private static final String DATABASE_CREATE_PRODUCTS = 
		"create table products (_id integer primary key autoincrement, "
		+ "name text, brand text, type string)";
		
	/**
	 * quantity Žs un String/text perqu serˆ el float d'un camp de text
	 * concatenat amb la corresponent unitat obtinguda d'un spinner d'strings
	 * amb {unitat(s), kg, g}
	 */
	private static final String DATABASE_CREATE_LIST_PRODUCT = 
		"create table list_product (_id integer primary key autoincrement, "
		+ "list_id integer not null, product text, brand text, quantity text, bought integer)";

	
	private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_PRODUCTS = "products";
    private static final String DATABASE_TABLE_TYPES = "types";
    private static final String DATABASE_TABLE_ESTABLISHMENTS = "establishments";
    private static final String DATABASE_TABLE_LISTS = "lists";
    private static final String DATABASE_TABLE_LIST_PRODUCT = "list_product";

    private static final int DATABASE_VERSION = 1; //1????????
	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_TYPES);
            db.execSQL(DATABASE_CREATE_ESTABLISHMENTS);
            db.execSQL(DATABASE_CREATE_PRODUCTS);
            db.execSQL(DATABASE_CREATE_LISTS);
            db.execSQL(DATABASE_CREATE_LIST_PRODUCT);
           //db.execSQL("insert into establishments values ('carrefour')");
          //  initializeEstablishments(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS types");
            db.execSQL("DROP TABLE IF EXISTS establishments");
            db.execSQL("DROP TABLE IF EXISTS products");
            db.execSQL("DROP TABLE IF EXISTS lists");
            db.execSQL("DROP TABLE IF EXISTS list_product");
            
            onCreate(db);
        }
    }
/*
	private static void initializeEstablishments(SQLiteDatabase db) {
		ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, "Supermercat");
        db.insert(DATABASE_TABLE_ESTABLISHMENTS, null, initialValues);
	}*/
	
	/**
	 *  Constructor
	 */
	public BuyAllDbAdapter(Context ctx) {
		mCtx = ctx;
	}
	
	/**
	 * Obre la BD
	 */
	public BuyAllDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Tanca la BD.
	 */
	public void close() {
		mDbHelper.close();
	}
	
	
	
	/**
	 * 
	 * 
	 * 
	 * MéTODES PER A LA CREACIî I INSERCIî DE NOVES TUPLES 
	 * 
	 * 
	 */
	
	
	/**
	 * Crea un nou tipus de nom name
	 * @param name el nom del tipus
	 * @return rowId o -1 si falla
	 */
	public long createType(String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		return mDb.insert(DATABASE_TABLE_TYPES, null, initialValues);
	}
	
	/**
	 * Crea un nou establiment de nom name
	 * @param name el nom de l'establisment
	 * @return rowId o -1 si falla
	 */
	public long createEstablishment(String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		return mDb.insert(DATABASE_TABLE_ESTABLISHMENTS, null, initialValues);
	}
	
	/**
	 * Crea un nou producte amb nom name, mara brand, preu price i tipus typeId
	 * @param name el nom del producte
	 * @param brand la marca del producte
	 * @param price el preu del producte
	 * @param typeId el tipus (id) del producte
	 * @return rowId o -1 si falla
	 */
	public long createProduct(String name, String brand, String type) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_BRAND, brand);
		initialValues.put(KEY_TYPE, type);
		return mDb.insert(DATABASE_TABLE_PRODUCTS, null, initialValues);
	}
	
	/**
	 * Crea una nova llista de la compra amb establiment establimentID i data l’mit dueDate
	 * @param establishmentId la id de l'establiment de la llista
	 * @param dueDate la data l’mit de la llista
	 * @return rowId o -1 si falla
	 */
	public long createList(String establishment, Integer day, Integer month, Integer year, String date) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ESTABLISHMENT, establishment);
		initialValues.put(KEY_DAY, day);
		initialValues.put(KEY_MONTH, month);
		initialValues.put(KEY_YEAR, year);
		initialValues.put(KEY_DATE, date);

		return mDb.insert(DATABASE_TABLE_LISTS, null, initialValues);
	}
	
	/**
	 * Crea una "instˆncia" de l'associaci— llista-producte per a indicar que
	 * a la llista listId hi ha una quantitat quantity del producte productId
	 * @param listId id de la llista a associar
	 * @param productId id del producte a associar
	 * @param quantity quantitat (p.e. 5kg) del producte productId a la llista listId
	 * @return rowId o -1 si falla
	 */
	public long createListProduct(long listId, String product, String brand, String quantity, Integer bought) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_LIST, listId);
		initialValues.put(KEY_PRODUCT, product);
		initialValues.put(KEY_BRAND, brand);
		initialValues.put(KEY_QUANTITY, quantity);
		initialValues.put(KEY_BOUGHT, bought);
		
		return mDb.insert(DATABASE_TABLE_LIST_PRODUCT, null, initialValues);
	}

	
	/**
	 * 
	 * 
	 * MæTODES PER A ESBORRAR TUPLES
	 * 
	 * 
	 * 
	 */	


	
	
	
	public boolean deleteType(long rowId) {
		return mDb.delete(DATABASE_TABLE_TYPES, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteEstablishment(long rowId) {
		return mDb.delete(DATABASE_TABLE_ESTABLISHMENTS, KEY_ROWID + "=" + rowId, null) > 0;
	}
	public boolean deleteProduct(long rowId) {
		return mDb.delete(DATABASE_TABLE_PRODUCTS, KEY_ROWID + "=" + rowId, null) > 0;
	}
	public boolean deleteList(long rowId) {
		return mDb.delete(DATABASE_TABLE_LISTS, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteListProduct(long rowId) {
		return mDb.delete(DATABASE_TABLE_LIST_PRODUCT, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	
	public Cursor fetchAllTypes() {
		return mDb.query(DATABASE_TABLE_TYPES, new String[] {KEY_ROWID, KEY_NAME}, null, null, null, null, KEY_NAME);
	}
	
	public Cursor fetchAllEstablishments() {
		return mDb.query(DATABASE_TABLE_ESTABLISHMENTS, new String[] {KEY_ROWID, KEY_NAME}, null, null, null, null, KEY_NAME);
	}
	
	public Cursor fetchAllProducts() {
		return mDb.query(DATABASE_TABLE_PRODUCTS, new String[] {KEY_ROWID, KEY_NAME, KEY_BRAND, KEY_TYPE}, null, null, null, null, KEY_NAME);
	}
	
	public Cursor fetchAllLists() {
		return mDb.query(DATABASE_TABLE_LISTS, new String[] {KEY_ROWID, KEY_ESTABLISHMENT, KEY_DAY, KEY_MONTH, KEY_DATE}, null, null, null, null, null);
	}
	
	/**
	 * Retorna un cursor sobre els productes (id i quantitat) presents a la llista listId
	 * @param listId id de la llista
	 * @return cursor sobre els productes de la llista listId
	 */
	public Cursor fetchProductsOf(String listId) {
		return mDb.query(DATABASE_TABLE_LIST_PRODUCT, new String[] {KEY_PRODUCT, KEY_BRAND, KEY_QUANTITY, KEY_BOUGHT}, KEY_LIST + "=" + listId, null, null, null, null);
	}
	
	public Cursor fetchList(long rowId) throws SQLException {
		Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_LISTS, new String[] {KEY_ROWID,
                    KEY_ESTABLISHMENT, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_DATE}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}
	
	
	public Cursor fetchType(long rowId) throws SQLException {

        Cursor mCursor =
            mDb.query(true, DATABASE_TABLE_TYPES, new String[] {KEY_ROWID,
                    KEY_NAME}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}

	public Cursor fetchEstablishment(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_ESTABLISHMENTS, new String[] {KEY_ROWID,
                    KEY_NAME}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}
	
	public Cursor fetchProduct(long rowId) throws SQLException {
		
		Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_PRODUCTS, new String[] {KEY_ROWID,
                    KEY_NAME, KEY_BRAND}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}
	
	public Cursor fetchListProduct(long rowId) throws SQLException {
		Cursor mCursor =

            mDb.query(true, DATABASE_TABLE_LIST_PRODUCT, new String[] {KEY_ROWID,
                    KEY_LIST, KEY_PRODUCT, KEY_BRAND, KEY_QUANTITY, KEY_BOUGHT}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}

    
	
	/**
	 * 
	 * MéTODES D'UPDATE
	 * 
	 */
	
	/**
	 * 
	 * @param rowId
	 * @param name
	 * @return
	 */
	public boolean updateType(long rowId, String name) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		
        return mDb.update(DATABASE_TABLE_TYPES, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateEstablishment(long rowId, String name) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		
        return mDb.update(DATABASE_TABLE_ESTABLISHMENTS, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean updateProduct(long rowId, String name, String brand, String type) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_BRAND, brand);
		args.put(KEY_TYPE, type);

        return mDb.update(DATABASE_TABLE_PRODUCTS, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean updateList(long rowId, String establishment, Integer day, Integer month, Integer year, String date) {
		ContentValues args = new ContentValues();
		args.put(KEY_ESTABLISHMENT, establishment);
		args.put(KEY_DAY, day);
		args.put(KEY_MONTH, month);
		args.put(KEY_YEAR, year);
		args.put(KEY_DATE, date);

        return mDb.update(DATABASE_TABLE_LISTS, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean updateListProduct(long rowId, long listId, String product, String brand, String quantity, Integer bought) {
		ContentValues args = new ContentValues();
		args.put(KEY_LIST, listId);
		args.put(KEY_PRODUCT, product);
		args.put(KEY_BRAND, brand);
		args.put(KEY_QUANTITY, quantity);
		args.put(KEY_BOUGHT, bought);
		
        return mDb.update(DATABASE_TABLE_LIST_PRODUCT, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
}
