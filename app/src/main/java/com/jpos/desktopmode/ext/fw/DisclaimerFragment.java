package com.jpos.desktopmode.ext.fw;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;

/**
 * Project: FloatingWindows.
 * Created by jonos on 8/7/2016.
 */

public class DisclaimerFragment extends SlideFragment implements View.OnClickListener {
    private boolean canContinue = false;

    public DisclaimerFragment() {
    } // Required public (empty) fragment constructor

    public static DisclaimerFragment newInstance() {
        return new DisclaimerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_disclaimer, container, false);

        Button disclaimerDecline = (Button) root.findViewById(R.id.disclaimerDecline);
        Button disclaimerAccept = (Button) root.findViewById(R.id.disclaimerAccept);

        disclaimerAccept.setOnClickListener(this);
        disclaimerDecline.setOnClickListener(this);

        return root;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.disclaimerAccept:
                canContinue = true;
                updateNavigation();
                nextSlide();
                break;
            case R.id.disclaimerDecline:
                canContinue = false;
                updateNavigation();
                Intent intent = new Intent(
                        Intent.ACTION_DELETE,
                        Uri.fromParts(
                                "package",
                                getActivity().getApplicationContext().getPackageName(),
                                null
                        )
                );
                startActivity(intent);
        }
    }

    @Override
    public boolean canGoForward() {
        return canContinue;
    }
}
