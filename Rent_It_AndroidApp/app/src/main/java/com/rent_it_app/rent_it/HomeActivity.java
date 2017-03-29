package com.rent_it_app.rent_it;

import android.app.SearchManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Category;
import com.rent_it_app.rent_it.json_models.CategoryEndpoint;
import com.rent_it_app.rent_it.views.AccountFragment;
import com.rent_it_app.rent_it.views.ChatListFragment;
import com.rent_it_app.rent_it.views.AvailabeItemFragment;
import com.rent_it_app.rent_it.views.FileClaimFragment;
import com.rent_it_app.rent_it.views.KeywordsFragment;
import com.rent_it_app.rent_it.views.ListItemFragment;
import com.rent_it_app.rent_it.views.StartRentalFragment;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView myStatusText;
    private FirebaseAuth mAuth;
   /* private FirebaseAuth.AuthStateListener mAuthListener;*/
    private Button mSignOutButton;
    /*private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;*/

    private String userId;
    private static final String TAG = HomeActivity.class.getName();
    public ListView browseList;
    private ArrayList<Category> cateList;
    Retrofit retrofit;
    CategoryEndpoint categoryEndpoint;
    Gson gson;
    public static int bgresource;

    Fragment searchFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FIND ITEM");

        browseList = (ListView) findViewById(R.id.category_list);
        //((TextView) findViewById(R.id.toolbar_title)).setText("Title!");

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Added from Main Activity
        //myStatusText = (TextView)findViewById(R.id.greetingMessage);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid().toString();
        Log.e(TAG, "my user id is " + userId);
        //myStatusText.setText("Hello, your user id is " + userId);

        gson = new Gson();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        categoryEndpoint = retrofit.create(CategoryEndpoint.class);


        if(user != null) {

            cateList = new ArrayList<>();

            Call<ArrayList<Category>> call = categoryEndpoint.getCategories();
            call.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    int statusCode = response.code();
                    //List<Item> items = response.body();
                    cateList = response.body();

                    browseList.setAdapter(new CategoryListAdapter());
                    browseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0,
                                                View arg1, int pos, long arg3) {
                            //startActivity(new Intent(this, BrowseActivity.class).putExtra(Config.EXTRA_DATA, cateList.get(pos)));
                            Intent myIntent = new Intent(HomeActivity.this, BrowseActivity.class);
                            myIntent.putExtra(Config.EXTRA_DATA, cateList.get(pos));
                            HomeActivity.this.startActivity(myIntent);
                        }
                    });

                    Log.d("retrofit.call.enqueue", ""+statusCode);
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    Log.d("retrofit.call.enqueue", "failed");
                }
            });

        }else{
            startActivity(new Intent(this, SignInActivity.class));
        }

        if(getIntent().hasExtra("fragment_name")){
            Class fragmentClass = null;
            Fragment fragment = null;

            switch(getIntent().getStringExtra("fragment_name")){
                case "ChatListFragment":
                    fragmentClass = ChatListFragment.class;
                    break;
                default:
                    break;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_home, fragment).commit();
        }
    }

    private class CategoryListAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() { return cateList.size(); }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Category getItem(int arg0)
        {
            return cateList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem_id(int)
         */
        @Override
        public long getItemId(int arg0) {return arg0;}

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.category_item, arg2, false);

            Category c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getName());

            String picture = c.getImage();


            if(picture.equals("vehicles")) {
                bgresource = R.drawable.vehicles;
            } else if(picture.equals("sports")) {
                bgresource = R.drawable.sports;
            } else if(picture.equals("outdoor")) {
                bgresource = R.drawable.outdoor;
            } else if(picture.equals("party")) {
                bgresource = R.drawable.party;
            } else if(picture.equals("garden")) {
                bgresource = R.drawable.garden;
            } else if(picture.equals("tools")) {
                bgresource = R.drawable.tools;
            } else if(picture.equals("clothes")) {
                bgresource = R.drawable.clothes;
            } else if(picture.equals("electronics")) {
                bgresource = R.drawable.electronics;
            } else if(picture.equals("books")) {
                bgresource = R.drawable.books;
            } else if(picture.equals("miscellaneous")) {
                bgresource = R.drawable.miscellaneous;
            } else if(picture.equals("camping")) {
                bgresource = R.drawable.camping;
            } else{
                bgresource = R.drawable.bg;
            }

            lbl.setBackgroundResource(bgresource);

            return v;
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //Search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        //search
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener(){

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("OnActionExpandListener:", "Expand");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Go back to previous activity / fragment here
                if(searchFragment != null) {
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                    searchFragment = null;
                }
                Log.d("OnActionExpandListener:", "Collapse");
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // if the KeywordsFragment is already open,
                // we want to start the fragment and pass in even empty string

                // however, if the KeywordsFragment is not open yet,
                // only trigger when the newText is not empty
                if( searchFragment != null )
                {
                    ((KeywordsFragment) searchFragment).setSearchText(newText);
                }
                if (searchFragment == null && !newText.isEmpty()) {

                    Class fragmentClass = KeywordsFragment.class;

                    Bundle searchBundle = new Bundle();
                    searchBundle.putString("searchText", newText);
                    try {
                        searchFragment = (Fragment) fragmentClass.newInstance();
                        searchFragment.setArguments(searchBundle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_home, searchFragment, "KeywordsFragment").commit();

                    // either start an activity / fragment or somehow
                    // show a list
                    Log.d("queryText: ", newText);
                }
                return false;
            }
        });

        return true;
    }

    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_search) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Toast.makeText(HomeActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }else {
            FragmentManager manager = getSupportFragmentManager();
            if (id == R.id.nav_list) {
                fragmentClass = ListItemFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                manager.beginTransaction().replace(R.id.content_home, fragment, "ListItemFragment").commit();
            } else if (id == R.id.nav_trade) {
                fragmentClass = StartRentalFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                manager.beginTransaction().replace(R.id.content_home, fragment, "StartRentalFragment").commit();
            } else if (id == R.id.nav_rental) {
                //fragmentClass = ListItemFragment.class;
            } else if (id == R.id.nav_inventory) {
                fragmentClass = AvailabeItemFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                manager.beginTransaction().replace(R.id.content_home, fragment, "AvailableItemFragment").commit();
            } else if (id == R.id.nav_inbox) {
                fragmentClass = ChatListFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                manager.beginTransaction().replace(R.id.content_home, fragment, "ChatListFragment").commit();
            } else if (id == R.id.nav_account) {
                fragmentClass = AccountFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                manager.beginTransaction().replace(R.id.content_home, fragment, "AccountFragment").commit();
            } else if (id == R.id.nav_claim) {
                fragmentClass = FileClaimFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                manager.beginTransaction().replace(R.id.content_home, fragment, "FileClaimFragment").commit();
            }




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
