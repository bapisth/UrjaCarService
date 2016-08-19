package com.urja.carservices.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.urja.carservices.R;
import com.urja.carservices.adapters.CarServiceRecyclerViewAdapter;
import com.urja.carservices.models.WashDetailing;
import com.urja.carservices.utils.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class WashDetailingFragment extends Fragment {

    private static final String TAG = WashDetailingFragment.class.getSimpleName();
    static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Object> mContentItems = new ArrayList<>();
    private DatabaseReference mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mCarServiceWashTypesRef = mDatabaseRootRef.child(DatabaseConstants.TABLE_CAR_SERVICE+"/"+DatabaseConstants.TABLE_WASH_DETAILING);
    private FirebaseAuth firebaseAuth;

    public static WashDetailingFragment newInstance() {
        return new WashDetailingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager;

        if (GRID_LAYOUT) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        mAdapter = new CarServiceRecyclerViewAdapter(mContentItems, getActivity());

        //mAdapter = new RecyclerViewMaterialAdapter();
        mRecyclerView.setAdapter(mAdapter);


        fetchServiceData(this);


    }

    private void fetchServiceData(WashDetailingFragment washDetailingFragment) {

        mCarServiceWashTypesRef.orderByKey().addValueEventListener(new ValueEventListener() {
            WashDetailing mWashDetailing = null;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: Key="+snapshot.getKey());
                    Log.d(TAG, "onDataChange: value="+snapshot.getValue());
                    mContentItems.add(new WashDetailing(snapshot.getKey(), snapshot.getValue().toString()));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}
