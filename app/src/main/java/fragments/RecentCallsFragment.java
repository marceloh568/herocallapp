package fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.CallAdapterRV;
import adapters.RecentsAdapterRV;
import br.com.ownard.forgetme.R;
import models.Contato;


public class RecentCallsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecentsAdapterRV adapterRV;

    private List<Contato> recentsCalls = new ArrayList<>();

    private static final int MY_PERMISSIONS_REQUEST_RECENT_CALLS = 24;


/*    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    } */

    public RecentCallsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        /* MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false); */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent_calls, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_recent_list);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        this.recentsCalls = getRecentCalls();
        adapterRV = new RecentsAdapterRV(recentsCalls, R.layout.contact_list_layout, getActivity());
        mRecyclerView.setAdapter(adapterRV);
        return view;
    }



    private List<Contato> getRecentCalls(){
        int limit = 20;
        List<Contato> listAux = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {

            ContentResolver ct = getActivity().getContentResolver();
            Cursor cursor = ct.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
            List<Contato> callAux = new ArrayList<>();

            if (cursor != null && cursor.getCount() > 0) {
                int i = 1;
                while (cursor.moveToNext() && i <= limit) {
                    i++;
                    String phone = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String contactName = this.getContactName(phone);
                    if (contactName == null || contactName == "") {
                        contactName = String.valueOf(R.string.undefined);
                    }

                    Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.ic_person_black_36dp);
                    Contato contato = new Contato(contactName, phone, 0);
                    contato.setPhoto(icon);

                    callAux.add(contato);
                }
                cursor.close();
            }

            return callAux;
        }

        return new ArrayList<>();
    }

    private String getContactName(String number){
        ContentResolver ct = getActivity().getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = ct.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    public void update() {
        if (recentsCalls != null) {
            adapterRV.notifyData(recentsCalls);
        }
    }

}
