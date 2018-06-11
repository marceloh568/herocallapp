package br.com.ownard.forgetme;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import adapters.CallPagerAdapter;
import controllers.CallController;
import fragments.BlockCallsFragment;
import fragments.ContactFragment;
import fragments.RecentCallsFragment;

public class MainActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 10;
    Toolbar toolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    CallPagerAdapter mCallPagerAdapter;


    private CallController callController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callController = new CallController(this);

        setContentView(R.layout.activity_main);



        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BlockCallEasier");

        mTabLayout = (TabLayout)findViewById(R.id.tabMenu);
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);

        mCallPagerAdapter = new CallPagerAdapter(getSupportFragmentManager());
        mCallPagerAdapter.addFragmentTab(new BlockCallsFragment(), "Bloqueados", R.drawable.ic_block_white_48dp);
        mCallPagerAdapter.addFragmentTab(new RecentCallsFragment(), "Recentes", R.drawable.ic_schedule_white_48dp);
        ContactFragment cf = new ContactFragment();
        mCallPagerAdapter.addFragmentTab(cf, "Contatos", R.drawable.ic_people_white_48dp);


        mViewPager.setAdapter(mCallPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        populateIcons();
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        mTabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#000000"));
        //mTabLayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));



    }

    public void populateIcons(){
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_block_white_48dp);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_schedule_white_48dp);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_people_white_48dp);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissao Concebida", Toast.LENGTH_SHORT).show();
                }else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                        new AlertDialog.Builder(this).
                                setTitle("Ler chamadas").
                                setMessage("Voce precisa liberar ler chamadas").show();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                            new AlertDialog.Builder(this).
                                    setTitle("Ler chamadas").
                                    setMessage("Voce precisa liberar nas configurações").show();
                        }
                    }
                }
                break;
        }
    }

    public void addContact(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_newblock, null);
        dialogBuilder.setView(dialogView);

        final EditText edtName = (EditText)dialogView.findViewById(R.id.new_name);
        final EditText edtPhone = (EditText)dialogView.findViewById(R.id.new_phone);

        dialogBuilder.setTitle(R.string.new_block);
        dialogBuilder.setMessage(R.string.info_block);
        dialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean callSaved = callController.insert(edtName.getText().toString(), edtPhone.getText().toString());
                
                if(callSaved){
                    Toast.makeText(MainActivity.this, R.string.number_blocked, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, R.string.block_error, Toast.LENGTH_SHORT).show();
                }
                mCallPagerAdapter.notifyDataSetChanged();
                populateIcons();
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                
            }
        });

        AlertDialog makeDialog = dialogBuilder.create();
        makeDialog.show();
    }

    public CallController getCallController(){
        return this.callController;
    }

    public void notifyRVChange(){
        ((BlockCallsFragment)mCallPagerAdapter.getItem(0)).update();
    }

    public void notifyRVItemRemoved(){
        ((ContactFragment)mCallPagerAdapter.getItem(2)).update();
        ((RecentCallsFragment)mCallPagerAdapter.getItem(1)).update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView =(SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String phone) {
                mTabLayout.getTabAt(0).select();
                ((BlockCallsFragment)mCallPagerAdapter.getItem(0)).search(phone);

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
