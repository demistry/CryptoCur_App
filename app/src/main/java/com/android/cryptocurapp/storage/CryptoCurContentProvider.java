package com.android.cryptocurapp.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.cryptocurapp.models.CryptoCurContract;

/**
 * Created by ILENWABOR DAVID on 29/10/2017.
 */

public class CryptoCurContentProvider extends ContentProvider {
    private CryptoCurDatabase cryptoCurDatabase;
    private static final int CONTENT_CRYPTOCUR_TABLE = 100;
    private static final int CONTENT_CRYPTOCUR_TABLE_ROW = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(CryptoCurContract.CRYTOCUR_AUTHORITY,CryptoCurContract.CRYPTOCUR_TABLE, CONTENT_CRYPTOCUR_TABLE);
        uriMatcher.addURI(CryptoCurContract.CRYTOCUR_AUTHORITY,CryptoCurContract.CRYPTOCUR_TABLE+"/#", CONTENT_CRYPTOCUR_TABLE_ROW);
    }
    @Override
    public boolean onCreate() {
        cryptoCurDatabase = new CryptoCurDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = cryptoCurDatabase.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match){
            case CONTENT_CRYPTOCUR_TABLE:
                cursor = database.query(CryptoCurContract.CRYPTOCUR_TABLE, projection, selection,
                        selectionArgs,null,null,sortOrder);
                return cursor;
            case CONTENT_CRYPTOCUR_TABLE_ROW:
                break;
            default:
                Toast.makeText(getContext(), "Cannot query unknown uri "+ uri, Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = cryptoCurDatabase.getWritableDatabase();
        int match = uriMatcher.match(uri);
        if (match == CONTENT_CRYPTOCUR_TABLE){
            database.insert(CryptoCurContract.CRYPTOCUR_TABLE,null, values);
        }
        if (match == UriMatcher.NO_MATCH)Toast.makeText(getContext(), "Cannot query unknown uri", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = cryptoCurDatabase.getWritableDatabase();
        int match = uriMatcher.match(uri);
        if (match == CONTENT_CRYPTOCUR_TABLE){
            database.delete(CryptoCurContract.CRYPTOCUR_TABLE, null,null);
        }
        if (match == CONTENT_CRYPTOCUR_TABLE_ROW){
            database.delete(CryptoCurContract.CRYPTOCUR_TABLE,selection,selectionArgs);
        }
        if (match == UriMatcher.NO_MATCH) Toast.makeText(getContext(), "Cannot query unknown uri", Toast.LENGTH_SHORT).show();
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
