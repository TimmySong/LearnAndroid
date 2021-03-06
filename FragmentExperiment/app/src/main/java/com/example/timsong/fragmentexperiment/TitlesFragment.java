package com.example.timsong.fragmentexperiment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by timsong on 2017/1/4.
 */

public class TitlesFragment extends ListFragment {
    private static final java.lang.String CURRENT_CHOICE_POSITION = "current_title_position";
    private static final String TAG = "TitlesFragment";
    boolean mDualPane;
    int mCurCheckPosition;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populates list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                Shakespeare.TITLES));

        // Check to see if we can use dual-pane mode.
        View detailsContainer = getActivity().findViewById(R.id.frame_layout_details);
        mDualPane = detailsContainer != null && detailsContainer.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_CHOICE_POSITION, mCurCheckPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * Helper function to show the detail of a selected item, either by displaying
     * a fragment in the current ui, or starting a new Activity to accommodate the
     * content.
     *
     * @param selectedPosition the position of the selected item in the title list.
     */
    private void showDetails(int selectedPosition) {
        Toast.makeText(getActivity(), "SelectedPosition: " + selectedPosition, Toast.LENGTH_LONG).show();
        mCurCheckPosition = selectedPosition;

        if (mDualPane) {
            // Display the content in the same page with enough room.
            getListView().setSelection(selectedPosition);
            getListView().setItemChecked(selectedPosition, true);  // only valid when choice mode has been set appropriately.

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.frame_layout_details); // Find with container's identifier here.
            if (detailsFragment == null || detailsFragment.getShownIndex() != selectedPosition) {
                // Make a new fragment to show this selection.
                detailsFragment = DetailsFragment.newInstance(selectedPosition);

                // Execute a transaction, replacing any existing fragment within the container in which this fragment will live in.
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_details, detailsFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                fragmentTransaction.commit();
            }
        } else {
            // Launch a new Activity to show the content.
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.Detail_INDEX, selectedPosition);
            startActivity(intent);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * This method get being called after onActivityCreated, so you need override the system restored view state here instead.
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored IS CALLED.");
        // Restore last state for checked position.
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt(CURRENT_CHOICE_POSITION, 0);
        }
        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }
    }
}
