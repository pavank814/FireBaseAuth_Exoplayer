package com.example.pavan.mobiofire.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.example.pavan.mobiofire.Adapter.listviewAdapter;
import com.example.pavan.mobiofire.DatabaseHandler.DatabaseHandler;

import com.example.pavan.mobiofire.R;



public class ThumbFragment extends Fragment {
    Context mContext;
    ProgressDialog progressDialog;
    private static ProgressDialog authDialog;

    public ThumbFragment(Context applicationContext) {
        this.mContext = applicationContext;
//        authDialog = createAuthDialog(mContext);
//        authDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.thumb_fragment, container, false);
        DatabaseHandler DatabaseHandler = new DatabaseHandler(mContext);

        ListView thumb_list = (ListView) view.findViewById(R.id.thumb_list);
        thumb_list.setAdapter(new listviewAdapter(mContext, DatabaseHandler, "normal_view"));
//        dismissDialog();
        return view;
    }
    private ProgressDialog createAuthDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cancelSendTask();
            }

            private void cancelSendTask() {
                //    Log.i(Setting_actvity.TAG, "Cancel Authenticated task!!");
            }
        });

        return progressDialog;
    }
    public static void dismissDialog() {
        if (authDialog != null) {
            try {
                authDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }


}
