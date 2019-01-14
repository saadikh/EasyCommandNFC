package cnt.nfc.mbds.fr.easycommandnfc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cnt.nfc.mbds.fr.easycommandnfc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {


    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

}
