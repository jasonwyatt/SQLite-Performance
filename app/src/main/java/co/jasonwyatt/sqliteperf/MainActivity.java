package co.jasonwyatt.sqliteperf;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.jasonwyatt.sqliteperf.inserts.BatchIntegerInsertsFragment;
import co.jasonwyatt.sqliteperf.inserts.IntegerInsertsFragment;
import co.jasonwyatt.sqliteperf.inserts.NoTransactionIntegerInsertsFragment;
import co.jasonwyatt.sqliteperf.inserts.TransactionIntegerInsertsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_container)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    private ActionBarDrawerToggle mDrawerToggle;
    private TestSuiteFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mNavigation.setNavigationItemSelectedListener(this);

        showFragment(IntegerInsertsFragment.class);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.run_test) {
            mCurrentFragment.runTests();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();

        if (item.getItemId() == R.id.benchmark_inserts) {
            showFragment(IntegerInsertsFragment.class);
            return true;
        } else if (item.getItemId() == R.id.benchmark_inserts_no_transactions) {
            showFragment(NoTransactionIntegerInsertsFragment.class);
            return true;
        } else if (item.getItemId() == R.id.benchmark_inserts_transactions) {
            showFragment(TransactionIntegerInsertsFragment.class);
            return true;
        } else if (item.getItemId() == R.id.benchmark_inserts_batch) {
            showFragment(BatchIntegerInsertsFragment.class);
            return true;
        }
        return false;
    }

    private void showFragment(Class fragmentClass) {
        FragmentManager mgr = getSupportFragmentManager();

        mCurrentFragment = (TestSuiteFragment) Fragment.instantiate(this, fragmentClass.getName());

        mgr.beginTransaction()
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();

        setTitle(mCurrentFragment.getChartTitle());
    }
}
