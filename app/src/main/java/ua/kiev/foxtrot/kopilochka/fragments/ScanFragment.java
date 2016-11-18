package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;

/**
 * Created by NickNb on 04.10.2016.
 */
public class ScanFragment extends BaseFragment implements ZBarScannerView.ResultHandler {
    private long mLastClickTime = 0;
    private ZBarScannerView mScannerView;
    Interfaces interfaces;
    int id;

    public static ScanFragment newInstance(int id){
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putInt(Const.action_id, id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interfaces = (Interfaces) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Interfaces");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_scan, container,
                false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getInt(Const.action_id, 0);
        } else return null;

        Button cancel_scan = (Button)rootView.findViewById(R.id.cancel_scan);
        cancel_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                interfaces.ScannResult(id, null);
            }
        });
        mScannerView = new ZBarScannerView(getActivity());
        FrameLayout scan_zone = (FrameLayout)rootView.findViewById(R.id.scan_zone);
        scan_zone.addView(mScannerView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
//        Toast.makeText(getActivity(), "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                interfaces.ScannResult(id, rawResult.getContents().toString());
                //mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        }, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}