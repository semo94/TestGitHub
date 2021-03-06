package com.example.saleem.testgithub.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.database.ApiHelper;
import com.example.saleem.testgithub.database.DataBaseAble;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.NotificationsItems;
import com.example.saleem.testgithub.gson.items.PendingItems;
import com.example.saleem.testgithub.listAdapters.NotificationAdapter;
import com.example.saleem.testgithub.listAdapters.PendingAdapter;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;


public class NotificationsActivity extends AppCompatActivity implements DataBaseAble, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private ListView listView;
    private GetResolver getResolver = new GetResolver();
    private Type listType;
    private NotificationsItems items;
    private NotificationAdapter adapter;
    private ApiHelper apiHelper;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initUI();
        swipeContainer.setRefreshing(true);
        listType = new TypeToken<NotificationsItems>() {
        }.getType();
        this.apiHelper = new ApiHelper(NotificationsActivity.this);
        GlobalConstants.db.GetCache(Config.Get_Notifications, 0, this, apiHelper.App, apiHelper.Cache);
        HttpConnect.getData(Config.Get_Notifications, getResolver);


    }


    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, NotificationsActivity.class, "NotificationsActivity"));
        setContentView(R.layout.pending_activity);
        initControls();


        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("Notifications");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                finish();
            }
        });
        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitleTextColor(Color.WHITE);
        materialMenu.setColor(Color.WHITE);
    }

    private void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.pending_list);
        listView.setVisibility(View.INVISIBLE);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class GetResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.e("Pending", response.toString() + " Pending response");
            swipeContainer.setRefreshing(false);
            items = (NotificationsItems) GlobalConstants.gson.fromJson(response.toString(), listType);
            setAdapterList();
            GlobalConstants.db.SetCache(Config.Get_Notifications, response.toString(), 0, null, apiHelper.App, apiHelper.Cache);
        }

    }

    private void setAdapterList() {
        if (adapter == null) {
            adapter = new NotificationAdapter(NotificationsActivity.this,
                    R.layout.contacts_items, items.getAllNotifications());
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
            YoYo.with(Techniques.SlideInUp).playOn(listView);
        } else {
            listView.setVisibility(View.VISIBLE);
            adapter.restart(items.getAllNotifications());
        }
    }


    @Override
    public void SetApp_db(String Key, int tag) {

    }

    @Override
    public void SetCache_db(String Key, int tag) {

    }

    @Override
    public void GetApp_db(String Key, String Value, int tag) {

    }

    @Override
    public void GetCache_db(String Key, String Value, int tag) {
        if (Value != null) {
            items = (NotificationsItems) GlobalConstants.gson.fromJson(Value, listType);
            setAdapterList();
        }
    }

    @Override
    public void onRefresh() {
        HttpConnect.getData(Config.Get_Notifications, getResolver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}

